package com.github.lyd.gateway.provider.event;

import com.github.lyd.gateway.provider.locator.AccessLocator;
import com.github.lyd.gateway.provider.locator.ZuulRouteLocator;
import org.springframework.context.ApplicationListener;

/**
 * 自定义动态限流刷新事件监听
 *
 * @author liuyadu
 */
public class GatewayRefreshRemoteListener implements ApplicationListener<GatewayRefreshRemoteApplicationEvent> {

    /**
     * 路由加载器
     */
    private ZuulRouteLocator routesLocator;

    /**
     * 权限加载器
     */
    private AccessLocator permissionLocator;

    public GatewayRefreshRemoteListener(ZuulRouteLocator routesLocator, AccessLocator permissionLocator) {
        this.routesLocator = routesLocator;
        this.permissionLocator = permissionLocator;
    }


    public ZuulRouteLocator getRoutesLocator() {
        return routesLocator;
    }

    public void setRoutesLocator(ZuulRouteLocator routesLocator) {
        this.routesLocator = routesLocator;
    }

    @Override
    public void onApplicationEvent(GatewayRefreshRemoteApplicationEvent event) {
        //重载路由
        routesLocator.doRefresh();
        //重载权限
        permissionLocator.doRefresh();
    }
}
