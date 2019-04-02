/*
 * MIT License
 *
 * Copyright (c) 2018 yadu.liu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.github.lyd.gateway.provider;

import com.github.lyd.gateway.provider.event.GatewayRefreshRemoteApplicationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * 网关服务
 * 提供微服务统一外部调用入口、身份认证(@EnableResourceServer)、接口调用鉴权、接口限流、黑白名单
 * 开发环境下提供在线调试文档.
 *
 * @author liuyadu
 */
@EnableZuulProxy
@EnableFeignClients
@EnableDiscoveryClient
@SpringCloudApplication
@RemoteApplicationEventScan(basePackages = "com.github.lyd.gateway.provider.event")
@MapperScan(basePackages = "com.github.lyd.gateway.provider.mapper")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * 项目启动完成加载类
     */
    @Component
    @Order(value = 1)
    public class MyApplicationRunner implements ApplicationRunner {
        @Autowired
        private ApplicationContext context;
        @Autowired
        private BusProperties bus;

        @Override
        public void run(ApplicationArguments var1) throws Exception {
            context.publishEvent(new GatewayRefreshRemoteApplicationEvent(context, bus.getId(), null));
        }

    }

}
