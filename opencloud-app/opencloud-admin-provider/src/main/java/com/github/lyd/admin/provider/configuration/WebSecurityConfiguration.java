package com.github.lyd.admin.provider.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Web服务配置类
 * @author: liuyadu
 * @date: 2018/10/23 10:31
 * @description:
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/", "/**/v2/api-docs/**", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/**/favicon.ico");
    }

}
