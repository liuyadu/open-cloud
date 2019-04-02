package com.github.lyd.task.client.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 任务详情
 * @author liuyadu
 */
public class TaskInfo implements Serializable {

    private static final long serialVersionUID = -7693857859775581311L;
    /**
     * 增加或修改标识
     */
    private int id;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务描述
     */
    private String jobDescription;
    /**
     * 任务类名
     */
    private String jobClassName;

    /**
     * 任务分组
     */
    private String jobGroupName;

    /**
     * 任务状态
     */
    private String jobStatus;

    /**
     * 任务类型 SimpleTrigger-简单任务,CronTrigger-表达式
     */
    private String jobTrigger;

    /**
     * 任务表达式
     */
    private String cronExpression;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 间隔时间（毫秒）
     */
    private Long milliSeconds;

    /**
     * 重复次数
     */
    private Integer repeatCount;

    /**
     * 起始时间
     */
    private Date startDate;

    /**
     * 终止时间
     */
    private Date endDate;

    /**
     * 执行数据
     */
    private Map data;

    /**
     * @return the milliSeconds
     */
    public Long getMilliSeconds() {
        return milliSeconds;
    }

    /**
     * @param milliSeconds the milliSeconds to set
     */
    public void setMilliSeconds(Long milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    /**
     * @return the repeatCount
     */
    public Integer getRepeatCount() {
        return repeatCount;
    }

    /**
     * @param repeatCount the repeatCount to set
     */
    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobTrigger() {
        return jobTrigger;
    }

    public void setJobTrigger(String jobTrigger) {
        this.jobTrigger = jobTrigger;
    }
}
