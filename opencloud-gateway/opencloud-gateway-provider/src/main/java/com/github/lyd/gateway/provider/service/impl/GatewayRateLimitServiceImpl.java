package com.github.lyd.gateway.provider.service.impl;

import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.gateway.client.model.GatewayRateLimitApisDto;
import com.github.lyd.gateway.client.model.entity.GatewayIpLimit;
import com.github.lyd.gateway.client.model.entity.GatewayRateLimit;
import com.github.lyd.gateway.client.model.entity.GatewayRateLimitApi;
import com.github.lyd.gateway.provider.mapper.GatewayRateLimitApisMapper;
import com.github.lyd.gateway.provider.mapper.GatewayRateLimitMapper;
import com.github.lyd.gateway.provider.service.GatewayRateLimitService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GatewayRateLimitServiceImpl implements GatewayRateLimitService {
    @Autowired
    private GatewayRateLimitMapper gatewayRateLimitMapper;

    @Autowired
    private GatewayRateLimitApisMapper gatewayRateLimitApisMapper;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<GatewayRateLimit> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        GatewayRateLimit query = pageParams.mapToObject(GatewayRateLimit.class);
        ExampleBuilder builder = new ExampleBuilder(GatewayIpLimit.class);
        Example example = builder.criteria().
                andLikeRight("policyName", query.getPolicyName()).
                andEqualTo("limitType", query.getLimitType()).end().build();
        List<GatewayRateLimit> list = gatewayRateLimitMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询接口流量限制
     *
     * @return
     */
    @Override
    public List<GatewayRateLimitApisDto> findRateLimitApiList() {
        List<GatewayRateLimitApisDto> list = gatewayRateLimitApisMapper.selectRateLimitApisDto();
        return list;
    }

    /**
     * 查询策略已绑定API列表
     *
     * @param policyId
     * @return
     */
    @Override
    public List<GatewayRateLimitApi> findRateLimitApiList(Long policyId) {
        GatewayRateLimitApi rateLimitApi = new GatewayRateLimitApi();
        rateLimitApi.setPolicyId(policyId);
        List<GatewayRateLimitApi> list = gatewayRateLimitApisMapper.select(rateLimitApi);
        return list;
    }

    /**
     * 获取IP限制策略
     *
     * @param policyId
     * @return
     */
    @Override
    public GatewayRateLimit getRateLimitPolicy(Long policyId) {
        return gatewayRateLimitMapper.selectByPrimaryKey(policyId);
    }

    /**
     * 添加IP限制策略
     *
     * @param policy
     */
    @Override
    public GatewayRateLimit addRateLimitPolicy(GatewayRateLimit policy) {
        policy.setCreateTime(new Date());
        policy.setUpdateTime(policy.getCreateTime());
        gatewayRateLimitMapper.insertSelective(policy);
        return policy;
    }

    /**
     * 更新IP限制策略
     *
     * @param policy
     */
    @Override
    public GatewayRateLimit updateRateLimitPolicy(GatewayRateLimit policy) {
        policy.setUpdateTime(new Date());
        gatewayRateLimitMapper.updateByPrimaryKeySelective(policy);
        return policy;
    }

    /**
     * 删除IP限制策略
     *
     * @param policyId
     */
    @Override
    public void removeRateLimitPolicy(Long policyId) {
        clearRateLimitApisByPolicyId(policyId);
        gatewayRateLimitMapper.deleteByPrimaryKey(policyId);
    }

    /**
     * 绑定API, 一个API只能绑定一个策略
     *
     * @param policyId
     * @param apis
     */
    @Override
    public void addRateLimitApis(Long policyId, String... apis) {
        // 先清空策略已有绑定
        clearRateLimitApisByPolicyId(policyId);
        if (apis != null && apis.length > 0) {
            List<GatewayRateLimitApi> list = Lists.newArrayList();
            for (String api : apis) {
                Long apiId = Long.parseLong(api);
                // 先api解除所有绑定, 一个API只能绑定一个策略
                clearRateLimitApisByApiId(apiId);
                GatewayRateLimitApi item = new GatewayRateLimitApi();
                item.setApiId(apiId);
                item.setPolicyId(policyId);
                // 重新绑定策略
                list.add(item);
            }
            gatewayRateLimitApisMapper.insertList(list);
        }
    }

    /**
     * 清空绑定的API
     *
     * @param policyId
     */
    @Override
    public void clearRateLimitApisByPolicyId(Long policyId) {
        GatewayRateLimitApi rateLimitApi = new GatewayRateLimitApi();
        rateLimitApi.setPolicyId(policyId);
        gatewayRateLimitApisMapper.delete(rateLimitApi);
    }

    /**
     * API解除所有策略
     *
     * @param apiId
     */
    @Override
    public void clearRateLimitApisByApiId(Long apiId) {
        GatewayRateLimitApi rateLimitApi = new GatewayRateLimitApi();
        rateLimitApi.setApiId(apiId);
        gatewayRateLimitApisMapper.delete(rateLimitApi);
    }
}
