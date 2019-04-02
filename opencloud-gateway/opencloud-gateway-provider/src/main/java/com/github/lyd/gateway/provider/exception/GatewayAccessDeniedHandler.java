package com.github.lyd.gateway.provider.exception;

import com.github.lyd.common.exception.OpenExceptionHandler;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.common.utils.WebUtils;
import com.github.lyd.gateway.provider.service.GatewayAccessLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 网关权限异常处理,记录日志
 */
@Slf4j
public class GatewayAccessDeniedHandler implements AccessDeniedHandler {
    private GatewayAccessLogsService gatewayAccessLogsService;

    public GatewayAccessDeniedHandler(GatewayAccessLogsService gatewayAccessLogsService) {
        this.gatewayAccessLogsService = gatewayAccessLogsService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        ResultBody responseData = OpenExceptionHandler.resolveException(exception, request, response);
        // 保存日志
        gatewayAccessLogsService.saveLogs(request, response);
        WebUtils.writeJson(response, responseData);
    }
}
