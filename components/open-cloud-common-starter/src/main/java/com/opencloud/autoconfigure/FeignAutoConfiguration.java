package com.opencloud.autoconfigure;

import com.opencloud.common.interceptor.FeignRequestInterceptor;
import feign.Request;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign OAuth2 request interceptor.
 *
 * @author liuyadu
 */
@Slf4j
@Configuration
public class FeignAutoConfiguration {
    public static int connectTimeOutMillis = 12000;
    public static int readTimeOutMillis = 12000;

    @Bean
    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        Encoder encoder = new FeignSpringFormEncoder(new SpringEncoder(messageConverters));
        log.info("FeignSpringFormEncoder [{}]", encoder);
        return encoder;
    }

    @Bean
    @ConditionalOnMissingBean(FeignRequestInterceptor.class)
    public RequestInterceptor feignRequestInterceptor() {
        FeignRequestInterceptor interceptor = new FeignRequestInterceptor();
        log.info("FeignRequestInterceptor [{}]", interceptor);
        return interceptor;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeOutMillis, readTimeOutMillis);
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default();
    }

}
