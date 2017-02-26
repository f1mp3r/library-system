CREATE DATABASE IF NOT EXISTS library;

USE library;

DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS books;

CREATE TABLE books(
isbn	VARCHAR(13)      NOT NULL,
title	VARCHAR(60)		NOT NULL,
location VARCHAR(10)	NOT NULL,
copies_in_stock		INT		NOT NULL DEFAULT 0,
currently_on_loan  INT	NOT NULL DEFAULT 0,
authors VARCHAR(100) NOT NULL,
PRIMARY KEY (isbn),
UNIQUE (title),
INDEX (location)
)
ENGINE=INNODB DEFAULT CHARSET=latin1;



CREATE TABLE users(
  id int NOT NULL Auto_increment, 
  email	VARCHAR(100)  NOT NULL,
  password varchar(60) NOT NULL,
  first_name varchar(20) NOT NULL,
  last_name varchar(20) NOT NULL,
  student_id varchar(10) NOT NULL,
  permission tinyint(1) NOT NULL DEFAULT '0',
  debt decimal(10,2) NOT NULL DEFAULT '0.00',
  phone_number varchar(20) DEFAULT NULL,
PRIMARY KEY (id),
UNIQUE (email)
)
ENGINE=INNODB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;


CREATE TABLE loans(
id	int NOT NULL AUTO_INCREMENT,
user_id	int	NOT NULL,
isbn VARCHAR(13)             NOT NULL,
borrow_date date			NOT NULL,
return_date	date			NOT NULL,
returned_date date NULL,
PRIMARY KEY (id)
)
ENGINE=INNODB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;




INSERT INTO books (isbn,title,location,copies_in_stock,currently_on_loan,authors) VALUES ("65464456","Book about stuff","C-40-41",2,0,"Potato");					




INSERT INTO loans (user_id,isbn,borrow_date,return_date,returned_date) VALUES (1,"5532626262","2017-02-26","2017-03-14", null);
