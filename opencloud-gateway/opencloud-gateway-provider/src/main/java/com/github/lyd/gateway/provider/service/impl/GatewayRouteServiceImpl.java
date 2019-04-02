package com.github.lyd.gateway.provider.service.impl;

import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.utils.StringUtils;
import com.github.lyd.gateway.client.model.entity.GatewayRoute;
import com.github.lyd.gateway.provider.mapper.GatewayRouteMapper;
import com.github.lyd.gateway.provider.service.GatewayRouteService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GatewayRouteServiceImpl implements GatewayRouteService {

    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<GatewayRoute> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        ExampleBuilder builder = new ExampleBuilder(GatewayRoute.class);
        Example example = builder.criteria().end().build();
        List<GatewayRoute> list = gatewayRouteMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询可用路由列表
     *
     * @return
     */
    @Override
    public List<GatewayRoute> findRouteList() {
        ExampleBuilder builder = new ExampleBuilder(GatewayRoute.class);
        Example example = builder.criteria().andEqualTo("status", BaseConstants.ENABLED).end().build();
        List<GatewayRoute> list = gatewayRouteMapper.selectByExample(example);
        return list;
    }

    /**
     * 获取路由信息
     *
     * @param routeId
     * @return
     */
    @Override
    public GatewayRoute getRoute(Long routeId) {
        return gatewayRouteMapper.selectByPrimaryKey(routeId);
    }

    /**
     * 添加路由
     *
     * @param route
     */
    @Override
    public GatewayRoute addRoute(GatewayRoute route) {
        if (StringUtils.isBlank(route.getPath())) {
            throw new OpenAlertException(String.format("path不能为空!"));
        }
        if (isExist(route.getPath())) {
            throw new OpenAlertException(String.format("path已存在!"));
        }
        route.setIsPersist(0);
        gatewayRouteMapper.insertSelective(route);
        return route;
    }

    /**
     * 更新路由
     *
     * @param route
     */
    @Override
    public GatewayRoute updateRoute(GatewayRoute route) {
        if (StringUtils.isBlank(route.getPath())) {
            throw new OpenAlertException(String.format("path不能为空"));
        }
        GatewayRoute saved = getRoute(route.getRouteId());
        if (saved == null) {
            throw new OpenAlertException("路由信息不存在!");
        }
        if (saved != null && saved.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许修改"));
        }
        if (!saved.getPath().equals(route.getPath())) {
            // 和原来不一致重新检查唯一性
            if (isExist(route.getPath())) {
                throw new OpenAlertException("path已存在!");
            }
        }
        gatewayRouteMapper.updateByPrimaryKeySelective(route);
        return route;
    }

    /**
     * 删除路由
     *
     * @param routeId
     */
    @Override
    public void removeRoute(Long routeId) {
        GatewayRoute saved = getRoute(routeId);
        if (saved != null && saved.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        gatewayRouteMapper.deleteByPrimaryKey(routeId);
    }

    /**
     * 查询地址是否存在
     *
     * @param path
     */
    @Override
    public Boolean isExist(String path) {
        GatewayRoute query = new GatewayRoute();
        query.setPath(path);
        int count = gatewayRouteMapper.selectCount(query);
        return count > 0;
    }
}
