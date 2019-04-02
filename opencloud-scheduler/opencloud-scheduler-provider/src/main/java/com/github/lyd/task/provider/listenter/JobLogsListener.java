package com.github.lyd.task.provider.listenter;

import com.alibaba.fastjson.JSONObject;
import com.github.lyd.common.utils.DateUtils;
import com.github.lyd.common.utils.StringUtils;
import com.github.lyd.task.client.model.entity.SchedulerJobLogs;
import com.github.lyd.task.provider.service.SchedulerJobLogsService;
import com.github.lyd.task.provider.service.feign.EmailRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;

/**
 * 任务调度监听
 *
 * @author liuyadu
 */
@Slf4j
public class JobLogsListener implements JobListener {
    private EmailRemoteService emailRemoteService;
    private SchedulerJobLogsService schedulerJobLogsService;

    public JobLogsListener(EmailRemoteService emailRemoteService, SchedulerJobLogsService schedulerJobLogsService) {
        this.emailRemoteService = emailRemoteService;
        this.schedulerJobLogsService = schedulerJobLogsService;
    }

    @Override
    public String getName() {
        return "JobLogsListener";
    }


    /**
     * 调度前执行
     *
     * @param job
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext job) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext job) {
    }

    /**
     * 调度完成或异常时执行
     *
     * @param job
     * @param e
     */
    @Override
    public void jobWasExecuted(JobExecutionContext job, JobExecutionException e) {
        JobDetail detail = job.getJobDetail();
        JobDataMap dataMap = detail.getJobDataMap();
        String jobName = detail.getKey().getName();
        String jobGroup = detail.getKey().getGroup();
        String alarmMail = dataMap.getString("alarmMail");
        String jobClass = detail.getJobClass().getName();
        String description = detail.getDescription();
        String exception = null;
        String cronExpression = null;
        Integer status = 1;
        Trigger trigger = job.getTrigger();
        String triggerClass = trigger.getClass().getName();
        if (trigger instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) trigger;
            cronExpression = cronTrigger.getCronExpression();
        }
        if (e != null) {
            status = 0;
            exception = StringUtils.getExceptionToString(e);
            if (StringUtils.isNotBlank(alarmMail)) {
                String title = String.format("[%s]任务执行异常-%s", jobName, DateUtils.formatDateTime(new Date()));
                try {
                    emailRemoteService.sendEmail(alarmMail, title, e.getMessage());
                } catch (Exception em) {
                    log.error("==> send alarmMail error:{}", em);
                }
            }
        }
        SchedulerJobLogs jobLog = new SchedulerJobLogs();
        jobLog.setJobName(jobName);
        jobLog.setJobGroup(jobGroup);
        jobLog.setJobClass(jobClass);
        jobLog.setJobDescription(description);
        jobLog.setRunTime(job.getJobRunTime());
        jobLog.setCreateTime(new Date());
        jobLog.setCronExpression(cronExpression);
        jobLog.setStartTime(job.getFireTime());
        jobLog.setTriggerClass(triggerClass);
        jobLog.setEndTime(new Date(job.getFireTime().getTime() + job.getJobRunTime()));
        jobLog.setJobData(JSONObject.toJSONString(dataMap));
        jobLog.setException(exception);
        jobLog.setStatus(status);
        schedulerJobLogsService.addLog(jobLog);
    }
}
