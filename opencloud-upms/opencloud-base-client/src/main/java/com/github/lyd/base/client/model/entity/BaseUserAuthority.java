package com.github.lyd.base.client.model.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户-授权
 */
@Table(name = "base_user_authority")
public class BaseUserAuthority implements Serializable {
    /**
     * 权限ID
     */
    @Column(name = "authority_id")
    private Long authorityId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 过期时间
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
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取过期时间
     *
     * @return expire_time - 过期时间
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * 设置过期时间
     *
     * @param expireTime 过期时间
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
