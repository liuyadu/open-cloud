package com.github.lyd.common.exception;

/**
 * 提示消息异常
 *
 * @author admin
 */
public class OpenAlertException extends OpenException {
    private static final long serialVersionUID = 4908906410210213271L;

    public OpenAlertException() {
    }

    public OpenAlertException(String msg) {
        super(msg);
    }

    public OpenAlertException(int code, String msg) {
        super(code, msg);
    }

    public OpenAlertException(int code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}
