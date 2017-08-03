/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : ntrs

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-08-03 17:18:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_name` varchar(20) DEFAULT NULL COMMENT '公司名称',
  `company_address` varchar(20) DEFAULT NULL COMMENT '公司地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of company
-- ----------------------------

-- ----------------------------
-- Table structure for company_contact
-- ----------------------------
DROP TABLE IF EXISTS `company_contact`;
CREATE TABLE `company_contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_id` varchar(20) DEFAULT NULL COMMENT '公司id',
  `name` varchar(20) DEFAULT NULL COMMENT '称谓',
  `call_num` varchar(20) DEFAULT NULL COMMENT '电话',
  `QQ` varchar(20) DEFAULT NULL COMMENT 'QQ',
  `wechat` varchar(20) DEFAULT NULL COMMENT '微信',
  `e_mail` varchar(20) DEFAULT NULL COMMENT '电子邮箱',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of company_contact
-- ----------------------------

-- ----------------------------
-- Table structure for finished_product
-- ----------------------------
DROP TABLE IF EXISTS `finished_product`;
CREATE TABLE `finished_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `finished_number` varchar(20) DEFAULT NULL COMMENT '成品编号',
  `trade_name` varchar(20) DEFAULT NULL COMMENT '品名',
  `specifications` varchar(20) DEFAULT NULL COMMENT '规格',
  `measurement_unit` varchar(20) DEFAULT NULL COMMENT '计量单位',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product
-- ----------------------------
INSERT INTO `finished_product` VALUES ('7', 'CP20170001', '肠衣', '成品', 'KG', '标准成品');
INSERT INTO `finished_product` VALUES ('8', 'CP20170002', '猪腿肉', '成品', 'KG', '标准成品');

-- ----------------------------
-- Table structure for finished_product_copy
-- ----------------------------
DROP TABLE IF EXISTS `finished_product_copy`;
CREATE TABLE `finished_product_copy` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `finished_number` varchar(20) DEFAULT NULL COMMENT '成品编号',
  `trade_name` varchar(20) DEFAULT NULL COMMENT '品名',
  `specifications` varchar(20) DEFAULT NULL COMMENT '规格',
  `measurement_unit` varchar(20) DEFAULT NULL COMMENT '计量单位',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_copy
-- ----------------------------

-- ----------------------------
-- Table structure for finished_product_outgoing
-- ----------------------------
DROP TABLE IF EXISTS `finished_product_outgoing`;
CREATE TABLE `finished_product_outgoing` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `outgoing_time` date DEFAULT NULL COMMENT '出库时间',
  `outgoing_number` varchar(20) DEFAULT NULL COMMENT '出库单号',
  `company` int(10) DEFAULT NULL COMMENT '客户公司',
  `t_user_id` int(10) DEFAULT NULL COMMENT '出库人',
  `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1：确认出库 0：未确认出库',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_outgoing
-- ----------------------------

-- ----------------------------
-- Table structure for finished_product_outgoing_detail
-- ----------------------------
DROP TABLE IF EXISTS `finished_product_outgoing_detail`;
CREATE TABLE `finished_product_outgoing_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `finished_product_outgoing_id` int(11) DEFAULT NULL COMMENT '半成品出库单id',
  `finished_product_id` int(11) DEFAULT NULL COMMENT '半成品id',
  `num` decimal(10,4) DEFAULT NULL COMMENT '数量',
  `warehouse_id` int(10) DEFAULT NULL COMMENT '对应仓库',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_outgoing_detail
-- ----------------------------

-- ----------------------------
-- Table structure for finished_product_stock
-- ----------------------------
DROP TABLE IF EXISTS `finished_product_stock`;
CREATE TABLE `finished_product_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `finished_product_id` int(11) DEFAULT NULL COMMENT '成品id',
  `finished_product_stock_num` decimal(10,4) DEFAULT NULL COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_stock
-- ----------------------------

-- ----------------------------
-- Table structure for finished_product_stock_detail
-- ----------------------------
DROP TABLE IF EXISTS `finished_product_stock_detail`;
CREATE TABLE `finished_product_stock_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `warehouse_id` int(11) DEFAULT NULL COMMENT '对应仓库id',
  `finished_product_id` int(11) DEFAULT NULL COMMENT '成品id',
  `num` decimal(10,4) DEFAULT NULL COMMENT '该仓库内该成品的数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_stock_detail
-- ----------------------------

-- ----------------------------
-- Table structure for finished_product_storage
-- ----------------------------
DROP TABLE IF EXISTS `finished_product_storage`;
CREATE TABLE `finished_product_storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `storage_time` date DEFAULT NULL COMMENT '入库时间',
  `storage_number` varchar(20) DEFAULT NULL COMMENT '入库编号',
  `t_user_id` int(10) DEFAULT NULL COMMENT '入库人',
  `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1：确认入库 0：未确认入库',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_storage
-- ----------------------------

-- ----------------------------
-- Table structure for finished_product_storage_detail
-- ----------------------------
DROP TABLE IF EXISTS `finished_product_storage_detail`;
CREATE TABLE `finished_product_storage_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `finished_product_storage_id` int(11) DEFAULT NULL COMMENT '半成品入库单id',
  `finished_product_id` int(11) DEFAULT NULL COMMENT '半成品id',
  `num` decimal(10,4) DEFAULT NULL COMMENT '数量',
  `warehouse_id` int(10) DEFAULT NULL COMMENT '对应仓库',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_storage_detail
-- ----------------------------

-- ----------------------------
-- Table structure for semimanufactures
-- ----------------------------
DROP TABLE IF EXISTS `semimanufactures`;
CREATE TABLE `semimanufactures` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `semimanufactures_number` varchar(20) DEFAULT NULL COMMENT '半成品编号',
  `trade_name` varchar(20) DEFAULT NULL COMMENT '品名',
  `specifications` varchar(20) DEFAULT NULL COMMENT '规格',
  `measurement_unit` varchar(20) DEFAULT NULL COMMENT '计量单位',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures
-- ----------------------------

-- ----------------------------
-- Table structure for semimanufactures_outgoing
-- ----------------------------
DROP TABLE IF EXISTS `semimanufactures_outgoing`;
CREATE TABLE `semimanufactures_outgoing` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `outgoing_time` date DEFAULT NULL COMMENT '出库时间',
  `outgoing_number` varchar(20) DEFAULT NULL COMMENT '出库单号',
  `company_id` int(10) DEFAULT NULL COMMENT '客户公司',
  `t_user_id` int(10) DEFAULT NULL COMMENT '出库人',
  `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1：确认出库 0：未确认出库',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_outgoing
-- ----------------------------

-- ----------------------------
-- Table structure for semimanufactures_outgoing_detail
-- ----------------------------
DROP TABLE IF EXISTS `semimanufactures_outgoing_detail`;
CREATE TABLE `semimanufactures_outgoing_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `semimanufactures_outgoing_id` int(11) DEFAULT NULL COMMENT '半成品出库单id',
  `semimanufactures_id` int(11) DEFAULT NULL COMMENT '半成品id',
  `num` decimal(10,4) DEFAULT NULL COMMENT '数量',
  `warehouse_id` int(10) DEFAULT NULL COMMENT '对应仓库',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_outgoing_detail
-- ----------------------------

-- ----------------------------
-- Table structure for semimanufactures_stock
-- ----------------------------
DROP TABLE IF EXISTS `semimanufactures_stock`;
CREATE TABLE `semimanufactures_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `semimanufactures_outgoing_id` int(11) DEFAULT NULL COMMENT '半成品id',
  `semimanufactures_stock_num` decimal(10,4) DEFAULT NULL COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_stock
-- ----------------------------

-- ----------------------------
-- Table structure for semimanufactures_stock_detail
-- ----------------------------
DROP TABLE IF EXISTS `semimanufactures_stock_detail`;
CREATE TABLE `semimanufactures_stock_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `warehouse_id` int(11) DEFAULT NULL COMMENT '对应仓库id',
  `semimanufactures_id` int(11) DEFAULT NULL COMMENT '成品id',
  `num` decimal(10,4) DEFAULT NULL COMMENT '该仓库内该成品的数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_stock_detail
-- ----------------------------

-- ----------------------------
-- Table structure for semimanufactures_storage
-- ----------------------------
DROP TABLE IF EXISTS `semimanufactures_storage`;
CREATE TABLE `semimanufactures_storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `storage_time` date DEFAULT NULL COMMENT '入库时间',
  `storage_number` varchar(20) DEFAULT NULL COMMENT '入库编号',
  `t_user_id` int(10) DEFAULT NULL COMMENT '入库人',
  `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1：确认入库 0：未确认入库',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_storage
-- ----------------------------

-- ----------------------------
-- Table structure for semimanufactures_storage_detail
-- ----------------------------
DROP TABLE IF EXISTS `semimanufactures_storage_detail`;
CREATE TABLE `semimanufactures_storage_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `semimanufactures_storage_id` int(11) DEFAULT NULL COMMENT '半成品入库单id',
  `semimanufactures_id` int(11) DEFAULT NULL COMMENT '半成品id',
  `num` decimal(10,4) DEFAULT NULL COMMENT '数量',
  `warehouse_id` int(10) DEFAULT NULL COMMENT '对应仓库',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_storage_detail
-- ----------------------------

-- ----------------------------
-- Table structure for t_button
-- ----------------------------
DROP TABLE IF EXISTS `t_button`;
CREATE TABLE `t_button` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_id` int(11) NOT NULL COMMENT '菜单 id',
  `button_id` int(11) NOT NULL COMMENT '按钮 id',
  `button_name` varchar(50) NOT NULL COMMENT '按钮名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_button
-- ----------------------------
INSERT INTO `t_button` VALUES ('1', '6', '100', '新增');

-- ----------------------------
-- Table structure for t_lease_warehouse_in
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_in`;
CREATE TABLE `t_lease_warehouse_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_in_no` varchar(20) NOT NULL COMMENT '入库单号',
  `warehouse_in_date` date NOT NULL COMMENT '入库日期',
  `customer` int(11) NOT NULL COMMENT '客户 id',
  `warehouse_id` int(11) NOT NULL COMMENT '仓库 id',
  `location` varchar(50) DEFAULT NULL COMMENT '摆放位置',
  `warehouse_in_person` varchar(20) DEFAULT NULL COMMENT '入库人',
  `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1：已入库 0：未确认',
  `price_per_day` decimal(11,4) NOT NULL COMMENT '每日仓储费用：所有入库产品每日费用总和  元/天',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_in
-- ----------------------------

-- ----------------------------
-- Table structure for t_lease_warehouse_inventory
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_inventory`;
CREATE TABLE `t_lease_warehouse_inventory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_in_no` varchar(20) NOT NULL COMMENT '入库单号',
  `price_per_day` decimal(11,4) NOT NULL COMMENT '每日仓储费用：所有库存产品每日费用总和  元/天',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_inventory
-- ----------------------------

-- ----------------------------
-- Table structure for t_lease_warehouse_inventory_product
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_inventory_product`;
CREATE TABLE `t_lease_warehouse_inventory_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inventory_id` int(11) NOT NULL COMMENT '租赁仓库库存 id',
  `product_id` int(11) NOT NULL COMMENT '入库产品 id',
  `quantity` decimal(11,4) NOT NULL COMMENT '库存数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_inventory_product
-- ----------------------------

-- ----------------------------
-- Table structure for t_lease_warehouse_in_product
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_in_product`;
CREATE TABLE `t_lease_warehouse_in_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_in_no` varchar(20) NOT NULL COMMENT '入库单号',
  `name` varchar(20) NOT NULL COMMENT '产品名称',
  `specification` varchar(20) DEFAULT NULL COMMENT '规格',
  `unit` varchar(10) NOT NULL COMMENT '单位',
  `quantity` decimal(11,4) NOT NULL COMMENT '数量',
  `unit_price` decimal(11,4) NOT NULL COMMENT '单价',
  `price_per_day` decimal(11,4) NOT NULL COMMENT '每天存放价格：数量*单价  元/天',
  `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1：已核算 0：未核算',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_in_product
-- ----------------------------

-- ----------------------------
-- Table structure for t_lease_warehouse_out
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_out`;
CREATE TABLE `t_lease_warehouse_out` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_in_no` varchar(20) NOT NULL COMMENT '入库单号',
  `warehouse_out_no` varchar(20) NOT NULL COMMENT '出库单号',
  `warehouse_out_date` date NOT NULL COMMENT '出库日期',
  `warehouse_out_person` varchar(20) DEFAULT NULL COMMENT '出库人',
  `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1：已出库 0：未确认',
  `out_cost` decimal(11,4) DEFAULT NULL COMMENT '出库费用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_out
-- ----------------------------

-- ----------------------------
-- Table structure for t_lease_warehouse_out_product
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_out_product`;
CREATE TABLE `t_lease_warehouse_out_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_out_no` varchar(20) NOT NULL COMMENT '出库单号',
  `product_id` int(11) NOT NULL COMMENT '入库产品 id',
  `out_quantity` decimal(11,4) NOT NULL COMMENT '出库数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_out_product
-- ----------------------------

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module_name` varchar(20) NOT NULL COMMENT '菜单名',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `url` varchar(255) DEFAULT NULL COMMENT '路径',
  `pid` int(11) DEFAULT NULL COMMENT '父级菜单 id',
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', '基础数据管理', 'fa-cogs', null, '0', null);
INSERT INTO `t_menu` VALUES ('2', '自用仓库管理', 'fa-cogs', null, '0', null);
INSERT INTO `t_menu` VALUES ('3', '租赁仓库管理', 'fa-cogs', null, '0', null);
INSERT INTO `t_menu` VALUES ('4', '租赁费用核算', 'fa-cogs', null, '0', null);
INSERT INTO `t_menu` VALUES ('5', '系统管理', 'fa-cogs', null, '0', null);
INSERT INTO `t_menu` VALUES ('6', '角色管理', null, '/system/role', '5', null);
INSERT INTO `t_menu` VALUES ('7', '账号管理', null, '/system/user', '5', null);
INSERT INTO `t_menu` VALUES ('8', '菜单管理', null, '/system/menu', '5', null);
INSERT INTO `t_menu` VALUES ('9', '按钮管理', null, '/system/button', '5', null);
INSERT INTO `t_menu` VALUES ('10', '权限管理', null, '/system/authority', '5', null);
INSERT INTO `t_menu` VALUES ('11', '入库管理', '', '/leasewarehouse/leaseIn', '3', '租赁仓库入库管理');
INSERT INTO `t_menu` VALUES ('12', '成品信息管理', '', '/database/finishedproduct', '1', '');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_type` varchar(20) NOT NULL COMMENT '角色',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '超级管理员', '超级管理员');

-- ----------------------------
-- Table structure for t_role_button
-- ----------------------------
DROP TABLE IF EXISTS `t_role_button`;
CREATE TABLE `t_role_button` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色 id',
  `button_ids` varchar(1024) NOT NULL COMMENT '按钮 ids',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_button
-- ----------------------------
INSERT INTO `t_role_button` VALUES ('1', '1', '10');

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色 id',
  `menu_ids` varchar(512) NOT NULL COMMENT '菜单 ids',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES ('1', '1', '0,1,2,3,4,5,6,7,8,9,10,11,120,1,12,2,3,11,4,5,6,7,8,9,10');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色 id',
  `account` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `user_name` varchar(20) NOT NULL COMMENT '姓名',
  `state` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1：启用 0：冻结',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '1', 'admin', '123456', 'admin', '1');

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `warehouse_name` varchar(20) DEFAULT NULL COMMENT '名称',
  `position` varchar(20) DEFAULT NULL COMMENT '方位',
  `pid` int(10) DEFAULT NULL COMMENT '父级id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse
-- ----------------------------
