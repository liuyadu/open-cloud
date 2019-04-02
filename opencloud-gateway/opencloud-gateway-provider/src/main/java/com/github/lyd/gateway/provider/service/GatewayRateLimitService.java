package com.github.lyd.gateway.provider.service;

import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.gateway.client.model.GatewayRateLimitApisDto;
import com.github.lyd.gateway.client.model.entity.GatewayRateLimit;
import com.github.lyd.gateway.client.model.entity.GatewayRateLimitApi;

import java.util.List;

/**
 * 访问日志
 * @author liuyadu
 */
public interface GatewayRateLimitService {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<GatewayRateLimit> findListPage(PageParams pageParams);

    /**
     * 查询接口流量限制
     *
     * @return
     */
    List<GatewayRateLimitApisDto> findRateLimitApiList();

    /**
     * 查询策略已绑定API列表
     *
     * @return
     */
    List<GatewayRateLimitApi> findRateLimitApiList(Long policyId);

    /**
     * 获取限流策略
     *
     * @param policyId
     * @return
     */
    GatewayRateLimit getRateLimitPolicy(Long policyId);

    /**
     * 添加限流策略
     *
     * @param policy
     * @return
     */
    GatewayRateLimit addRateLimitPolicy(GatewayRateLimit policy);

    /**
     * 更新限流策略
     *
     * @param policy
     */
    GatewayRateLimit updateRateLimitPolicy(GatewayRateLimit policy);

    /**
     * 删除限流策略
     *
     * @param policyId
     */
    void removeRateLimitPolicy(Long policyId);

    /**
     * 绑定API, 一个API只能绑定一个策略
     *
     * @param policyId
     * @param apis
     */
    void addRateLimitApis(Long policyId, String... apis);

    /**
     * 清空绑定的API
     *
     * @param policyId
     */
    void clearRateLimitApisByPolicyId(Long policyId);

    /**
     * API解除所有策略
     *
     * @param apiId
     */
    void clearRateLimitApisByApiId(Long apiId);
}
