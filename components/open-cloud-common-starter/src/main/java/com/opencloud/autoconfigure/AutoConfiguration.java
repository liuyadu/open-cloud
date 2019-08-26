package com.opencloud.autoconfigure;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.opencloud.common.annotation.ResourceAnnotationScan;
import com.opencloud.common.configuration.OpenCommonProperties;
import com.opencloud.common.configuration.OpenIdGenProperties;
import com.opencloud.common.exception.OpenGlobalExceptionHandler;
import com.opencloud.common.exception.OpenRestResponseErrorHandler;
import com.opencloud.common.filter.XssFilter;
import com.opencloud.common.gen.SnowflakeIdGenerator;
import com.opencloud.common.health.DbHealthIndicator;
import com.opencloud.common.mybatis.ModelMetaObjectHandler;
import com.opencloud.common.security.http.OpenRestTemplate;
import com.opencloud.common.security.oauth2.client.OpenOAuth2ClientProperties;
import com.opencloud.common.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * 默认配置类
 *
 * @author liuyadu
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({OpenCommonProperties.class, OpenIdGenProperties.class, OpenOAuth2ClientProperties.class})
public class AutoConfiguration {


    /**
     * xss过滤
     * body缓存
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean XssFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new XssFilter());
        log.info("XssFilter [{}]", filterRegistrationBean);
        return filterRegistrationBean;
    }

    /**
     * 分页插件
     */
    @ConditionalOnMissingBean(PaginationInterceptor.class)
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        log.info("PaginationInterceptor [{}]", paginationInterceptor);
        return paginationInterceptor;
    }

    /**
     * 默认加密配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(BCryptPasswordEncoder.class)
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info("BCryptPasswordEncoder [{}]", encoder);
        return encoder;
    }


    /**
     * Spring上下文工具配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        log.info("SpringContextHolder [{}]", holder);
        return holder;
    }

    /**
     * 统一异常处理配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OpenGlobalExceptionHandler.class)
    public OpenGlobalExceptionHandler exceptionHandler() {
        OpenGlobalExceptionHandler exceptionHandler = new OpenGlobalExceptionHandler();
        log.info("OpenGlobalExceptionHandler [{}]", exceptionHandler);
        return exceptionHandler;
    }

    /**
     * ID生成器配置
     *
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OpenIdGenProperties.class)
    public SnowflakeIdGenerator snowflakeIdWorker(OpenIdGenProperties properties) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(properties.getWorkId(), properties.getCenterId());
        log.info("SnowflakeIdGenerator [{}]", snowflakeIdGenerator);
        return snowflakeIdGenerator;
    }


    /**
     * 自定义注解扫描
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ResourceAnnotationScan.class)
    public ResourceAnnotationScan resourceAnnotationScan(AmqpTemplate amqpTemplate) {
        ResourceAnnotationScan scan = new ResourceAnnotationScan(amqpTemplate);
        log.info("ResourceAnnotationScan [{}]", scan);
        return scan;
    }

    /**
     * 自定义Oauth2请求类
     *
     * @param openCommonProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OpenRestTemplate.class)
    public OpenRestTemplate openRestTemplate(OpenCommonProperties openCommonProperties, BusProperties busProperties, ApplicationEventPublisher publisher) {
        OpenRestTemplate restTemplate = new OpenRestTemplate(openCommonProperties, busProperties, publisher);
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new OpenRestResponseErrorHandler());
        log.info("OpenRestTemplate [{}]", restTemplate);
        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new OpenRestResponseErrorHandler());
        log.info("RestTemplate [{}]", restTemplate);
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(DbHealthIndicator.class)
    public DbHealthIndicator dbHealthIndicator() {
        DbHealthIndicator dbHealthIndicator = new DbHealthIndicator();
        log.info("DbHealthIndicator [{}]", dbHealthIndicator);
        return dbHealthIndicator;
    }

    /**
     * 自动填充模型数据
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ModelMetaObjectHandler.class)
    public ModelMetaObjectHandler modelMetaObjectHandler() {
        ModelMetaObjectHandler metaObjectHandler = new ModelMetaObjectHandler();
        log.info("ModelMetaObjectHandler [{}]", metaObjectHandler);
        return metaObjectHandler;
    }
}
