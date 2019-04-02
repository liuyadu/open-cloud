/*
Navicat MySQL Data Transfer

Source Server         : 39.106.187.125
Source Server Version : 50643
Source Host           : 39.106.187.125:3306
Source Database       : open-platform

Target Server Type    : MYSQL
Target Server Version : 50643
File Encoding         : 65001

Date: 2019-03-21 01:14:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for notify_http_logs
-- ----------------------------
DROP TABLE IF EXISTS `notify_http_logs`;
CREATE TABLE `notify_http_logs` (
  `msg_id` varchar(50) NOT NULL,
  `retry_nums` decimal(8,0) NOT NULL COMMENT '重试次数',
  `total_nums` decimal(8,0) DEFAULT NULL COMMENT '通知总次数',
  `delay` decimal(16,0) NOT NULL COMMENT '延迟时间',
  `result` decimal(1,0) NOT NULL COMMENT '通知结果',
  `url` text NOT NULL COMMENT '通知地址',
  `type` varchar(255) DEFAULT NULL COMMENT '通知类型',
  `data` longtext COMMENT '请求数据',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Http异步通知日志';
SET FOREIGN_KEY_CHECKS=1;
