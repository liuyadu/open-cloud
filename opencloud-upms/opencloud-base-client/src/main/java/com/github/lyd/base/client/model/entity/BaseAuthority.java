package com.github.lyd.base.client.model.entity;

import com.github.lyd.common.gen.SnowflakeId;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统权限-菜单权限、操作权限、API权限
 */
@Table(name = "base_authority")
public class BaseAuthority implements Serializable {
    @Id
    @Column(name = "authority_id")
    @KeySql(genId = SnowflakeId.class)
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 菜单资源ID
     */
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * API资源ID
     */
    @Column(name = "api_id")
    private Long apiId;

    /**
     * 操作资源ID
     */
    @Column(name = "operation_id")
    private Long operationId;

    /**
     * 状态
     */
    private Integer status;


    private static final long serialVersionUID = 1L;

    /**
     * @return authority_id
     */
    public Long getAuthorityId() {
        return authorityId;
    }

    /**
     * @param authorityId
     */
    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    /**
     * 获取权限标识
     *
     * @return authority - 权限标识
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * 设置权限标识
     *
     * @param authority 权限标识
     */
    public void setAuthority(String authority) {
        this.authority = authority == null ? null : authority.trim();
    }

    /**
     * 获取菜单资源ID
     *
     * @return menu_id - 菜单资源ID
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * 设置菜单资源ID
     *
     * @param menuId 菜单资源ID
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取API资源ID
     *
     * @return api_id - API资源ID
     */
    public Long getApiId() {
        return apiId;
    }

    /**
     * 设置API资源ID
     *
     * @param apiId API资源ID
     */
    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    /**
     * @return operation_id
     */
    public Long getOperationId() {
        return operationId;
    }

    /**
     * @param operationId
     */
    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
