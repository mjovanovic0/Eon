-- RECREATE DATABASE
DROP DATABASE IF EXISTS `EON`;
CREATE DATABASE `EON` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `EON`;

-- RECREATE USER
GRANT USAGE ON `EON`.* TO 'EON-ADMIN-USER'@'localhost';
DROP USER 'EON-ADMIN-USER'@'localhost';
CREATE USER 'EON-ADMIN-USER'@'localhost' IDENTIFIED BY 'BDA21C733D22F4CD68E542CB3CFF4AA544E9650111863005BBB1B779D7B047FE';
GRANT ALL PRIVILEGES ON `EON`.* TO 'EON-ADMIN-USER'@'localhost';

-- ==============================================================
-- TABLE: USERS
-- ==============================================================
CREATE TABLE `EON`.`USERS` (
	USER_ID       INT(10)      NOT NULL PRIMARY KEY AUTO_INCREMENT,
	USER_NAME     VARCHAR(20)  NOT NULL,
	USER_PASSWORD VARCHAR(41)  NOT NULL,
	USER_EMAIL    VARCHAR(255) NOT NULL,
	USER_SESSION  VARCHAR(41)
);

-- ==============================================================
-- TABLE: ROOMS
-- ==============================================================
CREATE TABLE `EON`.`ROOMS` (
	ROOM_ID       INT(10)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
	ROOM_NAME     VARCHAR(50) NOT NULL,
	ROOM_CAPACITY INT(10)     NOT NULL
);

-- ==============================================================
-- TABLE: CHAT
-- ==============================================================
CREATE TABLE `EON`.`CHAT` (
	CHAT_ID          INT(10)     NOT NULL PRIMARY KEY AUTO_INCREMENT,
	CHAT_ROOM_ID     INT(10)     NOT NULL,
	CHAT_SENDER_ID   INT(10)     NOT NULL,
	CHAT_SENT        TIMESTAMP   NOT NULL,
	CHAT_MESSAGE     TEXT        NOT NULL,
	FOREIGN KEY (CHAT_SENDER_ID) REFERENCES USERS(USER_ID)
);

-- ==============================================================
-- TABLE: USERS_IN_ROOM
-- ==============================================================
CREATE TABLE `EON`.`USERS_IN_ROOM` (
	ID      INT(10)   NOT NULL PRIMARY KEY AUTO_INCREMENT,
	USER_ID INT(10)   NOT NULL,
	ROOM_ID INT(10)   NOT NULL,
	FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID),
	FOREIGN KEY (ROOM_ID) REFERENCES ROOMS(ROOM_ID)
);
