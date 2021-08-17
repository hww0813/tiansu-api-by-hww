/*
 Navicat Premium Data Transfer

 Source Server         : 200mysql
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 192.168.1.200:3306
 Source Schema         : itms

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 29/06/2021 15:33:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for busi_camera
-- ----------------------------
CREATE TABLE `busi_camera_test` (
  `ID` bigint(20) NOT NULL,
  `DEVICE_CODE` varchar(50) DEFAULT NULL COMMENT '设备编号test',
  `DEVICE_DOMAIN` varchar(255) DEFAULT NULL COMMENT '设备域',
  `DEVICE_NAME` varchar(255) DEFAULT NULL COMMENT '设备名称',
  `MAC_ADDRESS` varchar(20) DEFAULT NULL COMMENT 'MAC地址',
  `LONGITUDE` decimal(20,2) DEFAULT NULL COMMENT '经度',
  `LATITUDE` decimal(20,2) DEFAULT NULL COMMENT '纬度',
  `REGION` bigint(20) DEFAULT NULL COMMENT '行政区域代码',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `IP_ADDRESS` bigint(20) DEFAULT NULL COMMENT 'IP地址',
  `STATUS` varchar(20) DEFAULT NULL COMMENT '状态',
  `SIP_SERVER_ID` bigint(20) DEFAULT NULL COMMENT '信令服务器ID',
  `DEVICE_TYPE` varchar(20) DEFAULT '1' COMMENT '设备类型',
  `DOMAIN_IP` bigint(20) DEFAULT NULL COMMENT '域IP',
  `DOMAIN_PORT` bigint(20) DEFAULT NULL COMMENT '域端口',
  `IS_GB` tinyint(1) DEFAULT NULL COMMENT '是否国标',
  `MANUFACTURER` varchar(20) DEFAULT NULL COMMENT '制造商',
  `IS_PROBE` varchar(20) DEFAULT NULL COMMENT '审计',
  `IS_IMPORT` varchar(20) DEFAULT NULL COMMENT '导入',
  `REGION_NAME` varchar(255) DEFAULT NULL COMMENT '行政地区名称',
  `IS_CHECK` tinyint(1) DEFAULT NULL COMMENT '是否确认不是服务器',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `idx_device_code` (`DEVICE_CODE`) USING BTREE COMMENT '摄像头编码',
  KEY `idx_region_id` (`REGION`) USING BTREE COMMENT '地区索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
