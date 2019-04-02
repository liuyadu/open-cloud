package com.github.lyd.gateway.provider.configuration;

import com.github.lyd.common.configuration.CommonProperties;
import com.github.lyd.common.gen.SnowflakeIdGenerator;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.common.security.OpenHelper;
import com.github.lyd.common.utils.WebUtils;
import com.github.lyd.gateway.provider.exception.GatewayAccessDeniedHandler;
import com.github.lyd.gateway.provider.exception.GatewayAuthenticationEntryPoint;
import com.github.lyd.gateway.provider.filter.PreRequestFilter;
import com.github.lyd.gateway.provider.filter.SignatureFilter;
import com.github.lyd.gateway.provider.service.GatewayAccessLogsService;
import com.github.lyd.gateway.provider.service.feign.BaseAppRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    private ResourceServerProperties properties;
    @Autowired
    private ApiGatewayProperties apiGatewayProperties;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private BaseAppRemoteService baseAppRemoteService;
    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GatewayAccessLogsService gatewayAccessLogsService;
    private OAuth2WebSecurityExpressionHandler expressionHandler;

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 构建远程获取token,这里是为了支持自定义用户信息转换器
        resources.tokenServices(OpenHelper.buildRemoteTokenServices(properties));
        resources.expressionHandler(expressionHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                // 动态访问控制
                .anyRequest().access("@accessControl.access(request,authentication)")
                // SSO退出
                .and().logout().logoutSuccessHandler(new SsoLogoutSuccessHandler(commonProperties.getApiServerAddr() + "/auth/logout", restTemplate))
                .and()
                //认证鉴权错误处理,为了统一异常处理。每个资源服务器都应该加上。
                .exceptionHandling()
                .accessDeniedHandler(new GatewayAccessDeniedHandler(gatewayAccessLogsService))
                .authenticationEntryPoint(new GatewayAuthenticationEntryPoint(gatewayAccessLogsService))
                .and()
                .csrf().disable();
        // 网关日志前置过滤器
        http.addFilterBefore(new PreRequestFilter(snowflakeIdGenerator, redisTemplate, gatewayAccessLogsService), AbstractPreAuthenticatedProcessingFilter.class);
        // 增加签名验证过滤器
        http.addFilterAfter(new SignatureFilter(baseAppRemoteService, apiGatewayProperties), AbstractPreAuthenticatedProcessingFilter.class);
    }

    static class SsoLogoutSuccessHandler implements LogoutSuccessHandler {
        private String defaultTargetUrl;
        private RestTemplate restTemplate;

        public SsoLogoutSuccessHandler(String defaultTargetUrl, RestTemplate restTemplate) {
            this.defaultTargetUrl = defaultTargetUrl;
            this.restTemplate = restTemplate;
        }

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            try {
                restTemplate.getForEntity(defaultTargetUrl, String.class);
            } catch (Exception e) {
                log.error("sso logout error:", e);
            }
            WebUtils.writeJson(response, ResultBody.success("退出成功", null));
        }
    }

}

