CREATE SCHEMA IF NOT EXISTS emarket;

CREATE TABLE IF NOT EXISTS emarket.user (
    id CHAR(36) PRIMARY KEY NOT NULL,
    username VARCHAR(16) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL,
    first_name VARCHAR(28),
    last_name VARCHAR(28),
    email VARCHAR(24),
    dob DATE,
    gender CHAR(1)
);