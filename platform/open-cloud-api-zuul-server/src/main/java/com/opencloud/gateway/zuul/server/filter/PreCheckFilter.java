package com.opencloud.gateway.zuul.server.filter;

import com.opencloud.base.client.model.AuthorityResource;
import com.opencloud.common.constants.ErrorCode;
import com.opencloud.common.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问验证前置过滤器
 *
 * @author liuyadu
 */
@Slf4j
public class PreCheckFilter extends OncePerRequestFilter {

    private static final String STATUS_0 = "0";
    private static final String STATUS_2 = "2";

    private AccessDeniedHandler accessDeniedHandler;

    private AccessManager accessManager;

    public PreCheckFilter(AccessManager accessManager, AccessDeniedHandler accessDeniedHandler) {
        this.accessManager = accessManager;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = accessManager.getRequestPath(request);
        String remoteIpAddress = WebUtils.getRemoteAddress(request);
        String origin = request.getHeader(HttpHeaders.ORIGIN);
        AuthorityResource resource = accessManager.getResource(requestPath);
        if (resource != null) {
            // 资源是否公共访问验证
            if (STATUS_0.equals(resource.getIsOpen().toString())) {
                // 未公开
                accessDeniedHandler.handle(request, response, new AccessDeniedException(ErrorCode.ACCESS_DENIED_NOT_OPEN.getMessage()));
                return;
            }
            // 资源状态验证
            if (STATUS_0.equals(resource.getStatus().toString())) {
                // 禁用
                accessDeniedHandler.handle(request, response, new AccessDeniedException(ErrorCode.ACCESS_DENIED_DISABLED.getMessage()));
                return;
            } else if (STATUS_2.equals(resource.getStatus().toString())) {
                // 维护中
                accessDeniedHandler.handle(request, response, new AccessDeniedException(ErrorCode.ACCESS_DENIED_UPDATING.getMessage()));
                return;
            }
        }


        // ip黑名单验证
        boolean deny = accessManager.matchIpOrOriginBlacklist(requestPath, remoteIpAddress, origin);
        if (deny) {
            // 拒绝
            accessDeniedHandler.handle(request, response, new AccessDeniedException(ErrorCode.ACCESS_DENIED_BLACK_LIMITED.getMessage()));
            return;
        }

        // ip白名单验证
        boolean[] matchIpWhiteListResult = accessManager.matchIpOrOriginWhiteList(requestPath, remoteIpAddress, origin);
        boolean hasWhiteList = matchIpWhiteListResult[0];
        boolean allow = matchIpWhiteListResult[1];
        if (hasWhiteList) {
            // 接口存在白名单限制
            if (!allow) {
                accessDeniedHandler.handle(request, response, new AccessDeniedException(ErrorCode.ACCESS_DENIED_WHITE_LIMITED.getMessage()));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
