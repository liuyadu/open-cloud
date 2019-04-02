package com.github.lyd.gateway.provider.service;

import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.gateway.client.model.entity.GatewayAccessLogs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 网关访问日志
 */
public interface GatewayAccessLogsService {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<GatewayAccessLogs> findListPage(PageParams pageParams);

    /**
     * 保存访问日志
     * @param request
     * @param response
     */
    void  saveLogs(HttpServletRequest request, HttpServletResponse response);
}
