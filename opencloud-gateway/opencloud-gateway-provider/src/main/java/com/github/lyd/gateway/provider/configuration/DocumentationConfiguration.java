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
package com.github.lyd.gateway.provider.configuration;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.github.lyd.common.configuration.SwaggerProperties;
import com.github.lyd.gateway.provider.locator.ZuulRouteLocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger在线文档配置
 * 聚合网关服务代理的所有微服务
 *
 * @author admin
 */
@Component
@Primary
@Slf4j
public class DocumentationConfiguration implements SwaggerResourcesProvider {

    private SwaggerProperties swaggerProperties;

    private ZuulRouteLocator zuulRoutesLocator;

    private NamingService namingService;

    public DocumentationConfiguration() {
    }

    @Autowired
    public DocumentationConfiguration(SwaggerProperties swaggerProperties, ZuulRouteLocator zuulRoutesLocator, NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.swaggerProperties = swaggerProperties;
        this.zuulRoutesLocator = zuulRoutesLocator;
        this.namingService = nacosDiscoveryProperties.namingServiceInstance();
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource(swaggerProperties.getTitle(), "/v2/api-docs", "2.0"));
        List<Route> routes = zuulRoutesLocator.getRoutes();
        routes.forEach(route -> {
            // 只加载未被忽略的服务
            if (!swaggerProperties.getIgnores().contains(route.getId())) {
                try {
                    // 获取健康服务元数据中文名称 bootstrap.properties -> spring.cloud.nacos.discovery.metadata.name=网关服务
                    Instance instance = namingService.selectOneHealthyInstance(route.getId());
                    String name = instance.getMetadata().get("name");
                    if (name == null) {
                        name = route.getId();
                    }
                    resources.add(swaggerResource(name, route.getFullPath().replace("**", "v2/api-docs"), "2.0"));
                } catch (Exception e) {
                    log.error("加载文档出错:{}", e.getMessage());
                }
            }
        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
