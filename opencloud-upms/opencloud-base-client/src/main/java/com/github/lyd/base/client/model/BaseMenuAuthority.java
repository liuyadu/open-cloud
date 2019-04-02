package com.github.lyd.base.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.lyd.base.client.model.entity.BaseResourceMenu;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限
 * @author liuyadu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseMenuAuthority extends BaseResourceMenu implements Serializable {

    private static final long serialVersionUID = 3474271304324863160L;
    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;


    private List<BaseOperationAuthority> operationList;

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


    public List<BaseOperationAuthority> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<BaseOperationAuthority> operationList) {
        this.operationList = operationList;
    }
}
