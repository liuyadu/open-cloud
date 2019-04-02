package com.github.lyd.task.provider.mapper;

import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.task.client.model.entity.SchedulerJobLogs;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerJobLogsMapper extends CrudMapper<SchedulerJobLogs> {
}
