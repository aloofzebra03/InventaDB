INSERT INTO COMPANY (company_id, company_name, industry, address, website, contact_phone)
VALUES
    (1, 'Trident Technologies', 'Technology', '123 Main Street, Bengaluru, Karnataka', 'www.tridenttech.in', '+91 80 12345678'),
    (2, 'Shubh Industries', 'Manufacturing', '456 Industrial Area, Pune, Maharashtra', 'www.shubhindustries.com', '+91 20 87654321'),
    (3, 'Swiggy Eats', 'Food Delivery', '789 Market Road, Gurgaon, Haryana', 'www.swiggyeats.in', '+91 12 34567890'),
    (4, 'Bombay Fashion', 'Retail', '555 Fashion Boulevard, Mumbai, Maharashtra', 'www.bombayfashion.com', '+91 22 65432198'),
    (5, 'Tata Constructions', 'Construction', '100 Nirman Marg, Delhi', 'www.tataconstructions.com', '+91 11 45678901');


INSERT INTO PRODUCT (product_id, product_name, price, inventory_id,quantity)
VALUES
    (1, 'Trident Tablet', 19999.00, NULL,500),
    (2, 'Shubh Steel Almira', 7500.00, NULL,1000),
    (3, 'Swiggy Express Lunch', 149.00, NULL,1500), -- Price is per meal
    (4, 'Bombay Kurta Set', 899.00, NULL,0),
    (5, 'Tata Steel Beam', 2500.00, NULL,2000); 
 

INSERT INTO INVENTORY (inventory_id, company_id, location)
VALUES
    (1, NULL,  'Godown A, Bengaluru'),
    (2, NULL,  'Warehouse B, Pune'),
    (3, NULL, 'Central Distribution Center, Delhi'), -- Out of stock for now
    (4, NULL, 'Retail Store 1, Mumbai'),
    (5, NULL, 'Main Factory, Jamshedpur');


INSERT INTO SALES (product_id, customer_name, sales_date, quantity, unit_price)
VALUES
    (NULL, 'Rahul Kapoor', '2024-05-03', 1, 19999.00),
    (NULL, 'Priya Sharma', '2024-05-01', 2, 899.00),
    (NULL, 'Big Bulk Pvt. Ltd.', '2024-04-28', 5, 7000.00),  -- Discounted bulk price
    (NULL, 'Harpreet Singh', '2024-05-03', 1, 2500.00),
    (NULL, 'Aisha Khan', '2024-05-02', 2, 149.00); 

-- All Foreign Keys are inserted as NULL
