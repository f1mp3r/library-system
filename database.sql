CREATE DATABASE IF NOT EXISTS library;

USE library;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS reservations;

CREATE TABLE reservations(
`id`				INT                     NOT NULL	AUTO_INCREMENT,
`title` varchar(60) NOT NULL,
`date_left`  		DATE NULL,
`user_id`				int(13)			NOT NULL,
`book_id`				int(13)             NOT NULL,
PRIMARY KEY (id)
)
ENGINE=INNODB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE loans(
`id`				INT                     NOT NULL	AUTO_INCREMENT,
`title` varchar(60) NOT NULL,
`isbn`				VARCHAR(13)             NOT NULL,
`date_borrowed`                   DATE			NOT NULL,
`date_due`  		DATE NULL,
`date_returned`                   DATE			NULL,
`returned` enum('yes','no') null default 'no',
`student_id`				varchar(10)			NOT NULL,
PRIMARY KEY (id)
)
ENGINE=INNODB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

CREATE TABLE books(
`id` int NOT NULL auto_increment PRIMARY KEY,
`isbn`	VARCHAR(13)      NOT NULL,
`title`	VARCHAR(150)		NOT NULL,
`location` VARCHAR(10)	NOT NULL,
`copies_in_stock`		INT		NOT NULL DEFAULT 0,
`currently_on_loan`  INT	NOT NULL DEFAULT 0,
`currently_reserved`  INT	NOT NULL DEFAULT 0,
`authors` VARCHAR(100) NOT NULL,
UNIQUE (isbn),
INDEX (title)
)
ENGINE=INNODB DEFAULT CHARSET=latin1;



CREATE TABLE users(
  `id` int NOT NULL Auto_increment, 
  `email`	VARCHAR(100)  NOT NULL,
  `password` varchar(60) NOT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `student_id` varchar(10) NOT NULL,
  `permission` tinyint(1) NOT NULL DEFAULT '0',
  `debt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `phone_number` varchar(20) DEFAULT NULL,
  `loaned` int Not NULL Default 0,
  
PRIMARY KEY (id),
UNIQUE (email),
UNIQUE (student_id)
)
ENGINE=INNODB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

# CHANGES
ALTER TABLE `loans` CHANGE `returned` `returned` BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE `loans` DROP `title`, DROP `isbn`;
ALTER TABLE `loans` ADD `book_id` INT NOT NULL AFTER `id`;
ALTER TABLE `loans` CHANGE `student_id` `user_id` int NOT NULL;