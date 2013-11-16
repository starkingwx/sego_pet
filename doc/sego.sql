# SQL Manager 2010 for MySQL 4.5.0.9
# ---------------------------------------
# Host     : localhost
# Port     : 3306
# Database : sego


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES latin1 */;

SET FOREIGN_KEY_CHECKS=0;

DROP DATABASE IF EXISTS `sego`;

CREATE DATABASE `sego`
    CHARACTER SET 'utf8'
    COLLATE 'utf8_general_ci';

USE `sego`;

#
# Structure for the `f_blacklist` table : 
#

CREATE TABLE `f_blacklist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginid` varchar(20) DEFAULT NULL,
  `blackpetid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Structure for the `f_guanzhu` table : 
#

CREATE TABLE `f_guanzhu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginid` varchar(32) DEFAULT NULL,
  `petid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Structure for the `f_liuyan` table : 
#

CREATE TABLE `f_liuyan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` varchar(32) DEFAULT NULL,
  `content` text,
  `petid` int(11) NOT NULL,
  `parentid` int(11) DEFAULT '0',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Structure for the `f_location` table : 
#

CREATE TABLE `f_location` (
  `deviceId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `longitude` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `latitude` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id` int(11) DEFAULT NULL,
  `shijian` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`deviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Structure for the `f_pets` table : 
#

CREATE TABLE `f_pets` (
  `petid` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(40) DEFAULT NULL,
  `sex` tinyint(4) NOT NULL DEFAULT '0',
  `weight` float(9,2) NOT NULL DEFAULT '0.00',
  `chuanganid` varchar(60) DEFAULT NULL,
  `ownerid` varchar(20) DEFAULT NULL,
  `avatar` varchar(60) DEFAULT NULL,
  `breed` tinyint(4) NOT NULL DEFAULT '0',
  `age` int(11) NOT NULL DEFAULT '0',
  `height` float(9,2) NOT NULL DEFAULT '0.00',
  `district` varchar(20) DEFAULT NULL,
  `placeoftengo` varchar(60) DEFAULT NULL,
  `deviceId` varchar(60) DEFAULT NULL,
  `score` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`petid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 AVG_ROW_LENGTH=1024;

#
# Structure for the `f_yundong` table : 
#

CREATE TABLE `f_yundong` (
  `deviceId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `score` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rest` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `walk` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `run` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `strenuousExercise` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id` int(11) DEFAULT NULL,
  `shijian` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`deviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Structure for the `gallery` table : 
#

CREATE TABLE `gallery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(32) DEFAULT NULL,
  `cover_url` varchar(60) NOT NULL,
  `ownerid` varchar(32) NOT NULL,
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for the `im_device_info` table : 
#

CREATE TABLE `im_device_info` (
  `userId` varchar(32) NOT NULL,
  `brand` varchar(20) DEFAULT NULL,
  `model` varchar(20) DEFAULT NULL,
  `release_ver` varchar(20) DEFAULT NULL,
  `sdk` varchar(20) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for the `im_user` table : 
#

CREATE TABLE `im_user` (
  `id` varchar(32) NOT NULL,
  `username` varchar(64) DEFAULT NULL,
  `deviceId` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `userkey` varchar(32) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('processing','success','vos_account_error','vos_phone_error','vos_suite_error') NOT NULL DEFAULT 'success' COMMENT 'account register status',
  `nickname` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for the `photo` table : 
#

CREATE TABLE `photo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) DEFAULT NULL,
  `galleryid` int(11) NOT NULL,
  `path` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `ownerid` varchar(32) DEFAULT NULL,
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;