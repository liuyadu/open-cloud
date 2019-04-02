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
@Table(name = "gateway_ip_limit")
public class GatewayIpLimit implements Serializable {
    /**
     * 策略ID
     */
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
     * 策略类型:0-拒绝/黑名单 1-允许/白名单
     */
    @Column(name = "policy_type")
    private Integer policyType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最近一次修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * ip地址/IP段:多个用隔开;最多10个
     */
    @Column(name = "ip_address")
    private String ipAddress;

    private static final long serialVersionUID = 1L;

    /**
     * 获取策略ID
     *
     * @return policy_id - 策略ID
     */
    public Long getPolicyId() {
        return policyId;
    }

    /**
     * 设置策略ID
     *
     * @param policyId 策略ID
     */
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    /**
     * 获取策略名称
     *
     * @return policy_name - 策略名称
     */
    public String getPolicyName() {
        return policyName;
    }

    /**
     * 设置策略名称
     *
     * @param policyName 策略名称
     */
    public void setPolicyName(String policyName) {
        this.policyName = policyName == null ? null : policyName.trim();
    }

    /**
     * 获取策略类型:0-拒绝/黑名单 1-允许/白名单
     *
     * @return policy_type - 策略类型:0-拒绝/黑名单 1-允许/白名单
     */
    public Integer getPolicyType() {
        return policyType;
    }

    /**
     * 设置策略类型:0-拒绝/黑名单 1-允许/白名单
     *
     * @param policyType 策略类型:0-拒绝/黑名单 1-允许/白名单
     */
    public void setPolicyType(Integer policyType) {
        this.policyType = policyType;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最近一次修改时间
     *
     * @return update_time - 最近一次修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取ip地址/IP段:多个用,隔开,最多20个
     *
     * @return ip_address - ip地址/IP段:多个用,隔开,最多20个
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * 设置ip地址/IP段:多个用,隔开,最多20个
     *
     * @param ipAddress ip地址/IP段:多个用,隔开,最多20个
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }
}
