package com.github.lyd.gateway.provider.service.impl;

import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.gateway.client.model.GatewayIpLimitApisDto;
import com.github.lyd.gateway.client.model.entity.GatewayIpLimit;
import com.github.lyd.gateway.client.model.entity.GatewayIpLimitApi;
import com.github.lyd.gateway.provider.mapper.GatewayIpLimitApisMapper;
import com.github.lyd.gateway.provider.mapper.GatewayIpLimitMapper;
import com.github.lyd.gateway.provider.service.GatewayIpLimitService;
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
public class GatewayIpLimitServiceImpl implements GatewayIpLimitService {

    @Autowired
    private GatewayIpLimitMapper gatewayIpLimitMapper;
    @Autowired
    private GatewayIpLimitApisMapper gatewayIpLimitApisMapper;


    /**
     * 分页查询
     *
     * @param pageParams
     * @param keyword
     * @return
     */
    @Override
    public PageList<GatewayIpLimit> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        GatewayIpLimit query = pageParams.mapToObject(GatewayIpLimit.class);
        ExampleBuilder builder = new ExampleBuilder(GatewayIpLimit.class);
        Example example = builder.criteria().
                andLikeRight("policyName", query.getPolicyName()).
                andEqualTo("policyType", query.getPolicyType()).end().build();
        List<GatewayIpLimit> list = gatewayIpLimitMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询白名单
     *
     * @return
     */
    @Override
    public List<GatewayIpLimitApisDto> findBlackList() {
        List<GatewayIpLimitApisDto> list = gatewayIpLimitApisMapper.selectIpLimitApisDto(0);
        return list;
    }

    /**
     * 查询黑名单
     *
     * @return
     */
    @Override
    public List<GatewayIpLimitApisDto> findWhiteList() {
        List<GatewayIpLimitApisDto> list = gatewayIpLimitApisMapper.selectIpLimitApisDto(1);
        return list;
    }

    /**
     * 查询策略已绑定API列表
     *
     * @return
     */
    @Override
    public List<GatewayIpLimitApi> findIpLimitApiList(Long policyId) {
        GatewayIpLimitApi rateLimitApi = new GatewayIpLimitApi();
        rateLimitApi.setPolicyId(policyId);
        List<GatewayIpLimitApi> list = gatewayIpLimitApisMapper.select(rateLimitApi);
        return list;
    }

    /**
     * 获取IP限制策略
     *
     * @param policyId
     * @return
     */
    @Override
    public GatewayIpLimit getIpLimitPolicy(Long policyId) {
        return gatewayIpLimitMapper.selectByPrimaryKey(policyId);
    }

    /**
     * 添加IP限制策略
     *
     * @param policy
     */
    @Override
    public GatewayIpLimit addIpLimitPolicy(GatewayIpLimit policy) {
        policy.setCreateTime(new Date());
        policy.setUpdateTime(policy.getCreateTime());
        gatewayIpLimitMapper.insertSelective(policy);
        return policy;
    }

    /**
     * 更新IP限制策略
     *
     * @param policy
     */
    @Override
    public GatewayIpLimit updateIpLimitPolicy(GatewayIpLimit policy) {
        policy.setUpdateTime(new Date());
        gatewayIpLimitMapper.updateByPrimaryKeySelective(policy);
        return policy;
    }

    /**
     * 删除IP限制策略
     *
     * @param policyId
     */
    @Override
    public void removeIpLimitPolicy(Long policyId) {
        clearIpLimitApisByPolicyId(policyId);
        gatewayIpLimitMapper.deleteByPrimaryKey(policyId);
    }

    /**
     * 绑定API, 一个API只能绑定一个策略
     *
     * @param policyId
     * @param apis
     */
    @Override
    public void addIpLimitApis(Long policyId, String... apis) {
        // 先清空策略已有绑定
        clearIpLimitApisByPolicyId(policyId);
        if (apis != null && apis.length > 0) {
            List<GatewayIpLimitApi> list = Lists.newArrayList();
            for (String api : apis) {
                // 先api解除所有绑定, 一个API只能绑定一个策略
                Long apiId = Long.parseLong(api);
                clearIpLimitApisByApiId(apiId);
                GatewayIpLimitApi item = new GatewayIpLimitApi();
                item.setApiId(apiId);
                item.setPolicyId(policyId);
                // 重新绑定策略
                list.add(item);
            }
            gatewayIpLimitApisMapper.insertList(list);
        }
    }

    /**
     * 清空绑定的API
     *
     * @param policyId
     */
    @Override
    public void clearIpLimitApisByPolicyId(Long policyId) {
        GatewayIpLimitApi rateLimitApi = new GatewayIpLimitApi();
        rateLimitApi.setPolicyId(policyId);
        gatewayIpLimitApisMapper.delete(rateLimitApi);
    }

    /**
     * API解除所有策略
     *
     * @param apiId
     */
    @Override
    public void clearIpLimitApisByApiId(Long apiId) {
        GatewayIpLimitApi rateLimitApi = new GatewayIpLimitApi();
        rateLimitApi.setApiId(apiId);
        gatewayIpLimitApisMapper.delete(rateLimitApi);
    }
}
