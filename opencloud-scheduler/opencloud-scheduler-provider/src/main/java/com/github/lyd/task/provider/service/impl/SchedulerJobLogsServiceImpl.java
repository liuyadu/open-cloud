package com.github.lyd.task.provider.service.impl;

import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.task.client.model.entity.SchedulerJobLogs;
import com.github.lyd.task.provider.mapper.SchedulerJobLogsMapper;
import com.github.lyd.task.provider.service.SchedulerJobLogsService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SchedulerJobLogsServiceImpl implements SchedulerJobLogsService {
    @Autowired
    private SchedulerJobLogsMapper schedulerJobLogsMapper;
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<SchedulerJobLogs> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        SchedulerJobLogs query = pageParams.mapToObject(SchedulerJobLogs.class);
        ExampleBuilder builder = new ExampleBuilder(SchedulerJobLogs.class);
        Example example = builder.criteria()
                .andLikeRight("jobName", query.getJobName())
                .end().build();
        example.orderBy("logId").desc();
        List<SchedulerJobLogs> list = schedulerJobLogsMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 添加日志
     *
     * @param log
     */
    @Override
    public void addLog(SchedulerJobLogs log) {
        schedulerJobLogsMapper.insertSelective(log);
    }

    /**
     * 更细日志
     *
     * @param log
     */
    @Override
    public void modifyLog(SchedulerJobLogs log) {
        schedulerJobLogsMapper.updateByPrimaryKeySelective(log);
    }

    /**
     * 根据主键获取日志
     *
     * @param logId
     * @return
     */
    @Override
    public SchedulerJobLogs getLog(String logId) {
        return schedulerJobLogsMapper.selectByPrimaryKey(logId);
    }
}
