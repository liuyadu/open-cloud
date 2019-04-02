package com.github.lyd.msg.provider.service;


import com.github.lyd.msg.client.model.entity.NotifyHttpLogs;

/**
 * 异步通知日志接口
 *
 * @author: liuyadu
 * @date: 2019/2/13 14:39
 * @description:
 */
public interface NotifyHttpLogsService {

    /**
     * 添加日志
     *
     * @param log
     */
    void addLog(NotifyHttpLogs log);

    /**
     * 更细日志
     *
     * @param log
     */
    void modifyLog(NotifyHttpLogs log);


    /**
     * 根据主键获取日志
     *
     * @param logId
     * @return
     */
    NotifyHttpLogs getLog(String logId);
}
