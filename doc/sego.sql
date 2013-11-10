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
  `blackloginid` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for the `f_guanzhu` table : 
#

CREATE TABLE `f_guanzhu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginid` varchar(32) DEFAULT NULL,
  `petid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for the `f_liuyan` table : 
#

CREATE TABLE `f_liuyan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` varchar(20) DEFAULT NULL,
  `content` text,
  `petid` varchar(10) DEFAULT NULL,
  `parentid` varchar(10) DEFAULT NULL,
  `date` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for the `f_location` table : 
#

CREATE TABLE `f_location` (
  `deviceId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `longitude` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `latitude` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id` int(11) DEFAULT NULL,
  `shijian` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`deviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

#
# Structure for the `f_pets` table : 
#

CREATE TABLE `f_pets` (
  `petid` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(40) DEFAULT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `weight` varchar(20) DEFAULT NULL,
  `chuanganid` varchar(60) DEFAULT NULL,
  `ownerid` varchar(20) DEFAULT NULL,
  `avatar` varchar(60) DEFAULT NULL,
  `breed` varchar(20) DEFAULT NULL,
  `age` varchar(20) DEFAULT NULL,
  `height` varchar(20) DEFAULT NULL,
  `district` varchar(20) DEFAULT NULL,
  `placeoftengo` varchar(60) DEFAULT NULL,
  `deviceId` varchar(60) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`petid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for the `f_xiangce` table : 
#

CREATE TABLE `f_xiangce` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) DEFAULT NULL,
  `parentid` varchar(20) DEFAULT NULL,
  `path` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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



/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;