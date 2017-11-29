SELECT MD5('1'+ NOW());

CREATE DATABASE `gpsGroups`;

USE `gpsGroups`;

CREATE TABLE `users`(
	`id` INT AUTO_INCREMENT,
	`userNumber` VARCHAR(128) DEFAULT NULL,
	`rdate` DATETIME DEFAULT NOW(),
	`locked` TINYINT(1) DEFAULT 0,

	CONSTRAINT `pk_users` PRIMARY KEY(`id`)
)ENGINE=INNODB;

CREATE TABLE `groups`(
	`id` INT AUTO_INCREMENT,
	`fk_user` INT NOT NULL,
	`groupCode` VARCHAR(128) DEFAULT NULL,
	`created` DATETIME DEFAULT NOW(),
	`locked` TINYINT(1) DEFAULT 0,

	CONSTRAINT `pk_groups` PRIMARY KEY(`id`),
	CONSTRAINT `group_has_user` FOREIGN KEY(`fk_user`) REFERENCES `users`(`id`) 
);

CREATE TABLE `group_has_user`(
	`fk_group` INT NOT NULL,
	`fk_user` INT NOT NULL,

	CONSTRAINT `pk_group_has_user` PRIMARY KEY(`fk_group`, `fk_user`),
	CONSTRAINT `fk_group` FOREIGN KEY(`fk_group`) REFERENCES `groups`(`id`),
	CONSTRAINT `fk_user` FOREIGN KEY(`fk_user`) REFERENCES `users`(`id`) 
)ENGINE=INNODB;

CREATE TABLE `locations` (
	`id` INT AUTO_INCREMENT,
	`lat` FLOAT(10,6) NOT NULL,
	`lng` FLOAT(10,6) NOT NULL,
	`fk_user` INT NOT NULL,

	CONSTRAINT `pk_locations` PRIMARY KEY(`id`),
	CONSTRAINT `location_has_user` FOREIGN KEY(`fk_user`) REFERENCES `users`(`id`) 
)ENGINE=INNODB;

DELIMITER |

CREATE TRIGGER `a_ins_user` AFTER INSERT ON `users`
	FOR EACH ROW
    BEGIN
		UPDATE `users` SET `userNumber` = MD5(NEW.id) WHERE id = NEW.id;
	END;
|

DELIMITER ;

DROP TRIGGER `a_ins_user`;

DELIMITER |

CREATE PROCEDURE `sp_insert_user`()
	BEGIN
		INSERT INTO `users` VALUES();
        SET @id = LAST_INSERT_ID();
        UPDATE `users` SET `userNumber` = MD5(@id)  WHERE id = @id;
        SELECT (`userNumber`) FROM `users` WHERE id = @id;
	END|

DELIMITER ;

DROP PROCEDURE `sp_insert_user`;

CALL `sp_insert_user`;

INSERT INTO `users` VALUES ();

SELECT * FROM `users`;

SELECT LAST_INSERT_ID();
