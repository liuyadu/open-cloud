package com.opencloud.gateway.spring.server.filter;

import com.opencloud.gateway.spring.server.filter.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 移除GatewayContext过滤器
 */
@Slf4j
public class RemoveGatewayContextFilter implements WebFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(s -> exchange.getAttributes().remove(GatewayContext.CACHE_GATEWAY_CONTEXT));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
