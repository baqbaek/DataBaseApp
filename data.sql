CREATE DATABASE DataBaseApp;

USE DataBaseApp;

CREATE TABLE users (
    id INT AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100),
    PRIMARY KEY (id)
);

INSERT INTO users (name, email) VALUES ('Bartek Kubiczek', 'bartek@DataBaseApp.com'), ('Test Testowy', 'test@DataBaseApp.com');
