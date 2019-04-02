package com.github.lyd.base.client.model;

import com.github.lyd.base.client.model.entity.BaseUserAccount;

import java.io.Serializable;

/**
 * @author: liuyadu
 * @date: 2018/11/12 11:35
 * @description:
 */
public class BaseUserAccountDto extends BaseUserAccount implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;

    /**
     * 系统用户资料
     */
    private BaseUserDto userProfile;

    public BaseUserDto getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(BaseUserDto userProfile) {
        this.userProfile = userProfile;
    }
}
