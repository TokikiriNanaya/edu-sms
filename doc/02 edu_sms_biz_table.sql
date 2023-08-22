/*
Navicat MySQL Data Transfer

Source Server         : 120.78.184.122
Source Server Version : 50621
Source Host           : 120.78.184.122:3306
Source Database       : edu_sms

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2022-09-14 12:04:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for edu_sms_biz_user
-- ----------------------------
DROP TABLE IF EXISTS `edu_sms_biz_user`;
CREATE TABLE `edu_sms_biz_user` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(64) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(32) DEFAULT NULL COMMENT '电话',
  `id_type` int(11) DEFAULT NULL COMMENT '证件类型',
  `id_number` varchar(64) DEFAULT NULL COMMENT '证件号码',
  `status` int(11) DEFAULT NULL COMMENT '用户状态',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `desc_` varchar(64) DEFAULT NULL COMMENT '描述',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务用户表';

-- ----------------------------
-- Records of edu_sms_biz_user
-- ----------------------------

-- ----------------------------
-- Table structure for edu_sms_biz_user_account
-- ----------------------------
DROP TABLE IF EXISTS `edu_sms_biz_user_account`;
CREATE TABLE `edu_sms_biz_user_account` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `biz_user_id` bigint(20) NOT NULL COMMENT '业务用户id',
  `account_id` bigint(20) NOT NULL COMMENT '账号id',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `desc_` varchar(64) DEFAULT NULL COMMENT '描述',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户账号表';

-- ----------------------------
-- Records of edu_sms_biz_user_account
-- ----------------------------


-- ----------------------------
-- Table structure for edu_sms_org
-- ----------------------------
DROP TABLE IF EXISTS `edu_sms_org`;
CREATE TABLE `edu_sms_org` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(64) DEFAULT NULL COMMENT '组织节点名称',
  `pid` bigint(20) DEFAULT NULL COMMENT '上级节点id',
  `parent_path_full` varchar(256) DEFAULT NULL COMMENT '上级节点全路径名称',
  `org_type` int(11) DEFAULT NULL COMMENT '机构类型',
  `period` varchar(64) DEFAULT NULL COMMENT '学段',
  `leaf_flag` bit(1) DEFAULT b'1' COMMENT '是否叶子节点 0:不是 1:是',
  `sort_num` int(11) DEFAULT NULL COMMENT '排序号',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `desc_` varchar(64) DEFAULT NULL COMMENT '描述',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='综合机构表';

-- ----------------------------
-- Records of edu_sms_org
-- ----------------------------
INSERT INTO `edu_sms_org` VALUES ('1', '金牛区', '0', '', '10', null, '', '1', '7785600687770816', '6194473264270785', '2022-09-14 09:12:10', 'EDU-SMSOperAdmin', '6194473264270785', '2022-09-14 09:12:10', 'EDU-SMSOperAdmin', '\0', null, null, '1');

-- ----------------------------
-- Table structure for edu_sms_org_account
-- ----------------------------
DROP TABLE IF EXISTS `edu_sms_org_account`;
CREATE TABLE `edu_sms_org_account` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `org_id` bigint(20) NOT NULL COMMENT '所属机构id',
  `account_id` bigint(20) NOT NULL COMMENT '账号id',
  `role_code` varchar(16) DEFAULT NULL COMMENT '在当前机构下的角色身份',
  `sort_num` int(11) DEFAULT NULL COMMENT '身份优先级排序号',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `desc_` varchar(64) DEFAULT NULL COMMENT '描述',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构账号表';

-- ----------------------------
-- Records of edu_sms_org_account
-- ----------------------------
INSERT INTO `edu_sms_org_account` VALUES ('10001', '1', '7785660692611712', 'operAdmin', '1', '7785600687770816', '6194473264270785', '2022-09-14 09:12:10', 'EDU-SMSOperAdmin', '6194473264270785', '2022-09-14 09:12:10', 'EDU-SMSOperAdmin', '\0', null, null, '1');

-- ----------------------------
-- Table structure for edu_sms_org_school
-- ----------------------------
DROP TABLE IF EXISTS `edu_sms_org_school`;
CREATE TABLE `edu_sms_org_school` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `org_id` bigint(20) NOT NULL COMMENT '科室机构id',
  `school_id` bigint(20) NOT NULL COMMENT '学校id',
  `sort_num` int(11) NOT NULL COMMENT '排序号',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `desc_` varchar(64) DEFAULT NULL COMMENT '描述',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室学校分配';

-- ----------------------------
-- Records of edu_sms_org_school
-- ----------------------------

-- ----------------------------
-- Table structure for edu_sms_sys_conf
-- ----------------------------
DROP TABLE IF EXISTS `edu_sms_sys_conf`;
CREATE TABLE `edu_sms_sys_conf` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(64) DEFAULT NULL COMMENT '系统名称',
  `copyright` varchar(256) DEFAULT NULL COMMENT '版权信息',
  `keep_record` varchar(256) DEFAULT NULL COMMENT '备案信息',
  `status` bit(1) DEFAULT b'1' COMMENT '状态 0:禁用 1:启用',
  `month_report_begin` varchar(32) DEFAULT NULL COMMENT '月报上传时间开始',
  `month_report_end` varchar(32) DEFAULT NULL COMMENT '月报上传时间结束',
  `month_report_status` bit(1) DEFAULT NULL COMMENT '月报上传状态',
  `app_main_page_name` varchar(64) DEFAULT NULL COMMENT 'app首页名称',
  `app_list_name` varchar(64) DEFAULT NULL COMMENT 'app列表名称',
  `app_task_show` varchar(64) DEFAULT NULL COMMENT 'app扩展显示',
  `sync_big_data_plat` int(11) DEFAULT NULL COMMENT '同步大数据平台配置',
  `org_account_id` bigint(20) NOT NULL COMMENT '组织账号id',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
  `create_by_name` varchar(32) DEFAULT NULL COMMENT '创建人名字',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_name` varchar(32) DEFAULT NULL COMMENT '最后修改人名字',
  `delete_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  `desc_` varchar(64) DEFAULT NULL COMMENT '描述',
  `remark_` varchar(255) DEFAULT NULL COMMENT '备注',
  `version_` int(11) NOT NULL DEFAULT '1' COMMENT '数据版本',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ----------------------------
-- Records of edu_sms_sys_conf
-- ----------------------------
INSERT INTO `edu_sms_sys_conf` VALUES ('1', '安全管理系统', '© 2022 四川成都市金牛区教育局', null, '', null, null, '\0', '安全管理系统', '任务列表', '10,20,30,40', '30', '7785600687770816', '6194473264270785', '2022-09-14 09:12:10', 'EDU-SMSOperAdmin', '6194473264270785', '2022-09-14 09:12:10', 'EDU-SMSOperAdmin', '\0', null, null, '1');
