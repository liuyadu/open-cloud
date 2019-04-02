package com.github.lyd.base.client.model.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统角色-授权
 */
@Table(name = "base_role_authority")
public class BaseRoleAuthority implements Serializable {
    /**
     * 权限ID
     */
    @Column(name = "authority_id")
    private Long authorityId;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 过期时间:null表示长期
     */
    @Column(name = "expire_time")
    private Date expireTime;

    private static final long serialVersionUID = 1L;

    /**
     * 获取权限ID
     *
     * @return authority_id - 权限ID
     */
    public Long getAuthorityId() {
        return authorityId;
    }

    /**
     * 设置权限ID
     *
     * @param authorityId 权限ID
     */
    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    /**
     * 获取角色ID
     *
     * @return role_id - 角色ID
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 设置角色ID
     *
     * @param roleId 角色ID
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取过期时间:null表示长期
     *
     * @return expire_time - 过期时间:null表示长期
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * 设置过期时间:null表示长期
     *
     * @param expireTime 过期时间:null表示长期
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
