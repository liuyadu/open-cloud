/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : open-platform

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2019-03-18 04:59:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for gateway_access_logs
-- ----------------------------
DROP TABLE IF EXISTS `gateway_access_logs`;
CREATE TABLE `gateway_access_logs` (
                                     `access_id` bigint(20) NOT NULL COMMENT '访问ID',
                                     `path` varchar(255) DEFAULT NULL COMMENT '访问路径',
                                     `params` text COMMENT '请求数据',
                                     `headers` text COMMENT '请求头',
                                     `ip` varchar(500) DEFAULT NULL COMMENT '请求IP',
                                     `http_status` varchar(100) DEFAULT NULL COMMENT '响应状态',
                                     `method` varchar(50) DEFAULT NULL,
                                     `request_time` datetime DEFAULT NULL COMMENT '访问时间',
                                     `response_time` datetime DEFAULT NULL,
                                     `use_time` bigint(20) DEFAULT NULL,
                                     `user_agent` varchar(255) DEFAULT NULL,
                                     `region` varchar(255) DEFAULT NULL COMMENT '区域',
                                     `authentication` text COMMENT '认证信息',
                                     `server_ip` varchar(255) DEFAULT NULL COMMENT '服务器IP',
                                     `service_id` varchar(100) DEFAULT NULL COMMENT '服务名',
                                     `error` varchar(255) DEFAULT NULL COMMENT '错误信息',
                                     PRIMARY KEY (`access_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-访问日志';
SET FOREIGN_KEY_CHECKS=1;


-- ----------------------------
-- Records of gateway_access_logs
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_ip_limit
-- ----------------------------
DROP TABLE IF EXISTS `gateway_ip_limit`;
CREATE TABLE `gateway_ip_limit` (
                                  `policy_id` bigint(20) NOT NULL COMMENT '策略ID',
                                  `policy_name` varchar(100) NOT NULL COMMENT '策略名称',
                                  `policy_type` tinyint(3) NOT NULL DEFAULT '1' COMMENT '策略类型:0-拒绝/黑名单 1-允许/白名单',
                                  `create_time` datetime NOT NULL COMMENT '创建时间',
                                  `update_time` datetime NOT NULL COMMENT '最近一次修改时间',
                                  `ip_address` varchar(255) NOT NULL COMMENT 'ip地址/IP段:多个用隔开;最多10个',
                                  PRIMARY KEY (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-IP访问控制-策略';

-- ----------------------------
-- Records of gateway_ip_limit
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_ip_limit_api
-- ----------------------------
DROP TABLE IF EXISTS `gateway_ip_limit_api`;
CREATE TABLE `gateway_ip_limit_api` (
                                      `policy_id` bigint(20) NOT NULL COMMENT '策略ID',
                                      `api_id` bigint(20) NOT NULL COMMENT '接口资源ID',
                                      KEY `policy_id` (`policy_id`) USING BTREE,
                                      KEY `api_id` (`api_id`) USING BTREE,
                                      CONSTRAINT `gateway_ip_limit_api_ibfk_1` FOREIGN KEY (`policy_id`) REFERENCES `gateway_ip_limit` (`policy_id`),
                                      CONSTRAINT `gateway_ip_limit_api_ibfk_2` FOREIGN KEY (`api_id`) REFERENCES `base_resource_api` (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-IP访问控制-API接口';

-- ----------------------------
-- Records of gateway_ip_limit_api
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_rate_limit
-- ----------------------------
DROP TABLE IF EXISTS `gateway_rate_limit`;
CREATE TABLE `gateway_rate_limit` (
                                    `policy_id` bigint(20) NOT NULL,
                                    `policy_name` varchar(255) DEFAULT NULL,
                                    `service_id` varchar(0) NOT NULL COMMENT '服务名',
                                    `limit` bigint(20) NOT NULL DEFAULT '0' COMMENT '流量限制',
                                    `interval_unit` varchar(10) NOT NULL DEFAULT 'second' COMMENT '时间单位:second-秒,minute-分钟,hour-小时,day-天',
                                    `limit_type` varchar(255) DEFAULT NULL COMMENT '限流规则类型:url,origin,user',
                                    `create_time` datetime NOT NULL,
                                    `update_time` datetime NOT NULL,
                                    PRIMARY KEY (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-流量控制-策略';

-- ----------------------------
-- Records of gateway_rate_limit
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_rate_limit_api
-- ----------------------------
DROP TABLE IF EXISTS `gateway_rate_limit_api`;
CREATE TABLE `gateway_rate_limit_api` (
                                        `policy_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '限制数量',
                                        `api_id` bigint(11) NOT NULL DEFAULT '1' COMMENT '时间间隔(秒)',
                                        KEY `policy_id` (`policy_id`) USING BTREE,
                                        KEY `api_id` (`api_id`) USING BTREE,
                                        CONSTRAINT `gateway_rate_limit_api_ibfk_1` FOREIGN KEY (`policy_id`) REFERENCES `gateway_rate_limit` (`policy_id`),
                                        CONSTRAINT `gateway_rate_limit_api_ibfk_2` FOREIGN KEY (`api_id`) REFERENCES `base_resource_api` (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-流量控制-API接口';

-- ----------------------------
-- Records of gateway_rate_limit_api
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_rate_limit_origin
-- ----------------------------
DROP TABLE IF EXISTS `gateway_rate_limit_origin`;
CREATE TABLE `gateway_rate_limit_origin` (
                                           `policy_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '限制数量',
                                           `origins` bigint(11) NOT NULL DEFAULT '1' COMMENT '来源域名',
                                           KEY `policy_id` (`policy_id`) USING BTREE,
                                           CONSTRAINT `gateway_rate_limit_origin_ibfk_1` FOREIGN KEY (`policy_id`) REFERENCES `gateway_rate_limit` (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-流量控制-来源限流';

-- ----------------------------
-- Records of gateway_rate_limit_origin
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_rate_limit_role
-- ----------------------------
DROP TABLE IF EXISTS `gateway_rate_limit_role`;
CREATE TABLE `gateway_rate_limit_role` (
                                         `policy_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '限制数量',
                                         `role_id` bigint(11) NOT NULL DEFAULT '1' COMMENT '角色Id',
                                         KEY `policy_id` (`policy_id`) USING BTREE,
                                         KEY `role_id` (`role_id`) USING BTREE,
                                         CONSTRAINT `gateway_rate_limit_role_ibfk_1` FOREIGN KEY (`policy_id`) REFERENCES `gateway_rate_limit` (`policy_id`),
                                         CONSTRAINT `gateway_rate_limit_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-流量控制-角色限流';

-- ----------------------------
-- Records of gateway_rate_limit_role
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_route
-- ----------------------------
DROP TABLE IF EXISTS `gateway_route`;
CREATE TABLE `gateway_route` (
                               `route_id` bigint(20) NOT NULL COMMENT '路由ID',
                               `path` varchar(255) DEFAULT NULL COMMENT '路径',
                               `service_id` varchar(255) DEFAULT NULL COMMENT '服务ID',
                               `url` varchar(255) DEFAULT NULL COMMENT '完整地址',
                               `strip_prefix` tinyint(3) NOT NULL DEFAULT '1' COMMENT '忽略前缀',
                               `retryable` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0-不重试 1-重试',
                               `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
                               `route_desc` varchar(255) DEFAULT NULL,
                               `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否为保留数据:0-否 1-是',
                               PRIMARY KEY (`route_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-路由';

-- ----------------------------
-- Records of gateway_route
-- ----------------------------
INSERT INTO `gateway_route` VALUES ('55659578597467766', '/admin/**', 'opencloud-admin-provider', '', '1', '0', '1', '', '1');
INSERT INTO `gateway_route` VALUES ('556587504019439616', '/base/**', 'opencloud-base-provider', '', '1', '0', '1', '', '1');
INSERT INTO `gateway_route` VALUES ('556595619813130240', '/auth/**', 'opencloud-auth-provider', '', '1', '0', '1', '', '1');
INSERT INTO `gateway_route` VALUES ('556595914240688128', '/msg/**', 'opencloud-msg-provider', '', '1', '0', '1', '', '1');
SET FOREIGN_KEY_CHECKS=1;
