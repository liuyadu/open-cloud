package com.github.lyd.gateway.provider.controller;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.gateway.provider.locator.ZuulRouteLocator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "网关服务开放接口")
@RestController
public class GatewayController {
    @Autowired
    private ZuulRouteLocator zuulRoutesLocator;
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Value("${spring.application.name}")
    private String serviceId;
    @Value("${spring.cloud.nacos.discovery.metadata.name}")
    private String serviceName;

    @ApiOperation(value = "获取服务列表", notes = "获取服务列表")
    @GetMapping("/service/list")
    public ResultBody getServiceList() {
        List<Map> services = Lists.newArrayList();
        Map gateway = Maps.newHashMap();
        gateway.put("serviceId", serviceId);
        gateway.put("serviceName", serviceName);
        services.add(gateway);

        List<Route> routes = zuulRoutesLocator.getRoutes();
        if (routes != null && routes.size() > 0) {
            routes.forEach(route -> {
                try {
                    // 获取健康服务元数据中文名称 bootstrap.properties -> spring.cloud.nacos.discovery.metadata.name=网关服务
                    Instance instance = nacosDiscoveryProperties.namingServiceInstance().selectOneHealthyInstance(route.getId());
                    String name = instance.getMetadata().get("name");
                    if (name == null) {
                        name = route.getId();
                    }
                    Map service = Maps.newHashMap();
                    service.put("serviceId", route.getId());
                    service.put("serviceName", name);
                    services.add(service);
                } catch (Exception e) {
                }
            });
        }

        return ResultBody.success(services);
    }
}
