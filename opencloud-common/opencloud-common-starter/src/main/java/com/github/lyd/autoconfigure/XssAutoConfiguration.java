package com.github.lyd.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.lyd.common.filter.XssFilter;
import com.github.lyd.common.filter.XssStringJsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author: liuyadu
 * @date: 2019/2/21 17:53
 * @description:
 */
@Configuration
@Slf4j
public class XssAutoConfiguration {

    @Bean
    public FilterRegistrationBean xxsFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new XssFilter());
        log.info("bean [{}]",bean);
        return bean;
    }
    /**
     * 过滤json类型的
     *
     * @param builder
     * @return
     */
    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        //解析器
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        //注册xss解析器
        SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer");
        xssModule.addSerializer(new XssStringJsonSerializer());
        objectMapper.registerModule(xssModule);
        //返回
        log.info("bean [{}]",objectMapper);
        return objectMapper;
    }

}
