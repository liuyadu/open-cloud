package com.github.lyd.gateway.provider.controller;

import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.gateway.client.model.entity.GatewayRoute;
import com.github.lyd.gateway.provider.service.GatewayRouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 网关智能路由
 *
 * @author: liuyadu
 * @date: 2019/3/12 15:12
 * @description:
 */
@Api(tags = "网关智能路由")
@RestController
public class GatewayRouteController {

    @Autowired
    private GatewayRouteService gatewayRouteService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页路由列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页路由列表", notes = "获取分页路由列表")
    @GetMapping("/gateway/route")
    public ResultBody<PageList<GatewayRoute>> getRouteListPage(@RequestParam Map map) {
        return ResultBody.success(gatewayRouteService.findListPage(new PageParams(map)));
    }


    /**
     * 获取路由
     *
     * @param routeId
     * @return
     */
    @ApiOperation(value = "获取路由", notes = "获取路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeId", required = true, value = "路由ID", paramType = "path"),
    })
    @GetMapping("/gateway/route/{routeId}/info")
    public ResultBody<GatewayRoute> getRoute(@PathVariable("routeId") Long routeId) {
        return ResultBody.success(gatewayRouteService.getRoute(routeId));
    }

    /**
     * 添加路由
     *
     * @param path        路径表达式
     * @param serviceId   服务名方转发
     * @param url         地址转发
     * @param stripPrefix 忽略前缀
     * @param retryable   支持重试
     * @param status      是否启用
     * @param routeDesc   描述
     * @return
     */
    @ApiOperation(value = "添加路由", notes = "添加路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", required = true, value = "路径表达式", paramType = "form"),
            @ApiImplicitParam(name = "serviceId", required = false, value = "服务名方转发", paramType = "form"),
            @ApiImplicitParam(name = "url", required = false, value = "地址转发", paramType = "form"),
            @ApiImplicitParam(name = "stripPrefix", required = false, allowableValues = "0,1", defaultValue = "1", value = "忽略前缀", paramType = "form"),
            @ApiImplicitParam(name = "retryable", required = false, allowableValues = "0,1", defaultValue = "0", value = "支持重试", paramType = "form"),
            @ApiImplicitParam(name = "status", required = false, allowableValues = "0,1", defaultValue = "1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "routeDesc", required = false, value = "描述", paramType = "form")
    })
    @PostMapping("/gateway/route/add")
    public ResultBody<Long> addRoute(
            @RequestParam(value = "path") String path,
            @RequestParam(value = "serviceId", required = false) String serviceId,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "stripPrefix", required = false, defaultValue = "1") Integer stripPrefix,
            @RequestParam(value = "retryable", required = false, defaultValue = "0") Integer retryable,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "routeDesc", required = false, defaultValue = "") String routeDesc
    ) {
        GatewayRoute route = new GatewayRoute();
        route.setPath(path);
        route.setServiceId(serviceId);
        route.setUrl(url);
        route.setRetryable(retryable);
        route.setStripPrefix(stripPrefix);
        route.setStatus(status);
        route.setRouteDesc(routeDesc);
        Long routeId = null;
        GatewayRoute result = gatewayRouteService.addRoute(route);
        if (result != null) {
            routeId = result.getRouteId();
            // 刷新网关
            openRestTemplate.refreshGateway();
        }
        return ResultBody.success(routeId);
    }

    /**
     * 编辑路由
     *
     * @param routeId     路由ID
     * @param path        路径表达式
     * @param serviceId   服务名方转发
     * @param url         地址转发
     * @param stripPrefix 忽略前缀
     * @param retryable   支持重试
     * @param status      是否启用
     * @param routeDesc   描述
     * @return
     */
    @ApiOperation(value = "编辑路由", notes = "编辑路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeId", required = true, value = "路由Id", paramType = "form"),
            @ApiImplicitParam(name = "path", required = true, value = "路径表达式", paramType = "form"),
            @ApiImplicitParam(name = "serviceId", required = false, value = "服务名方转发", paramType = "form"),
            @ApiImplicitParam(name = "url", required = false, value = "地址转发", paramType = "form"),
            @ApiImplicitParam(name = "stripPrefix", required = false, allowableValues = "0,1", defaultValue = "1", value = "忽略前缀", paramType = "form"),
            @ApiImplicitParam(name = "retryable", required = false, allowableValues = "0,1", defaultValue = "0", value = "支持重试", paramType = "form"),
            @ApiImplicitParam(name = "status", required = false, allowableValues = "0,1", defaultValue = "1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "routeDesc", required = false, value = "描述", paramType = "form")
    })
    @PostMapping("/gateway/route/update")
    public ResultBody updateRoute(
            @RequestParam("routeId") Long routeId,
            @RequestParam(value = "path") String path,
            @RequestParam(value = "serviceId", required = false) String serviceId,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "stripPrefix", required = false, defaultValue = "1") Integer stripPrefix,
            @RequestParam(value = "retryable", required = false, defaultValue = "0") Integer retryable,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "routeDesc", required = false, defaultValue = "") String routeDesc
    ) {
        GatewayRoute route = new GatewayRoute();
        route.setRouteId(routeId);
        route.setPath(path);
        route.setServiceId(serviceId);
        route.setUrl(url);
        route.setRetryable(retryable);
        route.setStripPrefix(stripPrefix);
        route.setStatus(status);
        route.setRouteDesc(routeDesc);
        gatewayRouteService.updateRoute(route);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }


    /**
     * 移除路由
     *
     * @param routeId
     * @return
     */
    @ApiOperation(value = "移除路由", notes = "移除路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeId", required = true, value = "routeId", paramType = "form"),
    })
    @PostMapping("/gateway/route/remove")
    public ResultBody removeRoute(
            @RequestParam("routeId") Long routeId
    ) {
        gatewayRouteService.removeRoute(routeId);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }
}
