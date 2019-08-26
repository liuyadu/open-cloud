package com.opencloud.gateway.zuul.server.filter;

import com.opencloud.gateway.zuul.server.configuration.ApiProperties;
import com.opencloud.gateway.zuul.server.locator.ResourceLocator;
import com.opencloud.base.client.model.AuthorityResource;
import com.opencloud.base.client.model.IpLimitApi;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.constants.ErrorCode;
import com.opencloud.common.security.OpenAuthority;
import com.opencloud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 访问权限控制管理类
 *
 * @author liuyadu
 */
@Slf4j
@Component
public class AccessManager {

    private ResourceLocator resourceLocator;

    private ApiProperties apiProperties;

    private static final AntPathMatcher pathMatch = new AntPathMatcher();

    private Set<String> permitAll = new HashSet<>();

    private Set<String> authorityIgnores = new HashSet<>();


    public AccessManager(ResourceLocator resourceLocator, ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
        this.resourceLocator = resourceLocator;
        if (apiProperties != null) {
            if (apiProperties.getPermitAll() != null) {
                permitAll.addAll(apiProperties.getPermitAll());
            }
            if (apiProperties.getApiDebug()) {
                permitAll.add("/**/v2/api-docs/**");
                permitAll.add("/**/swagger-resources/**");
                permitAll.add("/webjars/**");
                permitAll.add("/doc.html");
                permitAll.add("/swagger-ui.html");
            }
            if (apiProperties.getAuthorityIgnores() != null) {
                authorityIgnores.addAll(apiProperties.getAuthorityIgnores());
            }
        }
    }

    /**
     * 权限验证
     *
     * @param request
     * @param authentication
     * @return
     */
    public boolean check(HttpServletRequest request, Authentication authentication) {
        if (!apiProperties.getAccessControl()) {
            return true;
        }
        String requestPath = getRequestPath(request);
        // 是否直接放行
        if (permitAll(requestPath)) {
            return true;
        }
        return checkAuthorities(request, authentication, requestPath);
    }


    /**
     * 始终放行
     *
     * @param requestPath
     * @return
     */
    public boolean permitAll(String requestPath) {
        Iterator<String> it = permitAll.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        // 动态权限列表
        List<AuthorityResource> authorityList = resourceLocator.getAuthorityResources();
        if (authorityList != null) {
            Iterator<AuthorityResource> it2 = authorityList.iterator();
            while (it2.hasNext()) {
                AuthorityResource auth = it2.next();
                Boolean isAuth = auth.getIsAuth() != null && auth.getIsAuth().equals(1) ? true : false;
                String fullPath = auth.getPath();
                // 无需认证,返回true
                if (StringUtils.isNotBlank(fullPath) && pathMatch.match(fullPath, requestPath) && !isAuth) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取资源信息
     *
     * @param requestPath
     * @return
     */
    public AuthorityResource getResource(String requestPath) {
        // 动态权限列表
        List<AuthorityResource> authorityList = resourceLocator.getAuthorityResources();
        if (authorityList != null) {
            Iterator<AuthorityResource> it2 = authorityList.iterator();
            while (it2.hasNext()) {
                AuthorityResource auth = it2.next();
                String fullPath = auth.getPath();
                if (!"/**".equals(fullPath) && !permitAll(requestPath) && StringUtils.isNotBlank(fullPath) && pathMatch.match(fullPath, requestPath)) {
                    return auth;
                }
            }
        }
        return null;
    }

    /**
     * 忽略鉴权
     *
     * @param requestPath
     * @return
     */
    public boolean authorityIgnores(String requestPath) {
        Iterator<String> it = authorityIgnores.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检查权限
     *
     * @param request
     * @param authentication
     * @param requestPath
     * @return
     */
    public boolean checkAuthorities(HttpServletRequest request, Authentication authentication, String requestPath) {
        Object principal = authentication.getPrincipal();
        // 已认证身份
        if (principal != null) {
            if (authorityIgnores(requestPath)) {
                // 认证通过,并且无需权限
                return true;
            }
            return mathAuthorities(request, authentication, requestPath);
        }
        return false;
    }

    /**
     * 权限验证
     *
     * @param request
     * @param authentication
     * @param requestPath
     * @return
     */
    public boolean mathAuthorities(HttpServletRequest request, Authentication authentication, String requestPath) {
        Collection<ConfigAttribute> attributes = getAttributes(requestPath);
        int result = 0;
        int expires = 0;
        if (authentication == null) {
            return false;
        } else {
            if (CommonConstants.ROOT.equals(authentication.getName())) {
                // 默认超级管理员账号,直接放行
                return true;
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator var6 = attributes.iterator();
            while (var6.hasNext()) {
                ConfigAttribute attribute = (ConfigAttribute) var6.next();
                Iterator var8 = authorities.iterator();
                while (var8.hasNext()) {
                    GrantedAuthority authority = (GrantedAuthority) var8.next();
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        result++;
                        if (authority instanceof OpenAuthority) {
                            OpenAuthority customer = (OpenAuthority) authority;
                            if (customer.getIsExpired() != null && customer.getIsExpired()) {
                                // 授权过期数
                                expires++;
                            }
                        }
                    }
                }
            }
            log.debug("mathAuthorities result[{}] expires[{}]", result, expires);
            if (expires > 0) {
                // 授权已过期
                throw new AccessDeniedException(ErrorCode.ACCESS_DENIED_AUTHORITY_EXPIRED.getMessage());
            }
            return result > 0;
        }
    }

    /**
     * 获取请求资源所需权限列表
     *
     * @param requestPath
     * @return
     */
    public Collection<ConfigAttribute> getAttributes(String requestPath) {
        // 匹配动态权限
        for (Iterator<String> iter = resourceLocator.getConfigAttributes().keySet().iterator(); iter.hasNext(); ) {
            String url = iter.next();
            // 防止匹配错误 忽略/**
            if (!"/**".equals(url) && pathMatch.match(url, requestPath)) {
                // 返回匹配到权限
                return resourceLocator.getConfigAttributes().get(url);
            }
        }
        return SecurityConfig.createList("AUTHORITIES_REQUIRED");
    }


    /**
     * IP黑名单验证
     *
     * @param requestPath
     * @param ipAddress
     * @param origin
     * @return
     */
    public boolean matchIpOrOriginBlacklist(String requestPath, String ipAddress, String origin) {
        List<IpLimitApi> blackList = resourceLocator.getIpBlacks();
        if (blackList != null) {
            for (IpLimitApi api : blackList) {
                if (pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty()) {
                    if (matchIpOrOrigin(api.getIpAddressSet(), ipAddress, origin)) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    /**
     * 白名单验证
     *
     * @param requestPath
     * @param ipAddress
     * @param origin
     * @return [hasWhiteList, allow]
     */
    public boolean[] matchIpOrOriginWhiteList(String requestPath, String ipAddress, String origin) {
        boolean hasWhiteList = false;
        boolean allow = false;
        List<IpLimitApi> whiteList = resourceLocator.getIpWhites();
        if (whiteList != null) {
            for (IpLimitApi api : whiteList) {
                if (pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty()) {
                    hasWhiteList = true;
                    allow = matchIpOrOrigin(api.getIpAddressSet(), ipAddress, origin);
                    break;
                }
            }
        }
        return new boolean[]{hasWhiteList, allow};
    }

    /**
     * 匹配IP或域名
     *
     * @param values
     * @param ipAddress
     * @param origin
     * @return
     */
    public boolean matchIpOrOrigin(Set<String> values, String ipAddress, String origin) {
        IpAddressMatcher ipAddressMatcher = null;
        for (String value : values) {
            if (StringUtils.matchIp(value)) {
                ipAddressMatcher = new IpAddressMatcher(value);
                if (ipAddressMatcher.matches(ipAddress)) {
                    return true;
                }
            } else {
                if (StringUtils.matchDomain(value) && StringUtils.isNotBlank(origin) && origin.contains(value)) {
                    return true;
                }
            }
        }
        return false;
    }


    public String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            url = StringUtils.isNotBlank(url) ? url + pathInfo : pathInfo;
        }
        return url;
    }

    public ApiProperties getApiProperties() {
        return apiProperties;
    }

    public void setApiProperties(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }
}
