package com.github.lyd.task.provider.service;


import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.task.client.model.entity.SchedulerJobLogs;

/**
 * 异步通知日志接口
 *
 * @author: liuyadu
 * @date: 2019/2/13 14:39
 * @description:
 */
public interface SchedulerJobLogsService {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<SchedulerJobLogs> findListPage(PageParams pageParams);

    /**
     * 添加日志
     *
     * @param log
     */
    void addLog(SchedulerJobLogs log);

    /**
     * 更细日志
     *
     * @param log
     */
    void modifyLog(SchedulerJobLogs log);


    /**
     * 根据主键获取日志
     *
     * @param logId
     * @return
     */
    SchedulerJobLogs getLog(String logId);
}
