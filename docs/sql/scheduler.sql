/*
Navicat MySQL Data Transfer

Source Server         : 39.106.187.125
Source Server Version : 50643
Source Host           : 39.106.187.125:3306
Source Database       : open-platform

Target Server Type    : MYSQL
Target Server Version : 50643
File Encoding         : 65001

Date: 2019-04-02 00:14:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for scheduler_job_logs
-- ----------------------------
DROP TABLE IF EXISTS `scheduler_job_logs`;
CREATE TABLE `scheduler_job_logs` (
                                    `log_id` bigint(20) NOT NULL,
                                    `job_name` varchar(255) NOT NULL COMMENT '任务名称',
                                    `job_group` varchar(255) NOT NULL COMMENT '任务组名',
                                    `job_class` varchar(255) DEFAULT NULL COMMENT '任务执行类',
                                    `job_description` varchar(255) DEFAULT NULL COMMENT '任务描述',
                                    `trigger_class` varchar(255) DEFAULT NULL COMMENT '任务触发器',
                                    `cron_expression` varchar(255) DEFAULT NULL COMMENT '任务表达式',
                                    `run_time` bigint(20) DEFAULT NULL COMMENT '运行时间',
                                    `start_time` datetime DEFAULT NULL COMMENT '开始时间',
                                    `end_time` datetime DEFAULT NULL COMMENT '结束时间',
                                    `create_time` datetime DEFAULT NULL COMMENT '日志创建时间',
                                    `job_data` varchar(255) DEFAULT NULL COMMENT '任务执行数据',
                                    `exception` text COMMENT '异常',
                                    `status` tinyint(3) DEFAULT '0' COMMENT '状态：0-失败 1-成功',
                                    PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务调度日志';
SET FOREIGN_KEY_CHECKS=1;
