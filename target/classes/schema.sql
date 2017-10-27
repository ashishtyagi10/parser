-- Database schema
-- create database
create database parser;
-- create table
CREATE TABLE `logrecords` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`date` DATETIME NULL DEFAULT NULL,
	`ip_address` VARCHAR(255) NULL DEFAULT NULL,
	`request_type` VARCHAR(255) NULL DEFAULT NULL,
	`status` VARCHAR(255) NULL DEFAULT NULL,
	`user_agent` VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=59
;
