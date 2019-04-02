package com.github.lyd.msg.provider.service.impl;

import com.github.lyd.msg.client.model.entity.NotifyHttpLogs;
import com.github.lyd.msg.provider.mapper.NotifyHttpLogsMapper;
import com.github.lyd.msg.provider.service.NotifyHttpLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 异步通知日志接口
 *
 * @author: liuyadu
 * @date: 2019/2/13 14:39
 * @description:
 */
@Service
public class NotifyHttpLogsServiceImpl implements NotifyHttpLogsService {
    @Autowired
    private NotifyHttpLogsMapper notifyHttpLogsMapper;

    /**
     * 添加日志
     *
     * @param log
     */
    @Override
    public void addLog(NotifyHttpLogs log) {
        notifyHttpLogsMapper.insertSelective(log);
    }

    /**
     * 更细日志
     *
     * @param log
     */
    @Override
    public void modifyLog(NotifyHttpLogs log) {
        notifyHttpLogsMapper.updateByPrimaryKeySelective(log);
    }

    /**
     * 根据主键获取日志
     *
     * @param logId
     * @return
     */
    @Override
    public NotifyHttpLogs getLog(String logId) {
        return notifyHttpLogsMapper.selectByPrimaryKey(logId);
    }
}
