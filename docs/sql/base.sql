/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : open-platform

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2019-07-30 15:52:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for base_account
-- ----------------------------
DROP TABLE IF EXISTS `base_account`;
CREATE TABLE `base_account` (
  `account_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户Id',
  `account` varchar(255) NOT NULL COMMENT '标识：手机号、邮箱、 用户名、或第三方应用的唯一标识',
  `password` varchar(255) NOT NULL COMMENT '密码凭证：站内的保存密码、站外的不保存或保存token）',
  `account_type` varchar(255) NOT NULL COMMENT '登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等',
  `domain` varchar(255) DEFAULT NULL COMMENT '账户域:@admin.com,@developer.com',
  `register_ip` varchar(255) DEFAULT NULL COMMENT '注册IP',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `status` int(11) DEFAULT NULL COMMENT '状态:0-禁用 1-启用 2-锁定',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`account_id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='登录账号';

-- ----------------------------
-- Records of base_account
-- ----------------------------
INSERT INTO `base_account` VALUES ('521677655368531968', '521677655146233856', 'admin', '$2a$10$A7EHximvrsa4ESX1uSlkJupbg2PLO2StzDzy67NX4YV25MxmbGvXu', 'username', '@admin.com', null, '2019-07-03 17:11:59', '1', '2019-07-11 17:38:21');
INSERT INTO `base_account` VALUES ('557063237787451392', '557063237640650752', 'test', '$2a$10$SdqHS7Y8VcrR0WfCf9FI3uhcUfYKu58per0fVJLW.iPOBt.bFYp0y', 'username', '@admin.com', null, '2019-07-03 17:12:02', '1', '2019-07-11 17:20:44');

-- ----------------------------
-- Table structure for base_account_logs
-- ----------------------------
DROP TABLE IF EXISTS `base_account_logs`;
CREATE TABLE `base_account_logs` (
  `id` bigint(20) NOT NULL,
  `login_time` datetime NOT NULL,
  `login_ip` varchar(255) NOT NULL COMMENT '登录Ip',
  `login_agent` varchar(500) NOT NULL COMMENT '登录设备',
  `login_nums` int(11) NOT NULL COMMENT '登录次数',
  `user_id` bigint(20) NOT NULL,
  `account` varchar(100) NOT NULL,
  `account_type` varchar(50) NOT NULL,
  `account_id` bigint(20) NOT NULL COMMENT '账号ID',
  `domain` varchar(255) DEFAULT NULL COMMENT '账号域',
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='登录日志';

-- ----------------------------
-- Records of base_account_logs
-- ----------------------------

-- ----------------------------
-- Table structure for base_action
-- ----------------------------
DROP TABLE IF EXISTS `base_action`;
CREATE TABLE `base_action` (
  `action_id` bigint(20) NOT NULL COMMENT '资源ID',
  `action_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源编码',
  `action_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源名称',
  `action_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '资源父节点',
  `priority` int(10) NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  `service_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务名称',
  PRIMARY KEY (`action_id`),
  UNIQUE KEY `action_code` (`action_code`) USING BTREE,
  UNIQUE KEY `action_id` (`action_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-功能操作';

-- ----------------------------
-- Records of base_action
-- ----------------------------
INSERT INTO `base_action` VALUES ('1131849293404176385', 'systemMenuView', '查看', '', '3', '0', '1', '2019-05-24 17:07:54', '2019-08-22 14:25:48', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131849510572654593', 'systemMenuEdit', '编辑', '', '3', '0', '1', '2019-05-24 17:08:46', '2019-05-24 17:08:46', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131858946338992129', 'systemRoleView', '查看', '', '8', '0', '1', '2019-05-24 17:46:16', '2019-05-24 17:46:16', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131863248310775809', 'systemRoleEdit', '编辑', '', '8', '0', '1', '2019-05-24 18:03:22', '2019-05-24 18:03:22', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131863723722551297', 'systemAppView', '查看', '', '9', '0', '1', '2019-05-24 18:05:15', '2019-05-24 18:05:15', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131863775899693057', 'systemAppEdit', '编辑', '', '9', '0', '1', '2019-05-24 18:05:27', '2019-05-24 18:05:27', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131864400507056130', 'systemUserView', '查看', '', '10', '0', '1', '2019-05-24 18:07:56', '2019-05-24 18:07:56', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131864444878598146', 'systemUserEdit', '编辑', '', '10', '0', '1', '2019-05-24 18:08:07', '2019-05-24 18:08:07', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131864827252322305', 'gatewayIpLimitView', '查看', '', '2', '0', '1', '2019-05-24 18:09:38', '2019-05-24 18:09:38', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131864864267055106', 'gatewayIpLimitEdit', '编辑', '', '2', '0', '1', '2019-05-24 18:09:47', '2019-05-24 18:09:47', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131865040289411074', 'gatewayRouteView', '查看', '', '5', '0', '1', '2019-05-24 18:10:29', '2019-05-24 18:10:29', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131865075609645057', 'gatewayRouteEdit', '编辑', '', '5', '0', '1', '2019-05-24 18:10:37', '2019-05-24 18:10:37', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131865482314526722', 'systemApiView', '查看', '', '6', '0', '1', '2019-05-24 18:12:14', '2019-05-24 18:12:14', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131865520738545666', 'systemApiEdit', '编辑', '', '6', '0', '1', '2019-05-24 18:12:23', '2019-05-24 18:12:23', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131865772929462274', 'gatewayLogsView', '查看', '', '12', '0', '1', '2019-05-24 18:13:23', '2019-05-24 18:13:23', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131865931146997761', 'gatewayRateLimitView', '查看', '', '14', '0', '1', '2019-05-24 18:14:01', '2019-05-24 18:14:01', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131865974704844802', 'gatewayRateLimitEdit', '编辑', '', '14', '0', '1', '2019-05-24 18:14:12', '2019-05-24 18:14:12', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131866278187905026', 'jobView', '查看', '', '16', '0', '1', '2019-05-24 18:15:24', '2019-05-25 03:23:15', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131866310622457857', 'jobEdit', '编辑', '', '16', '0', '1', '2019-05-24 18:15:32', '2019-05-25 03:23:21', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131866943459045377', 'schedulerLogsView', '查看', '', '19', '0', '1', '2019-05-24 18:18:03', '2019-05-24 18:18:03', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1131867094479155202', 'notifyHttpLogsView', '查看', '', '18', '0', '1', '2019-05-24 18:18:39', '2019-05-24 18:18:39', '1', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1164422088547635202', 'developerView', '查看', '', '1149253733673287682', '0', '1', '2019-08-22 14:20:34', '2019-08-22 14:24:53', '0', 'open-cloud-base-server');
INSERT INTO `base_action` VALUES ('1164422211189084162', 'developerEdit', '编辑', '', '1149253733673287682', '0', '1', '2019-08-22 14:21:04', '2019-08-22 14:21:04', '0', 'open-cloud-base-server');
-- ----------------------------
-- Table structure for base_api
-- ----------------------------
DROP TABLE IF EXISTS `base_api`;
CREATE TABLE `base_api` (
  `api_id` bigint(20) NOT NULL COMMENT '接口ID',
  `api_code` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '接口编码',
  `api_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '接口名称',
  `api_category` varchar(20) COLLATE utf8_bin DEFAULT 'default' COMMENT '接口分类:default-默认分类',
  `api_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
  `request_method` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求方式',
  `content_type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '响应类型',
  `service_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务ID',
  `path` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '请求路径',
  `priority` bigint(20) NOT NULL DEFAULT '0' COMMENT '优先级',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  `is_auth` tinyint(3) NOT NULL DEFAULT '1' COMMENT '是否需要认证: 0-无认证 1-身份认证 默认:1',
  `is_open` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否公开: 0-内部的 1-公开的',
  `class_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '类名',
  `method_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '方法名',
  PRIMARY KEY (`api_id`),
  UNIQUE KEY `api_code` (`api_code`),
  UNIQUE KEY `api_id` (`api_id`),
  KEY `service_id` (`service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-API接口';


-- ----------------------------
-- Records of base_api
-- ----------------------------
INSERT INTO `base_api` VALUES ('1', 'all', '全部', 'default', '所有请求', 'get,post', null, 'open-cloud-api-zuul-server', '/**', '0', '1', '2019-03-07 21:52:17', '2019-03-14 21:41:28', '1', '1', '1', null, null);
INSERT INTO `base_api` VALUES ('2', 'actuator', '监控端点', 'default', '监控端点', 'post', null, 'open-cloud-api-zuul-server', '/actuator/**', '0', '1', '2019-03-07 21:52:17', '2019-03-14 21:41:28', '1', '1', '1', null, null);
INSERT INTO `base_api` VALUES ('1149168010337116161', '9a1e16647d1e534f7b06ed9db83719fa', '获取功能按钮详情', 'default', '获取功能按钮详情', 'GET', '', 'open-cloud-base-server', '/action/{actionId}/info', '0', '1', '2019-07-11 12:06:18', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseActionController', 'getAction');
INSERT INTO `base_api` VALUES ('1149168010500694017', '26128c7aafae30020bcac2c47631497a', '获取分页功能按钮列表', 'default', '获取分页功能按钮列表', 'GET', '', 'open-cloud-base-server', '/action', '0', '1', '2019-07-11 12:06:18', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseActionController', 'findActionListPage');
INSERT INTO `base_api` VALUES ('1149168010571997185', '84d30b0aab28de096779d6f8bee960e7', '添加功能按钮', 'default', '添加功能按钮', 'POST', '', 'open-cloud-base-server', '/action/add', '0', '1', '2019-07-11 12:06:18', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseActionController', 'addAction');
INSERT INTO `base_api` VALUES ('1149168010655883266', '756154d7c8c6a79f02da79d9148beb9f', '编辑功能按钮', 'default', '添加功能按钮', 'POST', '', 'open-cloud-base-server', '/action/update', '0', '1', '2019-07-11 12:06:18', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseActionController', 'updateAction');
INSERT INTO `base_api` VALUES ('1149168010722992129', '92187c259636fdf5d4f108b688fc44ee', '移除功能按钮', 'default', '移除功能按钮', 'POST', '', 'open-cloud-base-server', '/action/remove', '0', '1', '2019-07-11 12:06:18', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseActionController', 'removeAction');
INSERT INTO `base_api` VALUES ('1149168010794295298', '214e294809bb1f22abc70c38959e1343', '添加接口资源', 'default', '添加接口资源', 'POST', '', 'open-cloud-base-server', '/api/add', '0', '1', '2019-07-11 12:06:18', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseApiController', 'addApi');
INSERT INTO `base_api` VALUES ('1149168010861404161', 'a0f00dd477c9232a51b94553ad7a7803', '移除接口资源', 'default', '移除接口资源', 'POST', '', 'open-cloud-base-server', '/api/remove', '0', '1', '2019-07-11 12:06:18', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseApiController', 'removeApi');
INSERT INTO `base_api` VALUES ('1149168010920124418', '6311ab783c8818cd5918e5d903fb371e', '编辑接口资源', 'default', '编辑接口资源', 'POST', '', 'open-cloud-base-server', '/api/update', '0', '1', '2019-07-11 12:06:18', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseApiController', 'updateApi');
INSERT INTO `base_api` VALUES ('1149168010991427586', 'aa37236289cedba711c8f648a94b1105', '获取分页接口列表', 'default', '获取分页接口列表', 'GET', '', 'open-cloud-base-server', '/api', '0', '1', '2019-07-11 12:06:18', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseApiController', 'getApiList');
INSERT INTO `base_api` VALUES ('1149168011045953537', 'e18bae5b2fc727e77d36a3f4697abf0f', '获取所有接口列表', 'default', '获取所有接口列表', 'GET', '', 'open-cloud-base-server', '/api/all', '0', '1', '2019-07-11 12:06:18', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseApiController', 'getApiAllList');
INSERT INTO `base_api` VALUES ('1149168011100479489', 'd4c4c1b5b3847cc63b52ed9007698325', '获取接口资源', 'default', '获取接口资源', 'GET', '', 'open-cloud-base-server', '/api/{apiId}/info', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseApiController', 'getApi');
INSERT INTO `base_api` VALUES ('1149168011163394050', '9e054a7dc4194e7635c2686e1109aa49', '删除应用信息', 'default', '删除应用信息', 'POST', '', 'open-cloud-base-server', '/app/remove', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAppController', 'removeApp');
INSERT INTO `base_api` VALUES ('1149168011217920002', 'fa32726ed4076d44cae0023132f7014f', '获取应用详情', 'default', '获取应用详情', 'GET', '', 'open-cloud-base-server', '/app/{appId}/info', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:18', '1', '0', '1', 'com.opencloud.base.server.controller.BaseAppController', 'getApp');
INSERT INTO `base_api` VALUES ('1149168011280834562', 'c5863e8ad8a5605354b9a122511ffa01', '添加应用信息', 'default', '添加应用信息', 'POST', '', 'open-cloud-base-server', '/app/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAppController', 'addApp');
INSERT INTO `base_api` VALUES ('1149168011331166209', 'a23793da67076d4be853daa422943c53', '编辑应用信息', 'default', '编辑应用信息', 'POST', '', 'open-cloud-base-server', '/app/update', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAppController', 'updateApp');
INSERT INTO `base_api` VALUES ('1149168011452801025', '93c006692e386f96555e7edece91c994', '重置应用秘钥', 'default', '重置应用秘钥', 'POST', '', 'open-cloud-base-server', '/app/reset', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAppController', 'resetAppSecret');
INSERT INTO `base_api` VALUES ('1149168011524104194', '385d34ec827de818225a981d737f307a', '获取应用开发配置信息', 'default', '获取应用开发配置信息', 'GET', '', 'open-cloud-base-server', '/app/client/{clientId}/info', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:18', '1', '0', '1', 'com.opencloud.base.server.controller.BaseAppController', 'getAppClientInfo');
INSERT INTO `base_api` VALUES ('1149168011578630146', 'ccb6139e4beb2a84e29f399215bfedc0', '获取分页应用列表', 'default', '获取分页应用列表', 'GET', '', 'open-cloud-base-server', '/app', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAppController', 'getAppListPage');
INSERT INTO `base_api` VALUES ('1149168011628961794', 'f09f5543ff4c0ceab04b99ddc5a94bc2', '完善应用开发信息', 'default', '完善应用开发信息', 'POST', '', 'open-cloud-base-server', '/app/client/update', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAppController', 'updateAppClientInfo');
INSERT INTO `base_api` VALUES ('1149168011687682050', '331bddbcd34cacdd74f80b48e3484017', '获取接口权限列表', 'default', '获取接口权限列表', 'GET', '', 'open-cloud-base-server', '/authority/api', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'findAuthorityApi');
INSERT INTO `base_api` VALUES ('1149168011742208002', '785b2368bf67d1a942ea64d946c70144', '获取应用已分配接口权限', 'default', '获取应用已分配接口权限', 'GET', '', 'open-cloud-base-server', '/authority/app', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '0', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'findAuthorityApp');
INSERT INTO `base_api` VALUES ('1149168011805122561', '282c563cbfb922ac6328c058051d31a4', '分配角色权限', 'default', '分配角色权限', 'POST', '', 'open-cloud-base-server', '/authority/role/grant', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'grantAuthorityRole');
INSERT INTO `base_api` VALUES ('1149168011851259906', 'f2be18d77a78f728df355f32e90cd780', '分配应用权限', 'default', '分配应用权限', 'POST', '', 'open-cloud-base-server', '/authority/app/grant', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'grantAuthorityApp');
INSERT INTO `base_api` VALUES ('1149168011909980161', '4611bd638b538605286600dbc899cbb0', '功能按钮授权', 'default', '功能按钮授权', 'POST', '', 'open-cloud-base-server', '/authority/action/grant', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'grantAuthorityAction');
INSERT INTO `base_api` VALUES ('1149168011960311809', '5262f2214a4ff43179c947dc458213d7', '获取功能权限列表', 'default', '获取功能权限列表', 'GET', '', 'open-cloud-base-server', '/authority/action', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'findAuthorityAction');
INSERT INTO `base_api` VALUES ('1149168012010643458', '8a6561a9a0efa7f5f9faadb6e39dc927', '获取用户已分配权限', 'default', '获取用户已分配权限', 'GET', '', 'open-cloud-base-server', '/authority/user', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'findAuthorityUser');
INSERT INTO `base_api` VALUES ('1149168012060975106', '1df7911bb85d15f51bcd78c43f18b24d', '获取菜单权限列表', 'default', '获取菜单权限列表', 'GET', '', 'open-cloud-base-server', '/authority/menu', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'findAuthorityMenu');
INSERT INTO `base_api` VALUES ('1149168012111306753', '80256cc196e1e6d2437289e038b13b8a', '获取所有访问权限列表', 'default', '获取所有访问权限列表', 'GET', '', 'open-cloud-base-server', '/authority/access', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '0', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'findAuthorityResource');
INSERT INTO `base_api` VALUES ('1149168012161638401', '5586d5f337d4ebf99e551fdbf55d1116', '获取角色已分配权限', 'default', '获取角色已分配权限', 'GET', '', 'open-cloud-base-server', '/authority/role', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'findAuthorityRole');
INSERT INTO `base_api` VALUES ('1149168012216164353', 'b96a9514487f7e7679fe39c9254ab972', '分配用户权限', 'default', '分配用户权限', 'POST', '', 'open-cloud-base-server', '/authority/user/grant', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseAuthorityController', 'grantAuthorityUser');
INSERT INTO `base_api` VALUES ('1149168012270690305', '3a1c4ef17565d863f35a3bccd7cc035e', '添加系统用户', 'default', '添加系统用户', 'POST', '', 'open-cloud-base-server', '/developer/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseDeveloperController', 'addUser');
INSERT INTO `base_api` VALUES ('1149168012316827649', '734d1179e332309b9de3927894864396', '更新系统用户', 'default', '更新系统用户', 'POST', '', 'open-cloud-base-server', '/developer/update', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseDeveloperController', 'updateUser');
INSERT INTO `base_api` VALUES ('1149168012421685249', '39ad69234435595ee67836e8a03d6ea6', '获取所有用户列表', 'default', '获取所有用户列表', 'GET', '', 'open-cloud-base-server', '/developer/all', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseDeveloperController', 'getUserAllList');
INSERT INTO `base_api` VALUES ('1149168012467822593', '50520c56c7714aa9be9a808cd30fd9bb', '获取账号登录信息', 'default', '仅限系统内部调用', 'POST', '', 'open-cloud-base-server', '/developer/login', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '0', '1', 'com.opencloud.base.server.controller.BaseDeveloperController', 'developerLogin');
INSERT INTO `base_api` VALUES ('1149168012522348546', '9aca4415969b60158f383b2b49b8fe79', '修改用户密码', 'default', '修改用户密码', 'POST', '', 'open-cloud-base-server', '/developer/update/password', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseDeveloperController', 'updatePassword');
INSERT INTO `base_api` VALUES ('1149168012576874498', 'c4c154b6ffa4b21835db1e09614c73e6', '系统分页用户列表', 'default', '系统分页用户列表', 'GET', '', 'open-cloud-base-server', '/developer', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseDeveloperController', 'getUserList');
INSERT INTO `base_api` VALUES ('1149168012623011841', '4a13f24fa2d5cf74a6e408e82182e1b1', '注册第三方系统登录账号', 'default', '仅限系统内部调用', 'POST', '', 'open-cloud-base-server', '/developer/add/thirdParty', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '0', '0', 'com.opencloud.base.server.controller.BaseDeveloperController', 'addDeveloperThirdParty');
INSERT INTO `base_api` VALUES ('1149168012673343490', '2901b2b8040f1c44c0318650fbfd4460', '获取分页菜单资源列表', 'default', '获取分页菜单资源列表', 'GET', '', 'open-cloud-base-server', '/menu', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseMenuController', 'getMenuListPage');
INSERT INTO `base_api` VALUES ('1149168012719480834', '87049d9db5261f50d524c45a32044415', '获取菜单资源详情', 'default', '获取菜单资源详情', 'GET', '', 'open-cloud-base-server', '/menu/{menuId}/info', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseMenuController', 'getMenu');
INSERT INTO `base_api` VALUES ('1149168012769812481', '50bcf72df4a18a6a8c46a7076a7a014a', '移除菜单资源', 'default', '移除菜单资源', 'POST', '', 'open-cloud-base-server', '/menu/remove', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseMenuController', 'removeMenu');
INSERT INTO `base_api` VALUES ('1149168012820144129', '638be151d314740a2f2aaebaaba3f479', '菜单所有资源列表', 'default', '菜单所有资源列表', 'GET', '', 'open-cloud-base-server', '/menu/all', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseMenuController', 'getMenuAllList');
INSERT INTO `base_api` VALUES ('1149168012870475777', 'e3c1b0b860591e11b50d6dccc9e2fa6a', '获取菜单下所有操作', 'default', '获取菜单下所有操作', 'GET', '', 'open-cloud-base-server', '/menu/action', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseMenuController', 'getMenuAction');
INSERT INTO `base_api` VALUES ('1149168012920807425', 'bbc5546a6cfa1161859b69ef5754eace', '编辑菜单资源', 'default', '编辑菜单资源', 'POST', '', 'open-cloud-base-server', '/menu/update', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseMenuController', 'updateMenu');
INSERT INTO `base_api` VALUES ('1149168012979527682', '720f3b2571fe2850108f090408514cae', '添加菜单资源', 'default', '添加菜单资源', 'POST', '', 'open-cloud-base-server', '/menu/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseMenuController', 'addMenu');
INSERT INTO `base_api` VALUES ('1149168013029859330', '8f2b70d4b55f13eaf82c4cf687903ced', '获取角色详情', 'default', '获取角色详情', 'GET', '', 'open-cloud-base-server', '/role/{roleId}/info', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseRoleController', 'getRole');
INSERT INTO `base_api` VALUES ('1149168013071802369', 'd9fc987468fd4c73350653d5c4e53453', '角色添加成员', 'default', '角色添加成员', 'POST', '', 'open-cloud-base-server', '/role/users/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseRoleController', 'addUserRoles');
INSERT INTO `base_api` VALUES ('1149168013122134017', 'f4e83562a36e943883c5eb6b7376e365', '查询角色成员', 'default', '查询角色成员', 'GET', '', 'open-cloud-base-server', '/role/users', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseRoleController', 'getRoleUsers');
INSERT INTO `base_api` VALUES ('1149168013176659970', '9b57678bc96af393c0c60e135acc90a9', '获取所有角色列表', 'default', '获取所有角色列表', 'GET', '', 'open-cloud-base-server', '/role/all', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseRoleController', 'getRoleAllList');
INSERT INTO `base_api` VALUES ('1149168013226991617', '4693b9a455022754623dd25f9905c19b', '获取分页角色列表', 'default', '获取分页角色列表', 'GET', '', 'open-cloud-base-server', '/role', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:19', '1', '1', '1', 'com.opencloud.base.server.controller.BaseRoleController', 'getRoleListPage');
INSERT INTO `base_api` VALUES ('1149168013277323266', '51ea3146eaebdd33fd065b029ee147fb', '添加角色', 'default', '添加角色', 'POST', '', 'open-cloud-base-server', '/role/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseRoleController', 'addRole');
INSERT INTO `base_api` VALUES ('1149168013327654913', 'b4cdd238a7e34e1794988d00c4d45779', '编辑角色', 'default', '编辑角色', 'POST', '', 'open-cloud-base-server', '/role/update', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseRoleController', 'updateRole');
INSERT INTO `base_api` VALUES ('1149168013373792257', '610b499835de759e37429ece2de1ee33', '删除角色', 'default', '删除角色', 'POST', '', 'open-cloud-base-server', '/role/remove', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseRoleController', 'removeRole');
INSERT INTO `base_api` VALUES ('1149168013419929601', '334d10291e4061c07a180546dd9cedea', '添加系统用户', 'default', '添加系统用户', 'POST', '', 'open-cloud-base-server', '/user/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseUserController', 'addUser');
INSERT INTO `base_api` VALUES ('1149168013466066945', 'd722dc79f9c8d3ece3b9324a03caf902', '更新系统用户', 'default', '更新系统用户', 'POST', '', 'open-cloud-base-server', '/user/update', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseUserController', 'updateUser');
INSERT INTO `base_api` VALUES ('1149168013512204290', 'c8293b83e96ca425be2e24f0cfa8c7bf', '获取所有用户列表', 'default', '获取所有用户列表', 'GET', '', 'open-cloud-base-server', '/user/all', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseUserController', 'getUserAllList');
INSERT INTO `base_api` VALUES ('1149168013562535938', '3fdb78cf597834182b4c6b558a800774', '修改用户密码', 'default', '修改用户密码', 'POST', '', 'open-cloud-base-server', '/user/update/password', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseUserController', 'updatePassword');
INSERT INTO `base_api` VALUES ('1149168013608673282', 'dc01f78458d3fe0cff028da861bdb65b', '系统分页用户列表', 'default', '系统分页用户列表', 'GET', '', 'open-cloud-base-server', '/user', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseUserController', 'getUserList');
INSERT INTO `base_api` VALUES ('1149168013659004929', '25c851a0698d161f2b00bdcf74668648', '用户分配角色', 'default', '用户分配角色', 'POST', '', 'open-cloud-base-server', '/user/roles/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseUserController', 'addUserRoles');
INSERT INTO `base_api` VALUES ('1149168013700947969', '86197fb92e8fd3359842cf511c68be8c', '获取账号登录信息', 'default', '仅限系统内部调用', 'POST', '', 'open-cloud-base-server', '/user/login', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '0', '1', 'com.opencloud.base.server.controller.BaseUserController', 'userLogin');
INSERT INTO `base_api` VALUES ('1149168013747085313', 'db9fbb85ee040fd2f8981e47a6873614', '获取用户已分配角色', 'default', '获取用户已分配角色', 'GET', '', 'open-cloud-base-server', '/user/roles', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.BaseUserController', 'getUserRoles');
INSERT INTO `base_api` VALUES ('1149168013797416961', 'bbd37ea32bf91697909dd261befa8a5f', '注册第三方系统登录账号', 'default', '仅限系统内部调用', 'POST', '', 'open-cloud-base-server', '/user/add/thirdParty', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '0', '1', 'com.opencloud.base.server.controller.BaseUserController', 'addUserThirdParty');
INSERT INTO `base_api` VALUES ('1149168013843554306', 'b0d37206d8ebf60a7359e1b15d79b361', '修改当前登录用户密码', 'default', '修改当前登录用户密码', 'GET', '', 'open-cloud-base-server', '/current/user/rest/password', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.CurrentUserController', 'restPassword');
INSERT INTO `base_api` VALUES ('1149168013889691650', '88fa6cc7c4fa7f0fb2b8d3df951fdfa9', '修改当前登录用户基本信息', 'default', '修改当前登录用户基本信息', 'POST', '', 'open-cloud-base-server', '/current/user/update', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.CurrentUserController', 'updateUserInfo');
INSERT INTO `base_api` VALUES ('1149168013940023298', '902ca6aa4d96c21a48cabe5363a4b3dd', '获取当前登录用户已分配菜单权限', 'default', '获取当前登录用户已分配菜单权限', 'GET', '', 'open-cloud-base-server', '/current/user/menu', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.CurrentUserController', 'findAuthorityMenu');
INSERT INTO `base_api` VALUES ('1149168013994549249', '0f86b27eb3b0fcd3e714e22d43b1172b', '获取分页访问日志列表', 'default', '获取分页访问日志列表', 'GET', '', 'open-cloud-base-server', '/gateway/access/logs', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayAccessLogsController', 'getAccessLogListPage');
INSERT INTO `base_api` VALUES ('1149168014036492290', '83ad6d5e0903bf19cfa28c7a83595de2', '获取接口白名单列表', 'default', '仅限内部调用', 'GET', '', 'open-cloud-base-server', '/gateway/api/whiteList', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '0', '0', 'com.opencloud.base.server.controller.GatewayController', 'getApiWhiteList');
INSERT INTO `base_api` VALUES ('1149168014082629634', '6eb627d08d2f0aee74bfd7c9b53b1124', '获取接口黑名单列表', 'default', '仅限内部调用', 'GET', '', 'open-cloud-base-server', '/gateway/api/blackList', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '0', '0', 'com.opencloud.base.server.controller.GatewayController', 'getApiBlackList');
INSERT INTO `base_api` VALUES ('1149168014128766978', '3fde01046c8643e9b536ae6f61d4431f', '获取路由列表', 'default', '仅限内部调用', 'GET', '', 'open-cloud-base-server', '/gateway/api/route', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '0', '0', 'com.opencloud.base.server.controller.GatewayController', 'getApiRouteList');
INSERT INTO `base_api` VALUES ('1149168014170710018', '2cfc3bbc760608bda59d68b2c0b4e2fd', '获取限流列表', 'default', '仅限内部调用', 'GET', '', 'open-cloud-base-server', '/gateway/api/rateLimit', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '0', '0', 'com.opencloud.base.server.controller.GatewayController', 'getApiRateLimitList');
INSERT INTO `base_api` VALUES ('1149168014212653057', 'f5abed6e35a7a1715fac80841e4e592f', '添加IP限制', 'default', '添加IP限制', 'POST', '', 'open-cloud-base-server', '/gateway/limit/ip/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayIpLimitController', 'addIpLimit');
INSERT INTO `base_api` VALUES ('1149168014258790401', 'c584e31594bc5bedfc959c729a2e90e7', '移除IP限制', 'default', '移除IP限制', 'POST', '', 'open-cloud-base-server', '/gateway/limit/ip/remove', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayIpLimitController', 'removeIpLimit');
INSERT INTO `base_api` VALUES ('1149168014405591042', '24b5559e0204223af9b73f3c68ee68f3', '获取IP限制', 'default', '获取IP限制', 'GET', '', 'open-cloud-base-server', '/gateway/limit/ip/{policyId}/info', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayIpLimitController', 'getIpLimit');
INSERT INTO `base_api` VALUES ('1149168014451728385', '160a9fb0ce4243b989bbaaac031a345e', '编辑IP限制', 'default', '编辑IP限制', 'POST', '', 'open-cloud-base-server', '/gateway/limit/ip/update', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayIpLimitController', 'updateIpLimit');
INSERT INTO `base_api` VALUES ('1149168014497865729', '99a30e8643b0820a04ecbdcf7c9628af', '绑定API', 'default', '一个API只能绑定一个策略', 'POST', '', 'open-cloud-base-server', '/gateway/limit/ip/api/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayIpLimitController', 'addIpLimitApis');
INSERT INTO `base_api` VALUES ('1149168014544003073', '0dfca465c864c74d04a4d6135fee1880', '获取分页接口列表', 'default', '获取分页接口列表', 'GET', '', 'open-cloud-base-server', '/gateway/limit/ip', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayIpLimitController', 'getIpLimitListPage');
INSERT INTO `base_api` VALUES ('1149168014585946113', '2aecc273984a1d3981dc0f62cafbe894', '查询策略已绑定API列表', 'default', '获取分页接口列表', 'GET', '', 'open-cloud-base-server', '/gateway/limit/ip/api/list', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayIpLimitController', 'getIpLimitApiList');
INSERT INTO `base_api` VALUES ('1149168014636277762', 'b993f44f1ad4dd08bfd4f36730af9ea1', '获取流量控制', 'default', '获取流量控制', 'GET', '', 'open-cloud-base-server', '/gateway/limit/rate/{policyId}/info', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:21', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRateLimitController', 'getRateLimit');
INSERT INTO `base_api` VALUES ('1149168014686609409', 'ab71840616ff1f1a619170538b1f1de9', '添加流量控制', 'default', '添加流量控制', 'POST', '', 'open-cloud-base-server', '/gateway/limit/rate/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:21', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRateLimitController', 'addRateLimit');
INSERT INTO `base_api` VALUES ('1149168014732746753', '5f512e5065e80c8d0ffd6be4f27c386e', '编辑流量控制', 'default', '编辑流量控制', 'POST', '', 'open-cloud-base-server', '/gateway/limit/rate/update', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRateLimitController', 'updateRateLimit');
INSERT INTO `base_api` VALUES ('1149168014774689793', 'f83ecfdd9325be59c60627f0164ec8a8', '绑定API', 'default', '一个API只能绑定一个策略', 'POST', '', 'open-cloud-base-server', '/gateway/limit/rate/api/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:21', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRateLimitController', 'addRateLimitApis');
INSERT INTO `base_api` VALUES ('1149168014816632833', '9c565ce4ded10ccd227001f6d7eab11c', '移除流量控制', 'default', '移除流量控制', 'POST', '', 'open-cloud-base-server', '/gateway/limit/rate/remove', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRateLimitController', 'removeRateLimit');
INSERT INTO `base_api` VALUES ('1149168014862770178', '0830e3c6c3d42f11d86f5e2ed0d99985', '查询策略已绑定API列表', 'default', '获取分页接口列表', 'GET', '', 'open-cloud-base-server', '/gateway/limit/rate/api/list', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:21', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRateLimitController', 'getRateLimitApiList');
INSERT INTO `base_api` VALUES ('1149168014908907521', '8dff62591ccefba3537b84aa9d72d7b7', '获取分页接口列表', 'default', '获取分页接口列表', 'GET', '', 'open-cloud-base-server', '/gateway/limit/rate', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:20', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRateLimitController', 'getRateLimitListPage');
INSERT INTO `base_api` VALUES ('1149168014955044866', '382e5fe59fdc31f240b5e42b2ae1a522', '获取分页路由列表', 'default', '获取分页路由列表', 'GET', '', 'open-cloud-base-server', '/gateway/route', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:21', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRouteController', 'getRouteListPage');
INSERT INTO `base_api` VALUES ('1149168015001182210', '177b640de1b90d39536f462f24e3051f', '获取路由', 'default', '获取路由', 'GET', '', 'open-cloud-base-server', '/gateway/route/{routeId}/info', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:21', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRouteController', 'getRoute');
INSERT INTO `base_api` VALUES ('1149168015055708162', '1307447addfddf7e075eea74100c150d', '添加路由', 'default', '添加路由', 'POST', '', 'open-cloud-base-server', '/gateway/route/add', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:21', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRouteController', 'addRoute');
INSERT INTO `base_api` VALUES ('1149168015118622721', '40a481fc86814de744e91e42c15b6f3e', '移除路由', 'default', '移除路由', 'POST', '', 'open-cloud-base-server', '/gateway/route/remove', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:21', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRouteController', 'removeRoute');
INSERT INTO `base_api` VALUES ('1149168015164760065', '61e8c492eb9e56441d1c428ab3e09f66', '编辑路由', 'default', '编辑路由', 'POST', '', 'open-cloud-base-server', '/gateway/route/update', '0', '1', '2019-07-11 12:06:19', '2019-08-19 18:59:21', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayRouteController', 'updateRoute');
INSERT INTO `base_api` VALUES ('1149168208614449153', '3c28ffa71681fb53f1f458548456256c', '登录获取用户访问令牌', 'default', '基于oauth2密码模式登录,无需签名,返回access_token', 'POST', '', 'open-cloud-uaa-admin-server', '/login/token', '0', '1', '2019-07-11 12:07:06', '2019-08-19 18:55:51', '1', '0', '1', 'com.opencloud.uaa.admin.server.controller.LoginController', 'getLoginToken');
INSERT INTO `base_api` VALUES ('1149168208677363714', '29c8264c689bce3567708f977779c3b9', '退出并移除令牌', 'default', '退出并移除令牌,令牌将失效', 'POST', '', 'open-cloud-uaa-admin-server', '/logout/token', '0', '1', '2019-07-11 12:07:06', '2019-08-19 18:55:51', '1', '1', '1', 'com.opencloud.uaa.admin.server.controller.LoginController', 'removeToken');
INSERT INTO `base_api` VALUES ('1149168208740278273', 'f98a1543766b726e4b5a6d7a7f32fd93', '获取当前登录用户信息', 'default', '获取当前登录用户信息', 'GET', '', 'open-cloud-uaa-admin-server', '/current/user', '0', '1', '2019-07-11 12:07:06', '2019-08-19 18:55:50', '1', '1', '1', 'com.opencloud.uaa.admin.server.controller.LoginController', 'getUserProfile');
INSERT INTO `base_api` VALUES ('1149265710541914114', 'be5eb57a4df27bf8dece0bda751a5a62', '发送邮件', 'default', '发送邮件', 'POST', '', 'open-cloud-msg-server', '/email', '0', '1', '2019-07-11 18:34:32', '2019-07-17 18:33:08', '1', '0', '1', 'com.opencloud.msg.server.controller.EmailController', 'send');
INSERT INTO `base_api` VALUES ('1149265710646771714', '0273e0d10bea64046178b7c5ff5055cc', '发送短信', 'default', '发送短信', 'POST', 'application/json;charset=UTF-8', 'open-cloud-msg-server', '/sms', '0', '1', '2019-07-11 18:34:32', '2019-07-17 18:33:08', '1', '0', '0', 'com.opencloud.msg.server.controller.SmsController', 'send');
INSERT INTO `base_api` VALUES ('1149265710743240705', '3b5928629e5398dcc534c548d5c01085', 'Webhook异步通知', 'default', '即时推送，重试通知时间间隔为 5s、10s、2min、5min、10min、30min、1h、2h、6h、15h，直到你正确回复状态 200 并且返回 success 或者超过最大重发次数', 'POST', 'application/json;charset=UTF-8', 'open-cloud-msg-server', '/webhook', '0', '1', '2019-07-11 18:34:32', '2019-07-17 18:33:08', '1', '0', '0', 'com.opencloud.msg.server.controller.WebHookController', 'send');
INSERT INTO `base_api` VALUES ('1149265710839709697', 'df78a9afa62bb2d1165aeea14aca9e41', '获取分页异步通知列表', 'default', '获取分页异步通知列表', 'GET', '', 'open-cloud-msg-server', '/webhook/logs', '0', '1', '2019-07-11 18:34:32', '2019-07-17 18:33:08', '1', '0', '0', 'com.opencloud.msg.server.controller.WebHookController', 'getLogsListPage');
INSERT INTO `base_api` VALUES ('1151324453580460033', '4fdfe1d02c78379a3a8eb2a84b0e8b82', '搜索', 'default', '搜索', 'POST', '', 'refuse_classify_server', '/items/search', '0', '1', '2019-07-17 10:55:14', '2019-07-17 10:55:14', '1', '0', '0', 'com.classify.server.controller.ItemsController', 'search');
INSERT INTO `base_api` VALUES ('1151324453773398018', '92eabcb8d4d4d587343cc2b6e11ccecc', '同步数据', 'default', '同步数据', 'POST', '', 'refuse_classify_server', '/items/sync', '0', '1', '2019-07-17 10:55:15', '2019-07-17 10:55:15', '1', '0', '0', 'com.classify.server.controller.ItemsController', 'sync');
INSERT INTO `base_api` VALUES ('1151324453869867009', 'd094367240c4c6847eeda0fe2317069c', '语音识别搜索', 'default', '语音识别搜索', 'POST', '', 'refuse_classify_server', '/items/search/speech', '0', '1', '2019-07-17 10:55:15', '2019-07-17 10:55:15', '1', '0', '0', 'com.classify.server.controller.ItemsController', 'searchSpeech');
INSERT INTO `base_api` VALUES ('1151324453974724609', 'd26205d1d1e42d925ea972a6d034ba15', '热门搜索词', 'default', '热门搜索词', 'POST', '', 'refuse_classify_server', '/items/hot', '0', '1', '2019-07-17 10:55:15', '2019-07-17 10:55:15', '1', '0', '0', 'com.classify.server.controller.ItemsController', 'hot');
INSERT INTO `base_api` VALUES ('1151324454205411329', '5b2390e91198ae02e58b4704e23f7a45', '图像识别搜索', 'default', '图像识别搜索', 'POST', '', 'refuse_classify_server', '/items/search/image', '0', '1', '2019-07-17 10:55:15', '2019-07-17 10:55:15', '1', '0', '0', 'com.classify.server.controller.ItemsController', 'searchImage');
INSERT INTO `base_api` VALUES ('1151324462224920577', 'f17677d7f0f33851333de6bc48d5ac1a', '获取服务列表', 'default', '获取服务列表', 'GET', '', 'open-cloud-base-server', '/gateway/service/list', '0', '1', '2019-07-17 10:55:17', '2019-08-20 14:27:16', '1', '1', '1', 'com.opencloud.base.server.controller.GatewayController', 'getServiceList');
INSERT INTO `base_api` VALUES ('1151395583808917505', '042dd26520a30c0fecd13ee015a79be5', '发送模板邮件', 'default', '发送模板邮件', 'POST', '', 'open-cloud-msg-server', '/email/tpl', '0', '1', '2019-07-17 15:37:53', '2019-07-17 18:33:08', '1', '0', '1', 'com.opencloud.msg.server.controller.EmailController', 'sendByTpl');
INSERT INTO `base_api` VALUES ('1151426336538128385', '93ff44fd4adab6c4097327eb5ffd405a', '获取任务列表', 'default', '获取任务列表', 'GET', '', 'open-cloud-task-server', '/job', '0', '1', '2019-07-17 17:40:05', '2019-07-17 18:25:46', '1', '1', '1', 'com.opencloud.task.server.controller.SchedulerController', 'getJobList');
INSERT INTO `base_api` VALUES ('1151426336915615746', '030aae5c245d4d780ca248fdd5e4bb67', '恢复任务', 'default', '恢复任务', 'POST', '', 'open-cloud-task-server', '/job/resume', '0', '1', '2019-07-17 17:40:05', '2019-07-17 18:25:46', '1', '1', '1', 'com.opencloud.task.server.controller.SchedulerController', 'resumeJob');
INSERT INTO `base_api` VALUES ('1151426338278764545', '7f542a952be1a9d8b089330f5f0aba3b', '删除任务', 'default', '删除任务', 'POST', '', 'open-cloud-task-server', '/job/delete', '0', '1', '2019-07-17 17:40:06', '2019-07-17 18:25:46', '1', '1', '1', 'com.opencloud.task.server.controller.SchedulerController', 'deleteJob');
INSERT INTO `base_api` VALUES ('1151426338278764547', 'b9f2f4ed495f3f71da31c14e8ad2193d', '暂停任务', 'default', '暂停任务', 'POST', '', 'open-cloud-task-server', '/job/pause', '0', '1', '2019-07-17 17:40:06', '2019-07-17 18:25:46', '1', '1', '1', 'com.opencloud.task.server.controller.SchedulerController', 'pauseJob');
INSERT INTO `base_api` VALUES ('1151426338278764549', '14467518667ccfa46541736433d0efa2', '获取任务执行日志列表', 'default', '获取任务执行日志列表', 'GET', '', 'open-cloud-task-server', '/job/logs', '0', '1', '2019-07-17 17:40:06', '2019-07-17 18:25:46', '1', '1', '1', 'com.opencloud.task.server.controller.SchedulerController', 'getJobLogList');
INSERT INTO `base_api` VALUES ('1151426338756915201', '903acc6b8c02184170bd3f316cd0f0c3', '添加远程调度任务', 'default', '添加远程调度任务', 'POST', '', 'open-cloud-task-server', '/job/add/http', '0', '1', '2019-07-17 17:40:06', '2019-07-17 18:25:47', '1', '1', '1', 'com.opencloud.task.server.controller.SchedulerController', 'addHttpJob');
INSERT INTO `base_api` VALUES ('1151426338903715841', 'cb8aded7fdffd0e8e86940f540b32472', '修改远程调度任务', 'default', '修改远程调度任务', 'POST', '', 'open-cloud-task-server', '/job/update/http', '0', '1', '2019-07-17 17:40:06', '2019-07-17 18:25:47', '1', '1', '1', 'com.opencloud.task.server.controller.SchedulerController', 'updateHttpJob');
INSERT INTO `base_api` VALUES ('1153543688817876993', '8aedf1bcd8ebc00a41361067602e4866', '获取所有表信息', 'default', '获取所有表信息', 'GET', '', 'open-cloud-generator-server', '/tables', '0', '1', '2019-07-23 13:53:41', '2019-07-23 13:53:41', '1', '1', '0', 'com.opencloud.generator.server.controller.GenerateController', 'tables');
INSERT INTO `base_api` VALUES ('1153543700121526273', '9ae39e2a7bb1f2502dcd5f4d280b739d', 'swaggerResources', 'default', '', '', '', 'open-cloud-base-server', '/swagger-resources', '0', '1', '2019-07-23 13:53:44', '2019-08-19 18:59:21', '1', '1', '0', 'springfox.documentation.swagger.web.ApiResourceController', 'swaggerResources');
INSERT INTO `base_api` VALUES ('1153543700201218049', '8e25fdc700610c63d638c55545441119', 'uiConfiguration', 'default', '', '', '', 'open-cloud-base-server', '/swagger-resources/configuration/ui', '0', '1', '2019-07-23 13:53:44', '2019-08-19 18:59:21', '1', '1', '0', 'springfox.documentation.swagger.web.ApiResourceController', 'uiConfiguration');
INSERT INTO `base_api` VALUES ('1153543700285104130', '0cea701426acbc8348e64ecb598a8bf7', 'securityConfiguration', 'default', '', '', '', 'open-cloud-base-server', '/swagger-resources/configuration/security', '0', '1', '2019-07-23 13:53:44', '2019-08-19 18:59:21', '1', '1', '0', 'springfox.documentation.swagger.web.ApiResourceController', 'securityConfiguration');
INSERT INTO `base_api` VALUES ('1153543700398350338', '604809625765c7df1e1e8ee8b7e7fe81', 'error', 'default', '', '', '', 'open-cloud-base-server', '/error', '0', '1', '2019-07-23 13:53:44', '2019-08-19 18:59:21', '1', '1', '0', 'org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController', 'error');
INSERT INTO `base_api` VALUES ('1153544494501732354', '09e8e7b093a0fb008071c09bf440fda1', 'handleError', 'default', '', '', '', 'open-cloud-uaa-admin-server', '/oauth/error', '0', '1', '2019-07-23 13:56:53', '2019-08-19 18:55:50', '1', '0', '0', 'com.opencloud.uaa.admin.server.controller.IndexController', 'handleError');
INSERT INTO `base_api` VALUES ('1153544494598201345', '1f7307e3c2809aff5437f3594594c8ca', 'welcome', 'default', '', 'GET', '', 'open-cloud-uaa-admin-server', '/', '0', '1', '2019-07-23 13:56:53', '2019-08-19 18:55:50', '1', '1', '0', 'com.opencloud.uaa.admin.server.controller.IndexController', 'welcome');
INSERT INTO `base_api` VALUES ('1153544494694670337', '4d27a9ae885d7dbfc82e7ae2c86ec9d6', 'login', 'default', '', 'GET', '', 'open-cloud-uaa-admin-server', '/login', '0', '1', '2019-07-23 13:56:54', '2019-08-19 18:55:50', '1', '0', '0', 'com.opencloud.uaa.admin.server.controller.IndexController', 'login');
INSERT INTO `base_api` VALUES ('1153544494765973506', 'c235abccc85e47cda9710697c59a4aa7', 'confirm_access', 'default', '', '', '', 'open-cloud-uaa-admin-server', '/oauth/confirm_access', '0', '1', '2019-07-23 13:56:54', '2019-08-19 18:55:50', '1', '0', '0', 'com.opencloud.uaa.admin.server.controller.IndexController', 'confirm_access');
INSERT INTO `base_api` VALUES ('1153544495072157698', '404ea9fec51f41d5ebdefcf30039d173', 'securityConfiguration', 'default', '', '', '', 'open-cloud-uaa-admin-server', '/swagger-resources/configuration/security', '0', '1', '2019-07-23 13:56:54', '2019-08-19 18:55:51', '1', '1', '0', 'springfox.documentation.swagger.web.ApiResourceController', 'securityConfiguration');
INSERT INTO `base_api` VALUES ('1153544495139266561', '154a10d28aa54f3ab5eec62c5b3cc396', 'swaggerResources', 'default', '', '', '', 'open-cloud-uaa-admin-server', '/swagger-resources', '0', '1', '2019-07-23 13:56:54', '2019-08-19 18:55:51', '1', '1', '0', 'springfox.documentation.swagger.web.ApiResourceController', 'swaggerResources');
INSERT INTO `base_api` VALUES ('1153544495210569729', 'd0cb7641b01ea63b4b680aa41372f3dd', 'uiConfiguration', 'default', '', '', '', 'open-cloud-uaa-admin-server', '/swagger-resources/configuration/ui', '0', '1', '2019-07-23 13:56:54', '2019-08-19 18:55:51', '1', '1', '0', 'springfox.documentation.swagger.web.ApiResourceController', 'uiConfiguration');
INSERT INTO `base_api` VALUES ('1153544495277678593', 'e57afa79be638071473395318684b732', 'error', 'default', '', '', '', 'open-cloud-uaa-admin-server', '/error', '0', '1', '2019-07-23 13:56:54', '2019-08-19 18:55:51', '1', '1', '0', 'org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController', 'error');
INSERT INTO `base_api` VALUES ('1156037702112673793', '8a2aa97d530b74b9e440505870e36d89', 'index', 'default', '', 'GET', '', 'open-cloud-api-zuul-server', '/', '0', '1', '2019-07-30 11:04:00', '2019-07-30 13:02:42', '1', '0', '0', 'com.opencloud.gateway.zuul.server.controller.IndexController', 'index');
INSERT INTO `base_api` VALUES ('1156037702343360514', 'f03bda1e0dcaeb9b777caf1ef6b23af5', 'swaggerResources', 'default', '', '', '', 'open-cloud-api-zuul-server', '/swagger-resources', '0', '1', '2019-07-30 11:04:01', '2019-07-30 13:02:42', '1', '1', '0', 'springfox.documentation.swagger.web.ApiResourceController', 'swaggerResources');
INSERT INTO `base_api` VALUES ('1156037702464995329', 'cf6a6c1897dc3bc2ae3fe59abba8c2f6', 'uiConfiguration', 'default', '', '', '', 'open-cloud-api-zuul-server', '/swagger-resources/configuration/ui', '0', '1', '2019-07-30 11:04:01', '2019-07-30 13:02:42', '1', '1', '0', 'springfox.documentation.swagger.web.ApiResourceController', 'uiConfiguration');
INSERT INTO `base_api` VALUES ('1156037702569852929', '7b980f1a7227e563fee6047756cf5ac3', 'securityConfiguration', 'default', '', '', '', 'open-cloud-api-zuul-server', '/swagger-resources/configuration/security', '0', '1', '2019-07-30 11:04:01', '2019-07-30 13:02:42', '1', '1', '0', 'springfox.documentation.swagger.web.ApiResourceController', 'securityConfiguration');
INSERT INTO `base_api` VALUES ('1156037702674710529', '89c3da13314b86a70f299e6ca1455150', 'errorHtml', 'default', '', '', 'text/html', 'open-cloud-api-zuul-server', '/error', '0', '1', '2019-07-30 11:04:01', '2019-07-30 13:02:42', '1', '1', '0', 'org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController', 'errorHtml');
INSERT INTO `base_api` VALUES ('1156037705061269506', '9275410ca1d4198af7a7e66b2661f422', '批量删除数据', 'default', '批量删除数据', 'POST', '', 'open-cloud-base-server', '/api/batch/remove', '0', '1', '2019-07-30 11:04:01', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseApiController', 'batchRemove');
INSERT INTO `base_api` VALUES ('1156037705921101825', 'de9889498a75766df0dd9644b21b07bf', '批量修改公开状态', 'default', '批量修改公开状态', 'POST', '', 'open-cloud-base-server', '/api/batch/update/open', '0', '1', '2019-07-30 11:04:01', '2019-08-21 11:44:34', '1', '1', '1', 'com.opencloud.base.server.controller.BaseApiController', 'batchUpdateOpen');
INSERT INTO `base_api` VALUES ('1156037706126622721', 'f7244837c4701df4f47e540682405e5e', '批量修改身份认证', 'default', '批量修改身份认证', 'POST', '', 'open-cloud-base-server', '/api/batch/update/auth', '0', '1', '2019-07-30 11:04:01', '2019-08-21 11:44:26', '1', '1', '1', 'com.opencloud.base.server.controller.BaseApiController', 'batchUpdateAuth');
INSERT INTO `base_api` VALUES ('1156037706206314497', '1c335308b42f13c97e2f41ae42418f56', '批量修改状态', 'default', '批量修改状态', 'POST', '', 'open-cloud-base-server', '/api/batch/update/status', '0', '1', '2019-07-30 11:04:01', '2019-08-19 18:59:18', '1', '1', '1', 'com.opencloud.base.server.controller.BaseApiController', 'batchUpdateStatus');
INSERT INTO `base_api` VALUES ('1156038470987317250', 'b8c71f653aa802bbedf16c4ec9b20e3e', '获取当前登录用户信息-SSO单点登录', 'default', '获取当前登录用户信息-SSO单点登录', 'GET', '', 'open-cloud-uaa-admin-server', '/current/user/sso', '0', '1', '2019-07-30 11:07:04', '2019-08-22 14:36:44', '1', '1', '0', 'com.opencloud.uaa.admin.server.controller.LoginController', 'principal');

-- ----------------------------
-- Table structure for base_app
-- ----------------------------
DROP TABLE IF EXISTS `base_app`;
CREATE TABLE `base_app` (
  `app_id` varchar(50) NOT NULL COMMENT '客户端ID',
  `api_key` varchar(255) DEFAULT NULL COMMENT 'API访问key',
  `secret_key` varchar(255) NOT NULL COMMENT 'API访问密钥',
  `app_name` varchar(255) NOT NULL COMMENT 'app名称',
  `app_name_en` varchar(255) NOT NULL COMMENT 'app英文名称',
  `app_icon` varchar(255) NOT NULL COMMENT '应用图标',
  `app_type` varchar(50) NOT NULL COMMENT 'app类型:server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用',
  `app_desc` varchar(255) DEFAULT NULL COMMENT 'app描述',
  `app_os` varchar(25) DEFAULT NULL COMMENT '移动应用操作系统:ios-苹果 android-安卓',
  `website` varchar(255) NOT NULL COMMENT '官网地址',
  `developer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '开发者ID:默认为0',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统应用-基础信息';

-- ----------------------------
-- Records of base_app
-- ----------------------------
INSERT INTO `base_app` VALUES ('1552274783265', '7gBZcbsC7kLIWCdELIl8nxcs', '0osTIhce7uPvDKHz6aa67bhCukaKoYl4', '平台用户认证服务器', 'open-cloud-uaa-admin-server', '', 'server', '资源服务器', '', 'http://www.baidu.com', '0', '2018-11-12 17:48:45', '2019-07-11 18:31:05', '1', '1');
INSERT INTO `base_app` VALUES ('1552294656514', 'rOOM15Zbc3UFWgW2h71gRFvi', '2Iw2B0UCMYd1cZjt8Fpr4KJUx75wQCwW', 'SSO单点登录DEMO', 'sso-demo', '', 'server', 'sso', '', 'http://www.baidu.com', '0', '2018-11-12 17:48:45', '2019-07-11 18:31:05', '1', '1');

-- ----------------------------
-- Table structure for base_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_authority`;
CREATE TABLE `base_authority` (
  `authority_id` bigint(20) NOT NULL,
  `authority` varchar(255) NOT NULL COMMENT '权限标识',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单资源ID',
  `api_id` bigint(20) DEFAULT NULL COMMENT 'API资源ID',
  `action_id` bigint(20) DEFAULT NULL,
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`authority_id`),
  KEY `menu_id` (`menu_id`),
  KEY `api_id` (`api_id`),
  KEY `action_id` (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限-菜单权限、操作权限、API权限';

-- ----------------------------
-- Records of base_authority
-- ----------------------------
INSERT INTO `base_authority` VALUES ('1', 'MENU_system', '1', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('2', 'MENU_gatewayIpLimit', '2', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('3', 'MENU_systemMenu', '3', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('5', 'MENU_gatewayRoute', '5', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('6', 'MENU_systemApi', '6', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('7', 'MENU_gatewayTrace', '7', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('8', 'MENU_systemRole', '8', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('9', 'MENU_systemApp', '9', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('10', 'MENU_systemUser', '10', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('11', 'MENU_apiDebug', '11', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('12', 'MENU_gatewayLogs', '12', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('13', 'MENU_gateway', '13', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('14', 'MENU_gatewayRateLimit', '14', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('15', 'MENU_task', '15', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('16', 'MENU_job', '16', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('17', 'MENU_message', '17', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('18', 'MENU_webhook', '18', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('19', 'MENU_taskLogs', '19', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('99', 'API_all', null, '1', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('100', 'API_actuator', null, '2', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131849293509033986', 'ACTION_systemMenuView', null, null, '1131849293404176385', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131849510677512193', 'ACTION_systemMenuEdit', null, null, '1131849510572654593', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131858946414489602', 'ACTION_systemRoleView', null, null, '1131858946338992129', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131863248373690369', 'ACTION_systemRoleEdit', null, null, '1131863248310775809', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131863723806437377', 'ACTION_systemAppView', null, null, '1131863723722551297', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131863775966801921', 'ACTION_systemAppEdit', null, null, '1131863775899693057', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131864400590942210', 'ACTION_systemUserView', null, null, '1131864400507056130', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131864444954095617', 'ACTION_systemUserEdit', null, null, '1131864444878598146', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131864827327819778', 'ACTION_gatewayIpLimitView', null, null, '1131864827252322305', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131864864325775361', 'ACTION_gatewayIpLimitEdit', null, null, '1131864864267055106', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131865040381685761', 'ACTION_gatewayRouteView', null, null, '1131865040289411074', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131865075697725442', 'ACTION_gatewayRouteEdit', null, null, '1131865075609645057', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131865482390024193', 'ACTION_systemApiView', null, null, '1131865482314526722', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131865520809848834', 'ACTION_systemApiEdit', null, null, '1131865520738545666', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131865773000765441', 'ACTION_gatewayLogsView', null, null, '1131865772929462274', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131865931214106626', 'ACTION_gatewayRateLimitView', null, null, '1131865931146997761', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131865974771953666', 'ACTION_gatewayRateLimitEdit', null, null, '1131865974704844802', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131866278280179714', 'ACTION_jobView', null, null, '1131866278187905026', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131866310676983810', 'ACTION_jobEdit', null, null, '1131866310622457857', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131866943534542850', 'ACTION_schedulerLogsView', null, null, '1131866943459045377', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1131867094550458369', 'ACTION_notifyHttpLogsView', null, null, '1131867094479155202', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168010446168066', 'API_9a1e16647d1e534f7b06ed9db83719fa', null, '1149168010337116161', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168010530054146', 'API_26128c7aafae30020bcac2c47631497a', null, '1149168010500694017', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168010601357314', 'API_84d30b0aab28de096779d6f8bee960e7', null, '1149168010571997185', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168010681049090', 'API_756154d7c8c6a79f02da79d9148beb9f', null, '1149168010655883266', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168010743963649', 'API_92187c259636fdf5d4f108b688fc44ee', null, '1149168010722992129', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168010823655425', 'API_214e294809bb1f22abc70c38959e1343', null, '1149168010794295298', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168010886569985', 'API_a0f00dd477c9232a51b94553ad7a7803', null, '1149168010861404161', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168010953678850', 'API_6311ab783c8818cd5918e5d903fb371e', null, '1149168010920124418', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011012399105', 'API_aa37236289cedba711c8f648a94b1105', null, '1149168010991427586', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011071119362', 'API_e18bae5b2fc727e77d36a3f4697abf0f', null, '1149168011045953537', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011125645314', 'API_d4c4c1b5b3847cc63b52ed9007698325', null, '1149168011100479489', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011188559874', 'API_9e054a7dc4194e7635c2686e1109aa49', null, '1149168011163394050', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011243085826', 'API_fa32726ed4076d44cae0023132f7014f', null, '1149168011217920002', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011301806082', 'API_c5863e8ad8a5605354b9a122511ffa01', null, '1149168011280834562', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011356332033', 'API_a23793da67076d4be853daa422943c53', null, '1149168011331166209', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011490549761', 'API_93c006692e386f96555e7edece91c994', null, '1149168011452801025', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011545075713', 'API_385d34ec827de818225a981d737f307a', null, '1149168011524104194', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011599601666', 'API_ccb6139e4beb2a84e29f399215bfedc0', null, '1149168011578630146', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011649933313', 'API_f09f5543ff4c0ceab04b99ddc5a94bc2', null, '1149168011628961794', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011708653570', 'API_331bddbcd34cacdd74f80b48e3484017', null, '1149168011687682050', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011767373826', 'API_785b2368bf67d1a942ea64d946c70144', null, '1149168011742208002', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011826094081', 'API_282c563cbfb922ac6328c058051d31a4', null, '1149168011805122561', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011876425729', 'API_f2be18d77a78f728df355f32e90cd780', null, '1149168011851259906', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011926757377', 'API_4611bd638b538605286600dbc899cbb0', null, '1149168011909980161', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168011981283329', 'API_5262f2214a4ff43179c947dc458213d7', null, '1149168011960311809', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012031614977', 'API_8a6561a9a0efa7f5f9faadb6e39dc927', null, '1149168012010643458', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012086140929', 'API_1df7911bb85d15f51bcd78c43f18b24d', null, '1149168012060975106', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012132278273', 'API_80256cc196e1e6d2437289e038b13b8a', null, '1149168012111306753', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012182609922', 'API_5586d5f337d4ebf99e551fdbf55d1116', null, '1149168012161638401', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012237135873', 'API_b96a9514487f7e7679fe39c9254ab972', null, '1149168012216164353', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012287467521', 'API_3a1c4ef17565d863f35a3bccd7cc035e', null, '1149168012270690305', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012341993473', 'API_734d1179e332309b9de3927894864396', null, '1149168012316827649', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012442656770', 'API_39ad69234435595ee67836e8a03d6ea6', null, '1149168012421685249', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012492988418', 'API_50520c56c7714aa9be9a808cd30fd9bb', null, '1149168012467822593', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012539125762', 'API_9aca4415969b60158f383b2b49b8fe79', null, '1149168012522348546', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012593651713', 'API_c4c154b6ffa4b21835db1e09614c73e6', null, '1149168012576874498', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012639789058', 'API_4a13f24fa2d5cf74a6e408e82182e1b1', null, '1149168012623011841', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012690120705', 'API_2901b2b8040f1c44c0318650fbfd4460', null, '1149168012673343490', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012736258049', 'API_87049d9db5261f50d524c45a32044415', null, '1149168012719480834', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012790784002', 'API_50bcf72df4a18a6a8c46a7076a7a014a', null, '1149168012769812481', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012836921346', 'API_638be151d314740a2f2aaebaaba3f479', null, '1149168012820144129', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012891447297', 'API_e3c1b0b860591e11b50d6dccc9e2fa6a', null, '1149168012870475777', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012941778945', 'API_bbc5546a6cfa1161859b69ef5754eace', null, '1149168012920807425', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168012996304898', 'API_720f3b2571fe2850108f090408514cae', null, '1149168012979527682', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013046636546', 'API_8f2b70d4b55f13eaf82c4cf687903ced', null, '1149168013029859330', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013092773889', 'API_d9fc987468fd4c73350653d5c4e53453', null, '1149168013071802369', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013143105537', 'API_f4e83562a36e943883c5eb6b7376e365', null, '1149168013122134017', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013197631489', 'API_9b57678bc96af393c0c60e135acc90a9', null, '1149168013176659970', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013243768834', 'API_4693b9a455022754623dd25f9905c19b', null, '1149168013226991617', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013302489090', 'API_51ea3146eaebdd33fd065b029ee147fb', null, '1149168013277323266', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013348626434', 'API_b4cdd238a7e34e1794988d00c4d45779', null, '1149168013327654913', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013390569474', 'API_610b499835de759e37429ece2de1ee33', null, '1149168013373792257', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013436706818', 'API_334d10291e4061c07a180546dd9cedea', null, '1149168013419929601', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013482844161', 'API_d722dc79f9c8d3ece3b9324a03caf902', null, '1149168013466066945', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013533175810', 'API_c8293b83e96ca425be2e24f0cfa8c7bf', null, '1149168013512204290', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013583507458', 'API_3fdb78cf597834182b4c6b558a800774', null, '1149168013562535938', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013625450498', 'API_dc01f78458d3fe0cff028da861bdb65b', null, '1149168013608673282', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013675782145', 'API_25c851a0698d161f2b00bdcf74668648', null, '1149168013659004929', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013726113794', 'API_86197fb92e8fd3359842cf511c68be8c', null, '1149168013700947969', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013768056834', 'API_db9fbb85ee040fd2f8981e47a6873614', null, '1149168013747085313', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013814194178', 'API_bbd37ea32bf91697909dd261befa8a5f', null, '1149168013797416961', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013864525825', 'API_b0d37206d8ebf60a7359e1b15d79b361', null, '1149168013843554306', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013910663170', 'API_88fa6cc7c4fa7f0fb2b8d3df951fdfa9', null, '1149168013889691650', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168013960994818', 'API_902ca6aa4d96c21a48cabe5363a4b3dd', null, '1149168013940023298', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014011326465', 'API_0f86b27eb3b0fcd3e714e22d43b1172b', null, '1149168013994549249', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014057463810', 'API_83ad6d5e0903bf19cfa28c7a83595de2', null, '1149168014036492290', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014099406849', 'API_6eb627d08d2f0aee74bfd7c9b53b1124', null, '1149168014082629634', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014141349889', 'API_3fde01046c8643e9b536ae6f61d4431f', null, '1149168014128766978', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014187487234', 'API_2cfc3bbc760608bda59d68b2c0b4e2fd', null, '1149168014170710018', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014233624578', 'API_f5abed6e35a7a1715fac80841e4e592f', null, '1149168014212653057', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014376230913', 'API_c584e31594bc5bedfc959c729a2e90e7', null, '1149168014258790401', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014426562561', 'API_24b5559e0204223af9b73f3c68ee68f3', null, '1149168014405591042', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014472699906', 'API_160a9fb0ce4243b989bbaaac031a345e', null, '1149168014451728385', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014518837249', 'API_99a30e8643b0820a04ecbdcf7c9628af', null, '1149168014497865729', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014560780290', 'API_0dfca465c864c74d04a4d6135fee1880', null, '1149168014544003073', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014606917634', 'API_2aecc273984a1d3981dc0f62cafbe894', null, '1149168014585946113', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014657249282', 'API_b993f44f1ad4dd08bfd4f36730af9ea1', null, '1149168014636277762', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014703386625', 'API_ab71840616ff1f1a619170538b1f1de9', null, '1149168014686609409', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014749523970', 'API_5f512e5065e80c8d0ffd6be4f27c386e', null, '1149168014732746753', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014795661313', 'API_f83ecfdd9325be59c60627f0164ec8a8', null, '1149168014774689793', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014833410049', 'API_9c565ce4ded10ccd227001f6d7eab11c', null, '1149168014816632833', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014879547393', 'API_0830e3c6c3d42f11d86f5e2ed0d99985', null, '1149168014862770178', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014925684737', 'API_8dff62591ccefba3537b84aa9d72d7b7', null, '1149168014908907521', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168014976016385', 'API_382e5fe59fdc31f240b5e42b2ae1a522', null, '1149168014955044866', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168015017959426', 'API_177b640de1b90d39536f462f24e3051f', null, '1149168015001182210', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168015076679682', 'API_1307447addfddf7e075eea74100c150d', null, '1149168015055708162', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168015135399937', 'API_40a481fc86814de744e91e42c15b6f3e', null, '1149168015118622721', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168015185731586', 'API_61e8c492eb9e56441d1c428ab3e09f66', null, '1149168015164760065', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168208639614978', 'API_3c28ffa71681fb53f1f458548456256c', null, '1149168208614449153', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168208706723841', 'API_29c8264c689bce3567708f977779c3b9', null, '1149168208677363714', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149168208765444098', 'API_f98a1543766b726e4b5a6d7a7f32fd93', null, '1149168208740278273', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149253733748785154', 'MENU_developer', '1149253733673287682', null, null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149265710541914115', 'API_be5eb57a4df27bf8dece0bda751a5a62', null, '1149265710541914114', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149265710692909058', 'API_0273e0d10bea64046178b7c5ff5055cc', null, '1149265710646771714', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149265710789378049', 'API_3b5928629e5398dcc534c548d5c01085', null, '1149265710743240705', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1149265710877458434', 'API_df78a9afa62bb2d1165aeea14aca9e41', null, '1149265710839709697', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151324453706289153', 'API_4fdfe1d02c78379a3a8eb2a84b0e8b82', null, '1151324453580460033', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151324453811146754', 'API_92eabcb8d4d4d587343cc2b6e11ccecc', null, '1151324453773398018', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151324453920198658', 'API_d094367240c4c6847eeda0fe2317069c', null, '1151324453869867009', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151324454025056258', 'API_d26205d1d1e42d925ea972a6d034ba15', null, '1151324453974724609', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151324454247354369', 'API_5b2390e91198ae02e58b4704e23f7a45', null, '1151324454205411329', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151324462254280706', 'API_f17677d7f0f33851333de6bc48d5ac1a', null, '1151324462224920577', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151395583808917506', 'API_042dd26520a30c0fecd13ee015a79be5', null, '1151395583808917505', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151426336538128386', 'API_93ff44fd4adab6c4097327eb5ffd405a', null, '1151426336538128385', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151426336915615747', 'API_030aae5c245d4d780ca248fdd5e4bb67', null, '1151426336915615746', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151426338278764546', 'API_7f542a952be1a9d8b089330f5f0aba3b', null, '1151426338278764545', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151426338278764548', 'API_b9f2f4ed495f3f71da31c14e8ad2193d', null, '1151426338278764547', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151426338681417729', 'API_14467518667ccfa46541736433d0efa2', null, '1151426338278764549', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151426338811441154', 'API_903acc6b8c02184170bd3f316cd0f0c3', null, '1151426338756915201', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1151426339037933569', 'API_cb8aded7fdffd0e8e86940f540b32472', null, '1151426338903715841', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153543688968871938', 'API_8aedf1bcd8ebc00a41361067602e4866', null, '1153543688817876993', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153543700159275010', 'API_9ae39e2a7bb1f2502dcd5f4d280b739d', null, '1153543700121526273', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153543700238966785', 'API_8e25fdc700610c63d638c55545441119', null, '1153543700201218049', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153543700343824386', 'API_0cea701426acbc8348e64ecb598a8bf7', null, '1153543700285104130', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153543700427710466', 'API_604809625765c7df1e1e8ee8b7e7fe81', null, '1153543700398350338', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153544494539481090', 'API_09e8e7b093a0fb008071c09bf440fda1', null, '1153544494501732354', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153544494644338690', 'API_1f7307e3c2809aff5437f3594594c8ca', null, '1153544494598201345', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153544494724030465', 'API_4d27a9ae885d7dbfc82e7ae2c86ec9d6', null, '1153544494694670337', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153544494807916545', 'API_c235abccc85e47cda9710697c59a4aa7', null, '1153544494765973506', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153544495109906433', 'API_404ea9fec51f41d5ebdefcf30039d173', null, '1153544495072157698', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153544495168626690', 'API_154a10d28aa54f3ab5eec62c5b3cc396', null, '1153544495139266561', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153544495244124162', 'API_d0cb7641b01ea63b4b680aa41372f3dd', null, '1153544495210569729', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1153544495307038721', 'API_e57afa79be638071473395318684b732', null, '1153544495277678593', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1156037702284640257', 'API_8a2aa97d530b74b9e440505870e36d89', null, '1156037702112673793', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1156037702410469377', 'API_f03bda1e0dcaeb9b777caf1ef6b23af5', null, '1156037702343360514', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1156037702523715586', 'API_cf6a6c1897dc3bc2ae3fe59abba8c2f6', null, '1156037702464995329', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1156037702620184577', 'API_7b980f1a7227e563fee6047756cf5ac3', null, '1156037702569852929', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1156037702708264962', 'API_89c3da13314b86a70f299e6ca1455150', null, '1156037702674710529', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1156037705195487233', 'API_9275410ca1d4198af7a7e66b2661f422', null, '1156037705061269506', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1156037705963044866', 'API_de9889498a75766df0dd9644b21b07bf', null, '1156037705921101825', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1156037706155982850', 'API_f7244837c4701df4f47e540682405e5e', null, '1156037706126622721', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1156037706244063234', 'API_1c335308b42f13c97e2f41ae42418f56', null, '1156037706206314497', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1156038471020871682', 'API_b8c71f653aa802bbedf16c4ec9b20e3e', null, '1156038470987317250', null, '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1164422088618938370', 'ACTION_developerView', null, null, '1164422088547635202', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');
INSERT INTO `base_authority` VALUES ('1164422211226832898', 'ACTION_developerEdit', null, null, '1164422211189084162', '1', '2019-07-30 15:43:15', '2019-07-30 15:43:15');

-- ----------------------------
-- Table structure for base_authority_action
-- ----------------------------
DROP TABLE IF EXISTS `base_authority_action`;
CREATE TABLE `base_authority_action` (
  `action_id` bigint(20) NOT NULL COMMENT '操作ID',
  `authority_id` bigint(20) NOT NULL COMMENT 'API',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  KEY `action_id` (`action_id`) USING BTREE,
  KEY `authority_id` (`authority_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统权限-功能操作关联表';

-- ----------------------------
-- Records of base_authority_action
-- ----------------------------
INSERT INTO `base_authority_action` VALUES ('1131864400507056130', '1149168013625450498', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864400507056130', '1131813663584489473', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864400507056130', '1131753771397967873', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864400507056130', '1131753771712540674', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864400507056130', '1131753772526235650', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131866943459045377', '1151426338681417729', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131866943459045377', '1131814634553282561', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1149168013675782145', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1149168013436706818', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1149168013583507458', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1149168013482844161', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1149168012086140929', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1149168012031614977', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1149168013768056834', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1149168013197631489', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1131813663827759105', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1131753772400406529', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1131753772249411585', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1131753771507019778', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131864444878598146', '1131753771607683074', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1164422211189084162', '1149168012287467521', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1164422211189084162', '1149168012539125762', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1164422211189084162', '1149168012341993473', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1164422211189084162', '1149168012442656770', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849293404176385', '1149168012891447297', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849293404176385', '1149168012736258049', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849293404176385', '1149168012836921346', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849293404176385', '1131813661852241922', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849293404176385', '1131813663894867969', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849293404176385', '1131813661235679233', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849293404176385', '1131753768596172802', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849293404176385', '1131753768814276610', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849293404176385', '1131844868619030529', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1164422088547635202', '1149168012593651713', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131858946338992129', '1149168013046636546', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131858946338992129', '1149168013243768834', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131858946338992129', '1131813663743873026', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131858946338992129', '1131813663672569857', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131858946338992129', '1131753770320031746', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131858946338992129', '1131753770445860866', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131858946338992129', '1131753770185814018', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131858946338992129', '1131753770596855809', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1149168013533175810', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1149168013143105537', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1149168013348626434', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1149168013390569474', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1149168013046636546', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1149168013092773889', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1149168012182609922', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1149168011826094081', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1149168013302489090', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1131753770705907714', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1131753770823348225', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1131753771100172289', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1131753771226001410', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863248310775809', '1131813664213635074', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131865482314526722', '1149168011125645314', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131865482314526722', '1149168011012399105', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131865482314526722', '1151324462254280706', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131865482314526722', '1131995510689820674', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131865482314526722', '1131753765458833410', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131865482314526722', '1131753765324615681', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131865482314526722', '1131753765182009346', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168011243085826', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168011545075713', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168011649933313', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168011767373826', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168011356332033', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168011188559874', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168011490549761', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168011301806082', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168011876425729', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168011708653570', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1149168012442656770', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863775899693057', '1131813663953588225', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863723722551297', '1149168011243085826', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863723722551297', '1149168011545075713', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863723722551297', '1149168011599601666', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863723722551297', '1131813663404134402', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863723722551297', '1132203893955006465', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863723722551297', '1132203893384581121', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131863723722551297', '1132203893577519106', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168012736258049', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168011926757377', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168012891447297', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168011981283329', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168010530054146', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168010601357314', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168010681049090', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168010446168066', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168010743963649', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168012790784002', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168012996304898', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1149168012941778945', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1131813661348925441', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1131813661483143169', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1131813661613166594', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1131813663500603394', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1131813663894867969', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1131813664033280001', '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1131753768357097474', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1131753768474537986', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131849510572654593', '1131753768705224706', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1156037705963044866', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1156037706155982850', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1156037706244063234', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1156037705195487233', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1149168011125645314', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1149168010953678850', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1149168010823655425', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1149168010886569985', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1131753764884213761', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1131753765026820098', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865520738545666', '1131753765614022657', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865772929462274', '1149168014011326465', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865772929462274', '1131753774162014210', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864827252322305', '1149168014426562561', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864827252322305', '1149168014560780290', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864827252322305', '1131753775453859842', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864827252322305', '1131753774891823105', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864827252322305', '1131753775000875010', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865931146997761', '1149168014925684737', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865931146997761', '1149168014657249282', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865931146997761', '1131753774187180034', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865931146997761', '1131753776095588354', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865931146997761', '1131753776204640257', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865931146997761', '1131753776292720641', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1149168011071119362', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1149168014472699906', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1149168014233624578', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1149168014518837249', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1149168014376230913', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1149168014426562561', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1149168014606917634', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1131753775768432642', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1131753775642603522', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1131753775181230082', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131864864267055106', '1131753775000875012', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1149168014795661313', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1149168014657249282', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1149168014833410049', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1149168014703386625', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1149168014879547393', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1149168014749523970', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1149168011071119362', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1131753776527601665', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1131753776657625090', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1131753776808620034', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865974704844802', '1131753776917671938', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865040289411074', '1149168014976016385', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865040289411074', '1149168015017959426', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865040289411074', '1131753777139970050', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865040289411074', '1131753777026723841', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865040289411074', '1131753774552084481', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865075609645057', '1149168015135399937', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865075609645057', '1149168015185731586', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865075609645057', '1149168015017959426', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865075609645057', '1149168014141349889', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865075609645057', '1149168015076679682', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865075609645057', '1131753777211273218', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865075609645057', '1131753777416794113', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131865075609645057', '1131753777517457410', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866278187905026', '1151426338681417729', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866278187905026', '1151426336538128386', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866278187905026', '1131814634553282561', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866278187905026', '1131814634746220545', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866310622457857', '1151426338278764546', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866310622457857', '1151426339037933569', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866310622457857', '1151426338278764548', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866310622457857', '1151426338811441154', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866310622457857', '1151426336915615747', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866310622457857', '1131814634217738242', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866310622457857', '1131814634293235714', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866310622457857', '1131814634381316097', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866310622457857', '1131814634469396481', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131866310622457857', '1131814634645557249', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131867094479155202', '1149265710877458434', '2019-08-22 15:00:00', '2019-08-22 15:00:00');
INSERT INTO `base_authority_action` VALUES ('1131867094479155202', '1131814508397006849', '2019-08-22 15:00:00', '2019-08-22 15:00:00');

-- ----------------------------
-- Table structure for base_authority_app
-- ----------------------------
DROP TABLE IF EXISTS `base_authority_app`;
CREATE TABLE `base_authority_app` (
  `authority_id` bigint(50) NOT NULL COMMENT '权限ID',
  `app_id` varchar(100) NOT NULL COMMENT '应用ID',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间:null表示长期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  KEY `authority_id` (`authority_id`) USING BTREE,
  KEY `app_id` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限-应用关联';

-- ----------------------------
-- Records of base_authority_app
-- ----------------------------

-- ----------------------------
-- Table structure for base_authority_role
-- ----------------------------
DROP TABLE IF EXISTS `base_authority_role`;
CREATE TABLE `base_authority_role` (
  `authority_id` bigint(20) NOT NULL COMMENT '权限ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间:null表示长期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  KEY `authority_id` (`authority_id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限-角色关联';

-- ----------------------------
-- Records of base_authority_role
-- ----------------------------
INSERT INTO `base_authority_role` VALUES ('1', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('3', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('8', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('9', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('10', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131858946414489602', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131863723806437377', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131864400590942210', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131849293509033986', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131858946414489602', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131863723806437377', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131864400590942210', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131849293509033986', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131858946414489602', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131863723806437377', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131864400590942210', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131849293509033986', '2', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('9', '3', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('9', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('10', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1149253733748785154', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('3', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('8', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('13', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('6', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('11', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('12', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('2', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('14', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('5', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('15', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('16', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('19', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('17', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('18', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131849293509033986', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131849510677512193', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131858946414489602', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131863248373690369', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131863775966801921', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131864400590942210', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131864444954095617', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131864827327819778', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131864864325775361', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131865040381685761', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131865075697725442', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131865482390024193', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131865773000765441', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131865931214106626', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131865974771953666', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131866278280179714', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131866310676983810', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131866943534542850', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131867094550458369', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131863723806437377', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1164422088618938370', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1164422211226832898', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');
INSERT INTO `base_authority_role` VALUES ('1131865520809848834', '1', null, '2019-08-22 14:54:41', '2019-08-22 14:54:41');

-- ----------------------------
-- Table structure for base_authority_user
-- ----------------------------
DROP TABLE IF EXISTS `base_authority_user`;
CREATE TABLE `base_authority_user` (
  `authority_id` bigint(20) NOT NULL COMMENT '权限ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  KEY `authority_id` (`authority_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限-用户关联';

-- ----------------------------
-- Records of base_authority_user
-- ----------------------------

-- ----------------------------
-- Table structure for base_developer
-- ----------------------------
DROP TABLE IF EXISTS `base_developer`;
CREATE TABLE `base_developer` (
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '登陆账号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `user_type` varchar(20) DEFAULT 'isp' COMMENT '开发者类型: isp-服务提供商 normal-自研开发者',
  `company_id` bigint(20) DEFAULT NULL COMMENT '企业ID',
  `user_desc` varchar(255) DEFAULT '' COMMENT '描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(3) DEFAULT '1' COMMENT '状态:0-禁用 1-正常 2-锁定',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-开发者信息';

-- ----------------------------
-- Records of base_developer
-- ----------------------------

-- ----------------------------
-- Table structure for base_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_menu`;
CREATE TABLE `base_menu` (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单Id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级菜单',
  `menu_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '菜单编码',
  `menu_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
  `menu_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `scheme` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '路径前缀',
  `path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求路径',
  `icon` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '菜单标题',
  `target` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '_self' COMMENT '打开方式:_self窗口内,_blank新窗口',
  `priority` bigint(20) NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  `service_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务名',
  PRIMARY KEY (`menu_id`),
  UNIQUE KEY `menu_code` (`menu_code`),
  KEY `service_id` (`service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-菜单信息';

-- ----------------------------
-- Records of base_menu
-- ----------------------------
INSERT INTO `base_menu` VALUES ('1', '0', 'system', '系统管理', '系统管理', '/', '', 'md-folder', '_self', '0', '1', '2018-07-29 21:20:10', '2019-05-25 01:49:23', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('2', '13', 'gatewayIpLimit', '访问控制', '来源IP/域名访问控制,白名单、黑名单', '/', 'gateway/ip-limit/index', 'md-document', '_self', '1', '1', '2018-07-29 21:20:13', '2019-07-11 18:05:32', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('3', '1', 'systemMenu', '功能菜单', '功能菜单资源', '/', 'system/menus/index', 'md-list', '_self', '3', '1', '2018-07-29 21:20:13', '2019-07-11 18:03:23', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('5', '13', 'gatewayRoute', '网关路由', '网关路由', '/', 'gateway/route/index', 'md-document', '_self', '5', '1', '2018-07-29 21:20:13', '2019-07-11 18:04:59', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('6', '13', 'systemApi', 'API列表', 'API接口资源', '/', 'system/api/index', 'md-document', '_self', '0', '1', '2018-07-29 21:20:13', '2019-03-13 21:48:12', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('8', '1', 'systemRole', '角色管理', '角色信息管理', '/', 'system/role/index', 'md-people', '_self', '8', '1', '2018-12-27 15:26:54', '2019-07-11 18:03:10', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('9', '1', 'systemApp', '应用管理', '应用信息管理', '/', 'system/app/index', 'md-apps', '_self', '0', '1', '2018-12-27 15:41:52', '2019-07-11 18:03:45', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('10', '1', 'systemUser', '用户管理', '系统用户', '/', 'system/user/index', 'md-person', '_self', '0', '1', '2018-12-27 15:46:29', '2019-07-11 18:03:55', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('11', '13', 'apiDebug', '接口调试', 'swagger接口调试', 'http://', 'localhost:8888', 'md-document', '_blank', '0', '1', '2019-01-10 20:47:19', '2019-05-25 03:26:47', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('12', '13', 'gatewayLogs', '访问日志', '', '/', 'gateway/logs/index', 'md-document', '_self', '0', '1', '2019-01-28 02:37:42', '2019-02-25 00:16:40', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('13', '0', 'gateway', 'API网关', 'API网关', '/', '', 'md-folder', '_self', '0', '1', '2019-02-25 00:15:09', '2019-03-18 04:44:20', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('14', '13', 'gatewayRateLimit', '流量控制', 'API限流', '/', 'gateway/rate-limit/index', 'md-document', '_self', '2', '1', '2019-03-13 21:47:20', '2019-07-11 18:05:18', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('15', '0', 'task', '任务调度', '任务调度', '/', '', 'md-document', '_self', '0', '1', '2019-04-01 16:30:27', '2019-07-11 18:07:50', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('16', '15', 'job', '定时任务', '定时任务列表', '/', 'task/job/index', 'md-document', '_self', '0', '1', '2019-04-01 16:31:15', '2019-07-11 18:08:12', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('17', '0', 'message', '消息管理', '消息管理', '/', '', 'md-document', '_self', '0', '1', '2019-04-04 16:37:23', '2019-04-04 16:37:23', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('18', '17', 'webhook', '异步通知日志', '异步通知日志', '/', 'msg/webhook/index', 'md-document', '_self', '0', '1', '2019-04-04 16:38:21', '2019-07-11 18:06:23', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('19', '15', 'taskLogs', '调度日志', '调度日志', '/', 'task/logs/index', 'md-document', '_self', '0', '1', '2019-05-24 18:17:49', '2019-07-11 18:08:26', '1', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('1141579952217567234', '0', 'monitor', '系统监控', '系统监控', '/', '', 'md-document', '_self', '0', '1', '2019-06-20 13:34:04', '2019-06-20 13:34:04', '0', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('1141580147030405121', '1141579952217567234', 'SpringBootAdmin', 'SpringBootAdmin', 'SpringBootAdmin', 'http://', 'localhost:8849', 'md-document', '_blank', '0', '1', '2019-06-20 13:34:51', '2019-07-11 18:11:58', '0', 'open-cloud-base-server');
INSERT INTO `base_menu` VALUES ('1149253733673287682', '1', 'developer', '开发者管理', '开发者管理', '/', 'system/developer/index', 'md-person', '_self', '0', '1', '2019-07-11 17:46:56', '2019-07-11 18:04:00', '0', 'open-cloud-base-server');

-- ----------------------------
-- Table structure for base_role
-- ----------------------------
DROP TABLE IF EXISTS `base_role`;
CREATE TABLE `base_role` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `role_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '角色名称',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `role_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统角色-基础信息';

-- ----------------------------
-- Records of base_role
-- ----------------------------
INSERT INTO `base_role` VALUES ('1', 'admin', '系统管理员', '1', '系统管理员', '2018-07-29 21:14:54', '2019-05-25 03:06:57', '1');
INSERT INTO `base_role` VALUES ('2', 'operator', '运营人员', '1', '运营人员', '2018-07-29 21:14:54', '2019-05-25 15:14:56', '1');
INSERT INTO `base_role` VALUES ('3', 'support', '客服', '1', '客服', '2018-07-29 21:14:54', '2019-05-25 15:17:07', '1');

-- ----------------------------
-- Table structure for base_role_user
-- ----------------------------
DROP TABLE IF EXISTS `base_role_user`;
CREATE TABLE `base_role_user` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  KEY `fk_user` (`user_id`) USING BTREE,
  KEY `fk_role` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统角色-用户关联';

-- ----------------------------
-- Records of base_role_user
-- ----------------------------
INSERT INTO `base_role_user` VALUES ('521677655146233856', '1', '2019-07-30 15:51:08', '2019-07-30 15:51:08');
INSERT INTO `base_role_user` VALUES ('557063237640650752', '2', '2019-07-30 15:51:08', '2019-07-30 15:51:08');

-- ----------------------------
-- Table structure for base_tentant
-- ----------------------------
DROP TABLE IF EXISTS `base_tentant`;
CREATE TABLE `base_tentant` (
  `tentant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `tentant_name` varchar(100) NOT NULL COMMENT '租户名称',
  `tentant_desc` varchar(255) NOT NULL COMMENT '租户描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`tentant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户信息表';

-- ----------------------------
-- Records of base_tentant
-- ----------------------------

-- ----------------------------
-- Table structure for base_tentant_modules
-- ----------------------------
DROP TABLE IF EXISTS `base_tentant_modules`;
CREATE TABLE `base_tentant_modules` (
  `module_id` bigint(20) NOT NULL COMMENT '模块ID',
  `tentant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `service_id` varchar(100) NOT NULL COMMENT '服务名称',
  `module_desc` varchar(255) NOT NULL COMMENT '模块描述',
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  PRIMARY KEY (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户模块';

-- ----------------------------
-- Records of base_tentant_modules
-- ----------------------------

-- ----------------------------
-- Table structure for base_user
-- ----------------------------
DROP TABLE IF EXISTS `base_user`;
CREATE TABLE `base_user` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '登陆账号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `user_type` varchar(20) DEFAULT 'normal' COMMENT '用户类型:super-超级管理员 normal-普通管理员',
  `company_id` bigint(20) DEFAULT NULL COMMENT '企业ID',
  `user_desc` varchar(255) DEFAULT '' COMMENT '描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(3) DEFAULT '1' COMMENT '状态:0-禁用 1-正常 2-锁定',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-管理员信息';

-- ----------------------------
-- Records of base_user
-- ----------------------------
INSERT INTO `base_user` VALUES ('521677655146233856', 'admin', '超级管理员', '', '515608851@qq.com', '', 'super', null, '', '2018-12-10 13:20:45', null, '1');
INSERT INTO `base_user` VALUES ('557063237640650752', 'test', '测试用户', '', '', '', 'normal', null, '', '2019-03-18 04:50:25', null, '1');
SET FOREIGN_KEY_CHECKS=1;
