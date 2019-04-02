package com.github.lyd.gateway.client.model.entity;

import com.github.lyd.common.gen.SnowflakeId;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liuyadu
 */
@Table(name = "gateway_rate_limit")
public class GatewayRateLimit implements Serializable {
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "policy_id")
    private Long policyId;

    /**
     * 策略名称
     */
    @Column(name = "policy_name")
    private String policyName;

    /**
     * 服务名称
     */
    @Column(name = "service_id")
    private String serviceId;
    /**
     * 限制数
     */
    private Long limit;

    /**
     * 单位时间:second-秒,minute-分钟,hour-小时,day-天
     */
    @Column(name = "interval_unit")
    private String intervalUnit;

    /**
     * 限流规则类型:url,origin,user
     */
    @Column(name = "limit_type")
    private String limitType;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;


    /**
     * 获取流量限制
     *
     * @return limit - 流量限制
     */
    public Long getLimit() {
        return limit;
    }

    /**
     * 设置流量限制
     *
     * @param limit 流量限制
     */
    public void setLimit(Long limit) {
        this.limit = limit;
    }


    /**
     * 获取时间单位:second-秒,minute-分钟,hour-小时,day-天
     *
     * @return interval_unit - 时间单位:second-秒,minute-分钟,hour-小时,day-天
     */
    public String getIntervalUnit() {
        return intervalUnit;
    }

    /**
     * 设置时间单位:second-秒,minute-分钟,hour-小时,day-天
     *
     * @param intervalUnit 时间单位:second-秒,minute-分钟,hour-小时,day-天
     */
    public void setIntervalUnit(String intervalUnit) {
        this.intervalUnit = intervalUnit == null ? null : intervalUnit.trim();
    }


    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }
}
