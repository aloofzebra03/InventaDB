CREATE DATABASE jdbc;
USE jdbc;

CREATE TABLE COMPANY (
    company_id INT ,
    company_name VARCHAR(100),
    industry VARCHAR(50),
    address VARCHAR(255),
    website VARCHAR(100),
    contact_phone VARCHAR(20),
    CONSTRAINT company_pk PRIMARY KEY (company_id)
);

CREATE TABLE PRODUCT (
    product_id INT,
    product_name VARCHAR(100),
    price DECIMAL(10,2),  
    quantity INT,
    inventory_id INT,
    CONSTRAINT product_pk PRIMARY KEY (product_id)
    -- FOREIGN KEY (inventory_id) REFERENCES company(company_id) ON DELETE CASCADE
);

CREATE TABLE INVENTORY (
    inventory_id INT,
    company_id INT,
    location VARCHAR(100), 
    CONSTRAINT inventory_pk PRIMARY KEY (inventory_id)
    -- FOREIGN KEY (company_id) REFERENCES product(product_id) ON DELETE CASCADE
);

CREATE TABLE SALES (
    sales_id INT AUTO_INCREMENT,
    product_id INT,
    customer_name VARCHAR(100),
    sales_date DATE,
    quantity INT,
    unit_price DECIMAL(10,2),
    CONSTRAINT sales_pk PRIMARY KEY (sales_id)
    -- FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE 
);
