/*
Navicat MySQL Data Transfer

Source Server         : xxx
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : media

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-04-09 09:57:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('1', 'vr');
INSERT INTO `category` VALUES ('2', '3d');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `video_id` int(11) NOT NULL,
  `choice` varchar(4) NOT NULL,
  `hot` varchar(4) NOT NULL,
  `free` varchar(4) NOT NULL,
  `recommend` varchar(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES ('1', '1', '0', '0', '1', '1');
INSERT INTO `tag` VALUES ('2', '2', '1', '0', '0', '1');
INSERT INTO `tag` VALUES ('3', '3', '0', '1', '0', '1');
INSERT INTO `tag` VALUES ('4', '4', '1', '1', '0', '0');
INSERT INTO `tag` VALUES ('5', '5', '0', '0', '1', '0');

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` int(11) NOT NULL,
  `update_time` int(11) NOT NULL,
  `path` varchar(255) NOT NULL,
  `category_id` int(11) NOT NULL,
  `cover` varchar(255) NOT NULL,
  `desc` text NOT NULL,
  `title` varchar(255) NOT NULL,
  `type` varchar(64) NOT NULL DEFAULT '1',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of video
-- ----------------------------
INSERT INTO `video` VALUES ('1', '111', '111', '111', '1', '111', '111', '1', '1', '1');
INSERT INTO `video` VALUES ('2', '222', '222', '222', '1', '222', '222', '2', '1', '1');
INSERT INTO `video` VALUES ('3', '333', '333', '333', '1', '333', '333', '3', '1', '1');
INSERT INTO `video` VALUES ('4', '444', '444', '444', '1', '444', '444', '4', '1', '1');
INSERT INTO `video` VALUES ('5', '555', '555', '555', '1', '555', '555', '5', '1', '1');
