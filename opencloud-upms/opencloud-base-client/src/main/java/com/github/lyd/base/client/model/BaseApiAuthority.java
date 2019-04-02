package com.github.lyd.base.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.lyd.base.client.model.entity.BaseResourceApi;

import java.io.Serializable;
import java.util.Date;

/**
 * API权限
 * @author liuyadu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseApiAuthority extends BaseResourceApi implements Serializable {

    private static final long serialVersionUID = 3474271304324863160L;
    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 服务名
     */
    private String serviceId;

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
