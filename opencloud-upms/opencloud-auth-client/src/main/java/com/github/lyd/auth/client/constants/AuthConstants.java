package com.github.lyd.auth.client.constants;

/**
 * @author: liuyadu
 * @date: 2018/11/2 18:15
 * @description:
 */
public class AuthConstants {

    /**
     * 服务名称
     */
    public static final String AUTH_SERVICE = "opencloud-auth-provider";

    // token有效期，默认12小时
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;
    // token有效期，默认7天
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 7;

    /**
     * 第三方登录
     */
    public static final String LOGIN_GITEE = "gitee";
    public static final String LOGIN_QQ = "qq";
    public static final String LOGIN_WECHAT = "wechat";

    /**
     * 自定义第三方登录请求头
     */
    public static final String HEADER_X_THIRDPARTY_LOGIN = "X-ThirdParty-Login";
}
