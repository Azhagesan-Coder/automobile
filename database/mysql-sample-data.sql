-- Run once after mysql-schema.sql, with all application services stopped.
-- Demo rows: 3 per application table. Password for each demo user: Demo@12345.
-- Auto-increment IDs are captured so existing rows do not break references.
-- Duplicate demo usernames/emails/VINs will fail: do not rerun blindly.
START TRANSACTION;

-- Reserve a block for manual user IDs without moving the sequence backwards.
SET @user_base = (SELECT COALESCE(MAX(id), 0) FROM security.`user`);
INSERT INTO security.user_seq (next_val)
SELECT @user_base + 51 WHERE NOT EXISTS (SELECT 1 FROM security.user_seq);
UPDATE security.user_seq SET next_val = GREATEST(COALESCE(next_val, 1), @user_base + 51);
SELECT next_val INTO @seed_user_id FROM security.user_seq FOR UPDATE;
UPDATE security.user_seq SET next_val = @seed_user_id + 100;

INSERT INTO security.`user` (id, username, password, role) VALUES
(@seed_user_id, 'demo_aarav', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@seed_user_id + 1, 'demo_meera', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@seed_user_id + 2, 'demo_rohan', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER');

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Aarav Sharma', 'aarav.demo@example.com', '9000000001', 'Pune', 'SUV, petrol', 'Purchased demo Creta');
SET @customer1 = LAST_INSERT_ID();
INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Meera Patel', 'meera.demo@example.com', '9000000002', 'Mumbai', 'Hatchback, automatic', 'Purchased demo Swift');
SET @customer2 = LAST_INSERT_ID();
INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Rohan Das', 'rohan.demo@example.com', '9000000003', 'Bengaluru', 'Sedan', 'Purchased demo City');
SET @customer3 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('DEMO0000000000001', 'Hyundai', 'Creta', 'White', 2026, 25, 1500000, 'NEW', 4, '2026-09-01');
SET @vehicle1 = LAST_INSERT_ID();
INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('DEMO0000000000002', 'Maruti Suzuki', 'Swift', 'Red', 2023, 28000, 650000, 'USED', 2, '2026-09-05');
SET @vehicle2 = LAST_INSERT_ID();
INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('DEMO0000000000003', 'Honda', 'City', 'Silver', 2025, 8000, 1200000, 'ON_SALE', 1, '2026-09-10');
SET @vehicle3 = LAST_INSERT_ID();

INSERT INTO sales_db.sales (customer_id, car_id, amount, sale_date) VALUES
(CAST(@customer1 AS CHAR), CAST(@vehicle1 AS CHAR), 1480000, '2026-09-20'),
(CAST(@customer2 AS CHAR), CAST(@vehicle2 AS CHAR), 630000, '2026-09-25'),
(CAST(@customer3 AS CHAR), CAST(@vehicle3 AS CHAR), 1150000, '2026-10-01');

INSERT INTO service_db.service_record (vehicle_id, service_type, appointment_date, status, cost, notes) VALUES
(@vehicle1, 'First inspection', '2026-10-15', 'Scheduled', 0, 'Check fluids and tyre pressure'),
(@vehicle2, 'Oil change', '2026-10-08', 'In Progress', 3500, 'Replace oil and oil filter'),
(@vehicle3, 'Wheel alignment', '2026-10-05', 'Completed', 1500, 'Alignment completed');

-- Metadata examples only: these INSERTs do not create physical files.
INSERT INTO files_db.files_document (file_name, file_type, file_path, upload_at) VALUES
('demo_invoice.pdf', 'application/pdf', 'uploads/demo_invoice.pdf', '2026-09-20 10:00:00'),
('demo_vehicle.jpg', 'image/jpeg', 'uploads/demo_vehicle.jpg', '2026-09-25 11:30:00'),
('demo_service.pdf', 'application/pdf', 'uploads/demo_service.pdf', '2026-10-05 15:00:00');

COMMIT;

SELECT * FROM security.`user`;
SELECT * FROM security.user_seq;
SELECT * FROM customer_db.customers;
SELECT * FROM inventory_db.vehicles;
SELECT * FROM sales_db.sales;
SELECT * FROM service_db.service_record;
SELECT * FROM files_db.files_document;
