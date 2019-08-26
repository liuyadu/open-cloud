package com.opencloud.gateway.spring.server.filter;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.opencloud.gateway.spring.server.configuration.ApiProperties;
import com.opencloud.gateway.spring.server.locator.ResourceLocator;
import com.opencloud.gateway.spring.server.util.matcher.ReactiveIpAddressMatcher;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * 访问权限控制管理类
 *
 * @author liuyadu
 */
@Slf4j
@Component
public class AccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private ResourceLocator resourceLocator;

    private ApiProperties apiProperties;

    private static final AntPathMatcher pathMatch = new AntPathMatcher();

    private Set<String> permitAll = new ConcurrentHashSet<>();

    private Set<String> authorityIgnores = new ConcurrentHashSet<>();


    public AccessManager(ResourceLocator resourceLocator, ApiProperties apiProperties) {
        this.resourceLocator = resourceLocator;
        this.apiProperties = apiProperties;
        // 默认放行
        permitAll.add("/");
        permitAll.add("/error");
        permitAll.add("/favicon.ico");
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


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        String requestPath = exchange.getRequest().getURI().getPath();
        if (!apiProperties.getAccessControl()) {
            return Mono.just(new AuthorizationDecision(true));
        }
        // 是否直接放行
        if (permitAll(requestPath)) {
            return Mono.just(new AuthorizationDecision(true));
        }
        return authentication.map(a -> {
            return new AuthorizationDecision(checkAuthorities(exchange, a, requestPath));
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }

    /**
     * 始终放行
     *
     * @param requestPath
     * @return
     */
    public boolean permitAll(String requestPath) {
        final Boolean[] result = {false};
        Iterator<String> it = permitAll.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        // 动态权限列表
        Flux<AuthorityResource> resources = resourceLocator.getAuthorityResources();
        resources.filter(res -> StringUtils.isNotBlank(res.getPath()))
                .subscribe(res -> {
                    Boolean isAuth = res.getIsAuth() != null && res.getIsAuth().intValue() == 1 ? true : false;
                    // 无需认证,返回true
                    if (pathMatch.match(res.getPath(), requestPath) && !isAuth) {
                        result[0] = true;
                        return;
                    }
                });
        return result[0];
    }

    /**
     * 获取资源状态
     *
     * @param requestPath
     * @return
     */
    public AuthorityResource getResource(String requestPath) {
        final AuthorityResource[] result = {null};
        // 动态权限列表
        Flux<AuthorityResource> resources = resourceLocator.getAuthorityResources();
        resources.filter(r -> !"/**".equals(r.getPath()) && !permitAll(requestPath) && StringUtils.isNotBlank(r.getPath()) && pathMatch.match(r.getPath(), requestPath))
                .subscribe(r -> result[0] = r);
        return result[0];
    }

    /**
     * 忽略鉴权
     *
     * @param requestPath
     * @return
     */
    private boolean authorityIgnores(String requestPath) {
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
     * @param exchange
     * @param authentication
     * @param requestPath
     * @return
     */
    private boolean checkAuthorities(ServerWebExchange exchange, Authentication authentication, String requestPath) {
        Object principal = authentication.getPrincipal();
        // 已认证身份
        if (principal != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                //check if this uri can be access by anonymous
                //return
            }
            if (authorityIgnores(requestPath)) {
                // 认证通过,并且无需权限
                return true;
            }
            return mathAuthorities(exchange, authentication, requestPath);
        }
        return false;
    }

    public boolean mathAuthorities(ServerWebExchange exchange, Authentication authentication, String requestPath) {
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

    private Collection<ConfigAttribute> getAttributes(String requestPath) {
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
        final Boolean[] result = {false};
        Flux<IpLimitApi> blackList = resourceLocator.getIpBlacks();
        blackList.filter(api -> pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty())
                .filter(api -> matchIpOrOrigin(api.getIpAddressSet(), ipAddress, origin))
                .subscribe(r -> result[0] = true);
        return result[0];

    }

    /**
     * 白名单验证
     *
     * @param requestPath
     * @param ipAddress
     * @param origin
     * @return [hasWhiteList, allow]
     */
    public Boolean[] matchIpOrOriginWhiteList(String requestPath, String ipAddress, String origin) {
        final Boolean[] result = {false, false};
        boolean hasWhiteList = false;
        boolean allow = false;
        Flux<IpLimitApi> whiteList = resourceLocator.getIpWhites();
        whiteList.filter(api -> pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty())
                .subscribe(api -> {
                    result[0] = true;
                    result[1] = matchIpOrOrigin(api.getIpAddressSet(), ipAddress, origin);
                });
        return result;
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
        ReactiveIpAddressMatcher ipAddressMatcher = null;
        for (String value : values) {
            if (StringUtils.matchIp(value)) {
                ipAddressMatcher = new ReactiveIpAddressMatcher(value);
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

    public ApiProperties getApiProperties() {
        return apiProperties;
    }

    public void setApiProperties(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }
}
