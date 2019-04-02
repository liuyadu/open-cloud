package com.github.lyd.base.client.model.entity;

import com.github.lyd.common.gen.SnowflakeId;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统资源-API接口
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@Table(name = "base_resource_api")
public class BaseResourceApi implements Serializable {
    private static final long serialVersionUID = -9099562653030770650L;
    /**
     * 资源ID
     */
    @Id
    @Column(name = "api_id")
    @KeySql(genId = SnowflakeId.class)
    private Long apiId;

    /**
     * 资源编码
     */
    @Column(name = "api_code")
    private String apiCode;

    /**
     * 资源名称
     */
    @Column(name = "api_name")
    private String apiName;

    /**
     * 服务ID
     */
    @Column(name = "service_id")
    private String serviceId;

    /**
     * 接口分类
     */
    @Column(name = "api_category")
    private String apiCategory;
    /**
     * 资源路径
     */
    private String path;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 资源描述
     */
    @Column(name = "api_desc")
    private String apiDesc;

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
     * 是否为开放接口：0-否  1-是
     */
    @Column(name = "is_open")
    private Integer isOpen;

    /**
     * 安全认证:0-否 1-是 默认:1
     */
    @Column(name = "is_auth")
    private Integer isAuth;

    /**
     * 请求方式
     */
    @Column(name = "request_method")
    private String requestMethod;
    /**
     * 响应类型
     */
    @Column(name = "content_type")
    private String contentType;

    /**
     * 类名
     */
    @Column(name = "class_name")
    private String className;

    /**
     * 方法名
     */
    @Column(name = "method_name")
    private String methodName;

    /**
     * 获取资源ID
     *
     * @return api_id - 资源ID
     */
    public Long getApiId() {
        return apiId;
    }

    /**
     * 设置资源ID
     *
     * @param apiId 资源ID
     */
    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    /**
     * 获取资源编码
     *
     * @return api_code - 资源编码
     */
    public String getApiCode() {
        return apiCode;
    }

    /**
     * 设置资源编码
     *
     * @param apiCode 资源编码
     */
    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    /**
     * 获取资源名称
     *
     * @return api_name - 资源名称
     */
    public String getApiName() {
        return apiName;
    }

    /**
     * 设置资源名称
     *
     * @param apiName 资源名称
     */
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    /**
     * 获取服务ID
     *
     * @return server_id - 服务ID
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * 设置服务ID
     *
     * @param serviceId 服务ID
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取优先级
     *
     * @return priority - 优先级
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 设置优先级
     *
     * @param priority 优先级
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
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

    public String getApiCategory() {
        return apiCategory;
    }

    public void setApiCategory(String apiCategory) {
        this.apiCategory = apiCategory;
    }

    public Integer getIsPersist() {
        return isPersist;
    }

    public void setIsPersist(Integer isPersist) {
        this.isPersist = isPersist;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
