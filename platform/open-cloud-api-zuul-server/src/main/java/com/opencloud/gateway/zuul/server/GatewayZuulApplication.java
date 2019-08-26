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
package com.opencloud.gateway.zuul.server;

import com.opencloud.gateway.zuul.server.locator.ResourceLocator;
import com.opencloud.gateway.zuul.server.locator.JdbcRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * 网关服务
 * 接口调用统一入口、数字验签、身份认证、接口鉴权、接口限流、黑白名单限制
 * 开发环境下提供在线调试文档.
 * @author liuyadu
 */
@EnableZuulProxy
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@RemoteApplicationEventScan(basePackages = "com.opencloud")
public class GatewayZuulApplication implements CommandLineRunner {
    @Autowired
    private ResourceLocator resourceLocator;
    @Autowired
    private JdbcRouteLocator jdbcRouteLocator;

    public static void main(String[] args) {
        SpringApplication.run(GatewayZuulApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        jdbcRouteLocator.doRefresh();
        resourceLocator.refresh();
    }


}
