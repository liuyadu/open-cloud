package com.github.lyd.task.provider.service;

import com.github.lyd.common.test.BaseTest;
import com.github.lyd.task.client.model.TaskInfo;
import com.github.lyd.task.provider.job.HttpExecuteJob;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2019/3/29 14:59
 * @description:
 */
public class SchedulerServiceTest extends BaseTest {

    @Autowired
    private SchedulerService taskService;
    @Test
    public void getJobList() throws Exception {
        List<TaskInfo> list= taskService.getJobList();
    }

    @Test
    public void addSimpleJob() throws Exception {
    }

    @Test
    public void addCronJob() throws Exception {
        String cron = "3 * * * * ? *";
        TaskInfo taskInfo = new TaskInfo();
        Map data= Maps.newHashMap();
        String serviceId = "opencloud-base-provider";
        data.put("serviceId",serviceId);
        data.put("method","get");
        data.put("path","/test");
        data.put("contentType","application/x-www-form-urlencoded");
        data.put("alarmMail","515608851@qq.com");
        taskInfo.setData(data);
        taskInfo.setJobName("定时任务测试");
        taskInfo.setJobDescription("定时任务描述3秒执行一次");
        taskInfo.setJobClassName(HttpExecuteJob.class.getName());
        taskInfo.setJobGroupName(Scheduler.DEFAULT_GROUP);
        taskInfo.setCronExpression(cron);
        taskService.addCronJob(taskInfo);
    }

    @Test
    public void editJob() throws Exception {
        String cron = "3 * * * * ? *";
        TaskInfo taskInfo = new TaskInfo();
        Map data= Maps.newHashMap();
        String serviceId = "opencloud-base-provider";
        data.put("serviceId",serviceId);
        data.put("method","get");
        data.put("path","/test");
        data.put("contentType","application/x-www-form-urlencoded");
        data.put("alarmMail","515608851@qq.com");
        taskInfo.setData(data);
        taskInfo.setJobName("定时任务测试");
        taskInfo.setJobDescription("定时任务描述3秒2222222执行一次");
        taskInfo.setJobClassName(HttpExecuteJob.class.getName());
        taskInfo.setJobGroupName(Scheduler.DEFAULT_GROUP);
        taskInfo.setCronExpression(cron);
        taskService.editCronJob(taskInfo);
    }

    @Test
    public void deleteJob() throws Exception {
        taskService.deleteJob("定时任务测试",Scheduler.DEFAULT_GROUP);
    }

    @Test
    public void pauseJob() throws Exception {
        taskService.pauseJob("定时任务测试",Scheduler.DEFAULT_GROUP);
    }

    @Test
    public void resumeJob() throws Exception {
        taskService.resumeJob("定时任务测试",Scheduler.DEFAULT_GROUP);
    }

}