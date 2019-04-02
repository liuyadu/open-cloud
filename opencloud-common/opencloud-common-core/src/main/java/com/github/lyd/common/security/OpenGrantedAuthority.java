package com.github.lyd.common.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 自定义已授权权限标识
 * @author liuyadu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class OpenGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = -4682269495406460314L;

    /**
     * 权限Id
     */
    private String authorityId;
    /**
     * 权限标识
     */
    private String authority;
    /**
     * 过期时间,用于判断权限是否已过期
     */
    private Date expireTime;

    /**
     * 权限所有者
     */
    private String owner;

    public Boolean getIsExpired() {
        if (expireTime != null && System.currentTimeMillis() > expireTime.getTime()) {
            return true;
        }
        return false;
    }

    public OpenGrantedAuthority() {
    }

    public OpenGrantedAuthority(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.authority = role;
    }

    public OpenGrantedAuthority(String role, Date expireTime) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.authority = role;
        this.expireTime = expireTime;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof OpenGrantedAuthority ? this.authority.equals(((OpenGrantedAuthority) obj).authority) : false;
        }
    }

    @Override
    public int hashCode() {
        return this.authority.hashCode();
    }

    @Override
    public String toString() {
        return this.authority;
    }

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
