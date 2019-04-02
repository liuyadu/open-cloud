package com.github.lyd.gateway.provider.service;

import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.gateway.client.model.entity.GatewayRoute;

import java.util.List;

/**
 * 路由管理
 *
 * @author liuyadu
 */
public interface GatewayRouteService {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<GatewayRoute> findListPage(PageParams pageParams);

    /**
     * 查询可用路由列表
     *
     * @return
     */
    List<GatewayRoute> findRouteList();

    /**
     * 获取路由信息
     *
     * @param routeId
     * @return
     */
    GatewayRoute getRoute(Long routeId);

    /**
     * 添加路由
     *
     * @param route
     */
    GatewayRoute addRoute(GatewayRoute route);

    /**
     * 更新路由
     *
     * @param route
     */
    GatewayRoute updateRoute(GatewayRoute route);

    /**
     * 删除路由
     *
     * @param routeId
     */
    void removeRoute(Long routeId);

    /**
     * 是否存在
     *
     * @param path
     * @return
     */
    Boolean isExist(String path);
}
