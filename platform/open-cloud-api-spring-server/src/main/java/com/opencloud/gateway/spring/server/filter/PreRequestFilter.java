package com.opencloud.gateway.spring.server.filter;

import com.opencloud.common.interceptor.FeignRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

/**
 * 请求前缀过滤器,增加请求时间
 *
 * @author liuyadu
 */
@Slf4j
public class PreRequestFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 添加自定义请求头
        String rid = UUID.randomUUID().toString();
        ServerHttpRequest request  = exchange.getRequest().mutate().header(FeignRequestInterceptor.X_REQUEST_ID, rid).build();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set(FeignRequestInterceptor.X_REQUEST_ID,rid);
        //将现在的request 变成 change对象
        ServerWebExchange build = exchange.mutate().request(request).response(response).build();
        // 添加请求时间
        build.getAttributes().put("requestTime", new Date());
        return chain.filter(build);
    }


}

