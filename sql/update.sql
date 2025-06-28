-- Update 'product' table
UPDATE PRODUCT
SET inventory_id = CASE 
WHEN product_name = 'Trident Tablet' THEN 1 -- Technology
WHEN product_name = 'Shubh Steel Almira' THEN 2 -- Manufacturing
WHEN product_name = 'Swiggy Express Lunch' THEN 3 -- Food Delivery
WHEN product_name = 'Bombay Kurta Set' THEN 4 -- Retail
WHEN product_name = 'Tata Steel Beam' THEN 5 -- Construction
END
WHERE inventory_id IS NULL; 

-- Update 'inventory' table
UPDATE INVENTORY
SET company_id = CASE 
WHEN location = 'Godown A, Bengaluru' THEN 1 
WHEN location = 'Warehouse B, Pune' THEN 2 
WHEN location = 'Central Distribution Center, Delhi' THEN 3 
WHEN location = 'Retail Store 1, Mumbai' THEN 4 
WHEN location = 'Main Factory, Jamshedpur' THEN 5 
END
WHERE company_id IS NULL; 

-- Update 'sales' table
UPDATE SALES
SET product_id = CASE 
WHEN customer_name = 'Rahul Kapoor' THEN 1
WHEN customer_name = 'Priya Sharma' THEN 4 
WHEN customer_name = 'Big Bulk Pvt. Ltd.' THEN 2
WHEN customer_name = 'Harpreet Singh' THEN 5 
WHEN customer_name = 'Aisha Khan' THEN 3 
END
WHERE product_id IS NULL; 
