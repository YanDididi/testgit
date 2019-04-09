/*
Navicat MySQL Data Transfer

Source Server         : xxx
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : redarmy

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-04-09 09:57:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '',
  `status` tinyint(4) DEFAULT '1' COMMENT '1：正常;0禁用;',
  `create_time` int(11) DEFAULT NULL,
  `update_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of company
-- ----------------------------
INSERT INTO `company` VALUES ('1', '魔鱼互动', '1', null, null);

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `imei` varchar(50) DEFAULT NULL,
  `number` varchar(20) DEFAULT NULL,
  `status` tinyint(11) DEFAULT '1',
  `type` tinyint(4) DEFAULT '1',
  `company_id` int(11) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `update_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `imei` (`imei`(32))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES ('1', 'aa2d19794e9835baad89aade7e6ce691', 'E001', '1', '1', '1', '1552994758', '1552994867');
INSERT INTO `device` VALUES ('2', '72860caa3fe2b85ba5b1f20ed2fc7645', 'L001', '1', '2', '1', '1552994881', '1552994906');
INSERT INTO `device` VALUES ('3', '373271a587cff04379288f710f238c76', 'L002', '1', '2', '1', '1553146012', '1553146042');
INSERT INTO `device` VALUES ('4', 'a430b46f0f489e2439263b95014c97e0', 'E9999', '1', '1', '1', '1553859261', '1553859288');

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1',
  `create_time` int(11) DEFAULT NULL,
  `update_time` int(11) DEFAULT NULL,
  `cover_img` varchar(255) DEFAULT NULL,
  `desc` text,
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES ('1', '重走长征路', '1', '1552557707', '1552557707', '/upload/images/201903141801381037.png', '重走长征路', '1');

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `leader_id` int(11) DEFAULT NULL,
  `company_id` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1',
  `create_time` int(11) DEFAULT NULL,
  `update_time` int(11) DEFAULT NULL,
  `resource_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES ('2', null, '3', '1', '1', '1553146044', '1553146044', '1');
INSERT INTO `room` VALUES ('3', null, '2', '1', '1', '1', '1553757008', '1');

-- ----------------------------
-- Table structure for roomexperiencer
-- ----------------------------
DROP TABLE IF EXISTS `roomexperiencer`;
CREATE TABLE `roomexperiencer` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `device_id` int(11) DEFAULT NULL,
  `room_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roomexperiencer
-- ----------------------------
INSERT INTO `roomexperiencer` VALUES ('4', '1', '3');
INSERT INTO `roomexperiencer` VALUES ('5', '4', '3');

-- ----------------------------
-- Table structure for screen
-- ----------------------------
DROP TABLE IF EXISTS `screen`;
CREATE TABLE `screen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_id` int(11) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `cover_img` varchar(255) DEFAULT NULL,
  `expiry_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of screen
-- ----------------------------
INSERT INTO `screen` VALUES ('31', '111', '1554259114', '/upload/screen/deviceId-111-TheLast.png', '1554259234');
INSERT INTO `screen` VALUES ('32', '123', '1554260890', '/upload/screen/deviceId-123-TheLast.jpg', '1554261010');

-- ----------------------------
-- Table structure for version
-- ----------------------------
DROP TABLE IF EXISTS `version`;
CREATE TABLE `version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resource_id` int(11) DEFAULT NULL,
  `version_name` varchar(50) DEFAULT NULL,
  `desc` text,
  `path` varchar(255) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  `update_time` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of version
-- ----------------------------
INSERT INTO `version` VALUES ('1', '1', 'v1', null, '/upload/scene/201903141800215280/cz/', '1552557707', '1552557707', '1');
