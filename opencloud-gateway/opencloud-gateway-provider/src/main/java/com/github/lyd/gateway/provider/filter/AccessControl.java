package com.github.lyd.gateway.provider.filter;

import com.github.lyd.base.client.model.AccessAuthority;
import com.github.lyd.common.constants.CommonConstants;
import com.github.lyd.common.constants.ResultEnum;
import com.github.lyd.common.security.OpenGrantedAuthority;
import com.github.lyd.common.utils.StringUtils;
import com.github.lyd.common.utils.WebUtils;
import com.github.lyd.gateway.client.model.GatewayIpLimitApisDto;
import com.github.lyd.gateway.provider.configuration.ApiGatewayProperties;
import com.github.lyd.gateway.provider.locator.AccessLocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 自定义动态访问控制
 *
 * @author liuyadu
 */
@Slf4j
@Service
public class AccessControl {

    private AccessLocator accessLocator;

    private ApiGatewayProperties apiGatewayProperties;

    private static final AntPathMatcher pathMatch = new AntPathMatcher();

    private Set<String> permitAll = new HashSet<>();

    private Set<String> noAuthorityAllow = new HashSet<>();


    public AccessControl(AccessLocator accessLocator, ApiGatewayProperties apiGatewayProperties) {
        this.accessLocator = accessLocator;
        this.apiGatewayProperties = apiGatewayProperties;
        if (apiGatewayProperties != null) {
            if (apiGatewayProperties.getPermitAll() != null) {
                permitAll.addAll(Arrays.asList(apiGatewayProperties.getPermitAll().split(",")));
            }
            if (apiGatewayProperties.getNoAuthorityAllow() != null) {
                noAuthorityAllow.addAll(Arrays.asList(apiGatewayProperties.getNoAuthorityAllow().split(",")));
            }
        }
    }

    /**
     * 访问控制
     * 1.IP黑名单
     * 2.IP白名单
     * 3.权限控制
     *
     * @param request
     * @param authentication
     * @return
     */
    public boolean access(HttpServletRequest request, Authentication authentication) {
        if (!apiGatewayProperties.getAccessControl()) {
            return true;
        }
        String requestPath = getRequestPath(request);
        String remoteIpAddress = WebUtils.getIpAddr(request);
        // 1.ip黑名单检测
        boolean deny = matchIpBlacklist(requestPath, remoteIpAddress);
        if (deny) {
            // 拒绝
            log.debug("==> access_denied:path={},message={}", requestPath, ResultEnum.ACCESS_DENIED_BLACK_IP_LIMITED.getMessage());
            request.setAttribute(CommonConstants.X_ACCESS_DENIED, ResultEnum.ACCESS_DENIED_BLACK_IP_LIMITED);
            return false;
        }

        // 2.是否直接放行
        if (isPermitAll(requestPath)) {
            return true;
        }

        // 3.ip白名单检测
        boolean[] matchIpWhiteListResult = matchIpWhiteList(requestPath, remoteIpAddress);
        boolean hasWhiteList = matchIpWhiteListResult[0];
        boolean allow = matchIpWhiteListResult[1];

        // 4.判断api是否需要认证
        boolean isAuth = isAuthAccess(requestPath);

        if (hasWhiteList) {
            // 接口存在白名单限制
            if (allow) {
                // IP白名单检测通过
                if (!isAuth) {
                    // 无需身份验证,允许
                    return true;
                } else {
                    // 校验身份
                    return checkAuthorities(request, authentication, requestPath);
                }
            } else {
                // IP白名单检测通过,拒绝
                log.debug("==> access_denied:path={},message={}", requestPath, ResultEnum.ACCESS_DENIED_WHITE_IP_LIMITED.getMessage());
                request.setAttribute(CommonConstants.X_ACCESS_DENIED, ResultEnum.ACCESS_DENIED_WHITE_IP_LIMITED);
                return false;
            }

        } else {
            // 接口不存在白名单限制,只校验身份
            return checkAuthorities(request, authentication, requestPath);
        }
    }


    private boolean isPermitAll(String requestPath) {
        Iterator<String> it = permitAll.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNoAuthorityAllow(String requestPath) {
        Iterator<String> it = noAuthorityAllow.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }


    private boolean checkAuthorities(HttpServletRequest request, Authentication authentication, String requestPath) {
        Object principal = authentication.getPrincipal();
        // 已认证身份
        if (principal != null) {

            if (authentication instanceof AnonymousAuthenticationToken) {
                //check if this uri can be access by anonymous
                //return
            }
            if (isNoAuthorityAllow(requestPath)) {
                // 认证通过,并且无需权限
                return true;
            }
            return mathAuthorities(request, authentication, requestPath);
        }
        return false;
    }

    public boolean mathAuthorities(HttpServletRequest request, Authentication authentication, String requestPath) {
        Collection<ConfigAttribute> attributes = getAttributes(requestPath);
        if (authentication == null) {
            return false;
        } else {
            if (CommonConstants.ROOT.equals(authentication.getName())) {
                // 默认超级管理员账号,直接放行
                return true;
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator var6 = attributes.iterator();
            Iterator var8 = authorities.iterator();
            while (var6.hasNext()) {
                ConfigAttribute attribute = (ConfigAttribute) var6.next();
                while (var8.hasNext()) {
                    GrantedAuthority authority = (GrantedAuthority) var8.next();
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        if (authority instanceof OpenGrantedAuthority) {
                            OpenGrantedAuthority customer = (OpenGrantedAuthority) authority;
                            if (customer.getIsExpired() != null && customer.getIsExpired()) {
                                // 授权已过期
                                log.debug("==> access_denied:path={},message={}", requestPath, ResultEnum.ACCESS_DENIED_AUTHORITY_EXPIRED.getMessage());
                                request.setAttribute(CommonConstants.X_ACCESS_DENIED, ResultEnum.ACCESS_DENIED_AUTHORITY_EXPIRED);
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private Collection<ConfigAttribute> getAttributes(String requestPath) {
        // 匹配动态权限
        for (Iterator<String> iter = accessLocator.getAllConfigAttribute().keySet().iterator(); iter.hasNext(); ) {
            String url = iter.next();
            // 防止匹配错误 忽略/**
            if (!"/**".equals(url) && pathMatch.match(url, requestPath)) {
                // 返回匹配到权限
                return accessLocator.getAllConfigAttribute().get(url);
            }
        }
        return SecurityConfig.createList("AUTHORITIES_REQUIRED");
    }


    private boolean matchIpBlacklist(String requestPath, String remoteIpAddress) {
        List<GatewayIpLimitApisDto> blackList = accessLocator.getIpBlackList();
        if (blackList != null) {
            for (GatewayIpLimitApisDto api : blackList) {
                if (pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty()) {
                    if (matchIp(api.getIpAddressSet(), remoteIpAddress)) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    private boolean[] matchIpWhiteList(String requestPath, String remoteIpAddress) {
        boolean hasWhiteList = false;
        boolean allow = false;
        List<GatewayIpLimitApisDto> whiteList = accessLocator.getIpWhiteList();
        if (whiteList != null) {
            for (GatewayIpLimitApisDto api : whiteList) {
                if (pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty()) {
                    hasWhiteList = true;
                    allow = matchIp(api.getIpAddressSet(), remoteIpAddress);
                    break;
                }
            }
        }
        return new boolean[]{hasWhiteList, allow};
    }

    private boolean matchIp(Set<String> ips, String remoteIpAddress) {
        IpAddressMatcher ipAddressMatcher = null;
        for (String ip : ips) {
            try {
                ipAddressMatcher = new IpAddressMatcher(ip);
                if (ipAddressMatcher.matches(remoteIpAddress)) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    private boolean isAuthAccess(String requestPath) {
        List<AccessAuthority> authorityList = accessLocator.getAuthorityList();
        if (authorityList != null) {
            for (AccessAuthority auth : authorityList) {
                String fullPath = auth.getPath();
                Boolean isAuth = auth.getIsAuth() != null && auth.getIsAuth().equals(1) ? true : false;
                // 无需认证,返回true
                if (StringUtils.isNotBlank(fullPath) && pathMatch.match(fullPath, requestPath) && !isAuth) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            url = StringUtils.isNotBlank(url) ? url + pathInfo : pathInfo;
        }
        return url;
    }
}
