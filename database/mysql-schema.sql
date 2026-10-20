-- AutoShowroom: schema for the current JPA entities, MySQL 8+.
-- Run on a fresh installation, before starting the services.
-- Existing tables are not migrated by CREATE TABLE IF NOT EXISTS.
CREATE DATABASE IF NOT EXISTS `security` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `customer_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `inventory_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `sales_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `service_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `files_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `security`.`user` (
  `id` BIGINT NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255),
  `role` VARCHAR(255),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username` (`username`)
) ENGINE=InnoDB;

-- User uses @GeneratedValue (AUTO), rather than IDENTITY.
-- Hibernate uses this table to emulate a sequence on MySQL.
CREATE TABLE IF NOT EXISTS `security`.`user_seq` (
  `next_val` BIGINT
) ENGINE=InnoDB;
-- Initialize only an empty sequence on a fresh database.
-- Do not run concurrently with the gateway or manually reset an existing sequence.
INSERT INTO `security`.`user_seq` (`next_val`)
SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM `security`.`user_seq`)
AND NOT EXISTS (SELECT 1 FROM `security`.`user`);

CREATE TABLE IF NOT EXISTS `customer_db`.`customers` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255),
  `email` VARCHAR(255) NOT NULL,
  `mobile` VARCHAR(255),
  `address` VARCHAR(255),
  `preferences` VARCHAR(255),
  `purchase_history` VARCHAR(255),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customers_email` (`email`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `inventory_db`.`vehicles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `vin` VARCHAR(255) NOT NULL,
  `make` VARCHAR(255),
  `model` VARCHAR(255),
  `color` VARCHAR(255),
  `year` INT,
  `mileage` DOUBLE,
  `price` DOUBLE,
  `category` ENUM('NEW', 'USED', 'ON_SALE'),
  `stock` INT,
  `date_added` DATE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vehicles_vin` (`vin`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `sales_db`.`sales` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `customer_id` VARCHAR(255),
  `car_id` VARCHAR(255),
  `amount` DOUBLE,
  `sale_date` DATE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `service_db`.`service_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `vehicle_id` BIGINT,
  `service_type` VARCHAR(255),
  `appointment_date` DATE,
  `status` VARCHAR(255),
  `cost` DOUBLE,
  `notes` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `files_db`.`files_document` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `file_name` VARCHAR(255),
  `file_type` VARCHAR(255),
  `file_path` VARCHAR(255),
  `upload_at` DATETIME(6),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- Show the resulting tables.
SELECT TABLE_SCHEMA AS database_name, TABLE_NAME AS table_name
FROM information_schema.TABLES
WHERE TABLE_SCHEMA IN ('security', 'customer_db', 'inventory_db',
                       'sales_db', 'service_db', 'files_db')
AND TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_SCHEMA, TABLE_NAME;
