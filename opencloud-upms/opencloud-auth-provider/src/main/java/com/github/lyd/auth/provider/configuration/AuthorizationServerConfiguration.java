package com.github.lyd.auth.provider.configuration;

import com.github.lyd.auth.client.config.SocialOAuth2ClientProperties;
import com.github.lyd.auth.client.constants.AuthConstants;
import com.github.lyd.auth.provider.exception.Oauth2WebResponseExceptionTranslator;
import com.github.lyd.common.security.OpenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * 平台认证服务器配置
 *
 * @author liuyadu
 */
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(SocialOAuth2ClientProperties.class)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    /**
     * 自定义获取客户端,为了支持自定义权限,
     */
    @Autowired
    @Qualifier(value = "clientDetailsServiceImpl")
    private ClientDetailsService customClientDetailsService;

    /**
     * 令牌store
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 授权store
     *
     * @return
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 授权码store
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(customClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .approvalStore(approvalStore())
                .tokenStore(tokenStore())
                .accessTokenConverter(OpenHelper.buildAccessTokenConverter())
                .authorizationCodeServices(authorizationCodeServices())
                .tokenServices(defaultTokenServices());
        // 自定义确认授权页面
        endpoints.pathMapping("/oauth/confirm_access", "/confirm_access");
        // 自定义异常转换类
        endpoints.exceptionTranslator(new Oauth2WebResponseExceptionTranslator());
    }


    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        // 是否支持刷新令牌
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(customClientDetailsService);
        // token有效期自定义设置，默认12小时
        tokenServices.setAccessTokenValiditySeconds(AuthConstants.ACCESS_TOKEN_VALIDITY_SECONDS);
        //默认30天，这里修改
        tokenServices.setRefreshTokenValiditySeconds(AuthConstants.REFRESH_TOKEN_VALIDITY_SECONDS);
        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("permitAll()")
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()")
                //开启客户端授权
                .allowFormAuthenticationForClients();
    }

}
