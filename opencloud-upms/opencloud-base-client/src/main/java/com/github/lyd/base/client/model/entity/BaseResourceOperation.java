package com.github.lyd.base.client.model.entity;

import com.github.lyd.common.gen.SnowflakeId;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统资源-功能操作
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@Table(name = "base_resource_operation")
public class BaseResourceOperation implements Serializable {
    private static final long serialVersionUID = 1471599074044557390L;
    /**
     * 资源ID
     */
    @Id
    @Column(name = "operation_id")
    @KeySql(genId = SnowflakeId.class)
    private Long operationId;

    /**
     * 资源编码
     */
    @Column(name = "operation_code")
    private String operationCode;

    /**
     * 资源名称
     */
    @Column(name = "operation_name")
    private String operationName;

    /**
     * 资源父节点
     */
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * 优先级 越小越靠前
     */
    private Integer priority;

    /**
     * 资源描述
     */
    @Column(name = "operation_desc")
    private String operationDesc;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    @Column(name = "is_persist")
    private Integer isPersist;

    /**
     * 服务ID
     */
    @Column(name = "service_id")
    private String serviceId;

    /**
     * 获取资源ID
     *
     * @return operation_id - 资源ID
     */
    public Long getOperationId() {
        return operationId;
    }

    /**
     * 设置资源ID
     *
     * @param operationId 资源ID
     */
    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    /**
     * 获取资源编码
     *
     * @return operation_code - 资源编码
     */
    public String getOperationCode() {
        return operationCode;
    }

    /**
     * 设置资源编码
     *
     * @param operationCode 资源编码
     */
    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    /**
     * 获取资源名称
     *
     * @return operation_name - 资源名称
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * 设置资源名称
     *
     * @param operationName 资源名称
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * 获取资源父节点
     *
     * @return menu_id - 资源父节点
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * 设置资源父节点
     *
     * @param menuId 资源父节点
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取优先级 越小越靠前
     *
     * @return priority - 优先级 越小越靠前
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 设置优先级 越小越靠前
     *
     * @param priority 优先级 越小越靠前
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getIsPersist() {
        return isPersist;
    }

    public void setIsPersist(Integer isPersist) {
        this.isPersist = isPersist;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
