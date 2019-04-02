/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : open-platform

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2019-03-18 04:59:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for base_app
-- ----------------------------
DROP TABLE IF EXISTS `base_app`;
CREATE TABLE `base_app` (
                          `app_id` varchar(50) NOT NULL COMMENT '客户端ID',
                          `app_secret` varchar(255) NOT NULL COMMENT '客户端秘钥',
                          `app_name` varchar(255) NOT NULL COMMENT 'app名称',
                          `app_name_en` varchar(255) NOT NULL COMMENT 'app英文名称',
                          `app_icon` varchar(255) NOT NULL COMMENT '应用图标',
                          `app_type` varchar(50) NOT NULL COMMENT 'app类型:server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用',
                          `app_desc` varchar(255) DEFAULT NULL COMMENT 'app描述',
                          `app_os` varchar(25) DEFAULT NULL COMMENT '移动应用操作系统:ios-苹果 android-安卓',
                          `website` varchar(255) NOT NULL COMMENT '官网地址',
                          `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID:默认为0',
                          `user_type` varchar(20) NOT NULL DEFAULT 'platform' COMMENT '用户类型:platform-平台 isp-服务提供商 dev-自研开发者',
                          `create_time` datetime NOT NULL COMMENT '创建时间',
                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                          `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
                          `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
                          PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统应用-基础信息';

-- ----------------------------
-- Records of base_app
-- ----------------------------
INSERT INTO `base_app` VALUES ('1552274783265', '2cde1eaa60fe4af1987f94caa13f29a2', '资源服务器', 'ResourceServer', '', 'server', '资源服务器', '', 'http://www.baidu.com', '521677655146233856', 'platform', '2018-11-12 17:48:45', '2019-03-16 00:29:42', '1', '1');
INSERT INTO `base_app` VALUES ('1552294656514', '74a02bade18a42388c3127751b96e1f7', '运营后台', 'Admin', '', 'pc', '运营后台', '', 'http://www.baidu.com', '521677655146233856', 'platform', '2018-11-12 17:48:45', '2019-03-16 00:24:26', '1', '1');

-- ----------------------------
-- Table structure for base_app_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_app_authority`;
CREATE TABLE `base_app_authority` (
                                    `authority_id` bigint(50) NOT NULL COMMENT '权限ID',
                                    `app_id` varchar(100) NOT NULL COMMENT '应用ID',
                                    `expire_time` datetime DEFAULT NULL COMMENT '过期时间:null表示长期',
                                    KEY `authority_id` (`authority_id`) USING BTREE,
                                    KEY `app_id` (`app_id`) USING BTREE,
                                    CONSTRAINT `base_app_authority_ibfk_1` FOREIGN KEY (`authority_id`) REFERENCES `base_authority` (`authority_id`),
                                    CONSTRAINT `base_app_authority_ibfk_2` FOREIGN KEY (`app_id`) REFERENCES `base_app` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统应用-授权';

-- ----------------------------
-- Records of base_app_authority
-- ----------------------------
INSERT INTO `base_app_authority` VALUES ('555852320928169984', '1552274783265', null);
INSERT INTO `base_app_authority` VALUES ('2', '1552274783265', null);
INSERT INTO `base_app_authority` VALUES ('555852320928169984', '1552294656514', null);

-- ----------------------------
-- Table structure for base_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_authority`;
CREATE TABLE `base_authority` (
                                `authority_id` bigint(20) NOT NULL,
                                `authority` varchar(255) NOT NULL COMMENT '权限标识',
                                `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单资源ID',
                                `api_id` bigint(20) DEFAULT NULL COMMENT 'API资源ID',
                                `operation_id` bigint(20) DEFAULT NULL,
                                `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态',
                                PRIMARY KEY (`authority_id`),
                                KEY `fk_base_authority_base_resource_menu` (`menu_id`) USING BTREE,
                                KEY `fk_base_authority_base_resource_api` (`api_id`) USING BTREE,
                                KEY `operation_id` (`operation_id`),
                                CONSTRAINT `base_authority_ibfk_1` FOREIGN KEY (`operation_id`) REFERENCES `base_resource_operation` (`operation_id`),
                                CONSTRAINT `fk_base_authority_base_resource_api` FOREIGN KEY (`api_id`) REFERENCES `base_resource_api` (`api_id`),
                                CONSTRAINT `fk_base_authority_base_resource_menu` FOREIGN KEY (`menu_id`) REFERENCES `base_resource_menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限-菜单权限、操作权限、API权限';

-- ----------------------------
-- Records of base_authority
-- ----------------------------
INSERT INTO `base_authority` VALUES ('1', 'all', null, '1', null, '1');
INSERT INTO `base_authority` VALUES ('2', 'actuator', null, '2', null, '1');
INSERT INTO `base_authority` VALUES ('3', 'menu:system', '1', null, null, '1');
INSERT INTO `base_authority` VALUES ('4', 'menu:gatewayIpLimit', '2', null, null, '1');
INSERT INTO `base_authority` VALUES ('5', 'menu:systemMenu', '3', null, null, '1');
INSERT INTO `base_authority` VALUES ('6', 'menu:systemMonitor', '4', null, null, '1');
INSERT INTO `base_authority` VALUES ('7', 'menu:gatewayRoute', '5', null, null, '1');
INSERT INTO `base_authority` VALUES ('8', 'menu:systemApi', '6', null, null, '1');
INSERT INTO `base_authority` VALUES ('9', 'menu:gatewayTrace', '7', null, null, '1');
INSERT INTO `base_authority` VALUES ('10', 'menu:systemRolei', '8', null, null, '1');
INSERT INTO `base_authority` VALUES ('11', 'menu:systemApp', '9', null, null, '1');
INSERT INTO `base_authority` VALUES ('12', 'menu:systemUser', '10', null, null, '1');
INSERT INTO `base_authority` VALUES ('13', 'menu:apiDebug', '11', null, null, '1');
INSERT INTO `base_authority` VALUES ('14', 'menu:gatewayLogs', '12', null, null, '1');
INSERT INTO `base_authority` VALUES ('15', 'menu:gateway', '13', null, null, '1');
INSERT INTO `base_authority` VALUES ('16', 'menu:help', '14', null, null, '1');
INSERT INTO `base_authority` VALUES ('17', 'menu:wiki', '15', null, null, '1');
INSERT INTO `base_authority` VALUES ('555416595988480000', 'operation:systemBrowse', null, null, '555416595535495168', '1');
INSERT INTO `base_authority` VALUES ('555416596055588864', 'operation:systemCreate', null, null, '555416596017840128', '1');
INSERT INTO `base_authority` VALUES ('555416596135280640', 'operation:systemEdit', null, null, '555416596101726208', '1');
INSERT INTO `base_authority` VALUES ('555416596202389504', 'operation:systemRemove', null, null, '555416596168835072', '1');
INSERT INTO `base_authority` VALUES ('555416596298858496', 'operation:systemDetail', null, null, '555416596252721152', '1');
INSERT INTO `base_authority` VALUES ('555416596403716096', 'operation:systemAuthBrowse', null, null, '555416596340801536', '1');
INSERT INTO `base_authority` VALUES ('555416596491796480', 'operation:systemAuthCreate', null, null, '555416596449853440', '1');
INSERT INTO `base_authority` VALUES ('555416596571488256', 'operation:systemAuthEdit', null, null, '555416596529545216', '1');
INSERT INTO `base_authority` VALUES ('555416596651180032', 'operation:systemAuthRemove', null, null, '555416596600848384', '1');
INSERT INTO `base_authority` VALUES ('555416596714094592', 'operation:systemAuthDetail', null, null, '555416596688928768', '1');
INSERT INTO `base_authority` VALUES ('555416596810563584', 'operation:systemMenuBrowse', null, null, '555416596760231936', '1');
INSERT INTO `base_authority` VALUES ('555416596936392704', 'operation:systemMenuCreate', null, null, '555416596877672448', '1');
INSERT INTO `base_authority` VALUES ('555416597087387648', 'operation:systemMenuEdit', null, null, '555416596986724352', '1');
INSERT INTO `base_authority` VALUES ('555416597326462976', 'operation:systemMenuRemove', null, null, '555416597179662336', '1');
INSERT INTO `base_authority` VALUES ('555416597448097792', 'operation:systemMenuDetail', null, null, '555416597393571840', '1');
INSERT INTO `base_authority` VALUES ('555416597531983872', 'operation:systemMonitorBrowse', null, null, '555416597490040832', '1');
INSERT INTO `base_authority` VALUES ('555416597586509824', 'operation:systemMonitorCreate', null, null, '555416597565538304', '1');
INSERT INTO `base_authority` VALUES ('555416597628452864', 'operation:systemMonitorEdit', null, null, '555416597607481344', '1');
INSERT INTO `base_authority` VALUES ('555416597691367424', 'operation:systemMonitorRemove', null, null, '555416597666201600', '1');
INSERT INTO `base_authority` VALUES ('555416597750087680', 'operation:systemMonitorDetail', null, null, '555416597712338944', '1');
INSERT INTO `base_authority` VALUES ('555416597800419328', 'operation:gatewayRouteBrowse', null, null, '555416597775253504', '1');
INSERT INTO `base_authority` VALUES ('555416597846556672', 'operation:gatewayRouteCreate', null, null, '555416597821390848', '1');
INSERT INTO `base_authority` VALUES ('555416597905276928', 'operation:gatewayRouteEdit', null, null, '555416597875916800', '1');
INSERT INTO `base_authority` VALUES ('555416597963997184', 'operation:gatewayRouteRemove', null, null, '555416597943025664', '1');
INSERT INTO `base_authority` VALUES ('555416598014328832', 'operation:gatewayRouteDetail', null, null, '555416597989163008', '1');
INSERT INTO `base_authority` VALUES ('555416598064660480', 'operation:systemApiBrowse', null, null, '555416598035300352', '1');
INSERT INTO `base_authority` VALUES ('555416598106603520', 'operation:systemApiCreate', null, null, '555416598089826304', '1');
INSERT INTO `base_authority` VALUES ('555416598165323776', 'operation:systemApiEdit', null, null, '555416598127575040', '1');
INSERT INTO `base_authority` VALUES ('555416598215655424', 'operation:systemApiRemove', null, null, '555416598194683904', '1');
INSERT INTO `base_authority` VALUES ('555416598261792768', 'operation:systemApiDetail', null, null, '555416598236626944', '1');
INSERT INTO `base_authority` VALUES ('555416598307930112', 'operation:gatewayTraceBrowse', null, null, '555416598282764288', '1');
INSERT INTO `base_authority` VALUES ('555416598349873152', 'operation:gatewayTraceCreate', null, null, '555416598328901632', '1');
INSERT INTO `base_authority` VALUES ('555416598391816192', 'operation:gatewayTraceEdit', null, null, '555416598370844672', '1');
INSERT INTO `base_authority` VALUES ('555416598437953536', 'operation:gatewayTraceRemove', null, null, '555416598412787712', '1');
INSERT INTO `base_authority` VALUES ('555416598479896576', 'operation:gatewayTraceDetail', null, null, '555416598458925056', '1');
INSERT INTO `base_authority` VALUES ('555416598526033920', 'operation:systemRoleBrowse', null, null, '555416598505062400', '1');
INSERT INTO `base_authority` VALUES ('555416598572171264', 'operation:systemRoleCreate', null, null, '555416598547005440', '1');
INSERT INTO `base_authority` VALUES ('555416598609920000', 'operation:systemRoleEdit', null, null, '555416598588948480', '1');
INSERT INTO `base_authority` VALUES ('555416598660251648', 'operation:systemRoleRemove', null, null, '555416598635085824', '1');
INSERT INTO `base_authority` VALUES ('555416598693806080', 'operation:systemRoleDetail', null, null, '555416598677028864', '1');
INSERT INTO `base_authority` VALUES ('555416598723166208', 'operation:systemAppBrowse', null, null, '555416598706388992', '1');
INSERT INTO `base_authority` VALUES ('555416598819635200', 'operation:systemAppCreate', null, null, '555416598802857984', '1');
INSERT INTO `base_authority` VALUES ('555416598857383936', 'operation:systemAppEdit', null, null, '555416598836412416', '1');
INSERT INTO `base_authority` VALUES ('555416598903521280', 'operation:systemAppRemove', null, null, '555416598874161152', '1');
INSERT INTO `base_authority` VALUES ('555416598937075712', 'operation:systemAppDetail', null, null, '555416598924492800', '1');
INSERT INTO `base_authority` VALUES ('555416598966435840', 'operation:systemUserBrowse', null, null, '555416598953852928', '1');
INSERT INTO `base_authority` VALUES ('555416598995795968', 'operation:systemUserCreate', null, null, '555416598979018752', '1');
INSERT INTO `base_authority` VALUES ('555416599029350400', 'operation:systemUserEdit', null, null, '555416599016767488', '1');
INSERT INTO `base_authority` VALUES ('555416599067099136', 'operation:systemUserRemove', null, null, '555416599046127616', '1');
INSERT INTO `base_authority` VALUES ('555416599092264960', 'operation:systemUserDetail', null, null, '555416599079682048', '1');
INSERT INTO `base_authority` VALUES ('555416599125819392', 'operation:apiDebugBrowse', null, null, '555416599109042176', '1');
INSERT INTO `base_authority` VALUES ('555416599159373824', 'operation:apiDebugCreate', null, null, '555416599142596608', '1');
INSERT INTO `base_authority` VALUES ('555416599188733952', 'operation:apiDebugEdit', null, null, '555416599176151040', '1');
INSERT INTO `base_authority` VALUES ('555416599226482688', 'operation:apiDebugRemove', null, null, '555416599209705472', '1');
INSERT INTO `base_authority` VALUES ('555416599255842816', 'operation:apiDebugDetail', null, null, '555416599243259904', '1');
INSERT INTO `base_authority` VALUES ('555416599293591552', 'operation:gatewayLogsBrowse', null, null, '555416599281008640', '1');
INSERT INTO `base_authority` VALUES ('555416599322951680', 'operation:gatewayLogsCreate', null, null, '555416599310368768', '1');
INSERT INTO `base_authority` VALUES ('555416599364894720', 'operation:gatewayLogsEdit', null, null, '555416599343923200', '1');
INSERT INTO `base_authority` VALUES ('555416599394254848', 'operation:gatewayLogsRemove', null, null, '555416599381671936', '1');
INSERT INTO `base_authority` VALUES ('555416599432003584', 'operation:gatewayLogsDetail', null, null, '555416599415226368', '1');
INSERT INTO `base_authority` VALUES ('555416599461363712', 'operation:gatewayBrowse', null, null, '555416599444586496', '1');
INSERT INTO `base_authority` VALUES ('555416599486529536', 'operation:gatewayCreate', null, null, '555416599473946624', '1');
INSERT INTO `base_authority` VALUES ('555416599515889664', 'operation:gatewayEdit', null, null, '555416599499112448', '1');
INSERT INTO `base_authority` VALUES ('555416599549444096', 'operation:gatewayRemove', null, null, '555416599532666880', '1');
INSERT INTO `base_authority` VALUES ('555416599582998528', 'operation:gatewayDetail', null, null, '555416599562027008', '1');
INSERT INTO `base_authority` VALUES ('555416599612358656', 'operation:helpBrowse', null, null, '555416599599775744', '1');
INSERT INTO `base_authority` VALUES ('555416599650107392', 'operation:helpCreate', null, null, '555416599633330176', '1');
INSERT INTO `base_authority` VALUES ('555416599683661824', 'operation:helpEdit', null, null, '555416599666884608', '1');
INSERT INTO `base_authority` VALUES ('555416599721410560', 'operation:helpRemove', null, null, '555416599704633344', '1');
INSERT INTO `base_authority` VALUES ('555416599754964992', 'operation:helpDetail', null, null, '555416599742382080', '1');
INSERT INTO `base_authority` VALUES ('555416599784325120', 'operation:wikiBrowse', null, null, '555416599771742208', '1');
INSERT INTO `base_authority` VALUES ('555416599809490944', 'operation:wikiCreate', null, null, '555416599796908032', '1');
INSERT INTO `base_authority` VALUES ('555416599838851072', 'operation:wikiEdit', null, null, '555416599826268160', '1');
INSERT INTO `base_authority` VALUES ('555416599935320064', 'operation:wikiRemove', null, null, '555416599918542848', '1');
INSERT INTO `base_authority` VALUES ('555416599968874496', 'operation:wikiDetail', null, null, '555416599952097280', '1');
INSERT INTO `base_authority` VALUES ('555416600002428928', 'operation:nacosBrowse', null, null, '555416599985651712', '1');
INSERT INTO `base_authority` VALUES ('555416600044371968', 'operation:nacosCreate', null, null, '555416600023400448', '1');
INSERT INTO `base_authority` VALUES ('555416600082120704', 'operation:nacosEdit', null, null, '555416600065343488', '1');
INSERT INTO `base_authority` VALUES ('555416600119869440', 'operation:nacosRemove', null, null, '555416600103092224', '1');
INSERT INTO `base_authority` VALUES ('555416600161812480', 'operation:nacosDetail', null, null, '555416600145035264', '1');
INSERT INTO `base_authority` VALUES ('555416688917479424', 'menu:nacos', '555410979983196160', null, null, '1');
INSERT INTO `base_authority` VALUES ('555507213150453760', 'menu:gatewayRateLimit', '555507213116899328', null, null, '1');
INSERT INTO `base_authority` VALUES ('555507213225951232', 'operation:gatewayRateLimitBrowse', null, null, '555507213175619584', '1');
INSERT INTO `base_authority` VALUES ('555507213280477184', 'operation:gatewayRateLimitCreate', null, null, '555507213246922752', '1');
INSERT INTO `base_authority` VALUES ('555507213339197440', 'operation:gatewayRateLimitEdit', null, null, '555507213305643008', '1');
INSERT INTO `base_authority` VALUES ('555507213393723392', 'operation:gatewayRateLimitRemove', null, null, '555507213364363264', '1');
INSERT INTO `base_authority` VALUES ('555507213452443648', 'operation:gatewayRateLimitDetail', null, null, '555507213423083520', '1');
INSERT INTO `base_authority` VALUES ('562312837087625216', 'menu:scheduler', '562312836991156224', null, null, '1');
INSERT INTO `base_authority` VALUES ('562312837196677120', 'operation:schedulerBrowse', null, null, '562312837121179648', '1');
INSERT INTO `base_authority` VALUES ('562312837284757504', 'operation:schedulerCreate', null, null, '562312837221842944', '1');
INSERT INTO `base_authority` VALUES ('562312837368643584', 'operation:schedulerEdit', null, null, '562312837309923328', '1');
INSERT INTO `base_authority` VALUES ('562312837452529664', 'operation:schedulerRemove', null, null, '562312837398003712', '1');
INSERT INTO `base_authority` VALUES ('562312837532221440', 'operation:schedulerDetail', null, null, '562312837486084096', '1');
INSERT INTO `base_authority` VALUES ('562313038229667840', 'menu:jobIndex', '562313038200307712', null, null, '1');
INSERT INTO `base_authority` VALUES ('562313038284193792', 'operation:jobIndexBrowse', null, null, '562313038254833664', '1');
INSERT INTO `base_authority` VALUES ('562313038342914048', 'operation:jobIndexCreate', null, null, '562313038305165312', '1');
INSERT INTO `base_authority` VALUES ('562313038397440000', 'operation:jobIndexEdit', null, null, '562313038359691264', '1');
INSERT INTO `base_authority` VALUES ('562313038456160256', 'operation:jobIndexRemove', null, null, '562313038422605824', '1');
INSERT INTO `base_authority` VALUES ('562313038514880512', 'operation:jobIndexDetail', null, null, '562313038481326080', '1');
INSERT INTO `base_authority` VALUES ('562424844877037568', 'menu:schedulerLogs', '562424844814123008', null, null, '1');
INSERT INTO `base_authority` VALUES ('562424844927369216', 'operation:schedulerLogsBrowse', null, null, '562424844898009088', '1');
INSERT INTO `base_authority` VALUES ('562424845002866688', 'operation:schedulerLogsCreate', null, null, '562424844939952128', '1');
INSERT INTO `base_authority` VALUES ('562424845044809728', 'operation:schedulerLogsEdit', null, null, '562424845015449600', '1');
INSERT INTO `base_authority` VALUES ('562424845095141376', 'operation:schedulerLogsRemove', null, null, '562424845069975552', '1');
INSERT INTO `base_authority` VALUES ('562424845141278720', 'operation:schedulerLogsDetail', null, null, '562424845111918592', '1');
INSERT INTO `base_authority` VALUES ('562298893052674048', '67891590cf678b5f53fe0ee963b04a35', null, '562298892914262016', null, '1');
INSERT INTO `base_authority` VALUES ('562298893149143040', 'fb54713e6051971c14802f0d5a68c27f', null, '562298893107200000', null, '1');
INSERT INTO `base_authority` VALUES ('562298893249806336', 'e92303031cec6acc0663eb3dc21872bf', null, '562298893199474688', null, '1');
INSERT INTO `base_authority` VALUES ('562298893346275328', 'fb271864be6bd2666988afc7e4be11f6', null, '562298893300137984', null, '1');
INSERT INTO `base_authority` VALUES ('562298893472104448', '53eba01f6c2935428de1d35a8d04cf01', null, '562298893434355712', null, '1');
INSERT INTO `base_authority` VALUES ('562298893572767744', 'ef15c81c6ae269971b0d2432d815a782', null, '562298893514047488', null, '1');
INSERT INTO `base_authority` VALUES ('562298893665042432', 'c197cbe12e500c40dd1f5b0641290e01', null, '562298893623099392', null, '1');
INSERT INTO `base_authority` VALUES ('562298893782482944', 'e3389206e6961f5d03a17c5f15301ab8', null, '562298893727956992', null, '1');
INSERT INTO `base_authority` VALUES ('562298893874757632', '4ed47c5032dd708bf1f682427626cd6d', null, '562298893832814592', null, '1');
INSERT INTO `base_authority` VALUES ('562298893958643712', 'fb80e459fd4f36e480c266178d0c51a4', null, '562298893925089280', null, '1');
INSERT INTO `base_authority` VALUES ('562298894046724096', '70ee98b2a16425b2b431621ce4bd86e9', null, '562298894004781056', null, '1');
INSERT INTO `base_authority` VALUES ('562298894113832960', 'f319a62f734c9a88177493d6c55dd895', null, '562298894084472832', null, '1');
INSERT INTO `base_authority` VALUES ('562298894180941824', '6ba4e46bec04860029fde87560ca748b', null, '562298894155776000', null, '1');
INSERT INTO `base_authority` VALUES ('562298894252244992', 'e5750d06feea771d151c6c83e25276a5', null, '562298894218690560', null, '1');
INSERT INTO `base_authority` VALUES ('562298894310965248', 'a3db5c3cb47fbe6fa5da0b4227bf89df', null, '562298894285799424', null, '1');
INSERT INTO `base_authority` VALUES ('562298894411628544', '89c23e898b8ac2fbf902a527775ee62d', null, '562298894357102592', null, '1');
INSERT INTO `base_authority` VALUES ('562298894508097536', '284905d765c744f58698f251c3af87d2', null, '562298894461960192', null, '1');
INSERT INTO `base_authority` VALUES ('562298894617149440', 'ef19885bdc0263d6c9e1c6cd30465652', null, '562298894583595008', null, '1');
INSERT INTO `base_authority` VALUES ('562298894688452608', '0fffec8ae424a358f27e6ef3afe834f1', null, '562298894659092480', null, '1');
INSERT INTO `base_authority` VALUES ('562298894776532992', '269b7d4c15acdf34d47bf49e88b74bed', null, '562298894738784256', null, '1');
INSERT INTO `base_authority` VALUES ('562298894847836160', '2a367b1889eeaf815d01a3b01b633002', null, '562298894814281728', null, '1');
INSERT INTO `base_authority` VALUES ('562298894927527936', 'aec7f2383f6de8f03c6e0b19b01bd703', null, '562298894898167808', null, '1');
INSERT INTO `base_authority` VALUES ('562298894990442496', 'b6585e7b6457fff110590fcaf1ac3d75', null, '562298894961082368', null, '1');
INSERT INTO `base_authority` VALUES ('562298895057551360', 'e6fc5eb7d3726d234f99fef695d48903', null, '562298895028191232', null, '1');
INSERT INTO `base_authority` VALUES ('562298895112077312', 'f9f29a13539cb90e0d3276428c196ff0', null, '562298895091105792', null, '1');
INSERT INTO `base_authority` VALUES ('562298895179186176', 'b4a93c3bdb9e2a1d973ef363f170efa3', null, '562298895149826048', null, '1');
INSERT INTO `base_authority` VALUES ('562298895233712128', 'e550390f284495cfd116b79d137e9f25', null, '562298895208546304', null, '1');
INSERT INTO `base_authority` VALUES ('562298895305015296', '1c7e061a84c19c361f53f96695a36af3', null, '562298895267266560', null, '1');
INSERT INTO `base_authority` VALUES ('562298895414067200', '8390f8df182208c9ac7f8488ff8ee16e', null, '562298895384707072', null, '1');
INSERT INTO `base_authority` VALUES ('562298895476981760', 'b9139035544d8c7cf63f175a239c8d97', null, '562298895447621632', null, '1');
INSERT INTO `base_authority` VALUES ('562298895548284928', 'f905e24c871726bf2edde51343a78ade', null, '562298895518924800', null, '1');
INSERT INTO `base_authority` VALUES ('562298895632171008', '4b08d8aec04c469a420a61d604242a0c', null, '562298895594422272', null, '1');
INSERT INTO `base_authority` VALUES ('562298895699279872', '3eb7d32f260dd614ea52ca9f6ff12991', null, '562298895674114048', null, '1');
INSERT INTO `base_authority` VALUES ('562298895766388736', 'e6fdf3a41e3a3e7d45fe0eb4ffa46274', null, '562298895741222912', null, '1');
INSERT INTO `base_authority` VALUES ('562298895867052032', 'e0785b531e8f641f4938ed5eadd53442', null, '562298895841886208', null, '1');
INSERT INTO `base_authority` VALUES ('562298895955132416', 'aa317afdfc55f53011fa11b58e9643b3', null, '562298895900606464', null, '1');
INSERT INTO `base_authority` VALUES ('562298896018046976', '45c59fca3c422ce4a1b2cffb61974a96', null, '562298895988686848', null, '1');
INSERT INTO `base_authority` VALUES ('562298896076767232', '9920cbb614a803ee15b77aa3aa64fe71', null, '562298896051601408', null, '1');
INSERT INTO `base_authority` VALUES ('562298896164847616', '3f52148c6faf7c637860f02f9c238b6f', null, '562298896122904576', null, '1');
INSERT INTO `base_authority` VALUES ('562298896244539392', 'bccf1cb7ee962a75827322925e49fb34', null, '562298896210984960', null, '1');
INSERT INTO `base_authority` VALUES ('562298896311648256', '0148fcc39d133f5201cd787cd67841c4', null, '562298896278093824', null, '1');
INSERT INTO `base_authority` VALUES ('562298896429088768', 'e66cdfdc4d729e21e48a31154374290a', null, '562298896403922944', null, '1');
INSERT INTO `base_authority` VALUES ('562298896504586240', 'ed3b5fd2a5d512af31c3c4c1d6f3a8e5', null, '562298896475226112', null, '1');
INSERT INTO `base_authority` VALUES ('562298896584278016', '75d874ddab349ab257a20f22a813e209', null, '562298896550723584', null, '1');
INSERT INTO `base_authority` VALUES ('562298896638803968', '9e561209e598a37567a237025222712e', null, '562298896617832448', null, '1');
INSERT INTO `base_authority` VALUES ('562298896689135616', '2ca547ed7ca20faa5c404b54e7cdf2f0', null, '562298896663969792', null, '1');
INSERT INTO `base_authority` VALUES ('562298896739467264', 'ddb84565e59202ad4db615274e87abaa', null, '562298896718495744', null, '1');
INSERT INTO `base_authority` VALUES ('562298896806576128', 'd14db6489f24cc19525e5b29b405b006', null, '562298896773021696', null, '1');
INSERT INTO `base_authority` VALUES ('562298896856907776', 'bf5a884847b83b4190d9dc96aa654f06', null, '562298896835936256', null, '1');
INSERT INTO `base_authority` VALUES ('562298896907239424', '013d98ba70eadf94e7381640ba32c5c7', null, '562298896882073600', null, '1');
INSERT INTO `base_authority` VALUES ('562298896957571072', '0e44c26ad426025be1e191a9c3f9dbfb', null, '562298896936599552', null, '1');
INSERT INTO `base_authority` VALUES ('562298897028874240', '5b538c92c8d9d0fc6a735fd23e25ea73', null, '562298896999514112', null, '1');
INSERT INTO `base_authority` VALUES ('562298897104371712', '60e95ec97e79923661d2a8c7dc55076a', null, '562298897079205888', null, '1');
INSERT INTO `base_authority` VALUES ('562298897179869184', '92b857fdc229fb30465097c2ca5e4c79', null, '562298897150509056', null, '1');
INSERT INTO `base_authority` VALUES ('562298897326669824', '3ba36990afc84e8cbe2cf76cca905120', null, '562298897293115392', null, '1');
INSERT INTO `base_authority` VALUES ('562298897393778688', '89bbed07cb76895d73c273ee9f963b1a', null, '562298897372807168', null, '1');
INSERT INTO `base_authority` VALUES ('562298897456693248', '395e1e9eaf48c4e35b436962a6e13a29', null, '562298897435721728', null, '1');
INSERT INTO `base_authority` VALUES ('562298897511219200', '29f30c2834dff3e4a1140c6052077c57', null, '562298897490247680', null, '1');
INSERT INTO `base_authority` VALUES ('562298897599299584', 'b24ae9431a3cf9c0df7df8dcb4878cec', null, '562298897548967936', null, '1');
INSERT INTO `base_authority` VALUES ('562299189074067456', 'c047892e3b6765afb52907fe0c085fea', null, '562299189036318720', null, '1');
INSERT INTO `base_authority` VALUES ('562299189162147840', '1d6a6c6b446d9f2582ccc6a92dd6c1be', null, '562299189124399104', null, '1');
INSERT INTO `base_authority` VALUES ('562299252697464832', '535284d1580c48b1307254bf9e55a6ca', null, '562299252663910400', null, '1');
INSERT INTO `base_authority` VALUES ('562299252781350912', '78949a8d459468e6a8c0d34b4166ba5b', null, '562299252743602176', null, '1');
INSERT INTO `base_authority` VALUES ('562299259865530368', '3caa88b4302bfb693ba9e84a87ca652d', null, '562299259823587328', null, '1');
INSERT INTO `base_authority` VALUES ('562299259945222144', '5b04bb3589580a21e946bad179c1f6f4', null, '562299259911667712', null, '1');
INSERT INTO `base_authority` VALUES ('562299260045885440', 'ec00be2c7f6193aa62f41b7ae27dccea', null, '562299260012331008', null, '1');
INSERT INTO `base_authority` VALUES ('562299332536041472', '1a54c6e056852f7b0fe4ab9b4adb48be', null, '562299332506681344', null, '1');
INSERT INTO `base_authority` VALUES ('562299332603150336', '0a42e801289a08c55ed0be3a784da8cb', null, '562299332573790208', null, '1');
INSERT INTO `base_authority` VALUES ('562299332661870592', 'b5db9245ed3b84e192e0c0032ab470ad', null, '562299332636704768', null, '1');
INSERT INTO `base_authority` VALUES ('562299332720590848', 'b694bef25e7396638d2d1404cbd5aa13', null, '562299332695425024', null, '1');
INSERT INTO `base_authority` VALUES ('562299332783505408', '2630edb488abf32304aff033dec4d2d1', null, '562299332754145280', null, '1');
INSERT INTO `base_authority` VALUES ('562299332909334528', '32ac536ad46a222bd7805c78de758b0e', null, '562299332879974400', null, '1');
INSERT INTO `base_authority` VALUES ('562299332972249088', 'a25e02c8e068895fd0c9e2f60dee4a4b', null, '562299332947083264', null, '1');
INSERT INTO `base_authority` VALUES ('562299333051940864', '078318b52696031a50a6c0168e604b45', null, '562299333014192128', null, '1');
INSERT INTO `base_authority` VALUES ('562299333127438336', 'ef95fa95dbe312fdc80449b6a23fbe29', null, '562299333098078208', null, '1');
INSERT INTO `base_authority` VALUES ('562299333198741504', 'b18b4629622f2c3a005427094c7a392f', null, '562299333165187072', null, '1');
INSERT INTO `base_authority` VALUES ('562299333265850368', 'd6cced60e45488a231444972a31fe1f7', null, '562299333240684544', null, '1');
INSERT INTO `base_authority` VALUES ('562299333341347840', '3f26417af3e3ebaec04803e7c8b205ee', null, '562299333311987712', null, '1');
INSERT INTO `base_authority` VALUES ('562299333429428224', 'b23a4190097e8ebdf22b9dca86e2aaea', null, '562299333383290880', null, '1');
INSERT INTO `base_authority` VALUES ('562299333492342784', 'a1c4fa533f12ba7c22220ba92f9f929f', null, '562299333467176960', null, '1');
INSERT INTO `base_authority` VALUES ('562299333555257344', '678bf59765815b544600d149037afecd', null, '562299333530091520', null, '1');
INSERT INTO `base_authority` VALUES ('562299333609783296', 'f0b8f8fa3bbfb696e8289da8b1b480db', null, '562299333584617472', null, '1');
INSERT INTO `base_authority` VALUES ('562299333668503552', 'b74d30a02fc3601fa2f5caba1d1341b1', null, '562299333643337728', null, '1');
INSERT INTO `base_authority` VALUES ('562299333727223808', '48f67022384e23b1a30dcd05c9b2143c', null, '562299333702057984', null, '1');
INSERT INTO `base_authority` VALUES ('562299333790138368', '5462d285813a391c6d398cd8b1c09035', null, '562299333760778240', null, '1');
INSERT INTO `base_authority` VALUES ('562299333861441536', '7ad6b1764445c012ae19fd0abcdce635', null, '562299333832081408', null, '1');
INSERT INTO `base_authority` VALUES ('562299333920161792', '8708ca693d92954527431c5f9b216b1f', null, '562299333894995968', null, '1');
INSERT INTO `base_authority` VALUES ('562314952497430528', 'f8d1616ba632aa6306240ec3362d6141', null, '562314952421933056', null, '1');
INSERT INTO `base_authority` VALUES ('562314952694562816', '4484dc5bc530dcb8afc9b4e819553d86', null, '562314952543567872', null, '1');
INSERT INTO `base_authority` VALUES ('562314952778448896', '1800e238638ed84f6028881d9ec3ed65', null, '562314952740700160', null, '1');
INSERT INTO `base_authority` VALUES ('562314952925249536', 'c262cc84168a318b69bf033983b901a0', null, '562314952883306496', null, '1');
INSERT INTO `base_authority` VALUES ('562314953109798912', 'f95af1bdd4d338c92c0581bf95832835', null, '562314953063661568', null, '1');
INSERT INTO `base_authority` VALUES ('562314953223045120', '32411da403302e19b793e3d33b40da60', null, '562314953181102080', null, '1');
-- ----------------------------
-- Table structure for base_resource_api
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_api`;
CREATE TABLE `base_resource_api` (
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
  `is_open` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否为开放接口：0-否  1-是',
  `is_auth` tinyint(3) NOT NULL DEFAULT '1' COMMENT '是否需要认证: 0-无认证 1-身份认证 默认:1',
  `class_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '类名',
  `method_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '方法名',
  PRIMARY KEY (`api_id`),
  UNIQUE KEY `api_code` (`api_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-API接口';

-- ----------------------------
-- Records of base_resource_api
-- ----------------------------
INSERT INTO `base_resource_api` VALUES ('1', 'all', '全部', 'default', '所有请求', 'get,post', null, 'opencloud-gateway-provider', '/**', '0', '1', '2019-03-07 21:52:17', '2019-03-14 21:41:28', '1', '1', '1', null, null);
INSERT INTO `base_resource_api` VALUES ('2', 'actuator', '监控端点', 'default', '监控端点', 'post', null, 'opencloud-gateway-provider', '/actuator/**', '0', '1', '2019-03-07 21:52:17', '2019-03-14 21:41:28', '1', '1', '1', null, null);
INSERT INTO `base_resource_api` VALUES ('562298892914262016', '67891590cf678b5f53fe0ee963b04a35', '编辑接口资源', 'default', '编辑接口资源', 'POST', '', 'opencloud-base-provider', '/api/update', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseApiController', 'updateApi');
INSERT INTO `base_resource_api` VALUES ('562298893107200000', 'fb54713e6051971c14802f0d5a68c27f', '获取接口资源', 'default', '获取接口资源', 'GET', '', 'opencloud-base-provider', '/api/{apiId}/info', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseApiController', 'getApi');
INSERT INTO `base_resource_api` VALUES ('562298893199474688', 'e92303031cec6acc0663eb3dc21872bf', '添加接口资源', 'default', '添加接口资源', 'POST', '', 'opencloud-base-provider', '/api/add', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseApiController', 'addApi');
INSERT INTO `base_resource_api` VALUES ('562298893300137984', 'fb271864be6bd2666988afc7e4be11f6', '获取分页接口列表', 'default', '获取分页接口列表', 'GET', '', 'opencloud-base-provider', '/api', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseApiController', 'getApiList');
INSERT INTO `base_resource_api` VALUES ('562298893434355712', '53eba01f6c2935428de1d35a8d04cf01', '获取所有接口列表', 'default', '获取所有接口列表', 'GET', '', 'opencloud-base-provider', '/api/all', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseApiController', 'getApiAllList');
INSERT INTO `base_resource_api` VALUES ('562298893514047488', 'ef15c81c6ae269971b0d2432d815a782', '移除接口资源', 'default', '移除接口资源', 'POST', '', 'opencloud-base-provider', '/api/remove', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseApiController', 'removeApi');
INSERT INTO `base_resource_api` VALUES ('562298893623099392', 'c197cbe12e500c40dd1f5b0641290e01', '完善应用开发信息', 'default', '完善应用开发信息', 'POST', '', 'opencloud-base-provider', '/app/client/update', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAppController', 'updateAppClientInfo');
INSERT INTO `base_resource_api` VALUES ('562298893727956992', 'e3389206e6961f5d03a17c5f15301ab8', '获取应用详情', 'default', '仅限系统内部调用', 'GET', '', 'opencloud-base-provider', '/app/{appId}/info', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAppController', 'getApp');
INSERT INTO `base_resource_api` VALUES ('562298893832814592', '4ed47c5032dd708bf1f682427626cd6d', '获取分页应用列表', 'default', '获取分页应用列表', 'GET', '', 'opencloud-base-provider', '/app', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAppController', 'getAppListPage');
INSERT INTO `base_resource_api` VALUES ('562298893925089280', 'fb80e459fd4f36e480c266178d0c51a4', '获取应用开发配置信息', 'default', '获取应用开发配置信息', 'GET', '', 'opencloud-base-provider', '/app/client/{appId}/info', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAppController', 'getAppClientInfo');
INSERT INTO `base_resource_api` VALUES ('562298894004781056', '70ee98b2a16425b2b431621ce4bd86e9', '添加应用信息', 'default', '添加应用信息', 'POST', '', 'opencloud-base-provider', '/app/add', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAppController', 'addApp');
INSERT INTO `base_resource_api` VALUES ('562298894084472832', 'f319a62f734c9a88177493d6c55dd895', '删除应用信息', 'default', '删除应用信息', 'POST', '', 'opencloud-base-provider', '/app/remove', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAppController', 'removeApp');
INSERT INTO `base_resource_api` VALUES ('562298894155776000', '6ba4e46bec04860029fde87560ca748b', '编辑应用信息', 'default', '编辑应用信息', 'POST', '', 'opencloud-base-provider', '/app/update', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAppController', 'updateApp');
INSERT INTO `base_resource_api` VALUES ('562298894218690560', 'e5750d06feea771d151c6c83e25276a5', '重置应用秘钥', 'default', '重置应用秘钥', 'POST', '', 'opencloud-base-provider', '/app/reset', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAppController', 'resetAppSecret');
INSERT INTO `base_resource_api` VALUES ('562298894285799424', 'a3db5c3cb47fbe6fa5da0b4227bf89df', '获取所有访问权限列表', 'default', '获取所有访问权限列表', 'GET', '', 'opencloud-base-provider', '/authority/access/list', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAuthorityController', 'getAccessAuthorityList');
INSERT INTO `base_resource_api` VALUES ('562298894357102592', '89c23e898b8ac2fbf902a527775ee62d', '获取接口权限列表', 'default', '获取接口权限列表', 'GET', '', 'opencloud-base-provider', '/authority/api/list', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAuthorityController', 'getApiAuthorityList');
INSERT INTO `base_resource_api` VALUES ('562298894461960192', '284905d765c744f58698f251c3af87d2', '获取菜单权限列表', 'default', '获取菜单权限列表', 'GET', '', 'opencloud-base-provider', '/authority/menu/list', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAuthorityController', 'getMenuAuthorityList');
INSERT INTO `base_resource_api` VALUES ('562298894583595008', 'ef19885bdc0263d6c9e1c6cd30465652', '分配角色权限', 'default', '分配角色权限', 'POST', '', 'opencloud-base-provider', '/authority/grant/role', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAuthorityController', 'grantRoleAuthority');
INSERT INTO `base_resource_api` VALUES ('562298894659092480', '0fffec8ae424a358f27e6ef3afe834f1', '获取应用已分配接口权限', 'default', '获取应用已分配接口权限', 'GET', '', 'opencloud-base-provider', '/authority/granted/app', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAuthorityController', 'getGrantedAppAuthority');
INSERT INTO `base_resource_api` VALUES ('562298894738784256', '269b7d4c15acdf34d47bf49e88b74bed', '分配应用权限', 'default', '分配应用权限', 'POST', '', 'opencloud-base-provider', '/authority/grant/app', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAuthorityController', 'grantAppAuthority');
INSERT INTO `base_resource_api` VALUES ('562298894814281728', '2a367b1889eeaf815d01a3b01b633002', '获取登陆用户已分配权限', 'default', '获取登陆用户已分配权限', 'GET', '', 'opencloud-base-provider', '/authority/granted/me/menu', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAuthorityController', 'getGrantedMyMenuAuthority');
INSERT INTO `base_resource_api` VALUES ('562298894898167808', 'aec7f2383f6de8f03c6e0b19b01bd703', '获取用户已分配权限', 'default', '获取用户已分配权限', 'GET', '', 'opencloud-base-provider', '/authority/granted/user', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAuthorityController', 'getGrantedUserAuthority');
INSERT INTO `base_resource_api` VALUES ('562298894961082368', 'b6585e7b6457fff110590fcaf1ac3d75', '获取角色已分配权限', 'default', '获取角色已分配权限', 'GET', '', 'opencloud-base-provider', '/authority/granted/role', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAuthorityController', 'getGrantedRoleAuthority');
INSERT INTO `base_resource_api` VALUES ('562298895028191232', 'e6fc5eb7d3726d234f99fef695d48903', '分配用户权限', 'default', '分配用户权限', 'POST', '', 'opencloud-base-provider', '/authority/grant/user', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseAuthorityController', 'grantUserAuthority');
INSERT INTO `base_resource_api` VALUES ('562298895091105792', 'f9f29a13539cb90e0d3276428c196ff0', '获取菜单下所有操作', 'default', '获取菜单下所有操作', 'GET', '', 'opencloud-base-provider', '/menu/operation', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseMenuController', 'getMenuOperationList');
INSERT INTO `base_resource_api` VALUES ('562298895149826048', 'b4a93c3bdb9e2a1d973ef363f170efa3', '获取分页菜单资源列表', 'default', '获取分页菜单资源列表', 'GET', '', 'opencloud-base-provider', '/menu', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseMenuController', 'getMenuListPage');
INSERT INTO `base_resource_api` VALUES ('562298895208546304', 'e550390f284495cfd116b79d137e9f25', '获取菜单资源详情', 'default', '获取菜单资源详情', 'GET', '', 'opencloud-base-provider', '/menu/{menuId}/info', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseMenuController', 'getMenu');
INSERT INTO `base_resource_api` VALUES ('562298895267266560', '1c7e061a84c19c361f53f96695a36af3', '添加菜单资源', 'default', '添加菜单资源', 'POST', '', 'opencloud-base-provider', '/menu/add', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseMenuController', 'addMenu');
INSERT INTO `base_resource_api` VALUES ('562298895384707072', '8390f8df182208c9ac7f8488ff8ee16e', '菜单所有资源列表', 'default', '菜单所有资源列表', 'GET', '', 'opencloud-base-provider', '/menu/all', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseMenuController', 'getMenuAllList');
INSERT INTO `base_resource_api` VALUES ('562298895447621632', 'b9139035544d8c7cf63f175a239c8d97', '编辑菜单资源', 'default', '编辑菜单资源', 'POST', '', 'opencloud-base-provider', '/menu/update', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseMenuController', 'updateMenu');
INSERT INTO `base_resource_api` VALUES ('562298895518924800', 'f905e24c871726bf2edde51343a78ade', '移除菜单资源', 'default', '移除菜单资源', 'POST', '', 'opencloud-base-provider', '/menu/remove', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseMenuController', 'removeMenu');
INSERT INTO `base_resource_api` VALUES ('562298895594422272', '4b08d8aec04c469a420a61d604242a0c', '获取分页操作列表', 'default', '获取分页操作列表', 'GET', '', 'opencloud-base-provider', '/operation', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseOperationController', 'getOperationListPage');
INSERT INTO `base_resource_api` VALUES ('562298895674114048', '3eb7d32f260dd614ea52ca9f6ff12991', '获取菜单下所有操作', 'default', '获取菜单下所有操作', 'GET', '', 'opencloud-base-provider', '/operation/menu', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseOperationController', 'getOperationAllList');
INSERT INTO `base_resource_api` VALUES ('562298895741222912', 'e6fdf3a41e3a3e7d45fe0eb4ffa46274', '操作资源绑定API', 'default', '操作资源绑定API', 'POST', '', 'opencloud-base-provider', '/operation/api/add', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseOperationController', 'addOperationApi');
INSERT INTO `base_resource_api` VALUES ('562298895841886208', 'e0785b531e8f641f4938ed5eadd53442', '获取操作资源已绑定API', 'default', '获取操作资源已绑定API', 'GET', '', 'opencloud-base-provider', '/operation/api', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseOperationController', 'getOperationApi');
INSERT INTO `base_resource_api` VALUES ('562298895900606464', 'aa317afdfc55f53011fa11b58e9643b3', '获取操作资源详情', 'default', '获取操作资源详情', 'GET', '', 'opencloud-base-provider', '/operation/{operationId}/info', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseOperationController', 'getOperation');
INSERT INTO `base_resource_api` VALUES ('562298895988686848', '45c59fca3c422ce4a1b2cffb61974a96', '添加操作资源', 'default', '添加操作资源', 'POST', '', 'opencloud-base-provider', '/operation/add', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseOperationController', 'addOperation');
INSERT INTO `base_resource_api` VALUES ('562298896051601408', '9920cbb614a803ee15b77aa3aa64fe71', '移除操作资源', 'default', '移除操作资源', 'POST', '', 'opencloud-base-provider', '/operation/remove', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseOperationController', 'removeOperation');
INSERT INTO `base_resource_api` VALUES ('562298896122904576', '3f52148c6faf7c637860f02f9c238b6f', '编辑操作资源', 'default', '添加操作资源', 'POST', '', 'opencloud-base-provider', '/operation/update', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseOperationController', 'updateOperation');
INSERT INTO `base_resource_api` VALUES ('562298896210984960', 'bccf1cb7ee962a75827322925e49fb34', '获取角色详情', 'default', '获取角色详情', 'GET', '', 'opencloud-base-provider', '/role/{roleId}/info', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseRoleController', 'getRole');
INSERT INTO `base_resource_api` VALUES ('562298896278093824', '0148fcc39d133f5201cd787cd67841c4', '获取分页角色列表', 'default', '获取分页角色列表', 'GET', '', 'opencloud-base-provider', '/role', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseRoleController', 'getRoleListPage');
INSERT INTO `base_resource_api` VALUES ('562298896403922944', 'e66cdfdc4d729e21e48a31154374290a', '获取所有角色列表', 'default', '获取所有角色列表', 'GET', '', 'opencloud-base-provider', '/role/all', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseRoleController', 'getRoleAllList');
INSERT INTO `base_resource_api` VALUES ('562298896475226112', 'ed3b5fd2a5d512af31c3c4c1d6f3a8e5', '添加角色', 'default', '添加角色', 'POST', '', 'opencloud-base-provider', '/role/add', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseRoleController', 'addRole');
INSERT INTO `base_resource_api` VALUES ('562298896550723584', '75d874ddab349ab257a20f22a813e209', '编辑角色', 'default', '编辑角色', 'POST', '', 'opencloud-base-provider', '/role/update', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseRoleController', 'updateRole');
INSERT INTO `base_resource_api` VALUES ('562298896617832448', '9e561209e598a37567a237025222712e', '角色添加成员', 'default', '角色添加成员', 'POST', '', 'opencloud-base-provider', '/role/users/add', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseRoleController', 'addUserRoles');
INSERT INTO `base_resource_api` VALUES ('562298896663969792', '2ca547ed7ca20faa5c404b54e7cdf2f0', '删除角色', 'default', '删除角色', 'POST', '', 'opencloud-base-provider', '/role/remove', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseRoleController', 'removeRole');
INSERT INTO `base_resource_api` VALUES ('562298896718495744', 'ddb84565e59202ad4db615274e87abaa', '查询角色成员', 'default', '查询角色成员', 'GET', '', 'opencloud-base-provider', '/role/users', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseRoleController', 'getRoleUsers');
INSERT INTO `base_resource_api` VALUES ('562298896773021696', 'd14db6489f24cc19525e5b29b405b006', '修改用户密码', 'default', '修改用户密码', 'POST', '', 'opencloud-base-provider', '/user/update/password', '0', '1', '2019-04-01 15:35:03', '2019-04-01 15:35:03', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserController', 'updatePassword');
INSERT INTO `base_resource_api` VALUES ('562298896835936256', 'bf5a884847b83b4190d9dc96aa654f06', '获取用户已分配角色', 'default', '获取用户已分配角色', 'GET', '', 'opencloud-base-provider', '/user/roles', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserController', 'getUserRoles');
INSERT INTO `base_resource_api` VALUES ('562298896882073600', '013d98ba70eadf94e7381640ba32c5c7', '系统分页用户列表', 'default', '系统分页用户列表', 'GET', '', 'opencloud-base-provider', '/user', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserController', 'getUserList');
INSERT INTO `base_resource_api` VALUES ('562298896936599552', '0e44c26ad426025be1e191a9c3f9dbfb', '修改当前登录用户基本信息', 'default', '修改当前登录用户基本信息', 'POST', '', 'opencloud-base-provider', '/user/me/update', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserController', 'updateMyUserInfo');
INSERT INTO `base_resource_api` VALUES ('562298896999514112', '5b538c92c8d9d0fc6a735fd23e25ea73', '用户分配角色', 'default', '用户分配角色', 'POST', '', 'opencloud-base-provider', '/user/roles/add', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserController', 'addUserRoles');
INSERT INTO `base_resource_api` VALUES ('562298897079205888', '60e95ec97e79923661d2a8c7dc55076a', '修改当前登录用户密码', 'default', '修改当前登录用户密码', 'GET', '', 'opencloud-base-provider', '/user/me/rest/password', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserController', 'restMyPassword');
INSERT INTO `base_resource_api` VALUES ('562298897150509056', '92b857fdc229fb30465097c2ca5e4c79', '更新系统用户', 'default', '更新系统用户', 'POST', '', 'opencloud-base-provider', '/user/update', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserController', 'updateUser');
INSERT INTO `base_resource_api` VALUES ('562298897293115392', '3ba36990afc84e8cbe2cf76cca905120', '获取所有用户列表', 'default', '获取所有用户列表', 'GET', '', 'opencloud-base-provider', '/user/all', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserController', 'getUserAllList');
INSERT INTO `base_resource_api` VALUES ('562298897372807168', '89bbed07cb76895d73c273ee9f963b1a', '添加系统用户', 'default', '添加系统用户', 'POST', '', 'opencloud-base-provider', '/user/add', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserController', 'addUser');
INSERT INTO `base_resource_api` VALUES ('562298897435721728', '395e1e9eaf48c4e35b436962a6e13a29', '注册第三方登录账号', 'default', '仅限系统内部调用', 'POST', '', 'opencloud-base-provider', '/account/register/thirdParty', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserUserAccountController', 'registerThirdPartyAccount');
INSERT INTO `base_resource_api` VALUES ('562298897490247680', '29f30c2834dff3e4a1140c6052077c57', '获取账号登录信息', 'default', '仅限系统内部调用', 'POST', '', 'opencloud-base-provider', '/account/localLogin', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserUserAccountController', 'localLogin');
INSERT INTO `base_resource_api` VALUES ('562298897548967936', 'b24ae9431a3cf9c0df7df8dcb4878cec', '重置密码', 'default', '重置密码', 'POST', '', 'opencloud-base-provider', '/account/reset/password', '0', '1', '2019-04-01 15:35:04', '2019-04-01 15:35:04', '1', '0', '1', 'com.github.lyd.base.provider.controller.BaseUserUserAccountController', 'resetPassword');
INSERT INTO `base_resource_api` VALUES ('562299189036318720', 'c047892e3b6765afb52907fe0c085fea', '获取第三方登录配置', 'default', '任何人都可访问', 'GET', '', 'opencloud-auth-provider', '/login/other/config', '0', '1', '2019-04-01 15:36:13', '2019-04-01 15:36:13', '1', '0', '1', 'com.github.lyd.auth.provider.controller.ApiController', 'getLoginOtherConfig');
INSERT INTO `base_resource_api` VALUES ('562299189124399104', '1d6a6c6b446d9f2582ccc6a92dd6c1be', '获取用户基础信息', 'default', '', 'GET', '', 'opencloud-auth-provider', '/user/me', '0', '1', '2019-04-01 15:36:13', '2019-04-01 15:36:13', '1', '0', '1', 'com.github.lyd.auth.provider.controller.ApiController', 'getUserProfile');
INSERT INTO `base_resource_api` VALUES ('562299252663910400', '535284d1580c48b1307254bf9e55a6ca', '内部应用请求签名', 'default', '仅限系统内部调用', 'POST', '', 'opencloud-admin-provider', '/sign', '0', '1', '2019-04-01 15:36:28', '2019-04-01 15:36:28', '1', '0', '1', 'com.github.lyd.admin.provider.controller.AdminController', 'sign');
INSERT INTO `base_resource_api` VALUES ('562299252743602176', '78949a8d459468e6a8c0d34b4166ba5b', '获取用户访问令牌', 'default', '基于oauth2密码模式登录,无需签名,返回access_token', 'POST', '', 'opencloud-admin-provider', '/login/token', '0', '1', '2019-04-01 15:36:28', '2019-04-01 15:36:28', '1', '0', '1', 'com.github.lyd.admin.provider.controller.AdminController', 'getLoginToken');
INSERT INTO `base_resource_api` VALUES ('562299259823587328', '3caa88b4302bfb693ba9e84a87ca652d', '发送邮件', 'default', '发送邮件', 'POST', '', 'opencloud-msg-provider', '/email', '0', '1', '2019-04-01 15:36:30', '2019-04-01 15:36:30', '1', '0', '1', 'com.github.lyd.msg.provider.controller.EmailController', 'sendEmail');
INSERT INTO `base_resource_api` VALUES ('562299259911667712', '5b04bb3589580a21e946bad179c1f6f4', '发送HTTP异步通知', 'default', '发送HTTP异步通知', 'POST', 'application/json;charset=UTF-8', 'opencloud-msg-provider', '/http/notify', '0', '1', '2019-04-01 15:36:30', '2019-04-01 15:36:30', '1', '0', '1', 'com.github.lyd.msg.provider.controller.HttpNotifyController', 'sendHttpNotify');
INSERT INTO `base_resource_api` VALUES ('562299260012331008', 'ec00be2c7f6193aa62f41b7ae27dccea', '发送短信', 'default', '发送短信', 'POST', '', 'opencloud-msg-provider', '/sms', '0', '1', '2019-04-01 15:36:30', '2019-04-01 15:36:30', '1', '0', '1', 'com.github.lyd.msg.provider.controller.SmsController', 'sendSms');
INSERT INTO `base_resource_api` VALUES ('562299332506681344', '1a54c6e056852f7b0fe4ab9b4adb48be', '获取分页访问日志列表', 'default', '获取分页访问日志列表', 'GET', '', 'opencloud-gateway-provider', '/gateway/access/logs', '0', '1', '2019-04-01 15:36:47', '2019-04-01 15:36:47', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayAccessLogsController', 'getAccessLogListPage');
INSERT INTO `base_resource_api` VALUES ('562299332573790208', '0a42e801289a08c55ed0be3a784da8cb', '获取服务列表', 'default', '获取服务列表', 'GET', '', 'opencloud-gateway-provider', '/service/list', '0', '1', '2019-04-01 15:36:47', '2019-04-01 15:36:47', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayController', 'getServiceList');
INSERT INTO `base_resource_api` VALUES ('562299332636704768', 'b5db9245ed3b84e192e0c0032ab470ad', '查询策略已绑定API列表', 'default', '获取分页接口列表', 'GET', '', 'opencloud-gateway-provider', '/gateway/limit/ip/api/list', '0', '1', '2019-04-01 15:36:47', '2019-04-01 15:36:47', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController', 'getIpLimitApiList');
INSERT INTO `base_resource_api` VALUES ('562299332695425024', 'b694bef25e7396638d2d1404cbd5aa13', '获取分页接口列表', 'default', '获取分页接口列表', 'GET', '', 'opencloud-gateway-provider', '/gateway/limit/ip', '0', '1', '2019-04-01 15:36:47', '2019-04-01 15:36:47', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController', 'getIpLimitListPage');
INSERT INTO `base_resource_api` VALUES ('562299332754145280', '2630edb488abf32304aff033dec4d2d1', '编辑IP限制', 'default', '编辑IP限制', 'POST', '', 'opencloud-gateway-provider', '/gateway/limit/ip/update', '0', '1', '2019-04-01 15:36:47', '2019-04-01 15:36:47', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController', 'updateIpLimit');
INSERT INTO `base_resource_api` VALUES ('562299332879974400', '32ac536ad46a222bd7805c78de758b0e', '绑定API', 'default', '一个API只能绑定一个策略', 'POST', '', 'opencloud-gateway-provider', '/gateway/limit/ip/api/add', '0', '1', '2019-04-01 15:36:47', '2019-04-01 15:36:47', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController', 'addIpLimitApis');
INSERT INTO `base_resource_api` VALUES ('562299332947083264', 'a25e02c8e068895fd0c9e2f60dee4a4b', '添加IP限制', 'default', '添加IP限制', 'POST', '', 'opencloud-gateway-provider', '/gateway/limit/ip/add', '0', '1', '2019-04-01 15:36:47', '2019-04-01 15:36:47', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController', 'addIpLimit');
INSERT INTO `base_resource_api` VALUES ('562299333014192128', '078318b52696031a50a6c0168e604b45', '获取IP限制', 'default', '获取IP限制', 'GET', '', 'opencloud-gateway-provider', '/gateway/limit/ip/{policyId}/info', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController', 'getIpLimit');
INSERT INTO `base_resource_api` VALUES ('562299333098078208', 'ef95fa95dbe312fdc80449b6a23fbe29', '移除IP限制', 'default', '移除IP限制', 'POST', '', 'opencloud-gateway-provider', '/gateway/limit/ip/remove', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController', 'removeIpLimit');
INSERT INTO `base_resource_api` VALUES ('562299333165187072', 'b18b4629622f2c3a005427094c7a392f', '获取分页接口列表', 'default', '获取分页接口列表', 'GET', '', 'opencloud-gateway-provider', '/gateway/limit/rate', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController', 'getRateLimitListPage');
INSERT INTO `base_resource_api` VALUES ('562299333240684544', 'd6cced60e45488a231444972a31fe1f7', '查询策略已绑定API列表', 'default', '获取分页接口列表', 'GET', '', 'opencloud-gateway-provider', '/gateway/limit/rate/api/list', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController', 'getRateLimitApiList');
INSERT INTO `base_resource_api` VALUES ('562299333311987712', '3f26417af3e3ebaec04803e7c8b205ee', '获取流量控制', 'default', '获取流量控制', 'GET', '', 'opencloud-gateway-provider', '/gateway/limit/rate/{policyId}/info', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController', 'getRateLimit');
INSERT INTO `base_resource_api` VALUES ('562299333383290880', 'b23a4190097e8ebdf22b9dca86e2aaea', '编辑流量控制', 'default', '编辑流量控制', 'POST', '', 'opencloud-gateway-provider', '/gateway/limit/rate/update', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController', 'updateRateLimit');
INSERT INTO `base_resource_api` VALUES ('562299333467176960', 'a1c4fa533f12ba7c22220ba92f9f929f', '绑定API', 'default', '一个API只能绑定一个策略', 'POST', '', 'opencloud-gateway-provider', '/gateway/limit/rate/api/add', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController', 'addRateLimitApis');
INSERT INTO `base_resource_api` VALUES ('562299333530091520', '678bf59765815b544600d149037afecd', '移除流量控制', 'default', '移除流量控制', 'POST', '', 'opencloud-gateway-provider', '/gateway/limit/rate/remove', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController', 'removeRateLimit');
INSERT INTO `base_resource_api` VALUES ('562299333584617472', 'f0b8f8fa3bbfb696e8289da8b1b480db', '添加流量控制', 'default', '添加流量控制', 'POST', '', 'opencloud-gateway-provider', '/gateway/limit/rate/add', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController', 'addRateLimit');
INSERT INTO `base_resource_api` VALUES ('562299333643337728', 'b74d30a02fc3601fa2f5caba1d1341b1', '编辑路由', 'default', '编辑路由', 'POST', '', 'opencloud-gateway-provider', '/gateway/route/update', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRouteController', 'updateRoute');
INSERT INTO `base_resource_api` VALUES ('562299333702057984', '48f67022384e23b1a30dcd05c9b2143c', '获取分页路由列表', 'default', '获取分页路由列表', 'GET', '', 'opencloud-gateway-provider', '/gateway/route', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRouteController', 'getRouteListPage');
INSERT INTO `base_resource_api` VALUES ('562299333760778240', '5462d285813a391c6d398cd8b1c09035', '添加路由', 'default', '添加路由', 'POST', '', 'opencloud-gateway-provider', '/gateway/route/add', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRouteController', 'addRoute');
INSERT INTO `base_resource_api` VALUES ('562299333832081408', '7ad6b1764445c012ae19fd0abcdce635', '移除路由', 'default', '移除路由', 'POST', '', 'opencloud-gateway-provider', '/gateway/route/remove', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRouteController', 'removeRoute');
INSERT INTO `base_resource_api` VALUES ('562299333894995968', '8708ca693d92954527431c5f9b216b1f', '获取路由', 'default', '获取路由', 'GET', '', 'opencloud-gateway-provider', '/gateway/route/{routeId}/info', '0', '1', '2019-04-01 15:36:48', '2019-04-01 15:36:48', '1', '0', '1', 'com.github.lyd.gateway.provider.controller.GatewayRouteController', 'getRoute');
INSERT INTO `base_resource_api` VALUES ('562314952421933056', 'f8d1616ba632aa6306240ec3362d6141', '恢复任务', 'default', '恢复任务', 'POST', '', 'opencloud-scheduler-provider', '/job/resume', '0', '1', '2019-04-01 16:38:51', '2019-04-01 17:39:37', '1', '0', '1', 'com.github.lyd.task.provider.controller.SchedulerController', 'resumeJob');
INSERT INTO `base_resource_api` VALUES ('562314952543567872', '4484dc5bc530dcb8afc9b4e819553d86', '暂停任务', 'default', '暂停任务', 'POST', '', 'opencloud-scheduler-provider', '/job/pause', '0', '1', '2019-04-01 16:38:51', '2019-04-01 17:39:37', '1', '0', '1', 'com.github.lyd.task.provider.controller.SchedulerController', 'pauseJob');
INSERT INTO `base_resource_api` VALUES ('562314952740700160', '1800e238638ed84f6028881d9ec3ed65', '删除任务', 'default', '删除任务', 'POST', '', 'opencloud-scheduler-provider', '/job/delete', '0', '1', '2019-04-01 16:38:52', '2019-04-01 17:39:37', '1', '0', '1', 'com.github.lyd.task.provider.controller.SchedulerController', 'deleteJob');
INSERT INTO `base_resource_api` VALUES ('562314952883306496', 'c262cc84168a318b69bf033983b901a0', '添加远程调度任务', 'default', '添加远程调度任务', 'POST', '', 'opencloud-scheduler-provider', '/job/add/http', '0', '1', '2019-04-01 16:38:52', '2019-04-01 17:39:37', '1', '0', '1', 'com.github.lyd.task.provider.controller.SchedulerController', 'addHttpJob');
INSERT INTO `base_resource_api` VALUES ('562314953063661568', 'f95af1bdd4d338c92c0581bf95832835', '修改远程调度任务', 'default', '修改远程调度任务', 'POST', '', 'opencloud-scheduler-provider', '/job/update/http', '0', '1', '2019-04-01 16:38:52', '2019-04-01 17:39:37', '1', '0', '1', 'com.github.lyd.task.provider.controller.SchedulerController', 'updateHttpJob');
INSERT INTO `base_resource_api` VALUES ('562314953181102080', '32411da403302e19b793e3d33b40da60', '获取任务列表', 'default', '获取任务列表', 'GET', '', 'opencloud-scheduler-provider', '/job', '0', '1', '2019-04-01 16:38:52', '2019-04-01 17:39:37', '1', '0', '1', 'com.github.lyd.task.provider.controller.SchedulerController', 'getApiList');
-- ----------------------------
-- Table structure for base_resource_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_menu`;
CREATE TABLE `base_resource_menu` (
                                    `menu_id` bigint(20) NOT NULL COMMENT '菜单Id',
                                    `parent_id` bigint(20) DEFAULT NULL COMMENT '父级菜单',
                                    `menu_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '菜单编码',
                                    `menu_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
                                    `menu_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
                                    `prefix` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '路径前缀',
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
                                    UNIQUE KEY `menu_code` (`menu_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-菜单信息';

-- ----------------------------
-- Records of base_resource_menu
-- ----------------------------
INSERT INTO `base_resource_menu` VALUES ('1', '0', 'system', '系统管理', '系统管理', '/', '', 'md-folder', '_self', '1', '1', '2018-07-29 21:20:10', '2019-03-18 04:44:04', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('2', '13', 'gatewayIpLimit', 'IP访问控制', 'iP访问控制,白名单、黑名单', '/', 'gateway/ip-limit/index', 'md-document', '_self', '1', '1', '2018-07-29 21:20:13', '2019-03-13 21:48:21', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('3', '1', 'systemMenu', '菜单资源', '菜单资源', '/', 'system/menus/index', 'md-menu', '_self', '3', '1', '2018-07-29 21:20:13', '2019-03-18 04:39:10', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('4', '0', 'systemMonitor', '服务管理', '服务管理', '/', '', 'md-folder', '_self', '4', '1', '2018-07-29 21:20:13', '2019-03-18 04:44:13', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('5', '13', 'gatewayRoute', '智能路由', 'zuul动态路由', '/', 'gateway/route/index', 'md-document', '_self', '5', '1', '2018-07-29 21:20:13', '2019-02-25 00:15:23', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('6', '13', 'systemApi', 'API列表', 'API接口资源', '/', 'system/api/index', 'md-document', '_self', '0', '1', '2018-07-29 21:20:13', '2019-03-13 21:48:12', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('7', '4', 'gatewayTrace', '服务追踪', '服务追踪', 'http://', 'localhost:7080', 'md-document', '_blank', '7', '1', '2018-11-30 02:11:18', '2019-03-16 03:56:36', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('8', '1', 'systemRolei', '角色信息', '角色信息', '/', 'system/role/index', 'md-people', '_self', '8', '1', '2018-12-27 15:26:54', '2019-03-18 04:39:23', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('9', '1', 'systemApp', '应用信息', '应用信息', '/', 'system/app/index', 'md-apps', '_self', '0', '1', '2018-12-27 15:41:52', '2019-03-18 04:34:10', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('10', '1', 'systemUser', '系统用户', '系统用户', '/', 'system/user/index', 'md-person', '_self', '0', '1', '2018-12-27 15:46:29', '2019-03-18 04:40:07', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('11', '13', 'apiDebug', '接口调试', 'swagger接口调试', 'http://', 'localhost:8888', 'md-document', '_self', '0', '1', '2019-01-10 20:47:19', '2019-02-25 00:27:27', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('12', '13', 'gatewayLogs', '访问日志', '', '/', 'gateway/logs/index', 'md-document', '_self', '0', '1', '2019-01-28 02:37:42', '2019-02-25 00:16:40', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('13', '0', 'gateway', 'API网关', 'API网关', '/', '', 'md-folder', '_self', '0', '1', '2019-02-25 00:15:09', '2019-03-18 04:44:20', '0', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('14', '0', 'help', '帮助文档', '帮助文档', '/', '', 'md-folder', '_self', '0', '1', '2019-02-25 00:26:44', '2019-03-18 04:45:04', '0', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('15', '14', 'wiki', '使用手册', 'wiki', 'https://', 'gitee.com/liuyadu/open-cloud/wikis/pages', 'md-document', '_blank', '0', '1', '2019-02-25 01:02:44', '2019-03-13 23:02:49', '0', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('555410979983196160', '4', 'nacos', '服务与配置', '服务与配置', 'http://', 'localhost:8848/nacos/index.html', 'md-pulse', '_blank', '0', '1', '2019-03-13 15:24:56', '2019-03-18 04:42:58', '0', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('555507213116899328', '13', 'gatewayRateLimit', '流量控制', 'API限流', '/', 'gateway/rate-limit/index', 'md-document', '_self', '666', '1', '2019-03-13 21:47:20', '2019-03-13 22:13:10', '0', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('562312836991156224', '0', 'scheduler', '任务调度', '任务调度', '/', '', 'md-document', '_self', '0', '1', '2019-04-01 16:30:27', '2019-04-01 16:30:27', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('562313038200307712', '562312836991156224', 'jobIndex', '定时任务', '定时任务列表', '/', 'scheduler/job/index', 'md-document', '_self', '0', '1', '2019-04-01 16:31:15', '2019-04-01 16:36:35', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('562424844814123008', '562312836991156224', 'schedulerLogs', '执行日志', '任务调度日志', '/', 'scheduler/logs/index', 'md-document', '_self', '0', '1', '2019-04-01 23:55:32', '2019-04-02 00:11:24', '0', 'opencloud-base-provider');

-- ----------------------------
-- Table structure for base_resource_operation
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_operation`;
CREATE TABLE `base_resource_operation` (
                                         `operation_id` bigint(20) NOT NULL COMMENT '资源ID',
                                         `operation_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源编码',
                                         `operation_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源名称',
                                         `operation_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
                                         `menu_id` bigint(20) DEFAULT NULL COMMENT '资源父节点',
                                         `priority` int(10) NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
                                         `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
                                         `create_time` datetime NOT NULL,
                                         `update_time` datetime DEFAULT NULL,
                                         `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
                                         `api_ids` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '绑定的API资源:多个用,号隔开',
                                         `service_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务名称',
                                         PRIMARY KEY (`operation_id`),
                                         UNIQUE KEY `operation_code` (`operation_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-功能操作';

-- ----------------------------
-- Records of base_resource_operation
-- ----------------------------
INSERT INTO `base_resource_operation` VALUES ('555416595535495168', 'systemBrowse', '浏览', '查看列表', '1', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596017840128', 'systemCreate', '创建', '新增数据', '1', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596101726208', 'systemEdit', '编辑', '编辑数据', '1', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596168835072', 'systemRemove', '删除', '删除数据', '1', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596252721152', 'systemDetail', '详情', '查看详情', '1', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596340801536', 'systemAuthBrowse', '浏览', '查看列表', '2', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596449853440', 'systemAuthCreate', '创建', '新增数据', '2', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596529545216', 'systemAuthEdit', '编辑', '编辑数据', '2', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596600848384', 'systemAuthRemove', '删除', '删除数据', '2', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596688928768', 'systemAuthDetail', '详情', '查看详情', '2', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596760231936', 'systemMenuBrowse', '浏览', '查看列表', '3', '0', '1', '2019-03-13 15:47:15', '2019-03-18 01:20:08', '1', '2,555567985696178176,555567986031722496,555567986245632000', 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596877672448', 'systemMenuCreate', '创建', '新增数据', '3', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596986724352', 'systemMenuEdit', '编辑', '编辑数据', '3', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597179662336', 'systemMenuRemove', '删除', '删除数据', '3', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597393571840', 'systemMenuDetail', '详情', '查看详情', '3', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597490040832', 'systemMonitorBrowse', '浏览', '查看列表', '4', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597565538304', 'systemMonitorCreate', '创建', '新增数据', '4', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597607481344', 'systemMonitorEdit', '编辑', '编辑数据', '4', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597666201600', 'systemMonitorRemove', '删除', '删除数据', '4', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597712338944', 'systemMonitorDetail', '详情', '查看详情', '4', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597775253504', 'gatewayRouteBrowse', '浏览', '查看列表', '5', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597821390848', 'gatewayRouteCreate', '创建', '新增数据', '5', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597875916800', 'gatewayRouteEdit', '编辑', '编辑数据', '5', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597943025664', 'gatewayRouteRemove', '删除', '删除数据', '5', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597989163008', 'gatewayRouteDetail', '详情', '查看详情', '5', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598035300352', 'systemApiBrowse', '浏览', '查看列表', '6', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598089826304', 'systemApiCreate', '创建', '新增数据', '6', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598127575040', 'systemApiEdit', '编辑', '编辑数据', '6', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598194683904', 'systemApiRemove', '删除', '删除数据', '6', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598236626944', 'systemApiDetail', '详情', '查看详情', '6', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598282764288', 'gatewayTraceBrowse', '浏览', '查看列表', '7', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598328901632', 'gatewayTraceCreate', '创建', '新增数据', '7', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598370844672', 'gatewayTraceEdit', '编辑', '编辑数据', '7', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598412787712', 'gatewayTraceRemove', '删除', '删除数据', '7', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598458925056', 'gatewayTraceDetail', '详情', '查看详情', '7', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598505062400', 'systemRoleBrowse', '浏览', '查看列表', '8', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598547005440', 'systemRoleCreate', '创建', '新增数据', '8', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598588948480', 'systemRoleEdit', '编辑', '编辑数据', '8', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598635085824', 'systemRoleRemove', '删除', '删除数据', '8', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598677028864', 'systemRoleDetail', '详情', '查看详情', '8', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598706388992', 'systemAppBrowse', '浏览', '查看列表', '9', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598802857984', 'systemAppCreate', '创建', '新增数据', '9', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598836412416', 'systemAppEdit', '编辑', '编辑数据', '9', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598874161152', 'systemAppRemove', '删除', '删除数据', '9', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598924492800', 'systemAppDetail', '详情', '查看详情', '9', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598953852928', 'systemUserBrowse', '浏览', '查看列表', '10', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598979018752', 'systemUserCreate', '创建', '新增数据', '10', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599016767488', 'systemUserEdit', '编辑', '编辑数据', '10', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599046127616', 'systemUserRemove', '删除', '删除数据', '10', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599079682048', 'systemUserDetail', '详情', '查看详情', '10', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599109042176', 'apiDebugBrowse', '浏览', '查看列表', '11', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599142596608', 'apiDebugCreate', '创建', '新增数据', '11', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599176151040', 'apiDebugEdit', '编辑', '编辑数据', '11', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599209705472', 'apiDebugRemove', '删除', '删除数据', '11', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599243259904', 'apiDebugDetail', '详情', '查看详情', '11', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599281008640', 'gatewayLogsBrowse', '浏览', '查看列表', '12', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599310368768', 'gatewayLogsCreate', '创建', '新增数据', '12', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599343923200', 'gatewayLogsEdit', '编辑', '编辑数据', '12', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599381671936', 'gatewayLogsRemove', '删除', '删除数据', '12', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599415226368', 'gatewayLogsDetail', '详情', '查看详情', '12', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599444586496', 'gatewayBrowse', '浏览', '查看列表', '13', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599473946624', 'gatewayCreate', '创建', '新增数据', '13', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599499112448', 'gatewayEdit', '编辑', '编辑数据', '13', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599532666880', 'gatewayRemove', '删除', '删除数据', '13', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599562027008', 'gatewayDetail', '详情', '查看详情', '13', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599599775744', 'helpBrowse', '浏览', '查看列表', '14', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599633330176', 'helpCreate', '创建', '新增数据', '14', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599666884608', 'helpEdit', '编辑', '编辑数据', '14', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599704633344', 'helpRemove', '删除', '删除数据', '14', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599742382080', 'helpDetail', '详情', '查看详情', '14', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599771742208', 'wikiBrowse', '浏览', '查看列表', '15', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599796908032', 'wikiCreate', '创建', '新增数据', '15', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599826268160', 'wikiEdit', '编辑', '编辑数据', '15', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599918542848', 'wikiRemove', '删除', '删除数据', '15', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599952097280', 'wikiDetail', '详情', '查看详情', '15', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599985651712', 'nacosBrowse', '浏览', '查看列表', '555410979983196160', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416600023400448', 'nacosCreate', '创建', '新增数据', '555410979983196160', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416600065343488', 'nacosEdit', '编辑', '编辑数据', '555410979983196160', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416600103092224', 'nacosRemove', '删除', '删除数据', '555410979983196160', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416600145035264', 'nacosDetail', '详情', '查看详情', '555410979983196160', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555507213175619584', 'gatewayRateLimitBrowse', '浏览', '查看列表', '555507213116899328', '0', '1', '2019-03-13 21:47:20', '2019-03-13 21:47:20', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555507213246922752', 'gatewayRateLimitCreate', '创建', '新增数据', '555507213116899328', '0', '1', '2019-03-13 21:47:20', '2019-03-13 21:47:20', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555507213305643008', 'gatewayRateLimitEdit', '编辑', '编辑数据', '555507213116899328', '0', '1', '2019-03-13 21:47:20', '2019-03-13 21:47:20', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555507213364363264', 'gatewayRateLimitRemove', '删除', '删除数据', '555507213116899328', '0', '1', '2019-03-13 21:47:20', '2019-03-13 21:47:20', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555507213423083520', 'gatewayRateLimitDetail', '详情', '查看详情', '555507213116899328', '0', '1', '2019-03-13 21:47:20', '2019-03-13 21:47:20', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562312837121179648', 'schedulerBrowse', '浏览', '查看列表', '562312836991156224', '0', '1', '2019-04-01 16:30:27', '2019-04-01 16:30:27', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562312837221842944', 'schedulerCreate', '创建', '新增数据', '562312836991156224', '0', '1', '2019-04-01 16:30:27', '2019-04-01 16:30:27', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562312837309923328', 'schedulerEdit', '编辑', '编辑数据', '562312836991156224', '0', '1', '2019-04-01 16:30:27', '2019-04-01 16:30:27', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562312837398003712', 'schedulerRemove', '删除', '删除数据', '562312836991156224', '0', '1', '2019-04-01 16:30:27', '2019-04-01 16:30:27', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562312837486084096', 'schedulerDetail', '详情', '查看详情', '562312836991156224', '0', '1', '2019-04-01 16:30:27', '2019-04-01 16:30:27', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562313038254833664', 'jobIndexBrowse', '浏览', '查看列表', '562313038200307712', '0', '1', '2019-04-01 16:31:15', '2019-04-01 16:31:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562313038305165312', 'jobIndexCreate', '创建', '新增数据', '562313038200307712', '0', '1', '2019-04-01 16:31:15', '2019-04-01 16:31:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562313038359691264', 'jobIndexEdit', '编辑', '编辑数据', '562313038200307712', '0', '1', '2019-04-01 16:31:15', '2019-04-01 16:31:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562313038422605824', 'jobIndexRemove', '删除', '删除数据', '562313038200307712', '0', '1', '2019-04-01 16:31:15', '2019-04-01 16:31:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562313038481326080', 'jobIndexDetail', '详情', '查看详情', '562313038200307712', '0', '1', '2019-04-01 16:31:15', '2019-04-01 16:31:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562424844898009088', 'schedulerLogsBrowse', '浏览', '查看列表', '562424844814123008', '0', '1', '2019-04-01 23:55:32', '2019-04-01 23:55:32', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562424844939952128', 'schedulerLogsCreate', '创建', '新增数据', '562424844814123008', '0', '1', '2019-04-01 23:55:32', '2019-04-01 23:55:32', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562424845015449600', 'schedulerLogsEdit', '编辑', '编辑数据', '562424844814123008', '0', '1', '2019-04-01 23:55:32', '2019-04-01 23:55:32', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562424845069975552', 'schedulerLogsRemove', '删除', '删除数据', '562424844814123008', '0', '1', '2019-04-01 23:55:32', '2019-04-01 23:55:32', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('562424845111918592', 'schedulerLogsDetail', '详情', '查看详情', '562424844814123008', '0', '1', '2019-04-01 23:55:32', '2019-04-01 23:55:32', '1', null, 'opencloud-base-provider');
-- ----------------------------
-- Table structure for base_resource_operation_api
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_operation_api`;
CREATE TABLE `base_resource_operation_api` (
                                             `operation_id` bigint(20) NOT NULL COMMENT '操作ID',
                                             `api_id` bigint(20) NOT NULL COMMENT 'API',
                                             KEY `operation_id` (`operation_id`),
                                             KEY `api_id` (`api_id`),
                                             CONSTRAINT `base_resource_operation_api_ibfk_2` FOREIGN KEY (`api_id`) REFERENCES `base_resource_api` (`api_id`),
                                             CONSTRAINT `base_resource_operation_api_ibfk_1` FOREIGN KEY (`operation_id`) REFERENCES `base_resource_operation` (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统操作资源-API关联表';

-- ----------------------------
-- Records of base_resource_operation_api
-- ----------------------------
INSERT INTO `base_resource_operation_api` VALUES ('555416596760231936', '2');
INSERT INTO `base_resource_operation_api` VALUES ('555416596760231936', '555567985696178176');

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
INSERT INTO `base_role` VALUES ('1', 'admin', '系统管理员', '1', '系统管理员', '2018-07-29 21:14:54', '2019-02-13 23:09:46', '1');
INSERT INTO `base_role` VALUES ('2', 'dev', '自研开发者', '1', '第三方开发者', '2018-07-29 21:14:54', '2019-02-19 22:44:21', '1');
INSERT INTO `base_role` VALUES ('3', 'isp', '服务提供商', '1', '第三方开发者', '2018-07-29 21:14:54', '2019-01-29 01:19:46', '1');

-- ----------------------------
-- Table structure for base_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_role_authority`;
CREATE TABLE `base_role_authority` (
                                     `authority_id` bigint(20) NOT NULL COMMENT '权限ID',
                                     `role_id` bigint(20) NOT NULL COMMENT '角色ID',
                                     `expire_time` datetime DEFAULT NULL COMMENT '过期时间:null表示长期',
                                     KEY `authority_id` (`authority_id`) USING BTREE,
                                     KEY `role_id` (`role_id`) USING BTREE,
                                     CONSTRAINT `base_role_authority_ibfk_1` FOREIGN KEY (`authority_id`) REFERENCES `base_authority` (`authority_id`),
                                     CONSTRAINT `base_role_authority_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统角色-授权';

-- ----------------------------
-- Records of base_role_authority
-- ----------------------------

-- ----------------------------
-- Table structure for base_role_user
-- ----------------------------
DROP TABLE IF EXISTS `base_role_user`;
CREATE TABLE `base_role_user` (
                                `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                                `role_id` bigint(20) NOT NULL COMMENT '角色ID',
                                KEY `fk_user` (`user_id`) USING BTREE,
                                KEY `fk_role` (`role_id`) USING BTREE,
                                CONSTRAINT `base_role_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`user_id`),
                                CONSTRAINT `base_role_user_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统角色-角色与用户关联';

-- ----------------------------
-- Records of base_role_user
-- ----------------------------
INSERT INTO `base_role_user` VALUES ('521677655146233856', '1');

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
                           `user_type` varchar(20) DEFAULT 'platform' COMMENT '用户类型:platform-平台 isp-服务提供商 dev-自研开发者',
                           `company_id` bigint(20) DEFAULT NULL COMMENT '企业ID',
                           `register_ip` varchar(100) DEFAULT NULL COMMENT '注册IP',
                           `register_time` datetime DEFAULT NULL COMMENT '注册时间',
                           `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态:0-禁用 1-启用 2-锁定',
                           `user_desc` varchar(255) DEFAULT '' COMMENT '描述',
                           `create_time` datetime NOT NULL COMMENT '创建时间',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-基础信息';

-- ----------------------------
-- Records of base_user
-- ----------------------------
INSERT INTO `base_user` VALUES ('521677655146233856', 'admin', '超级管理员', '', '515608851@qq.com', '18518226890', 'platform', null, null, '2018-12-10 13:20:45', '1', '拥有所有权限,不受任何限制.仅限admin账号。', '2018-12-10 13:20:45', '2018-12-10 13:20:45');
INSERT INTO `base_user` VALUES ('557063237640650752', 'test', '测试用户', '', '', '', 'platform', null, null, '2019-03-18 04:50:25', '1', '', '2019-03-18 04:50:25', '2019-03-18 04:50:25');

-- ----------------------------
-- Table structure for base_user_account
-- ----------------------------
DROP TABLE IF EXISTS `base_user_account`;
CREATE TABLE `base_user_account` (
                                   `account_id` bigint(20) NOT NULL,
                                   `user_id` bigint(20) DEFAULT NULL COMMENT '用户Id',
                                   `account` varchar(255) DEFAULT NULL COMMENT '标识：手机号、邮箱、 用户名、或第三方应用的唯一标识',
                                   `password` varchar(255) DEFAULT NULL COMMENT '密码凭证：站内的保存密码、站外的不保存或保存token）',
                                   `account_type` varchar(255) DEFAULT NULL COMMENT '登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等',
                                   PRIMARY KEY (`account_id`),
                                   KEY `user_id` (`user_id`) USING BTREE,
                                   CONSTRAINT `base_user_account_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-登录账号';

-- ----------------------------
-- Records of base_user_account
-- ----------------------------
INSERT INTO `base_user_account` VALUES ('521677655368531968', '521677655146233856', 'admin', '$2a$10$A7EHximvrsa4ESX1uSlkJupbg2PLO2StzDzy67NX4YV25MxmbGvXu', 'username');
INSERT INTO `base_user_account` VALUES ('557063237787451392', '557063237640650752', 'test', '$2a$10$V29RaVpl/zQepgdJXHoqUe2o8Rvrwuu2Xn7lRoXQPxRk4gfC5YwBy', 'username');

-- ----------------------------
-- Table structure for base_user_account_logs
-- ----------------------------
DROP TABLE IF EXISTS `base_user_account_logs`;
CREATE TABLE `base_user_account_logs` (
                                        `id` bigint(20) NOT NULL,
                                        `login_time` datetime NOT NULL,
                                        `login_ip` varchar(255) NOT NULL COMMENT '登录Ip',
                                        `login_agent` varchar(500) NOT NULL COMMENT '登录设备',
                                        `login_nums` int(11) NOT NULL COMMENT '登录次数',
                                        `user_id` bigint(20) NOT NULL,
                                        `account` varchar(100) NOT NULL,
                                        `account_type` varchar(50) NOT NULL,
                                        `account_id` bigint(20) NOT NULL COMMENT '账号ID',
                                        PRIMARY KEY (`id`),
                                        KEY `account_id` (`account_id`) USING BTREE,
                                        CONSTRAINT `base_user_account_logs_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `base_user_account` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-登录日志';

-- ----------------------------
-- Records of base_user_account_logs
-- ----------------------------

-- ----------------------------
-- Table structure for base_user_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_user_authority`;
CREATE TABLE `base_user_authority` (
                                     `authority_id` bigint(20) NOT NULL COMMENT '权限ID',
                                     `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                                     `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
                                     KEY `authority_id` (`authority_id`) USING BTREE,
                                     KEY `user_id` (`user_id`) USING BTREE,
                                     CONSTRAINT `base_user_authority_ibfk_1` FOREIGN KEY (`authority_id`) REFERENCES `base_authority` (`authority_id`),
                                     CONSTRAINT `base_user_authority_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-授权';

-- ----------------------------
-- Records of base_user_authority
-- ----------------------------
SET FOREIGN_KEY_CHECKS=1;
