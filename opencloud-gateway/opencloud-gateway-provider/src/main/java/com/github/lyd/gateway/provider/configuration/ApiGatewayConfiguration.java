package com.github.lyd.gateway.provider.configuration;

import com.github.lyd.gateway.provider.endpoint.GatewayRefreshBusEndpoint;
import com.github.lyd.gateway.provider.event.GatewayRefreshRemoteListener;
import com.github.lyd.gateway.provider.filter.ZuulErrorFilter;
import com.github.lyd.gateway.provider.filter.ZuulResponseFilter;
import com.github.lyd.gateway.provider.locator.AccessLocator;
import com.github.lyd.gateway.provider.locator.ZuulRouteLocator;
import com.github.lyd.gateway.provider.service.GatewayIpLimitService;
import com.github.lyd.gateway.provider.service.GatewayRateLimitService;
import com.github.lyd.gateway.provider.service.GatewayRouteService;
import com.github.lyd.gateway.provider.service.feign.BaseAuthorityRemoteService;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义bus配置类
 *
 * @author: liuyadu
 * @date: 2018/10/23 10:31
 * @description:
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ApiGatewayProperties.class})
public class ApiGatewayConfiguration {

    private ZuulRouteLocator zuulRoutesLocator;
    private AccessLocator accessLocator;

    /**
     * 响应过滤器
     *
     * @return
     */
    @Bean
    public ZuulFilter zuulResponseFilter() {
        return new ZuulResponseFilter();
    }

    /**
     * 错误过滤器
     *
     * @return
     */
    @Bean
    public ZuulFilter zuulErrorFilter() {
        return new ZuulErrorFilter();
    }


    /**
     * 访问控制加载器
     *
     * @param zuulRoutesLocator
     * @param rateLimitProperties
     * @param baseAuthorityRemoteService
     * @param gatewayIpLimitService
     * @param gatewayRateLimitService
     * @return
     */
    @Bean
    public AccessLocator accessLocator(ZuulRouteLocator zuulRoutesLocator, RateLimitProperties rateLimitProperties, BaseAuthorityRemoteService baseAuthorityRemoteService, GatewayIpLimitService gatewayIpLimitService, GatewayRateLimitService gatewayRateLimitService) {
        accessLocator = new AccessLocator(zuulRoutesLocator,rateLimitProperties, baseAuthorityRemoteService, gatewayIpLimitService,gatewayRateLimitService);
        return accessLocator;
    }

    /**
     * 初始化路由加载器
     *
     * @return
     */
    @Bean
    public ZuulRouteLocator zuulRouteLocator(ZuulProperties zuulProperties, ServerProperties serverProperties, GatewayRouteService gatewayRouteService) {
        zuulRoutesLocator = new ZuulRouteLocator(serverProperties.getServlet().getContextPath(), zuulProperties, gatewayRouteService);
        log.info("ZuulRoutesLocator:{}", zuulRoutesLocator);
        return zuulRoutesLocator;
    }

    /**
     * 配置网关刷新bus监听
     *
     * @return
     */
    @Bean
    public GatewayRefreshRemoteListener gatewayRefreshRemoteListener() {
        GatewayRefreshRemoteListener rateLimitRefreshRemoteListener = new GatewayRefreshRemoteListener(zuulRoutesLocator, accessLocator);
        log.info("bean [{}]", rateLimitRefreshRemoteListener);
        return rateLimitRefreshRemoteListener;
    }

    /**
     * 配置网关刷新bus端点
     *
     * @param context
     * @param bus
     * @return
     */
    @Bean
    @ConditionalOnEnabledEndpoint
    @ConditionalOnClass({Endpoint.class})
    public GatewayRefreshBusEndpoint gatewayRefreshBusEndpoint(ApplicationContext context, BusProperties bus) {
        GatewayRefreshBusEndpoint endpoint = new GatewayRefreshBusEndpoint(context, bus.getId());
        log.info("bean [{}]", endpoint);
        return endpoint;
    }


}
