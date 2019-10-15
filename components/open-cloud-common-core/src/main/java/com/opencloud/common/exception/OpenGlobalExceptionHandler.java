package com.opencloud.common.exception;

import com.opencloud.common.constants.ErrorCode;
import com.opencloud.common.model.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理器
 *
 * @author LYD
 * @date 2017/7/3
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class OpenGlobalExceptionHandler {


    /**
     * 统一异常处理
     * AuthenticationException
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({AuthenticationException.class})
    public static ResultBody authenticationException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex, request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * OAuth2Exception
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({OAuth2Exception.class, InvalidTokenException.class})
    public static ResultBody oauth2Exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex, request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * 自定义异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({OpenException.class})
    public static ResultBody openException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex, request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * 其他异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({Exception.class})
    public static ResultBody exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResultBody resultBody = resolveException(ex, request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        return resultBody;
    }

    /**
     * 静态解析异常。可以直接调用
     *
     * @param ex
     * @return
     */
    public static ResultBody resolveException(Exception ex, String path) {
        ErrorCode code = ErrorCode.ERROR;
        int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = ex.getMessage();
        String superClassName = ex.getClass().getSuperclass().getName();
        String className = ex.getClass().getName();
        if (className.contains("UsernameNotFoundException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.USERNAME_NOT_FOUND;
        } else if (className.contains("BadCredentialsException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.BAD_CREDENTIALS;
        } else if (className.contains("AccountExpiredException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.ACCOUNT_EXPIRED;
        } else if (className.contains("LockedException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.ACCOUNT_LOCKED;
        } else if (className.contains("DisabledException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.ACCOUNT_DISABLED;
        } else if (className.contains("CredentialsExpiredException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.CREDENTIALS_EXPIRED;
        } else if (className.contains("InvalidClientException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.INVALID_CLIENT;
        } else if (className.contains("UnauthorizedClientException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.UNAUTHORIZED_CLIENT;
        }else if (className.contains("InsufficientAuthenticationException") || className.contains("AuthenticationCredentialsNotFoundException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.UNAUTHORIZED;
        } else if (className.contains("InvalidGrantException")) {
            code = ErrorCode.ALERT;
            if ("Bad credentials".contains(message)) {
                code = ErrorCode.BAD_CREDENTIALS;
            } else if ("User is disabled".contains(message)) {
                code = ErrorCode.ACCOUNT_DISABLED;
            } else if ("User account is locked".contains(message)) {
                code = ErrorCode.ACCOUNT_LOCKED;
            }
        } else if (className.contains("InvalidScopeException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.INVALID_SCOPE;
        } else if (className.contains("InvalidTokenException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.INVALID_TOKEN;
        } else if (className.contains("InvalidRequestException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCode.INVALID_REQUEST;
        } else if (className.contains("RedirectMismatchException")) {
            code = ErrorCode.REDIRECT_URI_MISMATCH;
        } else if (className.contains("UnsupportedGrantTypeException")) {
            code = ErrorCode.UNSUPPORTED_GRANT_TYPE;
        } else if (className.contains("UnsupportedResponseTypeException")) {
            code = ErrorCode.UNSUPPORTED_RESPONSE_TYPE;
        } else if (className.contains("UserDeniedAuthorizationException")) {
            code = ErrorCode.ACCESS_DENIED;
        } else if (className.contains("AccessDeniedException")) {
            code = ErrorCode.ACCESS_DENIED;
            httpStatus = HttpStatus.FORBIDDEN.value();
            if (ErrorCode.ACCESS_DENIED_BLACK_LIMITED.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_BLACK_LIMITED;
            } else if (ErrorCode.ACCESS_DENIED_WHITE_LIMITED.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_WHITE_LIMITED;
            } else if (ErrorCode.ACCESS_DENIED_AUTHORITY_EXPIRED.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_AUTHORITY_EXPIRED;
            }else if (ErrorCode.ACCESS_DENIED_UPDATING.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_UPDATING;
            }else if (ErrorCode.ACCESS_DENIED_DISABLED.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_DISABLED;
            } else if (ErrorCode.ACCESS_DENIED_NOT_OPEN.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_NOT_OPEN;
            }
        } else if (className.contains("HttpMessageNotReadableException")
                || className.contains("TypeMismatchException")
                || className.contains("MissingServletRequestParameterException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCode.BAD_REQUEST;
        } else if (className.contains("NoHandlerFoundException")) {
            httpStatus = HttpStatus.NOT_FOUND.value();
            code = ErrorCode.NOT_FOUND;
        } else if (className.contains("HttpRequestMethodNotSupportedException")) {
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED.value();
            code = ErrorCode.METHOD_NOT_ALLOWED;
        } else if (className.contains("HttpMediaTypeNotAcceptableException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCode.MEDIA_TYPE_NOT_ACCEPTABLE;
        } else if (className.contains("MethodArgumentNotValidException")) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            code = ErrorCode.ALERT;
            return ResultBody.failed().code(code.getCode()).msg(bindingResult.getFieldError().getDefaultMessage());
        } else if (className.contains("IllegalArgumentException")) {
            //参数错误
            code = ErrorCode.ALERT;
            httpStatus = HttpStatus.BAD_REQUEST.value();
        } else if (className.contains("OpenAlertException")) {
            code = ErrorCode.ALERT;
        } else if (className.contains("OpenSignatureException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCode.SIGNATURE_DENIED;
        }else if(message.equalsIgnoreCase(ErrorCode.TOO_MANY_REQUESTS.name())){
            code = ErrorCode.TOO_MANY_REQUESTS;
        }
        return buildBody(ex, code, path, httpStatus);
    }

    /**
     * 构建返回结果对象
     *
     * @param exception
     * @return
     */
    private static ResultBody buildBody(Exception exception, ErrorCode resultCode, String path, int httpStatus) {
        if (resultCode == null) {
            resultCode = ErrorCode.ERROR;
        }
        ResultBody resultBody = ResultBody.failed().code(resultCode.getCode()).msg(exception.getMessage()).path(path).httpStatus(httpStatus);
        log.error("==> error:{} exception: {}",resultBody, exception);
        return resultBody;
    }

}
