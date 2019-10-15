package com.opencloud.gateway.zuul.server.filter;

import com.opencloud.common.interceptor.FeignRequestInterceptor;
import com.opencloud.gateway.zuul.server.filter.support.ModifyHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * 请求前缀过滤器,增加请求时间
 *
 * @author liuyadu
 */
@Slf4j
public class PreRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        request.setAttribute("requestTime", new Date());
        // 修复 请求防止流读取一次丢失问题
        ModifyHttpServletRequestWrapper requestWrapper = new ModifyHttpServletRequestWrapper(request);
        String sid =  UUID.randomUUID().toString();
        // 添加自定义请求头
        requestWrapper.putHeader(FeignRequestInterceptor.X_REQUEST_ID, sid);
        response.setHeader(FeignRequestInterceptor.X_REQUEST_ID,sid);
        filterChain.doFilter(requestWrapper, response);
    }
}
