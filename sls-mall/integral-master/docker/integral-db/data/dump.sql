CREATE USER IF NOT EXISTS 'integral' IDENTIFIED BY 'default_password';

GRANT ALL ON integral.* TO 'integral';

DROP TABLE IF EXISTS `reduce_integral`;
CREATE TABLE `reduce_integral` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reduce_id` bigint(20) DEFAULT NULL,
  `add_id` bigint(20) DEFAULT NULL,
  `value` float(11,3) DEFAULT NULL,
  `expire_time` date DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `deleted` tinyint(6) DEFAULT NULL,
  `create_time` datetime(4) DEFAULT NULL,
  `update_time` datetime(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `usable_integral`;
CREATE TABLE `usable_integral` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `record_id` bigint(20) DEFAULT NULL COMMENT '记录表id',
  `value` float(11,5) DEFAULT NULL,
  `expire_time` date DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `deleted` tinyint(6) DEFAULT NULL,
  `create_time` datetime(5) DEFAULT NULL,
  `update_time` datetime(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;


DROP TABLE IF EXISTS `integral_recored`;
CREATE TABLE `integral_recored` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` smallint(11) DEFAULT NULL COMMENT '积分记录类型',
  `original_id` varchar(40) DEFAULT NULL COMMENT '原始记录id',
  `value` float(11,4) DEFAULT NULL COMMENT '积分值',
  `expire_time` date DEFAULT NULL COMMENT '过期时间',
  `reason` varchar(255) DEFAULT NULL COMMENT '积分增减原因',
  `user_id` varchar(40) DEFAULT NULL COMMENT '用户id',
  `deleted` tinyint(11) DEFAULT NULL COMMENT '删除标记',
  `create_time` datetime(3) DEFAULT NULL,
  `update_time` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `anti_cheating_record`;
CREATE TABLE `anti_cheating_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` smallint(11) DEFAULT NULL COMMENT '积分记录类型',
  `original_id` varchar(40) DEFAULT NULL COMMENT '原始记录id',
  `reason` varchar(255) DEFAULT NULL COMMENT '积分增减原因',
  `user_id` varchar(40) DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(3) DEFAULT NULL,
  `update_time` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `add` (`original_id`,`type`) USING BTREE COMMENT '唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;