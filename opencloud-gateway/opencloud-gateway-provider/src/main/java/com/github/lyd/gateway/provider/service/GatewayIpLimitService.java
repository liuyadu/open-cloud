package com.github.lyd.gateway.provider.service;

import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.gateway.client.model.GatewayIpLimitApisDto;
import com.github.lyd.gateway.client.model.entity.GatewayIpLimit;
import com.github.lyd.gateway.client.model.entity.GatewayIpLimitApi;

import java.util.List;

/**
 * 网关IP访问控制
 *
 * @author liuyadu
 */
public interface GatewayIpLimitService {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<GatewayIpLimit> findListPage(PageParams pageParams);

    /**
     * 查询白名单
     *
     * @return
     */
    List<GatewayIpLimitApisDto> findBlackList();

    /**
     * 查询黑名单
     *
     * @return
     */
    List<GatewayIpLimitApisDto> findWhiteList();

    /**
     * 查询策略已绑定API列表
     *
     * @return
     */
    List<GatewayIpLimitApi> findIpLimitApiList(Long policyId);

    /**
     * 获取IP限制策略
     *
     * @param policyId
     * @return
     */
    GatewayIpLimit getIpLimitPolicy(Long policyId);

    /**
     * 添加IP限制策略
     *
     * @param policy
     * @return
     */
    GatewayIpLimit addIpLimitPolicy(GatewayIpLimit policy);

    /**
     * 更新IP限制策略
     *
     * @param policy
     */
    GatewayIpLimit updateIpLimitPolicy(GatewayIpLimit policy);

    /**
     * 删除IP限制策略
     *
     * @param policyId
     */
    void removeIpLimitPolicy(Long policyId);

    /**
     * 绑定API, 一个API只能绑定一个策略
     *
     * @param policyId
     * @param apis
     */
    void addIpLimitApis(Long policyId, String... apis);

    /**
     * 清空绑定的API
     *
     * @param policyId
     */
    void clearIpLimitApisByPolicyId(Long policyId);

    /**
     * API解除所有策略
     *
     * @param apiId
     */
    void clearIpLimitApisByApiId(Long apiId);

}
