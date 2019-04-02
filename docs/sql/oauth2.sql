/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : open-platform

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2019-03-18 05:00:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
                                    `token_id` varchar(256) DEFAULT NULL,
                                    `token` blob,
                                    `authentication_id` varchar(128) NOT NULL,
                                    `user_name` varchar(256) DEFAULT NULL,
                                    `client_id` varchar(256) DEFAULT NULL,
                                    `authentication` blob,
                                    `refresh_token` varchar(256) DEFAULT NULL,
                                    PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2访问令牌';

-- ----------------------------
-- Records of oauth_access_token
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_approvals
-- ----------------------------
DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals` (
                                 `userId` varchar(256) DEFAULT NULL,
                                 `clientId` varchar(256) DEFAULT NULL,
                                 `scope` varchar(256) DEFAULT NULL,
                                 `status` varchar(10) DEFAULT NULL,
                                 `expiresAt` datetime DEFAULT NULL,
                                 `lastModifiedAt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2已授权客户端';

-- ----------------------------
-- Records of oauth_approvals
-- ----------------------------
INSERT INTO `oauth_approvals` VALUES ('admin', '1552274783265', 'userProfile', 'APPROVED', '2019-04-11 23:04:28', '2019-03-11 23:04:28');

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
                                      `client_id` varchar(128) NOT NULL,
                                      `client_secret` varchar(256) NOT NULL,
                                      `resource_ids` varchar(256) DEFAULT NULL,
                                      `scope` varchar(1024) DEFAULT NULL,
                                      `authorized_grant_types` varchar(256) DEFAULT NULL,
                                      `web_server_redirect_uri` varchar(256) DEFAULT NULL,
                                      `authorities` varchar(2048) DEFAULT NULL,
                                      `access_token_validity` int(11) DEFAULT NULL,
                                      `refresh_token_validity` int(11) DEFAULT NULL,
                                      `additional_information` varchar(4096) DEFAULT NULL,
                                      `autoapprove` varchar(256) DEFAULT NULL,
                                      PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2客户端信息';

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('1552274783265', '$2a$10$xxfI6N5kSKINJXipQb9dJuS1Z7T2z4h/IZrhNnx3zNzCViCQMDtfq', '', 'userProfile', 'authorization_code,client_credentials,password', 'http://localhost:8888/login,http://localhost:8888/webjars/springfox-swagger-ui/o2c.html', '', '43200', '2592000', '{\"appIcon\":\"\",\"website\":\"http://www.baidu.com\",\"appName\":\"资源服务器\",\"appType\":\"server\",\"appDesc\":\"资源服务器\",\"appId\":\"1552274783265\",\"appNameEn\":\"ResourceServer\",\"updateTime\":1553011137731,\"userType\":\"platform\",\"userId\":521677655146233856,\"appOs\":\"\",\"status\":1}', '');
INSERT INTO `oauth_client_details` VALUES ('1552294656514', '$2a$10$UAzdXTnT9DAyfzSNInoX4.bt/8V0zdn23m7uQiwsyorHLucf4ftfO', '', 'userProfile', 'authorization_code,client_credentials,password', 'http://localhost:8888/oauth/admin/callback', '', '43200', '2592000', '{\"appIcon\":\"\",\"website\":\"http://www.baidu.com\",\"appName\":\"运营后台\",\"appType\":\"pc\",\"appDesc\":\"运营后台\",\"appId\":\"1552294656514\",\"appNameEn\":\"Admin\",\"updateTime\":1553011144275,\"userType\":\"platform\",\"userId\":521677655146233856,\"appOs\":\"\",\"status\":1}', '');

-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token` (
                                    `token_id` varchar(256) DEFAULT NULL,
                                    `token` blob,
                                    `authentication_id` varchar(128) NOT NULL,
                                    `user_name` varchar(256) DEFAULT NULL,
                                    `client_id` varchar(256) DEFAULT NULL,
                                    PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2客户端令牌';

-- ----------------------------
-- Records of oauth_client_token
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
                            `code` varchar(256) DEFAULT NULL,
                            `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2授权码';

-- ----------------------------
-- Records of oauth_code
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token` (
                                     `token_id` varchar(256) DEFAULT NULL,
                                     `token` blob,
                                     `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2刷新令牌';

-- ----------------------------
-- Records of oauth_refresh_token
-- ----------------------------
SET FOREIGN_KEY_CHECKS=1;
