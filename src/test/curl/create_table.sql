--创建数据库--
DROP DATABASE jxyq;
CREATE DATABASE jxyq;



--5	健康相关表--

--5.1	健康数据--
--5.1.1	wristband_data--
DROP TABLE IF EXISTS `wristband_data`;
CREATE TABLE `wristband_data` (
  `exercise_data_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '运动数据编号',
  `measured_at` date DEFAULT NULL COMMENT '测量时间',
  `step_count` int(11) DEFAULT NULL COMMENT '步数',
  `distance` int(11) DEFAULT NULL COMMENT '公里数',
  `calories` int(11) DEFAULT NULL COMMENT '消耗能量',
  `walk_count` int(11) DEFAULT NULL COMMENT '行走步数',
  `walk_distance` int(11) DEFAULT NULL COMMENT '行走公里数',
  `walk_calories` int(11) DEFAULT NULL COMMENT '行走消耗能量',
  `run_count` int(11) DEFAULT NULL COMMENT '跑步步数',
  `run_distance` int(11) DEFAULT NULL COMMENT '跑步公里数',
  `run_calories` int(11) DEFAULT NULL COMMENT '跑步消耗能量',
  `deep_duration` int(11) DEFAULT NULL COMMENT '深睡时间',
  `shallow_duration` int(11) DEFAULT NULL COMMENT '浅睡时间',
  `heart_rate` int(11) DEFAULT NULL COMMENT '心率',
  `user_id` int(11) DEFAULT NULL COMMENT '用户编号',
  `terminal_id` int(11) DEFAULT NULL COMMENT '终端编号',
  `manufactory_id` int(11) DEFAULT NULL COMMENT '厂商信息',
  `doctor_id` int(11) DEFAULT NULL COMMENT '医生编号',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `proposal` text COMMENT '医生建议',
  PRIMARY KEY (`exercise_data_id`),
  UNIQUE KEY `measured_at` (`measured_at`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--5.1.2	sphygmomanometer_data--
DROP TABLE IF EXISTS `sphygmomanometer_data`;
CREATE TABLE `sphygmomanometer_data` (
  `sphygmomanometer_data_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '血压计数据编号',
  `measured_at` int(11) DEFAULT NULL COMMENT '测量时间',
  `systolic_pressure` int(11) DEFAULT NULL COMMENT '收缩压',
  `diastolic_pressure` int(11) DEFAULT NULL COMMENT '舒张压',
  `heart_rate` int(11) DEFAULT NULL COMMENT '心率',
  `user_id` int(11) DEFAULT NULL COMMENT '用户编号',
  `terminal_id` int(11) DEFAULT NULL COMMENT '终端信息',
  `manufactory_id` int(11) DEFAULT NULL COMMENT '厂商信息',
  `proposal` text COMMENT '医生建议',
  `doctor_id` int(11) DEFAULT '-1' COMMENT '医生编号',
  `status` int(11) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`sphygmomanometer_data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='血压计数据表';

--5.1.3	oximeter_data--
DROP TABLE IF EXISTS `oximeter_data`;
CREATE TABLE `oximeter_data` (
  `oximeter_data_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '血氧计数据编号',
  `measured_at` int(11) NOT NULL COMMENT '测量时间',
  `oximeter_value` int(11) NOT NULL COMMENT '血氧值',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `terminal_id` int(11) NOT NULL COMMENT '终端信息',
  `manufactory_id` int(11) NOT NULL COMMENT '厂商信息',
  `uploaded_at` int(11) NOT NULL COMMENT '入库时间',
  `proposal` text NOT NULL COMMENT '医生建议',
  `doctor_id` int(11) NOT NULL DEFAULT '-1' COMMENT '医生编号',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`oximeter_data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='血氧计数据表';

--5.1.4	glucosemeter_data--
DROP TABLE IF EXISTS `glucosemeter_data`;
CREATE TABLE `glucosemeter_data` (
  `glucosemeter_data_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '血糖计数据编号',
  `measured_at` int(11) NOT NULL COMMENT '测量时间',
  `glucosemeter_value` int(11) DEFAULT NULL COMMENT '血糖值',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `terminal_id` int(11) NOT NULL COMMENT '终端信息',
  `manufactory_id` int(11) NOT NULL COMMENT '厂商信息',
  `proposal` text COMMENT '医生建议',
  `doctor_id` int(11) DEFAULT NULL COMMENT '医生编号',
  `status` int(11) NOT NULL COMMENT '状态',
  PRIMARY KEY (`glucosemeter_data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='血糖计数据表';

--5.1.5	thermometer_data--
DROP TABLE IF EXISTS `thermometer_data`;
CREATE TABLE `thermometer_data` (
  `thermometer_data_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '体温计数据编号',
  `measured_at` int(11) NOT NULL COMMENT '测量时间',
  `thermometer_value` int(11) DEFAULT NULL COMMENT '体温值',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `terminal_id` int(11) NOT NULL COMMENT '终端信息',
  `manufactory_id` int(11) NOT NULL COMMENT '厂商信息',
  `uploaded_at` mediumtext NOT NULL COMMENT '入库时间',
  `proposal` text NOT NULL COMMENT '医生建议',
  `doctor_id` int(11) NOT NULL DEFAULT '-1' COMMENT '医生编号',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`thermometer_data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='体温计数据表';