drop database if exists shop;
create database shop;
use shop;

CREATE TABLE employee (
    employeeId INT PRIMARY KEY DEFAULT 123,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) DEFAULT "NONAME"
);

CREATE TABLE inventory (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    wholesalerPrice DECIMAL(10,2) NOT NULL,
    available boolean NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE historical_inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_product INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    wholesalerPrice DECIMAL(10, 2) NOT NULL,
    available INT NOT NULL,
    stock INT NOT NULL,
    created_at DATETIME NOT NULL
);


INSERT INTO employee (password, name) VALUES ('test', 'enric');

INSERT INTO inventory (id, name, wholesalerPrice, available, stock) VALUES
(1, 'Laptop Lenovo', 599.99, 1, 5),
(2, 'Monitor Samsung 24"', 159.50, 1, 7),
(3, 'Teclado Mecánico RGB', 49.90, 1, 12),
(4, 'Mouse Logitech MX Master', 79.99, 1, 10),
(5, 'Auriculares Sony WH-1000XM4', 299.99, 1, 3),
(6, 'Cámara Canon EOS M50', 699.99, 1, 4),
(7, 'Smartphone Samsung Galaxy S22', 799.99, 1, 6),
(8, 'Tablet iPad Air', 649.99, 1, 5),
(9, 'Impresora HP LaserJet', 199.99, 1, 3),
(10, 'Disco Duro Externo 1TB', 59.99, 1, 8),
(11, 'Memoria USB 128GB', 19.99, 1, 30),
(12, 'Cargador Portátil 20,000mAh', 39.99, 1, 15),
(13, 'Televisor LG OLED 55"', 1199.99, 1, 2),
(14, 'Consola PlayStation 5', 499.99, 1, 3),
(15, 'Router Wi-Fi 6 TP-Link', 129.99, 1, 5);


SELECT * FROM EMPLOYEE;
SELECT * FROM inventory;

SELECT * FROM historical_inventory;
DESCRIBE inventory;

SELECT * FROM product;
SELECT * FROM productlist;