package com.github.lyd.autoconfigure;

import com.github.lyd.common.annotation.AnnotationScan;
import com.github.lyd.common.configuration.CommonProperties;
import com.github.lyd.common.configuration.CorsProperties;
import com.github.lyd.common.configuration.IdGenProperties;
import com.github.lyd.common.exception.OpenExceptionHandler;
import com.github.lyd.common.gen.SnowflakeIdGenerator;
import com.github.lyd.common.health.DbHealthIndicator;
import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 默认配置类
 *
 * @author liuyadu
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CommonProperties.class, IdGenProperties.class,CorsProperties.class})
public class AutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "opencloud.cors", name = "enabled", havingValue = "true")
    public FilterRegistrationBean corsFilter(CorsProperties corsProperties) {
        log.debug("跨域配置:{}", corsProperties);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(corsProperties.getAllowCredentials());
        config.setAllowedOrigins(Lists.newArrayList(corsProperties.getAllowedOrigin().split(",")));
        config.setAllowedMethods(Lists.newArrayList(corsProperties.getAllowedMethod().split(",")));
        config.setAllowedHeaders(Lists.newArrayList(corsProperties.getAllowedHeader().split(",")));
        config.setMaxAge(corsProperties.getMaxAge());
        config.addExposedHeader("Authorization");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        //最大优先级,设置0不好使
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    /**
     * 默认加密配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(BCryptPasswordEncoder.class)
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
        log.info("bean [{}]",encoder);
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
        log.info("bean [{}]",holder);
        return holder;
    }

    /**
     * 统一异常处理配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OpenExceptionHandler.class)
    public OpenExceptionHandler exceptionHandler() {
        OpenExceptionHandler exceptionHandler = new OpenExceptionHandler();
        log.info("bean [{}]", exceptionHandler);
        return exceptionHandler;
    }

    /**
     * ID生成器配置
     *
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IdGenProperties.class)
    public SnowflakeIdGenerator snowflakeIdWorker(IdGenProperties properties) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(properties.getWorkId(), properties.getCenterId());
        log.info("bean [{}] properties [{}]", snowflakeIdGenerator,properties);
        return snowflakeIdGenerator;
    }


    /**
     * 自定义注解扫描
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AnnotationScan.class)
    public AnnotationScan annotationScan(AmqpTemplate amqpTemplate) {
        AnnotationScan scan = new AnnotationScan(amqpTemplate);
        log.info("bean [{}]", scan);
        return scan;
    }

    /**
     * 自定义Oauth2请求类
     *
     * @param commonProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OpenRestTemplate.class)
    public OpenRestTemplate openRestTemplate(CommonProperties commonProperties) {
        OpenRestTemplate restTemplate = new OpenRestTemplate(commonProperties);
        log.info("bean [{}]", restTemplate);
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(DbHealthIndicator.class)
    public DbHealthIndicator dbHealthIndicator() {
        DbHealthIndicator dbHealthIndicator = new DbHealthIndicator();
        return dbHealthIndicator;
    }

}
