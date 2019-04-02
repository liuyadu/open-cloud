package com.github.lyd.gateway.client.model.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author liuyadu
 */
@Table(name = "gateway_rate_limit_api")
public class GatewayRateLimitApi implements Serializable {
    /**
     * 限制数量
     */
    @Column(name = "policy_id")
    private Long policyId;

    /**
     * 时间间隔(秒)
     */
    @Column(name = "api_id")
    private Long apiId;


    private static final long serialVersionUID = 1L;

    /**
     * 获取限制数量
     *
     * @return policy_id - 限制数量
     */
    public Long getPolicyId() {
        return policyId;
    }

    /**
     * 设置限制数量
     *
     * @param policyId 限制数量
     */
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    /**
     * 获取时间间隔(秒)
     *
     * @return api_id - 时间间隔(秒)
     */
    public Long getApiId() {
        return apiId;
    }

    /**
     * 设置时间间隔(秒)
     *
     * @param apiId 时间间隔(秒)
     */
    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

}
