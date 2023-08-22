/*
Navicat MySQL Data Transfer

Source Server         : 120.78.184.122
Source Server Version : 50621
Source Host           : 120.78.184.122:3306
Source Database       : edu_sms

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2022-09-12 20:20:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for amc_application
-- ----------------------------
DROP TABLE IF EXISTS `amc_application`;
CREATE TABLE `amc_application` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(16) NOT NULL COMMENT '中文或字母，简称 6个汉字内',
  `access_id` varchar(64) NOT NULL COMMENT '接入ID',
  `access_secret` varchar(64) NOT NULL COMMENT '接入密钥',
  `biz_code` varchar(64) NOT NULL COMMENT '代码',
  `full_name` varchar(128) NOT NULL COMMENT '应用全称',
  `application_group` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否应用组',
  `group_application_id` bigint(20) DEFAULT NULL COMMENT '所属应用组ID',
  `type` mediumint(4) NOT NULL COMMENT '应用类型(100：入口应用 200：普通应用)',
  `client_type` mediumint(4) NOT NULL COMMENT '终端类型(10-19，PC端扩展; 20-29，手机移动端; 30-39，web应用; 40-69，第三方平台; 10：PC-Windwos; 20：安卓; 21：IOS; 30：Web-PC; 31：Web-手机; 40：微信小程序)',
  `logo` varchar(256) DEFAULT NULL COMMENT '应用图标',
  `start_url_type` mediumint(4) NOT NULL COMMENT '启动地址类型',
  `start_url` varchar(256) NOT NULL COMMENT '启动地址',
  `system_id` bigint(20) NOT NULL COMMENT '所属平台系统ID',
  `pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '上级应用',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  PRIMARY KEY (`id_`),
  KEY `system_id` (`system_id`) USING BTREE,
  CONSTRAINT `amc_application_ibfk_1` FOREIGN KEY (`system_id`) REFERENCES `amc_system` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of amc_application
-- ----------------------------
INSERT INTO `amc_application` VALUES ('7785491535269056', '', '34cc8872eec949a1892f396b07c2931d', '92b173d52e1c42948013a823fd28935b', 'EDU_SMS_PC_ROOT', '教育安全管理系统PC端', '\0', null, '100', '30', null, '100', '', '6177193737513089', '0', '6194472083443137', '2022-09-12 09:40:12', '6194472083443137', '2022-09-12 09:40:12', '\0', '', '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin');

-- ----------------------------
-- Table structure for amc_client_user_record
-- ----------------------------
DROP TABLE IF EXISTS `amc_client_user_record`;
CREATE TABLE `amc_client_user_record` (
  `id_` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `application_id` bigint(20) DEFAULT NULL COMMENT '应用id',
  `type_` varchar(20) NOT NULL COMMENT '客户端类型',
  `package_name` varchar(32) NOT NULL COMMENT '客户端包名，根据不同客户端类型定义，如Android为com.amc.xxx这种格式',
  `client_version` varchar(32) NOT NULL COMMENT '客户端版本，根据不同客户端类型定义',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '最后修改人名字',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of amc_client_user_record
-- ----------------------------

-- ----------------------------
-- Table structure for amc_client_version
-- ----------------------------
DROP TABLE IF EXISTS `amc_client_version`;
CREATE TABLE `amc_client_version` (
  `id_` bigint(20) NOT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  `type_` varchar(20) NOT NULL COMMENT '客户端类型',
  `package_name` varchar(32) NOT NULL COMMENT '包名，根据不同客户端类型定义，如Android为com.amc.xxx这种格式',
  `client_version` varchar(32) NOT NULL COMMENT '客户端版本，根据不同客户端类型定义，如Android为1.0.0这种格式',
  `minimum_version` varchar(32) DEFAULT NULL COMMENT '最小支持版本',
  `url_` varchar(128) DEFAULT NULL COMMENT '客户端安装包下载地址，安装包一般是放在ftp服务器上',
  `url2_` varchar(128) DEFAULT NULL COMMENT '客户端安装包下载地址2，这个地址用于存放流式下载地址，可以支持断点续传模式',
  `url3_` varchar(1024) DEFAULT NULL COMMENT '客户端安装包下载地址3，存放客户端商城下载地址，一般采用json格式存放，如[{“channe”l:”huawei”,”url”:”https://xx.xx.xx”},{“channe”l:”oppo”,”url”:”https://xx.xx.xx”}]',
  `message_` varchar(1024) DEFAULT NULL COMMENT '客户端更新说明',
  `is_force` tinyint(1) DEFAULT NULL COMMENT '是否强制更新',
  `size_` int(11) DEFAULT NULL COMMENT '安装包大小，单位kb',
  `md5_` varchar(64) DEFAULT NULL COMMENT '安装包签名',
  `state_` int(2) NOT NULL COMMENT '版本状态，-10停用 10正式版本 20预发布版本 说明：“预发布版本”说明，只有当前需要提前让部分用户升级到新版本时才会采用这个状态，并且“预发布版本”一个应用只会有一个，当全部用户升级完成后需要将状态修改为“正式版本”',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '最后修改人名字',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of amc_client_version
-- ----------------------------

-- ----------------------------
-- Table structure for amc_module
-- ----------------------------
DROP TABLE IF EXISTS `amc_module`;
CREATE TABLE `amc_module` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(16) NOT NULL COMMENT '模块名称',
  `code` varchar(64) NOT NULL COMMENT '模块编码',
  `sort` int(11) NOT NULL DEFAULT '0',
  `introduce` varchar(16) DEFAULT NULL COMMENT '描述',
  `pid` bigint(20) NOT NULL COMMENT '上级模块pid',
  `biz_type` mediumint(4) NOT NULL COMMENT '模块类型(100：普通模块,200：应用)',
  `biz_id` bigint(20) DEFAULT NULL COMMENT '业务ID',
  `logo` varchar(256) DEFAULT NULL COMMENT '图标',
  `start_url_type` mediumint(4) DEFAULT NULL COMMENT '启动地址类型',
  `start_url` varchar(256) DEFAULT NULL COMMENT '启动地址',
  `application_id` bigint(20) NOT NULL COMMENT '应用ID',
  `status` mediumint(4) DEFAULT NULL COMMENT '状态(10:草稿 20:启用 30:禁用)',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `init_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据初始ID',
  PRIMARY KEY (`id_`),
  KEY `application_id` (`application_id`) USING BTREE,
  CONSTRAINT `amc_module_ibfk_1` FOREIGN KEY (`application_id`) REFERENCES `amc_application` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of amc_module
-- ----------------------------
INSERT INTO `amc_module` VALUES ('7785494585183424', '首页', 'homePage', '1', '', '0', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:40:58', '6194472083443137', '2022-09-12 09:40:58', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785495365520576', '检查项目', 'checkItem', '2', '', '0', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:41:10', '6194472083443137', '2022-09-12 09:41:10', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785496198024384', '月报管理', 'monthReportManage', '3', '', '0', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:41:23', '6194472083443137', '2022-09-12 09:41:23', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785496977247424', '指标管理', 'kpiManage', '4', '', '0', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:41:35', '6194472083443137', '2022-09-12 09:41:35', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785497562418368', '系统管理', 'sysManage', '5', '', '0', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:41:44', '6194472083443137', '2022-09-12 09:41:44', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785498640354496', '检查任务', 'checkTask', '10', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:42:00', '6194472083443137', '2022-09-12 09:42:00', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785499597769920', '学校任务分配（监管）', 'schoolTask_eara', '20', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:42:15', '6194472083443137', '2022-09-12 09:42:15', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785501121481920', '自查（监管）', 'selfCheck_eara', '30', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:42:38', '6194472083443137', '2022-09-12 09:42:38', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785501987998912', '督查（区管）', 'deptCheck_eara', '40', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:42:51', '6194472083443137', '2022-09-12 09:42:51', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785502789766336', '督查（科室）', 'deptCheck_dept', '45', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:43:03', '6194472083443137', '2022-09-12 09:43:03', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785503486217408', '整改（监管）', 'reform_eara', '50', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:43:14', '6194472083443137', '2022-09-12 09:43:14', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785504151669952', '检查报告（监管）', 'checkReport_eara', '60', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:43:24', '6194472083443137', '2022-09-12 09:43:24', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785505105349824', '学校任务分配（校方）', 'schoolTask_school', '120', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:43:39', '6194472083443137', '2022-09-12 09:43:39', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785505834110144', '自查（校方）', 'selfCheck_school', '130', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:43:50', '6194472083443137', '2022-09-12 09:43:50', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785506607566016', '督查（校方）', 'deptCheck_school', '140', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:44:02', '6194472083443137', '2022-09-12 09:44:02', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785507381349568', '整改（校方）', 'reform_school', '150', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:44:13', '6194472083443137', '2022-09-12 09:44:13', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785508412820672', '检查报告（校方）', 'checkReport_school', '160', '', '7785495365520576', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:44:29', '6194472083443137', '2022-09-12 09:44:29', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785515952213184', '月报管理（上报）', 'monthReport', '10', '', '7785496198024384', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:46:24', '6194472083443137', '2022-09-12 09:46:24', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785527292234944', '月报查询', 'monthReportView', '20', '', '7785496198024384', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:49:17', '6194472083443137', '2022-09-12 09:49:17', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785528049765568', '月报统计', 'monthReportStat', '30', '', '7785496198024384', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:49:29', '6194472083443137', '2022-09-12 09:49:29', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785533664562368', '基础指标', 'kpiBase', '10', '', '7785496977247424', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:50:54', '6194472083443137', '2022-09-12 09:50:54', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785534336109760', '指标模板', 'kpiTemp', '20', '', '7785496977247424', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:51:05', '6194472083443137', '2022-09-12 09:51:05', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785538013007040', '系统配置', 'sysConf', '10', '', '7785497562418368', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:52:01', '6194472083443137', '2022-09-12 09:52:01', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785538752515264', '机构管理', 'orgManage', '20', '', '7785497562418368', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:52:12', '6194472083443137', '2022-09-12 09:52:12', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785539394309312', '用户管理（区管）', 'userManage_aera', '30', '', '7785497562418368', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:52:22', '6194472083443137', '2022-09-12 09:52:22', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785540333046976', '用户管理（校管）', 'userManage_school', '35', '', '7785497562418368', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:52:36', '6194472083443137', '2022-09-12 09:52:36', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785541026942144', '科室学校分配', 'orgSchool', '40', '', '7785497562418368', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:52:47', '6194472083443137', '2022-09-12 09:52:47', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785541643504832', '系统日志', 'sysLog', '50', '', '7785497562418368', '100', null, null, null, '', '7785491535269056', '20', '6194472083443137', '2022-09-12 09:52:56', '6194472083443137', '2022-09-12 09:52:56', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_module` VALUES ('7785613495340229', '首页', 'homePage', '1', '', '0', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785494585183424');
INSERT INTO `amc_module` VALUES ('7785613495340230', '检查项目', 'checkItem', '2', '', '0', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785495365520576');
INSERT INTO `amc_module` VALUES ('7785613495340231', '月报管理', 'monthReportManage', '3', '', '0', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785496198024384');
INSERT INTO `amc_module` VALUES ('7785613495340232', '指标管理', 'kpiManage', '4', '', '0', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785496977247424');
INSERT INTO `amc_module` VALUES ('7785613495340233', '系统管理', 'sysManage', '5', '', '0', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785497562418368');
INSERT INTO `amc_module` VALUES ('7785613495340234', '基础指标', 'kpiBase', '10', '', '7785613495340232', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785533664562368');
INSERT INTO `amc_module` VALUES ('7785613495340235', '系统配置', 'sysConf', '10', '', '7785613495340233', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785538013007040');
INSERT INTO `amc_module` VALUES ('7785613495340236', '月报管理（上报）', 'monthReport', '10', '', '7785613495340231', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785515952213184');
INSERT INTO `amc_module` VALUES ('7785613495340237', '检查任务', 'checkTask', '10', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785498640354496');
INSERT INTO `amc_module` VALUES ('7785613495340238', '指标模板', 'kpiTemp', '20', '', '7785613495340232', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785534336109760');
INSERT INTO `amc_module` VALUES ('7785613495340239', '月报查询', 'monthReportView', '20', '', '7785613495340231', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785527292234944');
INSERT INTO `amc_module` VALUES ('7785613495340240', '机构管理', 'orgManage', '20', '', '7785613495340233', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785538752515264');
INSERT INTO `amc_module` VALUES ('7785613495340241', '学校任务分配（监管）', 'schoolTask_eara', '20', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785499597769920');
INSERT INTO `amc_module` VALUES ('7785613495340242', '自查（监管）', 'selfCheck_eara', '30', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785501121481920');
INSERT INTO `amc_module` VALUES ('7785613495340243', '月报统计', 'monthReportStat', '30', '', '7785613495340231', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785528049765568');
INSERT INTO `amc_module` VALUES ('7785613495340244', '用户管理（区管）', 'userManage_aera', '30', '', '7785613495340233', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785539394309312');
INSERT INTO `amc_module` VALUES ('7785613495340245', '用户管理（校管）', 'userManage_school', '35', '', '7785613495340233', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785540333046976');
INSERT INTO `amc_module` VALUES ('7785613495340246', '科室学校分配', 'orgSchool', '40', '', '7785613495340233', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785541026942144');
INSERT INTO `amc_module` VALUES ('7785613495340247', '督查（区管）', 'deptCheck_eara', '40', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785501987998912');
INSERT INTO `amc_module` VALUES ('7785613495340248', '督查（科室）', 'deptCheck_dept', '45', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785502789766336');
INSERT INTO `amc_module` VALUES ('7785613495340249', '系统日志', 'sysLog', '50', '', '7785613495340233', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785541643504832');
INSERT INTO `amc_module` VALUES ('7785613495340250', '整改（监管）', 'reform_eara', '50', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785503486217408');
INSERT INTO `amc_module` VALUES ('7785613495340251', '检查报告（监管）', 'checkReport_eara', '60', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785504151669952');
INSERT INTO `amc_module` VALUES ('7785613495340252', '学校任务分配（校方）', 'schoolTask_school', '120', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785505105349824');
INSERT INTO `amc_module` VALUES ('7785613495340253', '自查（校方）', 'selfCheck_school', '130', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785505834110144');
INSERT INTO `amc_module` VALUES ('7785613495340254', '督查（校方）', 'deptCheck_school', '140', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785506607566016');
INSERT INTO `amc_module` VALUES ('7785613495340255', '整改（校方）', 'reform_school', '150', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785507381349568');
INSERT INTO `amc_module` VALUES ('7785613495340256', '检查报告（校方）', 'checkReport_school', '160', '', '7785613495340230', '100', null, null, null, '', '7785491535269056', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785508412820672');

-- ----------------------------
-- Table structure for amc_permission
-- ----------------------------
DROP TABLE IF EXISTS `amc_permission`;
CREATE TABLE `amc_permission` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `application_id` bigint(20) NOT NULL COMMENT '应用ID',
  `module_id` bigint(20) NOT NULL COMMENT '模块ID',
  `name` varchar(16) NOT NULL COMMENT '权限名称（新增、修改、删除、查询列表等）',
  `code` varchar(32) NOT NULL COMMENT '权限代码',
  `type` mediumint(4) NOT NULL COMMENT '权限类型(10：操作功能 20:展示块 30：数据权限)',
  `status` mediumint(4) DEFAULT NULL COMMENT '状态(10:草稿 20:启用 30:禁用)',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `init_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据初始ID',
  PRIMARY KEY (`id_`),
  KEY `application_id` (`application_id`) USING BTREE,
  KEY `module_id` (`module_id`) USING BTREE,
  CONSTRAINT `amc_permission_ibfk_1` FOREIGN KEY (`application_id`) REFERENCES `amc_application` (`id_`),
  CONSTRAINT `amc_permission_ibfk_2` FOREIGN KEY (`module_id`) REFERENCES `amc_module` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of amc_permission
-- ----------------------------

-- ----------------------------
-- Table structure for amc_role
-- ----------------------------
DROP TABLE IF EXISTS `amc_role`;
CREATE TABLE `amc_role` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `pid` bigint(20) NOT NULL COMMENT '上级ID',
  `system_id` bigint(20) NOT NULL COMMENT '系统ID',
  `name` varchar(16) NOT NULL COMMENT '角色名称',
  `code` varchar(16) NOT NULL COMMENT '角色编码（只能为：英文、数字或短线。 保留字如下： _system:系统管理员 _ security：安全管理员 _auditor：安全审计员）',
  `type` mediumint(4) NOT NULL COMMENT '角色类型(0-9：保留 10：系统三员角色 20：业务系统角色)',
  `introduce` varchar(512) DEFAULT NULL COMMENT '描述',
  `status` mediumint(4) DEFAULT NULL COMMENT '状态(10:草稿 20:启用 30:禁用)',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `init_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据初始ID',
  PRIMARY KEY (`id_`),
  KEY `system_id` (`system_id`) USING BTREE,
  CONSTRAINT `amc_role_ibfk_1` FOREIGN KEY (`system_id`) REFERENCES `amc_system` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of amc_role
-- ----------------------------
INSERT INTO `amc_role` VALUES ('7785543392398528', '0', '6177193737513089', '区管理员', 'areaAdmin', '20', '', '20', '6194472083443137', '2022-09-12 09:53:23', '6194472083443137', '2022-09-12 09:53:23', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_role` VALUES ('7785543923371200', '0', '6177193737513089', '科室人员', 'deptUser', '20', '', '20', '6194472083443137', '2022-09-12 09:53:31', '6194472083443137', '2022-09-12 09:53:31', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_role` VALUES ('7785544392740032', '0', '6177193737513089', '校管理员', 'schoolAdmin', '20', '', '20', '6194472083443137', '2022-09-12 09:53:38', '6194472083443137', '2022-09-12 09:53:38', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_role` VALUES ('7785544974503104', '0', '6177193737513089', '校责任人', 'schoolUser', '20', '', '20', '6194472083443137', '2022-09-12 09:53:47', '6194472083443137', '2022-09-12 09:53:47', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_role` VALUES ('7785545465367744', '0', '6177193737513089', '运维人员', 'operAdmin', '20', '', '20', '6194472083443137', '2022-09-12 09:53:55', '6194472083443137', '2022-09-12 09:53:55', '\0', null, '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `amc_role` VALUES ('7785613495340224', '0', '6177193737513089', '区管理员', 'areaAdmin', '20', '', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785543392398528');
INSERT INTO `amc_role` VALUES ('7785613495340225', '0', '6177193737513089', '科室人员', 'deptUser', '20', '', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785543923371200');
INSERT INTO `amc_role` VALUES ('7785613495340226', '0', '6177193737513089', '校管理员', 'schoolAdmin', '20', '', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785544392740032');
INSERT INTO `amc_role` VALUES ('7785613495340227', '0', '6177193737513089', '校责任人', 'schoolUser', '20', '', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785544974503104');
INSERT INTO `amc_role` VALUES ('7785613495340228', '0', '6177193737513089', '运维人员', 'operAdmin', '20', '', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785545465367744');

-- ----------------------------
-- Table structure for amc_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `amc_role_permission`;
CREATE TABLE `amc_role_permission` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `application_id` bigint(20) NOT NULL COMMENT '应用ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `module_id` bigint(20) NOT NULL COMMENT '模块ID',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限ID',
  `type` mediumint(20) NOT NULL COMMENT '授权类型（10：模块 20：菜单 30：功能）',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `init_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据初始ID',
  PRIMARY KEY (`id_`),
  KEY `application_id` (`application_id`) USING BTREE,
  KEY `module_id` (`module_id`) USING BTREE,
  KEY `permission_id` (`permission_id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE,
  CONSTRAINT `amc_role_permission_ibfk_1` FOREIGN KEY (`application_id`) REFERENCES `amc_application` (`id_`),
  CONSTRAINT `amc_role_permission_ibfk_2` FOREIGN KEY (`module_id`) REFERENCES `amc_module` (`id_`),
  CONSTRAINT `amc_role_permission_ibfk_3` FOREIGN KEY (`permission_id`) REFERENCES `amc_permission` (`id_`),
  CONSTRAINT `amc_role_permission_ibfk_4` FOREIGN KEY (`role_id`) REFERENCES `amc_role` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of amc_role_permission
-- ----------------------------
INSERT INTO `amc_role_permission` VALUES ('7787958481994624', '7785491535269056', '7785613495340224', '7785613495340229', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994625', '7785491535269056', '7785613495340224', '7785613495340230', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994626', '7785491535269056', '7785613495340224', '7785613495340231', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994627', '7785491535269056', '7785613495340224', '7785613495340232', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994628', '7785491535269056', '7785613495340224', '7785613495340233', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994629', '7785491535269056', '7785613495340224', '7785613495340234', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994630', '7785491535269056', '7785613495340224', '7785613495340235', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994631', '7785491535269056', '7785613495340224', '7785613495340236', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994632', '7785491535269056', '7785613495340224', '7785613495340237', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994633', '7785491535269056', '7785613495340224', '7785613495340238', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994634', '7785491535269056', '7785613495340224', '7785613495340240', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994635', '7785491535269056', '7785613495340224', '7785613495340241', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994636', '7785491535269056', '7785613495340224', '7785613495340242', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994637', '7785491535269056', '7785613495340224', '7785613495340243', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994638', '7785491535269056', '7785613495340224', '7785613495340244', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994639', '7785491535269056', '7785613495340224', '7785613495340246', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994640', '7785491535269056', '7785613495340224', '7785613495340247', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994641', '7785491535269056', '7785613495340224', '7785613495340249', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994642', '7785491535269056', '7785613495340224', '7785613495340250', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787958481994643', '7785491535269056', '7785613495340224', '7785613495340251', null, '10', '7785600687770816', '2022-09-12 20:07:34', '7785600687770816', '2022-09-12 20:07:34', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379968', '7785491535269056', '7785613495340228', '7785613495340229', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379969', '7785491535269056', '7785613495340228', '7785613495340230', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379970', '7785491535269056', '7785613495340228', '7785613495340231', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379971', '7785491535269056', '7785613495340228', '7785613495340232', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379972', '7785491535269056', '7785613495340228', '7785613495340233', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379973', '7785491535269056', '7785613495340228', '7785613495340234', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379974', '7785491535269056', '7785613495340228', '7785613495340235', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379975', '7785491535269056', '7785613495340228', '7785613495340236', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379976', '7785491535269056', '7785613495340228', '7785613495340237', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379977', '7785491535269056', '7785613495340228', '7785613495340238', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379978', '7785491535269056', '7785613495340228', '7785613495340240', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379979', '7785491535269056', '7785613495340228', '7785613495340241', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379980', '7785491535269056', '7785613495340228', '7785613495340242', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379981', '7785491535269056', '7785613495340228', '7785613495340243', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379982', '7785491535269056', '7785613495340228', '7785613495340244', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379983', '7785491535269056', '7785613495340228', '7785613495340246', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379984', '7785491535269056', '7785613495340228', '7785613495340247', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379985', '7785491535269056', '7785613495340228', '7785613495340249', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379986', '7785491535269056', '7785613495340228', '7785613495340250', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787960117379987', '7785491535269056', '7785613495340228', '7785613495340251', null, '10', '7785600687770816', '2022-09-12 20:07:59', '7785600687770816', '2022-09-12 20:07:59', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155072', '7785491535269056', '7785613495340225', '7785613495340229', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155073', '7785491535269056', '7785613495340225', '7785613495340230', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155074', '7785491535269056', '7785613495340225', '7785613495340231', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155075', '7785491535269056', '7785613495340225', '7785613495340233', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155076', '7785491535269056', '7785613495340225', '7785613495340237', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155077', '7785491535269056', '7785613495340225', '7785613495340239', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155078', '7785491535269056', '7785613495340225', '7785613495340241', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155079', '7785491535269056', '7785613495340225', '7785613495340242', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155080', '7785491535269056', '7785613495340225', '7785613495340243', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155081', '7785491535269056', '7785613495340225', '7785613495340248', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155082', '7785491535269056', '7785613495340225', '7785613495340249', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155083', '7785491535269056', '7785613495340225', '7785613495340250', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7787972511155084', '7785491535269056', '7785613495340225', '7785613495340251', null, '10', '7785600687770816', '2022-09-12 20:11:08', '7785600687770816', '2022-09-12 20:11:08', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306048', '7785491535269056', '7785613495340226', '7785613495340229', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306049', '7785491535269056', '7785613495340226', '7785613495340230', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306050', '7785491535269056', '7785613495340226', '7785613495340231', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306051', '7785491535269056', '7785613495340226', '7785613495340233', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306052', '7785491535269056', '7785613495340226', '7785613495340236', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306053', '7785491535269056', '7785613495340226', '7785613495340237', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306054', '7785491535269056', '7785613495340226', '7785613495340243', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306055', '7785491535269056', '7785613495340226', '7785613495340245', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306056', '7785491535269056', '7785613495340226', '7785613495340249', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306057', '7785491535269056', '7785613495340226', '7785613495340252', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306058', '7785491535269056', '7785613495340226', '7785613495340253', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306059', '7785491535269056', '7785613495340226', '7785613495340254', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306060', '7785491535269056', '7785613495340226', '7785613495340255', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788001120306061', '7785491535269056', '7785613495340226', '7785613495340256', null, '10', '7785600687770816', '2022-09-12 20:18:25', '7785600687770816', '2022-09-12 20:18:25', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748736', '7785491535269056', '7785613495340227', '7785613495340229', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748737', '7785491535269056', '7785613495340227', '7785613495340230', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748738', '7785491535269056', '7785613495340227', '7785613495340231', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748739', '7785491535269056', '7785613495340227', '7785613495340233', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748740', '7785491535269056', '7785613495340227', '7785613495340237', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748741', '7785491535269056', '7785613495340227', '7785613495340239', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748742', '7785491535269056', '7785613495340227', '7785613495340243', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748743', '7785491535269056', '7785613495340227', '7785613495340249', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748744', '7785491535269056', '7785613495340227', '7785613495340252', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748745', '7785491535269056', '7785613495340227', '7785613495340253', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748746', '7785491535269056', '7785613495340227', '7785613495340254', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748747', '7785491535269056', '7785613495340227', '7785613495340255', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');
INSERT INTO `amc_role_permission` VALUES ('7788003907748748', '7785491535269056', '7785613495340227', '7785613495340256', null, '10', '7785600687770816', '2022-09-12 20:19:07', '7785600687770816', '2022-09-12 20:19:07', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816', '0');

-- ----------------------------
-- Table structure for amc_system
-- ----------------------------
DROP TABLE IF EXISTS `amc_system`;
CREATE TABLE `amc_system` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(32) NOT NULL COMMENT '系统名称',
  `code` varchar(32) NOT NULL COMMENT '系统编码(必须唯一,英文名称或编码)',
  `cover` varchar(256) DEFAULT NULL COMMENT '封面图片',
  `logo` varchar(256) DEFAULT NULL COMMENT '系统图标',
  `type` mediumint(4) NOT NULL COMMENT '系统类型(10:平台应用，20:业务子系统)',
  `default_org_account_id` bigint(20) DEFAULT '0' COMMENT '默认的组织账号ID',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of amc_system
-- ----------------------------
INSERT INTO `amc_system` VALUES ('6177193737513089', '教育安全管理系统', 'EDU-SMS', null, null, '10', '6194472083443137', '4475602840341696', '2021-12-02 08:48:55', '4475602840341696', '2021-12-02 08:48:55', '\0', '', '1', 'DIGITALTempAdmin', 'DIGITALTempAdmin');

-- ----------------------------
-- Table structure for cmc_constant
-- ----------------------------
DROP TABLE IF EXISTS `cmc_constant`;
CREATE TABLE `cmc_constant` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `system_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '系统ID',
  `name` varchar(128) NOT NULL COMMENT '名称',
  `code` varchar(1024) NOT NULL COMMENT '代码',
  `value` varchar(1024) NOT NULL COMMENT '常量值',
  `state` mediumint(4) NOT NULL COMMENT '状态(10：禁用 20：启用)',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `init_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据初始ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of cmc_constant
-- ----------------------------
INSERT INTO `cmc_constant` VALUES ('7785547846814912', '6177193737513089', '导入用户_文件模板', 'import_bizUser', '.xlsx', '20', '6194472083443137', '2022-09-12 09:54:31', '6194472083443137', '2022-09-12 09:54:31', '\0', '', '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `cmc_constant` VALUES ('7785548438736064', '6177193737513089', '导入学校_文件模板', 'import_school', '.xlsx', '20', '6194472083443137', '2022-09-12 09:54:40', '6194472083443137', '2022-09-12 09:54:40', '\0', '', '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `cmc_constant` VALUES ('7785549383306432', '6177193737513089', '导入指标项_文件模板', 'import_kpiBaseItem', '.xlsx', '20', '6194472083443137', '2022-09-12 09:54:54', '6194472083443137', '2022-09-12 09:54:54', '\0', '', '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `cmc_constant` VALUES ('7785550307822784', '6177193737513089', '导入学校指标项责任人_文件模板', 'import_duty', '.xlsx', '20', '6194472083443137', '2022-09-12 09:55:08', '6194472083443137', '2022-09-12 09:55:08', '\0', '', '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `cmc_constant` VALUES ('7785552756444352', '6177193737513089', '月报_文件模板', 'fileTemp_monthReport', '.docx', '20', '6194472083443137', '2022-09-12 09:55:46', '6194472083443137', '2022-09-12 09:55:46', '\0', '', '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `cmc_constant` VALUES ('7785553725590720', '6177193737513089', '月报台账_文件模板', 'fileTemp_monthStandingBook', '.docx', '20', '6194472083443137', '2022-09-12 09:56:01', '6194472083443137', '2022-09-12 09:56:01', '\0', '', '1', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', '6194472083443137', '0');
INSERT INTO `cmc_constant` VALUES ('7785613500976320', '6177193737513089', '导入用户_文件模板', 'import_bizUser', '.xlsx', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', '', '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785547846814912');
INSERT INTO `cmc_constant` VALUES ('7785613500976321', '6177193737513089', '导入学校_文件模板', 'import_school', '.xlsx', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', '', '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785548438736064');
INSERT INTO `cmc_constant` VALUES ('7785613500976322', '6177193737513089', '导入指标项_文件模板', 'import_kpiBaseItem', '.xlsx', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', '', '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785549383306432');
INSERT INTO `cmc_constant` VALUES ('7785613500976323', '6177193737513089', '导入学校指标项责任人_文件模板', 'import_duty', '.xlsx', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', '', '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785550307822784');
INSERT INTO `cmc_constant` VALUES ('7785613500976324', '6177193737513089', '月报_文件模板', 'fileTemp_monthReport', '.docx', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', '', '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785552756444352');
INSERT INTO `cmc_constant` VALUES ('7785613500976325', '6177193737513089', '月报台账_文件模板', 'fileTemp_monthStandingBook', '.docx', '20', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', '', '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816', '7785553725590720');

-- ----------------------------
-- Table structure for cmc_dict
-- ----------------------------
DROP TABLE IF EXISTS `cmc_dict`;
CREATE TABLE `cmc_dict` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `system_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '系统ID',
  `type` mediumint(4) DEFAULT NULL COMMENT '字典类型(10: 表 ,20:树)',
  `name` varchar(128) NOT NULL COMMENT '字典名称',
  `code` varchar(32) NOT NULL COMMENT '字典代码',
  `state` mediumint(4) NOT NULL COMMENT '状态(10：禁用 20：启用)',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `init_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据初始ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of cmc_dict
-- ----------------------------

-- ----------------------------
-- Table structure for cmc_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `cmc_dict_item`;
CREATE TABLE `cmc_dict_item` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `system_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '系统ID',
  `dict_id` bigint(20) NOT NULL COMMENT '字典ID',
  `name` varchar(128) NOT NULL COMMENT '字典项名称',
  `code` varchar(32) NOT NULL COMMENT '字典项代码',
  `value` varchar(32) DEFAULT NULL COMMENT '字典项值',
  `state` mediumint(4) NOT NULL COMMENT '状态(10：禁用 20：启用)',
  `pid` bigint(20) DEFAULT NULL,
  `mask` varchar(255) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `init_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据初始ID',
  PRIMARY KEY (`id_`),
  KEY `dict_id` (`dict_id`) USING BTREE,
  CONSTRAINT `cmc_dict_item_ibfk_1` FOREIGN KEY (`dict_id`) REFERENCES `cmc_dict` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of cmc_dict_item
-- ----------------------------

-- ----------------------------
-- Table structure for cmc_mask
-- ----------------------------
DROP TABLE IF EXISTS `cmc_mask`;
CREATE TABLE `cmc_mask` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织ID（租户ID存在租户副本 0：内置角色）',
  `system_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '系统ID',
  `name` varchar(128) NOT NULL COMMENT '名称',
  `level` int(11) NOT NULL COMMENT '层级',
  `mask` int(11) DEFAULT NULL COMMENT '掩码号',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` bit(1) DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `init_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据初始ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of cmc_mask
-- ----------------------------
INSERT INTO `cmc_mask` VALUES ('7785605015178432', '6194473264270785', '0', 'com.antnest.uom.entity.Org', '2', '1', '6194473264270785', '2022-09-12 10:09:03', '6194473264270785', '2022-09-12 10:09:03', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '0');

-- ----------------------------
-- Table structure for foss_file_object
-- ----------------------------
DROP TABLE IF EXISTS `foss_file_object`;
CREATE TABLE `foss_file_object` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(512) NOT NULL COMMENT '文件名称（显示名称）',
  `type` mediumint(4) NOT NULL COMMENT '文件类型(10：普通文件（其它）20：文档类 30：图片类 40：视频类 50：音频类)',
  `length` int(11) NOT NULL COMMENT '文件大小',
  `extension` varchar(64) NOT NULL COMMENT '扩展名 （不要“.”）',
  `md5` varchar(64) NOT NULL COMMENT '32位md5',
  `thumbnail1` varchar(256) DEFAULT NULL COMMENT '缩略图1(96X96):相对资源目录根路径',
  `thumbnail2` varchar(256) DEFAULT NULL COMMENT '缩略图2(512X512):相对资源目录根路径',
  `thumbnail_other` varchar(256) DEFAULT NULL COMMENT '组合缩率图，采用json封装，可按业务自定义',
  `biz_type` mediumint(4) NOT NULL COMMENT '10：存储业务20：文档业务30：流媒体服务',
  `biz_object_name` varchar(256) NOT NULL COMMENT '业务对象名称',
  `biz_object` varchar(256) NOT NULL COMMENT '业务对象(编码）',
  `biz_object_id` bigint(20) NOT NULL COMMENT '业务对象ID',
  `full_name` varchar(256) NOT NULL COMMENT '原始文件地址 （全路径）持久化：相对资源目录根路径 返前端：绝对路径',
  `publish_info` varchar(256) DEFAULT NULL COMMENT '发布资源(采用JSON格式分装，单独约定内容)',
  `data_level` mediumint(4) DEFAULT NULL COMMENT '数据级别(10：最高（红） 20：中（黄） 30：中低（橙） 40：低（绿）)',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of foss_file_object
-- ----------------------------

-- ----------------------------
-- Table structure for lac_log_audit
-- ----------------------------
DROP TABLE IF EXISTS `lac_log_audit`;
CREATE TABLE `lac_log_audit` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `organisation_id` bigint(20) DEFAULT NULL COMMENT '组织ID（租户ID存在租户副本 0：内置角色）',
  `who` bigint(20) NOT NULL COMMENT '账号ID',
  `who_name` varchar(200) DEFAULT NULL COMMENT '姓名',
  `when_time` datetime NOT NULL COMMENT '时间',
  `biz_type` varchar(128) NOT NULL COMMENT '业务类型',
  `biz_name` varchar(128) NOT NULL COMMENT '业务名称',
  `biz_desc` varchar(128) DEFAULT NULL COMMENT '业务描述',
  `log` text COMMENT '操作内容',
  `obj_clz` varchar(512) DEFAULT NULL COMMENT '操作类',
  `obj_pk` text COMMENT '操作主键',
  `trace_id` varchar(36) DEFAULT NULL COMMENT '追踪ID',
  `remote_ip` varchar(32) DEFAULT NULL COMMENT 'IP',
  `x_real_ip` varchar(32) DEFAULT NULL COMMENT 'IP',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of lac_log_audit
-- ----------------------------

-- ----------------------------
-- Table structure for lac_log_error
-- ----------------------------
DROP TABLE IF EXISTS `lac_log_error`;
CREATE TABLE `lac_log_error` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `organisation_id` bigint(20) DEFAULT NULL COMMENT '组织ID（租户ID存在租户副本 0：内置角色）',
  `account_id` bigint(20) DEFAULT NULL COMMENT '账号ID',
  `application_id` bigint(20) DEFAULT NULL COMMENT '应用ID',
  `app` varchar(128) DEFAULT NULL COMMENT '应用名称',
  `client` varchar(200) DEFAULT NULL COMMENT '客户端',
  `day_tag` int(11) DEFAULT NULL COMMENT '日期',
  `time` datetime NOT NULL COMMENT '时间',
  `nano_time` bigint(20) NOT NULL COMMENT '请求纳秒记录',
  `url` varchar(512) NOT NULL COMMENT '请求地址',
  `http_method` varchar(16) DEFAULT NULL COMMENT '请求方式(GET/POST)',
  `remote_ip` varchar(32) DEFAULT NULL COMMENT 'IP',
  `x_real_ip` varchar(32) DEFAULT NULL COMMENT 'IP',
  `trace_id` varchar(36) DEFAULT NULL COMMENT '跟踪ID',
  `host_machine` varchar(64) DEFAULT NULL COMMENT 'host',
  `accept` varchar(200) DEFAULT NULL COMMENT 'accept',
  `user_agent` varchar(2046) DEFAULT NULL COMMENT '浏览器UA',
  `class_method` varchar(200) DEFAULT NULL COMMENT '访问类',
  `args` text COMMENT '访问类方法参数',
  `state` varchar(16) DEFAULT NULL COMMENT '请求状态码',
  `exception_msg` text COMMENT '错误信息',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of lac_log_error
-- ----------------------------

-- ----------------------------
-- Table structure for uam_account_auth
-- ----------------------------
DROP TABLE IF EXISTS `uam_account_auth`;
CREATE TABLE `uam_account_auth` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `org_account_id` bigint(20) NOT NULL COMMENT '用户(授权)组织账号ID（租户ID存在租户副本 0：内置角色）',
  `account_id` bigint(20) NOT NULL COMMENT '授权账号ID',
  `biz_type` mediumint(4) NOT NULL COMMENT '业务类型(10：角色 20：模块菜单 30：功能权限 40：机构)',
  `biz_id` bigint(20) NOT NULL COMMENT '业务ID',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uam_account_auth
-- ----------------------------
INSERT INTO `uam_account_auth` VALUES ('7785613511396544', '7785600687770816', '7785600687770816', '10', '7785613495340224', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');
INSERT INTO `uam_account_auth` VALUES ('7785613511396545', '7785600687770816', '7785600687770816', '10', '7785613495340225', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');
INSERT INTO `uam_account_auth` VALUES ('7785613511396546', '7785600687770816', '7785600687770816', '10', '7785613495340226', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');
INSERT INTO `uam_account_auth` VALUES ('7785613511396547', '7785600687770816', '7785600687770816', '10', '7785613495340227', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');
INSERT INTO `uam_account_auth` VALUES ('7785613511396548', '7785600687770816', '7785600687770816', '10', '7785613495340228', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');
INSERT INTO `uam_account_auth` VALUES ('7787955297731456', '7785600687770816', '7785660692611712', '10', '7785613495340228', '7785600687770816', '2022-09-12 20:06:46', '7785600687770816', '2022-09-12 20:06:46', '\0', null, '1', '金牛区教育局', '金牛区教育局');

-- ----------------------------
-- Table structure for uam_organization_app
-- ----------------------------
DROP TABLE IF EXISTS `uam_organization_app`;
CREATE TABLE `uam_organization_app` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `organization_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `app_id` bigint(20) NOT NULL COMMENT '应用ID',
  `start_time` datetime NOT NULL COMMENT '开通应用开始时间',
  `end_time` datetime NOT NULL COMMENT '开通应用结束时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  PRIMARY KEY (`id_`),
  KEY `organization_id` (`organization_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uam_organization_app
-- ----------------------------
INSERT INTO `uam_organization_app` VALUES ('7785610536062144', '7785600687770816', '7785491535269056', '2022-09-01 00:00:00', '2032-10-31 23:59:59', '6194473264270785', '2022-09-12 10:10:27', '6194473264270785', '2022-09-12 10:10:27', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816');

-- ----------------------------
-- Table structure for uam_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `uam_role_auth`;
CREATE TABLE `uam_role_auth` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `org_account_id` bigint(20) NOT NULL COMMENT '用户(授权)组织账号ID（租户ID存在租户副本 0：内置角色）',
  `role_id` bigint(20) NOT NULL COMMENT '授权角色ID',
  `biz_type` mediumint(4) NOT NULL COMMENT '业务类型(10：机构 20：岗位 30:群组)',
  `biz_id` bigint(20) NOT NULL COMMENT '业务ID',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uam_role_auth
-- ----------------------------
INSERT INTO `uam_role_auth` VALUES ('7785613507529920', '7785600687770816', '7785613495340224', '10', '7785605016030400', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');
INSERT INTO `uam_role_auth` VALUES ('7785613507529921', '7785600687770816', '7785613495340225', '10', '7785605016030400', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');
INSERT INTO `uam_role_auth` VALUES ('7785613507529922', '7785600687770816', '7785613495340226', '10', '7785605016030400', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');
INSERT INTO `uam_role_auth` VALUES ('7785613507529923', '7785600687770816', '7785613495340227', '10', '7785605016030400', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');
INSERT INTO `uam_role_auth` VALUES ('7785613507529924', '7785600687770816', '7785613495340228', '10', '7785605016030400', '6194473264270785', '2022-09-12 10:11:12', '6194473264270785', '2022-09-12 10:11:12', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');

-- ----------------------------
-- Table structure for uim_account
-- ----------------------------
DROP TABLE IF EXISTS `uim_account`;
CREATE TABLE `uim_account` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `login_name` varchar(32) NOT NULL COMMENT '登录名称',
  `cn_name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `nick_name` varchar(32) NOT NULL COMMENT '昵称',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `logo` varchar(256) DEFAULT NULL COMMENT '账号LOGO',
  `cipher_key` varchar(32) NOT NULL COMMENT '加密因子',
  `access_secret` varchar(128) NOT NULL COMMENT '接入密码',
  `type` mediumint(4) NOT NULL COMMENT '账号类型(100：个人 200：组织 300：设备)',
  `biz_type` mediumint(4) DEFAULT NULL COMMENT '业务类型',
  `entity_id` bigint(20) DEFAULT NULL COMMENT '实体信息ID(关联账户实体ID)',
  `init_pass` tinyint(1) NOT NULL COMMENT '是否是初始密码',
  `pass_change_period` mediumint(4) NOT NULL COMMENT '密码修改周期',
  `pass_change_unit` mediumint(4) NOT NULL COMMENT '密码修改周期单位',
  `pass_last_modify` datetime NOT NULL COMMENT '上次修改密码时间',
  `pass_rule` varchar(256) DEFAULT NULL COMMENT '密码规则',
  `state` mediumint(4) NOT NULL COMMENT '账号状态(1.新建（草稿） 2.启用（正常） 3.锁定（暂停） 4.停用)',
  `confirm_passed` tinyint(1) NOT NULL COMMENT '是否完成实名认证',
  `account_type` mediumint(4) DEFAULT NULL COMMENT '账号类型',
  `account_level` mediumint(9) DEFAULT '20' COMMENT '账号层级(10:集团 20:工厂)',
  `system_id` bigint(20) DEFAULT NULL COMMENT '所属平台账号(System的ID)',
  `enable_time` datetime NOT NULL COMMENT '启用时间',
  `disable_time` datetime DEFAULT NULL COMMENT '停用时间',
  `lock_time` datetime DEFAULT NULL COMMENT '账号锁定开始时间',
  `unlock_time` datetime NOT NULL COMMENT '账号锁定结束时间',
  `unlock_desc` varchar(64) DEFAULT NULL COMMENT '账号锁定备注信息',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uim_account
-- ----------------------------
INSERT INTO `uim_account` VALUES ('6194472083443137', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', 'EDU-SMSTempAdmin', null, 'EDU-SMSTempAdmin', null, '944a3bf1183e4385bbf7bfe1ebd8067b', '101d42472b35ae899a5b1107837916f9', '200', '10', null, '0', '1', '30', '2022-09-12 09:32:01', null, '20', '1', null, '20', '6177193737513089', '2022-09-12 09:32:01', null, null, '2022-09-12 09:32:01', null, '-1000', '2022-09-12 09:32:01', '-1000', '2022-09-12 09:32:01', '\0', null, '1', null, null);
INSERT INTO `uim_account` VALUES ('6194473264270785', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', null, 'EDU-SMSOperAdmin', null, 'eb86e1c998b84bccab3efb2579534e67', 'e9b56d1824961bd6588e3bed207cd9a9', '200', '20', null, '0', '1', '30', '2022-09-12 09:32:19', null, '20', '1', null, '20', '6177193737513089', '2022-09-12 09:32:19', null, null, '2022-09-12 09:32:19', null, '-1000', '2022-09-12 09:32:19', '-1000', '2022-09-12 09:32:19', '\0', null, '1', null, null);
INSERT INTO `uim_account` VALUES ('7785600687770816', '02882002266', '金牛区教育局', '02882002266', '02882002266', null, null, '8d37f11a9b454b3cbbc463e02613ebab', 'bd0a00885cf49bd6', '200', '100', null, '1', '1', '30', '2022-09-12 10:07:57', null, '20', '0', null, '20', '6177193737513089', '2022-09-12 10:07:57', null, null, '2022-09-12 10:07:57', null, '6194473264270785', '2022-09-12 10:07:57', '6194473264270785', '2022-09-12 10:07:57', '\0', '', '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');
INSERT INTO `uim_account` VALUES ('7785660692611712', 'superAdmin', 'superAdmin', '运维人员', '13910000001', 'superAdmin@qq.com', null, '6cd73312869342dc80566701d6f8d7a8', 'fd674745257edf2d2dcd132d4c751910', '100', '100', null, '0', '1', '30', '2022-09-12 10:23:13', null, '20', '0', null, '20', '6177193737513089', '2022-09-12 10:23:13', null, null, '2022-09-12 10:23:13', null, '6194473264270785', '2022-09-12 10:23:13', '6194473264270785', '2022-09-12 10:23:13', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin');

-- ----------------------------
-- Table structure for uim_account_bind
-- ----------------------------
DROP TABLE IF EXISTS `uim_account_bind`;
CREATE TABLE `uim_account_bind` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `master_account_id` bigint(20) NOT NULL COMMENT '统一账号（主）',
  `sub_account_id` bigint(20) NOT NULL COMMENT '业务账号（子）',
  `third_sys_name` varchar(32) NOT NULL COMMENT '绑定账户平台(第三方平台名称编码： 系统约定)',
  `third_account_type` varchar(32) NOT NULL COMMENT '绑定账户类型(第三方平台约定，如微信平台:UUID，OpenID)',
  `third_account_id` varchar(32) NOT NULL COMMENT '绑定账户ID(第三方平台账号)',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  PRIMARY KEY (`id_`),
  KEY `master_account_id` (`master_account_id`) USING BTREE,
  KEY `sub_account_id` (`sub_account_id`) USING BTREE,
  CONSTRAINT `uim_account_bind_ibfk_1` FOREIGN KEY (`master_account_id`) REFERENCES `uim_account` (`id_`),
  CONSTRAINT `uim_account_bind_ibfk_2` FOREIGN KEY (`sub_account_id`) REFERENCES `uim_account` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uim_account_bind
-- ----------------------------

-- ----------------------------
-- Table structure for uim_organization
-- ----------------------------
DROP TABLE IF EXISTS `uim_organization`;
CREATE TABLE `uim_organization` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `organization_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `name` varchar(256) NOT NULL COMMENT '名称',
  `simple_name` varchar(64) NOT NULL COMMENT '简称',
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  `license_type` mediumint(4) DEFAULT NULL COMMENT '身份标识类型',
  `license_code` varchar(32) DEFAULT NULL COMMENT '身份标识编码',
  `license_photo1` varchar(256) DEFAULT NULL COMMENT '证件照片1',
  `license_photo2` varchar(256) DEFAULT NULL COMMENT '证件照片2',
  `license_photo3` varchar(256) DEFAULT NULL COMMENT '证件照片3',
  `license_photo4` varchar(256) DEFAULT NULL COMMENT '证件照片4',
  `introduce` varchar(1024) DEFAULT NULL COMMENT '组织介绍',
  `master_name` varchar(32) DEFAULT NULL COMMENT '负责人',
  `master_address` varchar(256) DEFAULT NULL COMMENT '负责人联系地址',
  `master_phone` varchar(16) DEFAULT NULL COMMENT '负责人电话',
  `master_contact_type` varchar(32) DEFAULT NULL COMMENT '负责人联系方式',
  `master_email` varchar(32) DEFAULT NULL COMMENT '负责人邮箱',
  `master_other_contact` varchar(64) DEFAULT NULL COMMENT '负责人其它联系方式',
  `master_person_id` bigint(20) DEFAULT NULL COMMENT '负责人信息ID(关联person记录)',
  `contacter_name` varchar(32) DEFAULT NULL COMMENT '联系人',
  `contacter_address` varchar(256) DEFAULT NULL COMMENT '联系人联系地址',
  `contacter_phone` varchar(16) DEFAULT NULL COMMENT '联系人电话',
  `contacter_email` varchar(32) DEFAULT NULL COMMENT '联系人邮箱',
  `contacter_other_contact` varchar(64) DEFAULT NULL COMMENT '联系人其它联系方式',
  `contacter_person_id` bigint(20) DEFAULT NULL COMMENT '联系人信息ID(关联person记录)',
  `state` bit(1) DEFAULT b'0' COMMENT '实名认证状态',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  PRIMARY KEY (`id_`),
  KEY `organization_id` (`organization_id`) USING BTREE,
  CONSTRAINT `uim_organization_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `uim_account` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uim_organization
-- ----------------------------

-- ----------------------------
-- Table structure for uim_organization_member
-- ----------------------------
DROP TABLE IF EXISTS `uim_organization_member`;
CREATE TABLE `uim_organization_member` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `organization_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `account_id` bigint(20) NOT NULL COMMENT '成员账号ID',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  PRIMARY KEY (`id_`),
  KEY `organization_id` (`organization_id`) USING BTREE,
  KEY `account_id` (`account_id`) USING BTREE,
  CONSTRAINT `uim_organization_member_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `uim_account` (`id_`),
  CONSTRAINT `uim_organization_member_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `uim_account` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uim_organization_member
-- ----------------------------
INSERT INTO `uim_organization_member` VALUES ('6194472084491712', '6194472083443137', '6194472083443137', '-1000', '2022-09-12 09:32:01', '-1000', '2022-09-12 09:32:01', '\0', null, '1', null, null, '6194472083443137');
INSERT INTO `uim_organization_member` VALUES ('6194473264532928', '6194473264270785', '6194473264270785', '-1000', '2022-09-12 09:32:19', '-1000', '2022-09-12 09:32:19', '\0', null, '1', null, null, '6194473264270785');
INSERT INTO `uim_organization_member` VALUES ('7785600688491712', '7785600687770816', '7785600687770816', '6194473264270785', '2022-09-12 10:07:57', '6194473264270785', '2022-09-12 10:07:57', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816');
INSERT INTO `uim_organization_member` VALUES ('7785660694577792', '7785600687770816', '7785660692611712', '6194473264270785', '2022-09-12 10:23:13', '6194473264270785', '2022-09-12 10:23:23', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816');

-- ----------------------------
-- Table structure for uim_person
-- ----------------------------
DROP TABLE IF EXISTS `uim_person`;
CREATE TABLE `uim_person` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `account_id` bigint(20) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '真实姓名',
  `gender` mediumint(4) NOT NULL COMMENT '性别',
  `birthday` varchar(20) NOT NULL COMMENT '出生年月',
  `license_type` mediumint(4) NOT NULL COMMENT '身份证明类型',
  `license_code` varchar(32) NOT NULL COMMENT '身份证明编号',
  `license_photo1` varchar(256) DEFAULT NULL COMMENT '证件照片1',
  `license_photo2` varchar(256) DEFAULT NULL COMMENT '证件照片2',
  `license_photo3` varchar(256) DEFAULT NULL COMMENT '证件照片3',
  `license_photo4` varchar(256) DEFAULT NULL COMMENT '证件照片4',
  `state` bit(1) NOT NULL DEFAULT b'0' COMMENT '实名认证状态',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  PRIMARY KEY (`id_`),
  KEY `account_id` (`account_id`) USING BTREE,
  CONSTRAINT `uim_person_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `uim_account` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uim_person
-- ----------------------------

-- ----------------------------
-- Table structure for uom_org
-- ----------------------------
DROP TABLE IF EXISTS `uom_org`;
CREATE TABLE `uom_org` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `organization_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '组织账号ID',
  `type` mediumint(4) NOT NULL COMMENT '节点类型(10：机构部门 20：群组 )',
  `pid` bigint(20) NOT NULL COMMENT '上级节点pid',
  `name` varchar(64) NOT NULL COMMENT '组织节点全名（机构、群组）',
  `simple_name` varchar(64) DEFAULT NULL COMMENT '组织节点简称（机构、群组）',
  `code` varchar(64) DEFAULT NULL COMMENT '机构编号',
  `code_path` varchar(1024) DEFAULT NULL COMMENT '编号（含路径）',
  `nick_name` varchar(64) DEFAULT NULL COMMENT '组织节点代号（机构、群组）',
  `logo` varchar(256) DEFAULT NULL COMMENT '图标',
  `state` varchar(255) DEFAULT NULL COMMENT '组织节点状态：10草稿；20启用；30停用',
  `enable_time` datetime DEFAULT NULL COMMENT '节点启用时间',
  `disable_time` datetime DEFAULT NULL COMMENT '节点停用时间',
  `sort_number` int(11) DEFAULT NULL COMMENT '排序号（节点内排序）',
  `topic_id` bigint(20) DEFAULT '0' COMMENT '群组信息订阅主题',
  `location` varchar(255) DEFAULT NULL COMMENT '地址',
  `longitude` double(10,6) DEFAULT NULL,
  `latitude` double(10,6) DEFAULT NULL,
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) DEFAULT '0' COMMENT '组织ID (组织账号）',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uom_org
-- ----------------------------
INSERT INTO `uom_org` VALUES ('7785605016030400', '7785600687770816', '10', '0', '', '金牛区', '0001', '0001', null, null, '20', '2022-09-12 10:09:03', null, '2', '0', null, null, null, '6194473264270785', '2022-09-12 10:09:03', '6194473264270785', '2022-09-12 10:09:03', '\0', null, '1', 'EDU-SMSOperAdmin', 'EDU-SMSOperAdmin', '7785600687770816');

-- ----------------------------
-- Table structure for uom_org_member
-- ----------------------------
DROP TABLE IF EXISTS `uom_org_member`;
CREATE TABLE `uom_org_member` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `organization_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  `org_id` bigint(20) NOT NULL COMMENT '组织节点ID',
  `account_id` bigint(20) NOT NULL COMMENT '成员账号ID',
  `type` mediumint(4) NOT NULL COMMENT '节点类型(10：机构部门 20：群组)',
  `sort_number` int(11) DEFAULT NULL COMMENT '成员排序（节点内排序）',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号ID',
  PRIMARY KEY (`id_`),
  KEY `org_id` (`org_id`) USING BTREE,
  CONSTRAINT `uom_org_member_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `uom_org` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uom_org_member
-- ----------------------------
INSERT INTO `uom_org_member` VALUES ('7787954597348224', '7785600687770816', '7785605016030400', '7785660692611712', '10', '1', '7785600687770816', '2022-09-12 20:06:35', '7785600687770816', '2022-09-12 20:06:35', '\0', null, '1', '金牛区教育局', '金牛区教育局', '7785600687770816');

-- ----------------------------
-- Table structure for uom_post
-- ----------------------------
DROP TABLE IF EXISTS `uom_post`;
CREATE TABLE `uom_post` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `org_id` bigint(20) NOT NULL COMMENT '组织节点ID',
  `name` varchar(32) NOT NULL COMMENT '岗位名称',
  `code` varchar(32) NOT NULL COMMENT '岗位代号',
  `introduce` varchar(256) DEFAULT NULL COMMENT '岗位描述',
  `state` int(255) DEFAULT NULL COMMENT '组织节点状态：10草稿；20启用；30停用',
  `enable_time` datetime DEFAULT NULL COMMENT '节点启用时间',
  `disable_time` datetime DEFAULT NULL COMMENT '节点停用时间',
  `sort_number` int(11) DEFAULT NULL COMMENT '岗位排序（节点内排序）',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织实体账号ID',
  PRIMARY KEY (`id_`),
  KEY `org_id` (`org_id`) USING BTREE,
  CONSTRAINT `uom_post_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `uom_org` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uom_post
-- ----------------------------

-- ----------------------------
-- Table structure for uom_post_member
-- ----------------------------
DROP TABLE IF EXISTS `uom_post_member`;
CREATE TABLE `uom_post_member` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `org_id` bigint(20) NOT NULL COMMENT '组织节点ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  `account_id` bigint(20) NOT NULL COMMENT '成员账号ID',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建人时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织实体账号Id',
  PRIMARY KEY (`id_`),
  KEY `org_id` (`org_id`) USING BTREE,
  KEY `post_id` (`post_id`) USING BTREE,
  CONSTRAINT `uom_post_member_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `uom_org` (`id_`),
  CONSTRAINT `uom_post_member_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `uom_post` (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of uom_post_member
-- ----------------------------
