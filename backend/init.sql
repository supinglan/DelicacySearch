DROP DATABASE IF EXISTS ds;
CREATE DATABASE ds;
USE ds;
CREATE TABLE `user` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL UNIQUE
);
CREATE TABLE food_triple (
    source VARCHAR(255) NOT NULL,
    relation VARCHAR(255) NOT NULL,
    target VARCHAR(255) NOT NULL
);
