package com.opencloud.gateway.spring.server.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.opencloud.common.configuration.OpenCommonProperties;
import com.opencloud.common.utils.SpringContextHolder;
import com.opencloud.gateway.spring.server.actuator.ApiEndpoint;
import com.opencloud.gateway.spring.server.exception.JsonExceptionHandler;
import com.opencloud.gateway.spring.server.filter.GatewayContextFilter;
import com.opencloud.gateway.spring.server.filter.RemoveGatewayContextFilter;
import com.opencloud.gateway.spring.server.locator.ResourceLocator;
import com.opencloud.gateway.spring.server.locator.JdbcRouteDefinitionLocator;
import com.opencloud.gateway.spring.server.service.AccessLogService;
import com.opencloud.gateway.spring.server.service.feign.BaseAuthorityServiceClient;
import com.opencloud.gateway.spring.server.service.feign.GatewayServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

/**
 * 网关配置类
 *
 * @author liuyadu
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ApiProperties.class, OpenCommonProperties.class})
public class ApiConfiguration {


    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        log.info("SpringContextHolder [{}]", holder);
        return holder;
    }

    /**
     * 自定义异常处理[@@]注册Bean时依赖的Bean，会从容器中直接获取，所以直接注入即可
     *
     * @param viewResolversProvider
     * @param serverCodecConfigurer
     * @return
     */
    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                             ServerCodecConfigurer serverCodecConfigurer, AccessLogService accessLogService) {

        JsonExceptionHandler jsonExceptionHandler = new JsonExceptionHandler(accessLogService);
        jsonExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        jsonExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        jsonExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        log.info("ErrorWebExceptionHandler [{}]", jsonExceptionHandler);
        return jsonExceptionHandler;
    }

    /**
     * Jackson全局配置
     *
     * @param properties
     * @return
     */
    @Bean
    @Primary
    public JacksonProperties jacksonProperties(JacksonProperties properties) {
        properties.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        properties.getSerialization().put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        properties.setDateFormat("yyyy-MM-dd HH:mm:ss");
        properties.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        log.info("JacksonProperties [{}]", properties);
        return properties;
    }

    /**
     * 转换器全局配置
     *
     * @param converters
     * @return
     */
    @Bean
    public HttpMessageConverters httpMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        // 忽略为空的字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.getSerializationConfig().withFeatures(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        /**
         * 序列换成json时,将所有的long变成string
         * js中long过长精度丢失
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        log.info("MappingJackson2HttpMessageConverter [{}]", jackson2HttpMessageConverter);
        return new HttpMessageConverters(jackson2HttpMessageConverter);
    }

    @Bean
    @Primary
    public SwaggerProvider swaggerProvider(RouteDefinitionLocator routeDefinitionLocator) {
        return new SwaggerProvider(routeDefinitionLocator);
    }

    /**
     * 动态路由加载
     *
     * @return
     */
    @Bean
    public JdbcRouteDefinitionLocator jdbcRouteDefinitionLocator(JdbcTemplate jdbcTemplate) {
        JdbcRouteDefinitionLocator jdbcRouteDefinitionLocator =  new JdbcRouteDefinitionLocator(jdbcTemplate);
        log.info("JdbcRouteDefinitionLocator [{}]", jdbcRouteDefinitionLocator);
        return  jdbcRouteDefinitionLocator;
    }

    /**
     * 动态路由加载
     *
     * @return
     */
    @Bean
    @Lazy
    public ResourceLocator resourceLocator(RouteDefinitionLocator routeDefinitionLocator, BaseAuthorityServiceClient baseAuthorityServiceClient, GatewayServiceClient gatewayServiceClient) {
        ResourceLocator resourceLocator =  new ResourceLocator(routeDefinitionLocator, baseAuthorityServiceClient, gatewayServiceClient);
        log.info("ResourceLocator [{}]", resourceLocator);
        return resourceLocator;
    }

    /**
     * 网关bus端点
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

    @Bean
    @ConditionalOnMissingBean(GatewayContextFilter.class)
    public GatewayContextFilter gatewayContextFilter(){
        log.debug("Load GatewayContextFilter Config Bean");
        return new GatewayContextFilter();
    }

    @Bean
    @ConditionalOnMissingBean(RemoveGatewayContextFilter.class)
    public RemoveGatewayContextFilter removeGatewayContextFilter(){
        RemoveGatewayContextFilter gatewayContextFilter = new RemoveGatewayContextFilter();
        log.debug("Load RemoveGatewayContextFilter Config Bean");
        return gatewayContextFilter;
    }

     /*@Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }*/

    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }
}
