/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : ntrs

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-08-29 08:23:50
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
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of company
-- ----------------------------
INSERT INTO `company` VALUES ('10', '华康食品有限公司', '如皋市花市路220号', '');
INSERT INTO `company` VALUES ('11', '张朝阳', '花市路110号老张猪肉店', '私人');

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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of company_contact
-- ----------------------------
INSERT INTO `company_contact` VALUES ('22', '11', '张朝阳', '15652112', '', '', '', null);
INSERT INTO `company_contact` VALUES ('25', '10', '马主任', '15655656', '', '', '', null);
INSERT INTO `company_contact` VALUES ('26', '10', '陈主任', '15605111452', '', '', '', null);

-- ----------------------------
-- Table structure for finished_product
-- ----------------------------
DROP TABLE IF EXISTS `finished_product`;
CREATE TABLE `finished_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `finished_number` varchar(20) DEFAULT NULL COMMENT '成品编号',
  `foreign_code` varchar(255) DEFAULT NULL,
  `trade_name` varchar(20) DEFAULT NULL COMMENT '品名',
  `specifications` varchar(20) DEFAULT NULL COMMENT '规格',
  `measurement_unit` varchar(20) DEFAULT NULL COMMENT '计量单位',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product
-- ----------------------------
INSERT INTO `finished_product` VALUES ('20', 'CP2017001', 'CP33232', '肠衣', '成品', 'KG', '');
INSERT INTO `finished_product` VALUES ('21', 'CP2017002', 'CP66363', '猪肉', '成品', 'KG', '');
INSERT INTO `finished_product` VALUES ('22', 'CP2017003', 'CP99898', '住', '大', 'KG', '');

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_outgoing
-- ----------------------------
INSERT INTO `finished_product_outgoing` VALUES ('1', '2017-08-18', 'YC20170818001', '10', '1', '0', '');
INSERT INTO `finished_product_outgoing` VALUES ('2', '2017-08-18', 'YC20170818002', '10', '1', '0', '');

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_outgoing_detail
-- ----------------------------
INSERT INTO `finished_product_outgoing_detail` VALUES ('1', '1', '20', '19.6000', '17');
INSERT INTO `finished_product_outgoing_detail` VALUES ('2', '1', '21', '19.6000', '18');
INSERT INTO `finished_product_outgoing_detail` VALUES ('3', '2', '20', '0.9000', '17');
INSERT INTO `finished_product_outgoing_detail` VALUES ('4', '2', '21', '0.9000', '18');

-- ----------------------------
-- Table structure for finished_product_stock
-- ----------------------------
DROP TABLE IF EXISTS `finished_product_stock`;
CREATE TABLE `finished_product_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `finished_product_id` int(11) DEFAULT NULL COMMENT '成品id',
  `finished_product_stock_num` decimal(10,4) DEFAULT NULL COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_stock
-- ----------------------------
INSERT INTO `finished_product_stock` VALUES ('26', '20', '20.5000');
INSERT INTO `finished_product_stock` VALUES ('27', '21', '20.5000');
INSERT INTO `finished_product_stock` VALUES ('28', '22', '111.0000');

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
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_stock_detail
-- ----------------------------
INSERT INTO `finished_product_stock_detail` VALUES ('48', '17', '20', '20.5000');
INSERT INTO `finished_product_stock_detail` VALUES ('49', '18', '21', '20.5000');
INSERT INTO `finished_product_stock_detail` VALUES ('50', '17', '22', '111.0000');

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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_storage
-- ----------------------------
INSERT INTO `finished_product_storage` VALUES ('34', '2017-08-17', 'YR20170817001', '1', '1');
INSERT INTO `finished_product_storage` VALUES ('35', '2017-08-18', 'YR20170818001', '1', '0');
INSERT INTO `finished_product_storage` VALUES ('36', '2017-08-24', 'YR20170824001', '2', '0');

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
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of finished_product_storage_detail
-- ----------------------------
INSERT INTO `finished_product_storage_detail` VALUES ('45', '34', '20', '20.5000', '17');
INSERT INTO `finished_product_storage_detail` VALUES ('46', '34', '21', '20.5000', '18');
INSERT INTO `finished_product_storage_detail` VALUES ('49', '35', '21', '1.0000', '18');
INSERT INTO `finished_product_storage_detail` VALUES ('50', '35', '20', '1.0000', '17');
INSERT INTO `finished_product_storage_detail` VALUES ('51', '36', '20', '1.0000', '17');

-- ----------------------------
-- Table structure for semimanufactures
-- ----------------------------
DROP TABLE IF EXISTS `semimanufactures`;
CREATE TABLE `semimanufactures` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `semimanufactures_number` varchar(20) DEFAULT NULL COMMENT '半成品编号',
  `foreign_code` varchar(255) DEFAULT NULL,
  `trade_name` varchar(20) DEFAULT NULL COMMENT '品名',
  `specifications` varchar(20) DEFAULT NULL COMMENT '规格',
  `measurement_unit` varchar(20) DEFAULT NULL COMMENT '计量单位',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures
-- ----------------------------
INSERT INTO `semimanufactures` VALUES ('11', 'BP20170001', 'BP12345655', '半成品肠衣', '半成品', 'KG', '');
INSERT INTO `semimanufactures` VALUES ('12', 'BP20170002', 'BP12345655', '半成品猪肉', '半成品', 'KG', '');
INSERT INTO `semimanufactures` VALUES ('13', 'BP20170003', 'BP12345655', '猪肉', 'BP', 'KG', '');
INSERT INTO `semimanufactures` VALUES ('14', 'BP20170016', 'BP12345655', '酷酷', '大', 'KG', '');
INSERT INTO `semimanufactures` VALUES ('15', 'BP20170008', 'BP12345655', '半成品90斤3米', '半成品', 'kg', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_outgoing
-- ----------------------------
INSERT INTO `semimanufactures_outgoing` VALUES ('24', '2017-08-17', 'YC201708171001', '10', '1', '1', '');
INSERT INTO `semimanufactures_outgoing` VALUES ('25', '2017-08-18', 'YC201708181001', '10', '1', '0', '');
INSERT INTO `semimanufactures_outgoing` VALUES ('27', '2017-08-18', 'YC201708181819', '10', '1', '1', '');
INSERT INTO `semimanufactures_outgoing` VALUES ('29', '2017-08-24', 'YC201708241001', '11', '1', '0', '');
INSERT INTO `semimanufactures_outgoing` VALUES ('30', '2017-08-24', 'YC201708242411', '10', '1', '0', '');
INSERT INTO `semimanufactures_outgoing` VALUES ('32', '2017-08-24', 'YC201708242425', '10', '1', '0', '');
INSERT INTO `semimanufactures_outgoing` VALUES ('34', '2017-08-24', 'YC201708242425', '10', '1', '0', '');

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
) ENGINE=InnoDB AUTO_INCREMENT=249 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_outgoing_detail
-- ----------------------------
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('193', '24', '11', '20.5000', '18');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('194', '24', '11', '30.5000', '19');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('195', '25', '11', '3.0000', '17');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('196', '25', '11', '100.0000', '18');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('197', '25', '11', '100.0000', '19');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('198', '25', '12', '100.0000', '17');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('232', '27', '11', '1.0000', '17');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('233', '27', '13', '1.0000', '18');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('234', '27', '12', '1.0000', '19');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('237', '29', '11', '397.0000', '17');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('238', '29', '11', '0.1000', '19');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('239', '30', '13', '200.0000', '17');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('240', '30', '13', '3.7000', '18');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('243', '32', '13', '0.1000', '18');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('244', '32', '13', '600.0000', '19');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('247', '34', '14', '100.1000', '17');
INSERT INTO `semimanufactures_outgoing_detail` VALUES ('248', '34', '14', '20.1000', '19');

-- ----------------------------
-- Table structure for semimanufactures_stock
-- ----------------------------
DROP TABLE IF EXISTS `semimanufactures_stock`;
CREATE TABLE `semimanufactures_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `semimanufactures_id` int(11) DEFAULT NULL COMMENT '半成品id',
  `semimanufactures_stock_num` decimal(10,4) DEFAULT NULL COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_stock
-- ----------------------------
INSERT INTO `semimanufactures_stock` VALUES ('9', '11', '600.1000');
INSERT INTO `semimanufactures_stock` VALUES ('10', '12', '100.0000');
INSERT INTO `semimanufactures_stock` VALUES ('11', '13', '803.8000');
INSERT INTO `semimanufactures_stock` VALUES ('12', '14', '500.0000');

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
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_stock_detail
-- ----------------------------
INSERT INTO `semimanufactures_stock_detail` VALUES ('17', '17', '12', '100.0000');
INSERT INTO `semimanufactures_stock_detail` VALUES ('32', '17', '13', '200.0000');
INSERT INTO `semimanufactures_stock_detail` VALUES ('33', '19', '13', '600.0000');
INSERT INTO `semimanufactures_stock_detail` VALUES ('34', '18', '13', '3.8000');
INSERT INTO `semimanufactures_stock_detail` VALUES ('35', '17', '11', '400.0000');
INSERT INTO `semimanufactures_stock_detail` VALUES ('36', '18', '11', '100.0000');
INSERT INTO `semimanufactures_stock_detail` VALUES ('37', '19', '11', '100.1000');
INSERT INTO `semimanufactures_stock_detail` VALUES ('38', '17', '14', '200.0000');
INSERT INTO `semimanufactures_stock_detail` VALUES ('39', '19', '14', '300.0000');

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
  `batch_num` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_storage
-- ----------------------------
INSERT INTO `semimanufactures_storage` VALUES ('73', '2017-08-17', 'YR20170817001', '1', '1', 'PC2011001');
INSERT INTO `semimanufactures_storage` VALUES ('74', '2017-08-17', 'YR20170817002', '1', '1', 'PC2011001');
INSERT INTO `semimanufactures_storage` VALUES ('75', '2017-08-18', 'YR20170818001', '1', '1', 'PC2011001');
INSERT INTO `semimanufactures_storage` VALUES ('86', '2017-08-18', 'YR20170818002', '1', '1', 'PC2011001');
INSERT INTO `semimanufactures_storage` VALUES ('87', '2017-08-23', 'YR20170823001', '1', '1', 'PC2011001');
INSERT INTO `semimanufactures_storage` VALUES ('95', null, 'YR20170824001', '1', '0', 'PC20170001');
INSERT INTO `semimanufactures_storage` VALUES ('96', null, 'YR20170824002', '1', '0', 'PC5565');
INSERT INTO `semimanufactures_storage` VALUES ('99', null, 'YR20170824003', '1', '0', '地方');

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
) ENGINE=InnoDB AUTO_INCREMENT=282 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of semimanufactures_storage_detail
-- ----------------------------
INSERT INTO `semimanufactures_storage_detail` VALUES ('224', '73', '11', '20.0000', '17');
INSERT INTO `semimanufactures_storage_detail` VALUES ('225', '73', '11', '30.0000', '18');
INSERT INTO `semimanufactures_storage_detail` VALUES ('226', '73', '11', '20.0000', '19');
INSERT INTO `semimanufactures_storage_detail` VALUES ('227', '74', '11', '10.5000', '17');
INSERT INTO `semimanufactures_storage_detail` VALUES ('228', '74', '12', '10.5000', '18');
INSERT INTO `semimanufactures_storage_detail` VALUES ('229', '75', '11', '100.0000', '17');
INSERT INTO `semimanufactures_storage_detail` VALUES ('230', '75', '11', '100.0000', '19');
INSERT INTO `semimanufactures_storage_detail` VALUES ('251', '86', '11', '1.0000', '17');
INSERT INTO `semimanufactures_storage_detail` VALUES ('252', '86', '13', '1.0000', '18');
INSERT INTO `semimanufactures_storage_detail` VALUES ('253', '86', '12', '1.0000', '19');
INSERT INTO `semimanufactures_storage_detail` VALUES ('254', '87', '11', '100.0000', '17');
INSERT INTO `semimanufactures_storage_detail` VALUES ('264', '95', '11', '1.0000', '17');
INSERT INTO `semimanufactures_storage_detail` VALUES ('265', '95', '12', '1.0000', '18');
INSERT INTO `semimanufactures_storage_detail` VALUES ('274', '96', '11', '1.0000', '17');
INSERT INTO `semimanufactures_storage_detail` VALUES ('275', '96', '12', '1.0000', '18');
INSERT INTO `semimanufactures_storage_detail` VALUES ('279', '99', '12', '1.0000', '18');
INSERT INTO `semimanufactures_storage_detail` VALUES ('280', '99', '13', '1.0000', '18');
INSERT INTO `semimanufactures_storage_detail` VALUES ('281', '99', '14', '1.0000', '18');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_button
-- ----------------------------

-- ----------------------------
-- Table structure for t_lease_warehouse_in
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_in`;
CREATE TABLE `t_lease_warehouse_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_in_no` varchar(20) NOT NULL COMMENT '入库单号',
  `warehouse_in_date` date DEFAULT NULL COMMENT '入库日期',
  `customer` int(11) NOT NULL COMMENT '客户 id',
  `warehouse_id` int(11) NOT NULL COMMENT '仓库 id',
  `location` varchar(50) DEFAULT NULL COMMENT '摆放位置',
  `warehouse_in_person` varchar(20) DEFAULT NULL COMMENT '入库人',
  `is_in` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1：已入库 0：未确认',
  `is_checked` tinyint(1) DEFAULT '0',
  `is_all_out` tinyint(1) DEFAULT '0',
  `in_price_per_day` decimal(11,4) DEFAULT NULL COMMENT '每日仓储费用：所有入库产品每日费用总和  元/天',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_in
-- ----------------------------
INSERT INTO `t_lease_warehouse_in` VALUES ('23', 'LR20170823001', '2017-08-23', '10', '17', '', 'admin', '1', '1', '1', '11088.9000');
INSERT INTO `t_lease_warehouse_in` VALUES ('24', 'LR20170823002', '2017-08-23', '11', '17', '南', 'admin', '1', '1', '0', '400.0000');
INSERT INTO `t_lease_warehouse_in` VALUES ('25', 'LR20170823003', '2017-08-23', '11', '17', '', 'admin', '1', '1', '0', '410.0000');
INSERT INTO `t_lease_warehouse_in` VALUES ('26', 'LR20170823004', '2017-08-23', '11', '17', '', 'admin', '1', '1', '0', '3600.0000');

-- ----------------------------
-- Table structure for t_lease_warehouse_inventory
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_inventory`;
CREATE TABLE `t_lease_warehouse_inventory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_in_id` int(11) NOT NULL COMMENT '入库单号',
  `inventory_price_per_day` decimal(11,4) DEFAULT NULL COMMENT '每日仓储费用：所有库存产品每日费用总和  元/天',
  `is_all_out` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_inventory
-- ----------------------------
INSERT INTO `t_lease_warehouse_inventory` VALUES ('17', '23', '6593.4000', '0');
INSERT INTO `t_lease_warehouse_inventory` VALUES ('18', '25', '205.0000', '0');
INSERT INTO `t_lease_warehouse_inventory` VALUES ('19', '24', '400.0000', '0');
INSERT INTO `t_lease_warehouse_inventory` VALUES ('20', '26', '3600.0000', '0');

-- ----------------------------
-- Table structure for t_lease_warehouse_inventory_product
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_inventory_product`;
CREATE TABLE `t_lease_warehouse_inventory_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inventory_id` int(11) NOT NULL COMMENT '租赁仓库库存 id',
  `product_id` int(11) NOT NULL COMMENT '入库产品 id',
  `left_quantity` decimal(11,4) NOT NULL COMMENT '库存数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_inventory_product
-- ----------------------------
INSERT INTO `t_lease_warehouse_inventory_product` VALUES ('26', '17', '64', '66.0000');
INSERT INTO `t_lease_warehouse_inventory_product` VALUES ('27', '18', '66', '10.0000');
INSERT INTO `t_lease_warehouse_inventory_product` VALUES ('28', '19', '65', '200.0000');
INSERT INTO `t_lease_warehouse_inventory_product` VALUES ('29', '20', '67', '200.0000');

-- ----------------------------
-- Table structure for t_lease_warehouse_in_product
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_in_product`;
CREATE TABLE `t_lease_warehouse_in_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_in_id` int(11) NOT NULL COMMENT '入库单号',
  `name` varchar(20) NOT NULL COMMENT '产品名称',
  `specification` varchar(20) DEFAULT NULL COMMENT '规格',
  `unit` varchar(10) NOT NULL COMMENT '单位',
  `quantity` decimal(11,4) NOT NULL COMMENT '数量',
  `unit_price` decimal(11,4) DEFAULT NULL COMMENT '单价',
  `price_per_day` decimal(11,4) DEFAULT NULL COMMENT '每天存放价格：数量*单价  元/天',
  `is_checked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 1：已核算 0：未核算',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_in_product
-- ----------------------------
INSERT INTO `t_lease_warehouse_in_product` VALUES ('64', '23', 'A', 'A', 'A', '111.0000', '99.9000', '11088.9000', '1');
INSERT INTO `t_lease_warehouse_in_product` VALUES ('65', '24', '鱼丸', '大', 'KG', '200.0000', '2.0000', '400.0000', '1');
INSERT INTO `t_lease_warehouse_in_product` VALUES ('66', '25', '大章鱼', '大号', 'KG', '20.0000', '20.5000', '410.0000', '1');
INSERT INTO `t_lease_warehouse_in_product` VALUES ('67', '26', '猪肉', '大', '筒', '200.0000', '18.0000', '3600.0000', '1');

-- ----------------------------
-- Table structure for t_lease_warehouse_out
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_out`;
CREATE TABLE `t_lease_warehouse_out` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_in_id` int(11) NOT NULL COMMENT '入库单号',
  `warehouse_out_no` varchar(20) NOT NULL COMMENT '出库单号',
  `warehouse_out_date` date DEFAULT NULL COMMENT '出库日期',
  `warehouse_out_person` varchar(20) DEFAULT NULL COMMENT '出库人',
  `is_out` tinyint(1) DEFAULT '0' COMMENT '状态 1：已出库 0：未确认',
  `out_price_per_day` decimal(11,4) DEFAULT NULL,
  `out_cost` decimal(11,4) DEFAULT NULL COMMENT '出库费用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_out
-- ----------------------------
INSERT INTO `t_lease_warehouse_out` VALUES ('41', '23', 'LC20170823001', null, 'admin', '0', '6593.4000', '600.0000');
INSERT INTO `t_lease_warehouse_out` VALUES ('43', '23', 'LC20170823002', '2017-08-23', 'admin', '1', '4495.5000', '200.0000');
INSERT INTO `t_lease_warehouse_out` VALUES ('44', '25', 'LC20170823003', '2017-08-23', 'admin', '1', '205.0000', '200.0000');

-- ----------------------------
-- Table structure for t_lease_warehouse_out_product
-- ----------------------------
DROP TABLE IF EXISTS `t_lease_warehouse_out_product`;
CREATE TABLE `t_lease_warehouse_out_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warehouse_out_id` int(11) NOT NULL COMMENT '出库单号',
  `product_id` int(11) NOT NULL COMMENT '入库产品 id',
  `out_quantity` decimal(11,4) NOT NULL COMMENT '出库数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lease_warehouse_out_product
-- ----------------------------
INSERT INTO `t_lease_warehouse_out_product` VALUES ('91', '41', '64', '66.0000');
INSERT INTO `t_lease_warehouse_out_product` VALUES ('93', '43', '64', '45.0000');
INSERT INTO `t_lease_warehouse_out_product` VALUES ('94', '44', '66', '10.0000');

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
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', '基础数据管理', 'fa-database', null, '0', null);
INSERT INTO `t_menu` VALUES ('2', '自用仓库管理', 'fa-home', null, '0', null);
INSERT INTO `t_menu` VALUES ('3', '租赁仓库管理', 'fa-table', null, '0', null);
INSERT INTO `t_menu` VALUES ('4', '租赁费用核算', 'fa-bar-chart', null, '0', null);
INSERT INTO `t_menu` VALUES ('5', '系统管理', 'fa-cogs', null, '0', null);
INSERT INTO `t_menu` VALUES ('6', '角色管理', null, '/system/role', '5', null);
INSERT INTO `t_menu` VALUES ('7', '账号管理', null, '/system/user', '5', null);
INSERT INTO `t_menu` VALUES ('10', '权限管理', null, '/system/authority', '5', null);
INSERT INTO `t_menu` VALUES ('11', '租赁入库管理', '', '/leasewarehouse/leaseIn', '3', '租赁仓库入库管理');
INSERT INTO `t_menu` VALUES ('12', '成品信息管理', '', '/database/finishedproduct', '1', '');
INSERT INTO `t_menu` VALUES ('13', '半成品信息管理', '', '/database/semimanufactures', '1', '');
INSERT INTO `t_menu` VALUES ('14', '租赁出库管理', '', '/leasewarehouse/leaseOut', '3', '租赁仓库出库管理');
INSERT INTO `t_menu` VALUES ('15', '租赁库存管理', '', '/leasewarehouse/leaseInventory', '3', '租赁仓库库存管理');
INSERT INTO `t_menu` VALUES ('16', '条形码管理', '', '/database/barcode', '1', '');
INSERT INTO `t_menu` VALUES ('17', '仓库管理', '', '/database/storage', '1', '');
INSERT INTO `t_menu` VALUES ('18', '客户管理', '', '/database/customes', '1', '');
INSERT INTO `t_menu` VALUES ('19', '租赁入库费用管理', '', '/leaseprice/leaseInPrice', '4', '租赁仓库入库费用录入');
INSERT INTO `t_menu` VALUES ('20', '租赁出库费用管理', '', '/leaseprice/leaseOutPrice', '4', '租赁仓库出库费用');
INSERT INTO `t_menu` VALUES ('21', '租赁库存费用管理', '', '/leaseprice/leaseInventoryPrice', '4', '租赁费用-库存');
INSERT INTO `t_menu` VALUES ('22', '半成品入库管理', '', '/private/semiin', '2', '');
INSERT INTO `t_menu` VALUES ('23', '半成品出库管理', '', '/private/semiout', '2', '');
INSERT INTO `t_menu` VALUES ('24', '半成品库存管理', '', '/private/semistock', '2', '');
INSERT INTO `t_menu` VALUES ('25', '成品入库管理', '', '/private/finishedin', '2', '自用仓库成品入库管理');
INSERT INTO `t_menu` VALUES ('26', '成品出库管理', '', '/private/finishedout', '2', '成品出库管理');
INSERT INTO `t_menu` VALUES ('27', '成品库存管理', '', '/private/finishedstock', '2', '成品库存管理');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_type` varchar(20) NOT NULL COMMENT '角色',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '超级管理员', '超级管理员');
INSERT INTO `t_role` VALUES ('2', '仓管', '仓库管理');
INSERT INTO `t_role` VALUES ('3', '会计', '审核费用');
INSERT INTO `t_role` VALUES ('4', '管理员', '');

-- ----------------------------
-- Table structure for t_role_button
-- ----------------------------
DROP TABLE IF EXISTS `t_role_button`;
CREATE TABLE `t_role_button` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色 id',
  `button_ids` varchar(1024) DEFAULT NULL COMMENT '按钮 ids',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_button
-- ----------------------------
INSERT INTO `t_role_button` VALUES ('1', '1', '');
INSERT INTO `t_role_button` VALUES ('2', '2', '');
INSERT INTO `t_role_button` VALUES ('3', '3', '');
INSERT INTO `t_role_button` VALUES ('4', '4', '');

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色 id',
  `menu_ids` varchar(512) NOT NULL COMMENT '菜单 ids',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES ('1', '1', '0,1,2,3,4,5,6,7,8,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27');
INSERT INTO `t_role_menu` VALUES ('2', '2', '0,2,22,23,24,25,26,27,3,11,14,15');
INSERT INTO `t_role_menu` VALUES ('3', '3', '0,4,19,20,21,5,6,7,10');
INSERT INTO `t_role_menu` VALUES ('4', '4', '0,1,12,13,16,17,18');

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
  `type` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '1', 'admin', '6697BFAAD9A17D787670B699A12BC9DD3B8A544808CE6C73F1722693', 'admin', '1', '0');
INSERT INTO `t_user` VALUES ('2', '2', 'cangguan1', '02AB91E2E13832C3E9B01D6E6E34B916FECA81E0613AE600A1547915', '仓管1', '1', '0');
INSERT INTO `t_user` VALUES ('3', '3', 'kuaiji1', 'EF7DE28D85E8D9EAB873E004E5034CEF966C51F2B0CF7DF4883F3626', '会计1', '1', '0');
INSERT INTO `t_user` VALUES ('5', '2', '保管员', 'EFE46BCBDD726DE69505502E7FE20EB148B0192E6898F9B3F7B5F61B', '张大妈', '1', '0');
INSERT INTO `t_user` VALUES ('6', '1', '李代沫', 'A780B07CA7B5AC4A2EB29EA7DF47FB2D83D7054E14077428FC504AC9', '李代沫', '1', '0');

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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse
-- ----------------------------
INSERT INTO `warehouse` VALUES ('15', '一号仓库', '', '0');
INSERT INTO `warehouse` VALUES ('16', 'A区', '', '15');
INSERT INTO `warehouse` VALUES ('17', '1号货架', '嗯嗯', '16');
INSERT INTO `warehouse` VALUES ('18', '2号货架', '', '16');
INSERT INTO `warehouse` VALUES ('19', '3号货架', '嗯嗯', '16');
