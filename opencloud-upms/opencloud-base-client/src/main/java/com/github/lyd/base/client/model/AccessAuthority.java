package com.github.lyd.base.client.model;

import java.io.Serializable;

/**
 * 访问权限
 */
public class AccessAuthority implements Serializable {
    private static final long serialVersionUID = -320031660125425711L;

    /**
     * 访问路径
     */
    private String path;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 是否身份认证
     */
    private Integer isAuth;

    /**
     * 服务名称
     */
    private String serviceId;

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

}
