create database if not exists stocksystem;
use stocksystem;

CREATE TABLE IF NOT EXISTS t_user (
	_id integer PRIMARY KEY AUTO_INCREMENT,
	username varchar(255) UNIQUE KEY NOT NULL,
    password varchar(255) NOT NULL,
	nick varchar(255) UNIQUE KEY NOT NULL,
	email varchar(255) UNIQUE KEY NOT NULL,
	balance decimal(10, 3) default 0,  
    access_token varchar(255),
    expires_in bigint default 0
);

CREATE TABLE IF NOT EXISTS t_account (
	_id integer PRIMARY KEY AUTO_INCREMENT,
	card_numbar varchar(255) UNIQUE KEY NOT NULL,
	real_name varchar(255) NOT NULL,
	id_number varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	user_id integer NOT NULL,
	FOREIGN KEY(user_id) REFERENCES t_user(_id)
);

CREATE TABLE IF NOT EXISTS t_favor (
	_id integer PRIMARY KEY AUTO_INCREMENT,
	stock_code varchar(255) NOT NULL,
	user_id integer NOT NULL,
	FOREIGN KEY(user_id) REFERENCES t_user(_id)
);

CREATE TABLE IF NOT EXISTS t_trade (
	_id integer PRIMARY KEY AUTO_INCREMENT,
	stock_code varchar(255) NOT NULL,
	stock_name varchar(255) NOT NULL,
	user_id integer NOT NULL,
	trade_type tinyint(1) NOT NULL, -- 0 卖出，1购入
	status tinyint(1) NOT NULL, -- 0 处理中，1 处理完毕
	amount integer NOT NULL default 0,
	unit_price decimal(10, 3) NOT NULL default 0,
	trade_in datetime,
	FOREIGN KEY(user_id) REFERENCES t_user(_id)
);

CREATE TABLE IF NOT EXISTS t_stock (
	_id integer PRIMARY KEY AUTO_INCREMENT,
	stock_code varchar(255) NOT NULL,
	user_id integer NOT NULL,
	total tinyint(1) NOT NULL,
	FOREIGN KEY(user_id) REFERENCES t_user(_id)
);