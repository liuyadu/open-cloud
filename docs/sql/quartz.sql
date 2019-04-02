/*
Navicat MySQL Data Transfer

Source Server         : 39.106.187.125
Source Server Version : 50643
Source Host           : 39.106.187.125:3306
Source Database       : open-platform

Target Server Type    : MYSQL
Target Server Version : 50643
File Encoding         : 65001

Date: 2019-04-01 22:30:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
                                    `SCHED_NAME` varchar(120) NOT NULL,
                                    `TRIGGER_NAME` varchar(200) NOT NULL,
                                    `TRIGGER_GROUP` varchar(200) NOT NULL,
                                    `BLOB_DATA` blob,
                                    PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
                                    CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS` (
                                `SCHED_NAME` varchar(120) NOT NULL,
                                `CALENDAR_NAME` varchar(200) NOT NULL,
                                `CALENDAR` blob NOT NULL,
                                PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
                                    `SCHED_NAME` varchar(120) NOT NULL,
                                    `TRIGGER_NAME` varchar(200) NOT NULL,
                                    `TRIGGER_GROUP` varchar(200) NOT NULL,
                                    `CRON_EXPRESSION` varchar(200) NOT NULL,
                                    `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
                                    PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
                                    CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
                                     `SCHED_NAME` varchar(120) NOT NULL,
                                     `ENTRY_ID` varchar(95) NOT NULL,
                                     `TRIGGER_NAME` varchar(200) NOT NULL,
                                     `TRIGGER_GROUP` varchar(200) NOT NULL,
                                     `INSTANCE_NAME` varchar(200) NOT NULL,
                                     `FIRED_TIME` bigint(13) NOT NULL,
                                     `SCHED_TIME` bigint(13) NOT NULL,
                                     `PRIORITY` int(11) NOT NULL,
                                     `STATE` varchar(16) NOT NULL,
                                     `JOB_NAME` varchar(200) DEFAULT NULL,
                                     `JOB_GROUP` varchar(200) DEFAULT NULL,
                                     `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
                                     `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
                                     PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS` (
                                  `SCHED_NAME` varchar(120) NOT NULL,
                                  `JOB_NAME` varchar(200) NOT NULL,
                                  `JOB_GROUP` varchar(200) NOT NULL,
                                  `DESCRIPTION` varchar(250) DEFAULT NULL,
                                  `JOB_CLASS_NAME` varchar(250) NOT NULL,
                                  `IS_DURABLE` varchar(1) NOT NULL,
                                  `IS_NONCONCURRENT` varchar(1) NOT NULL,
                                  `IS_UPDATE_DATA` varchar(1) NOT NULL,
                                  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
                                  `JOB_DATA` blob,
                                  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS` (
                            `SCHED_NAME` varchar(120) NOT NULL,
                            `LOCK_NAME` varchar(40) NOT NULL,
                            PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
                                          `SCHED_NAME` varchar(120) NOT NULL,
                                          `TRIGGER_GROUP` varchar(200) NOT NULL,
                                          PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
                                      `SCHED_NAME` varchar(120) NOT NULL,
                                      `INSTANCE_NAME` varchar(200) NOT NULL,
                                      `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
                                      `CHECKIN_INTERVAL` bigint(13) NOT NULL,
                                      PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
                                      `SCHED_NAME` varchar(120) NOT NULL,
                                      `TRIGGER_NAME` varchar(200) NOT NULL,
                                      `TRIGGER_GROUP` varchar(200) NOT NULL,
                                      `REPEAT_COUNT` bigint(7) NOT NULL,
                                      `REPEAT_INTERVAL` bigint(12) NOT NULL,
                                      `TIMES_TRIGGERED` bigint(10) NOT NULL,
                                      PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
                                      CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
                                       `SCHED_NAME` varchar(120) NOT NULL,
                                       `TRIGGER_NAME` varchar(200) NOT NULL,
                                       `TRIGGER_GROUP` varchar(200) NOT NULL,
                                       `STR_PROP_1` varchar(512) DEFAULT NULL,
                                       `STR_PROP_2` varchar(512) DEFAULT NULL,
                                       `STR_PROP_3` varchar(512) DEFAULT NULL,
                                       `INT_PROP_1` int(11) DEFAULT NULL,
                                       `INT_PROP_2` int(11) DEFAULT NULL,
                                       `LONG_PROP_1` bigint(20) DEFAULT NULL,
                                       `LONG_PROP_2` bigint(20) DEFAULT NULL,
                                       `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
                                       `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
                                       `BOOL_PROP_1` varchar(1) DEFAULT NULL,
                                       `BOOL_PROP_2` varchar(1) DEFAULT NULL,
                                       PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
                                       CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS` (
                               `SCHED_NAME` varchar(120) NOT NULL,
                               `TRIGGER_NAME` varchar(200) NOT NULL,
                               `TRIGGER_GROUP` varchar(200) NOT NULL,
                               `JOB_NAME` varchar(200) NOT NULL,
                               `JOB_GROUP` varchar(200) NOT NULL,
                               `DESCRIPTION` varchar(250) DEFAULT NULL,
                               `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
                               `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
                               `PRIORITY` int(11) DEFAULT NULL,
                               `TRIGGER_STATE` varchar(16) NOT NULL,
                               `TRIGGER_TYPE` varchar(8) NOT NULL,
                               `START_TIME` bigint(13) NOT NULL,
                               `END_TIME` bigint(13) DEFAULT NULL,
                               `CALENDAR_NAME` varchar(200) DEFAULT NULL,
                               `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
                               `JOB_DATA` blob,
                               PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
                               KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
                               CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
