package com.github.lyd.base.client.model.entity;

import com.github.lyd.common.gen.SnowflakeId;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统用户-登录账号
 * @author liuyadu
 */
@Table(name = "base_user_account")
public class BaseUserAccount implements Serializable {
    private static final long serialVersionUID = -4484479600033295192L;
    @Id
    @Column(name = "account_id")
    @KeySql(genId = SnowflakeId.class)
    private Long accountId;

    /**
     * 系统用户Id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 标识：手机号、邮箱、 系统用户名、或第三方应用的唯一标识
     */
    private String account;

    /**
     * 密码凭证：站内的保存密码、站外的不保存或保存token）
     */
    private String password;

    /**
     * 登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等
     */
    @Column(name = "account_type")
    private String accountType;


    public BaseUserAccount() {
    }

    public BaseUserAccount(Long userId, String account, String password, String accountType) {
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.accountType = accountType;
    }

    /**
     * @return account_id
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}
