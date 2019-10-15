package com.opencloud.gateway.zuul.server.configuration;

import com.google.common.collect.Lists;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.netflix.zuul.ZuulFilter;
import com.opencloud.gateway.zuul.server.filter.ModifyHeaderFilter;
import com.opencloud.gateway.zuul.server.filter.ZuulErrorFilter;
import com.opencloud.gateway.zuul.server.filter.ZuulResponseFilter;
import com.opencloud.gateway.zuul.server.locator.ResourceLocator;
import com.opencloud.gateway.zuul.server.locator.JdbcRouteLocator;
import com.opencloud.gateway.zuul.server.service.AccessLogService;
import com.opencloud.gateway.zuul.server.actuator.ApiEndpoint;
import com.opencloud.gateway.zuul.server.service.feign.BaseAuthorityServiceClient;
import com.opencloud.gateway.zuul.server.service.feign.GatewayServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 网关配置类
 *
 * @author: liuyadu
 * @date: 2018/10/23 10:31
 * @description:
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ApiProperties.class})
public class GatewayConfiguration {
    private static final String ALLOWED_HEADERS = "*";
    private static final String ALLOWED_METHODS = "*";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_EXPOSE = "Authorization";
    private static final Long MAX_AGE = 18000L;

    @Autowired
    private JdbcRouteLocator jdbcRouteLocator;
    @Autowired
    private RateLimitProperties rateLimitProperties;
    @Autowired
    private BaseAuthorityServiceClient baseAuthorityServiceClient;
    @Autowired
    private AccessLogService accessLogService;

    /**
     * 响应过滤器
     *
     * @return
     */
    @Bean
    public ZuulFilter zuulResponseFilter() {
        ZuulFilter zuulFilter = new ZuulResponseFilter(accessLogService);
        log.info("ZuulErrorFilter [{}]", zuulFilter);
        return zuulFilter;
    }

    /**
     * 错误过滤器
     *
     * @return
     */
    @Bean
    public ZuulFilter zuulErrorFilter() {
        ZuulFilter zuulFilter = new ZuulErrorFilter(accessLogService);
        log.info("ZuulErrorFilter [{}]", zuulFilter);
        return zuulFilter;
    }

    /**
     * 修改请求头
     *
     * @return
     */
    @Bean
    public ZuulFilter modifyHeaderFilter() {
        ZuulFilter zuulFilter = new ModifyHeaderFilter();
        log.info("ModifyHeaderFilter [{}]", zuulFilter);
        return zuulFilter;
    }

    /**
     * 资源加载器
     *
     * @return
     */
    @Bean
    public ResourceLocator resourceLocator(GatewayServiceClient gatewayServiceClient) {
        ResourceLocator resourceLocator = new ResourceLocator(jdbcRouteLocator, rateLimitProperties, baseAuthorityServiceClient, gatewayServiceClient);
        log.info("CorsFilter [{}]", resourceLocator);
        return resourceLocator;
    }

    /**
     * 路由加载器
     *
     * @return
     */
    @Bean
    public JdbcRouteLocator jdbcRouteLocator(ZuulProperties zuulProperties, ServerProperties serverProperties, JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher) {
        jdbcRouteLocator = new JdbcRouteLocator(serverProperties.getServlet().getContextPath(), zuulProperties, jdbcTemplate, publisher);
        log.info("JdbcRouteLocator:{}", jdbcRouteLocator);
        return jdbcRouteLocator;
    }

    /**
     * 自定义网关监控端点
     *
     * @param context
     * @param bus
     * @return
     */
    @Bean
    @ConditionalOnEnabledEndpoint
    @ConditionalOnClass({Endpoint.class})
    public ApiEndpoint apiEndpoint(ApplicationContext context, BusProperties bus) {
        ApiEndpoint endpoint = new ApiEndpoint(context, bus.getId());
        log.info("ApiEndpoint [{}]", endpoint);
        return endpoint;
    }


    /**
     * 跨域配置
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Lists.newArrayList(ALLOWED_HEADERS.split(",")));
        config.setAllowedOrigins(Lists.newArrayList(ALLOWED_ORIGIN.split(",")));
        config.setAllowedMethods(Lists.newArrayList(ALLOWED_METHODS.split(",")));
        config.setMaxAge(MAX_AGE);
        config.addExposedHeader(ALLOWED_EXPOSE);

        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        //最大优先级,设置0不好使
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        log.info("CorsFilter [{}]", bean);
        return bean;
    }
}
