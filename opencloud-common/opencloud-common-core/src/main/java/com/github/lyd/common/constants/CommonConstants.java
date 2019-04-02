package com.github.lyd.common.constants;

/**
 * @author liuyadu
 */
public class CommonConstants {
    /**
     * 自定义错误
     */
    public static final String X_ERROR = "x.servlet.exception.code";
    public static final String X_ERROR_CODE = "x.servlet.exception.error";
    public static final String X_ERROR_MESSAGE = "x.servlet.exception.message";
    public static final String X_ACCESS_DENIED = "x.access.denied";
    /**
     * 默认超级管理员账号
     */
    public final static String ROOT = "admin";

    /**
     * 端点监控权限标识
     */
    public final static String AUTHORITY_ACTUATOR = "actuator";

    /**
     * 默认最小页码
     */
    public static final int MIN_PAGE = 0;
    /**
     * 最大显示条数
     */
    public static final int MAX_LIMIT = 999;
    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE = 1;
    /**
     * 默认显示条数
     */
    public static final int DEFAULT_LIMIT = 10;
    /**
     * 页码 KEY
     */
    public static final String PAGE_KEY = "page";
    /**
     * 显示条数 KEY
     */
    public static final String PAGE_LIMIT_KEY = "limit";
    /**
     * 排序字段 KEY
     */
    public static final String PAGE_SORT_KEY = "sort";
    /**
     * 排序方向 KEY
     */
    public static final String PAGE_ORDER_KEY = "order";

    /**
     * 客户端ID KEY
     */
    public static final String SIGN_CLIENT_ID_KEY = "clientId";

    /**
     * 客户端秘钥 KEY
     */
    public static final String SIGN_CLIENT_SECRET_KEY = "clientSecret";

    /**
     * 随机字符串 KEY
     */
    public static final String SIGN_NONCE_KEY = "nonce";
    /**
     * 时间戳 KEY
     */
    public static final String SIGN_TIMESTAMP_KEY = "timestamp";
    /**
     * 签名类型 KEY
     */
    public static final String SIGN_SIGN_TYPE_KEY = "signType";
    /**
     * 签名结果 KEY
     */
    public static final String SIGN_SIGN_KEY = "sign";
}
