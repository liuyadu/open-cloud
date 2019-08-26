package com.opencloud.gateway.zuul.server.configuration;

import com.opencloud.common.security.OpenHelper;
import com.opencloud.gateway.zuul.server.exception.JsonAccessDeniedHandler;
import com.opencloud.gateway.zuul.server.exception.JsonAuthenticationEntryPoint;
import com.opencloud.gateway.zuul.server.exception.JsonSignatureDeniedHandler;
import com.opencloud.gateway.zuul.server.filter.AccessManager;
import com.opencloud.gateway.zuul.server.filter.PreCheckFilter;
import com.opencloud.gateway.zuul.server.filter.PreRequestFilter;
import com.opencloud.gateway.zuul.server.filter.PreSignatureFilter;
import com.opencloud.gateway.zuul.server.service.AccessLogService;
import com.opencloud.gateway.zuul.server.service.feign.BaseAppServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * oauth2资源服务器配置
 *
 * @author: liuyadu
 * @date: 2018/10/23 10:31
 * @description:
 */
@Slf4j
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    private ApiProperties apiProperties;
    @Autowired
    private BaseAppServiceClient baseAppServiceClient;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private AccessManager accessManager;

    private OAuth2WebSecurityExpressionHandler expressionHandler;

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 构建redis获取token服务类
        resources.tokenServices(OpenHelper.buildRedisTokenServices(redisConnectionFactory));
        resources.expressionHandler(expressionHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                // 动态权限验证
                .anyRequest().access("@accessManager.check(request,authentication)")
                .and()
                //认证鉴权错误处理,为了统一异常处理。每个资源服务器都应该加上。
                .exceptionHandling()
                .accessDeniedHandler(new JsonAccessDeniedHandler(accessLogService))
                .authenticationEntryPoint(new JsonAuthenticationEntryPoint(accessLogService))
                .and()
                .csrf().disable();
        // 日志前置过滤器
        http.addFilterBefore(new PreRequestFilter(), AbstractPreAuthenticatedProcessingFilter.class);
        // 签名验证过滤器
        http.addFilterAfter(new PreSignatureFilter(baseAppServiceClient, apiProperties,new JsonSignatureDeniedHandler(accessLogService)), AbstractPreAuthenticatedProcessingFilter.class);
        // 访问验证前置过滤器
        http.addFilterAfter(new PreCheckFilter(accessManager, new JsonAccessDeniedHandler(accessLogService)), AbstractPreAuthenticatedProcessingFilter.class);
    }
}

