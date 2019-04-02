package com.github.lyd.common.constants;

/**
 * 自定义返回码
 *
 * @author admin
 */

public enum ResultEnum {
    /**
     * 成功
     */
    OK(0, "success"),
    FAIL(1000, "fail"),
    ALERT(1001, "alert"),

    /**
     * oauth2返回码
     */
    INVALID_TOKEN(2000, "invalid_token"),
    INVALID_SCOPE(2001, "invalid_scope"),
    INVALID_REQUEST(2002, "invalid_request"),
    INVALID_CLIENT(2003, "invalid_client"),
    INVALID_GRANT(2004, "invalid_grant"),
    REDIRECT_URI_MISMATCH(2005, "redirect_uri_mismatch"),
    UNAUTHORIZED_CLIENT(2006, "unauthorized_client"),
    EXPIRED_TOKEN(2007, "expired_token"),
    UNSUPPORTED_GRANT_TYPE(2008, "unsupported_grant_type"),
    UNSUPPORTED_RESPONSE_TYPE(2009, "unsupported_response_type"),
    TEMPORARILY_UNAVAILABLE(2011, "temporarily_unavailable"),
    UNAUTHORIZED(2012, "unauthorized"),
    SIGNATURE_DENIED(2013, "signature_denied"),

    ACCESS_DENIED(4030, "access_denied"),
    ACCESS_DENIED_BLACK_IP_LIMITED(4031, "access_denied_black_ip_limited"),
    ACCESS_DENIED_WHITE_IP_LIMITED(4032, "access_denied_white_ip_limited"),
    ACCESS_DENIED_AUTHORITY_EXPIRED(4033, "access_denied_authority_expired"),

    /**
     * 账号错误
     */
    BAD_CREDENTIALS(3000, "bad_credentials"),
    ACCOUNT_DISABLED(3001, "account_disabled"),
    ACCOUNT_EXPIRED(3002, "account_expired"),
    CREDENTIALS_EXPIRED(3003, "credentials_expired"),
    ACCOUNT_LOCKED(3004, "account_locked"),
    USERNAME_NOT_FOUND(3004, "username_not_found"),

    /**
     * 请求错误
     */
    BAD_REQUEST(4000, "bad_request"),
    NOT_FOUND(4004, "not_found"),
    METHOD_NOT_ALLOWED(4005, "method_not_allowed"),
    MEDIA_TYPE_NOT_ACCEPTABLE(4006, "media_type_not_acceptable"),
    TOO_MANY_REQUEST(4007, "too_many_request"),
    /**
     * 系统错误
     */
    ERROR(5000, "error"),
    SERVICE_NOT_FOUND(5004, "service_not_found");



    private int code;
    private String message;

    ResultEnum() {
    }

    private ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultEnum getResultEnum(int code) {
        for (ResultEnum type : ResultEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return ERROR;
    }

    public static ResultEnum getResultEnum(String message) {
        for (ResultEnum type : ResultEnum.values()) {
            if (type.getMessage().equals(message)) {
                return type;
            }
        }
        return ERROR;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
