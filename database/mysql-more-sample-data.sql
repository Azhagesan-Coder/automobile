-- Additional demo dataset: 20 rows per application table (120 total).
-- Run ONCE, after schema creation, with application services stopped.
-- New usernames/emails/VINs differ from mysql-sample-data.sql.
-- On ANY error: stop and execute ROLLBACK; do not execute COMMIT.
-- Demo password for all 20 users: Demo@12345 (BCrypt).
-- user_seq is an internal single-row counter: update it, do not add demo rows.
START TRANSACTION;
SET @user_base = (SELECT COALESCE(MAX(id), 0) FROM security.`user`);
INSERT INTO security.user_seq (next_val)
SELECT @user_base + 51 WHERE NOT EXISTS (SELECT 1 FROM security.user_seq);
UPDATE security.user_seq SET next_val = GREATEST(COALESCE(next_val, 1), @user_base + 51);
SELECT next_val INTO @more_user_id FROM security.user_seq FOR UPDATE;
UPDATE security.user_seq SET next_val = @more_user_id + 100;

INSERT INTO security.`user` (id, username, password, role) VALUES
(@more_user_id + 0, 'extra_ananya_rao', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 1, 'extra_vikram_singh', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 2, 'extra_priya_nair', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 3, 'extra_arjun_menon', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 4, 'extra_sneha_joshi', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 5, 'extra_karan_shah', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 6, 'extra_isha_gupta', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 7, 'extra_aditya_verma', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 8, 'extra_neha_reddy', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 9, 'extra_rahul_kapoor', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 10, 'extra_pooja_iyer', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 11, 'extra_siddharth_jain', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 12, 'extra_kavya_kumar', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 13, 'extra_manish_bhat', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 14, 'extra_ritika_sen', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 15, 'extra_varun_malhotra', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 16, 'extra_divya_pillai', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 17, 'extra_nikhil_sethi', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 18, 'extra_aditi_kulkarni', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER'),
(@more_user_id + 19, 'extra_sanjay_bose', '$2a$10$paq1kuq1VS9HKwOeINTvQOTKLjI90UGiLccHMCaDr90scx92iD.r6', 'USER');

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Ananya Rao', 'extra.ananya.rao@example.com', '9100000001', 'Chennai', 'Interested in Tata Nexon', 'Demo purchase: Nexon');
SET @more_customer1 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Vikram Singh', 'extra.vikram.singh@example.com', '9100000002', 'Delhi', 'Interested in Mahindra XUV700', 'Demo purchase: XUV700');
SET @more_customer2 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Priya Nair', 'extra.priya.nair@example.com', '9100000003', 'Hyderabad', 'Interested in Kia Seltos', 'Demo purchase: Seltos');
SET @more_customer3 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Arjun Menon', 'extra.arjun.menon@example.com', '9100000004', 'Kochi', 'Interested in Toyota Innova', 'Demo purchase: Innova');
SET @more_customer4 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Sneha Joshi', 'extra.sneha.joshi@example.com', '9100000005', 'Ahmedabad', 'Interested in Maruti Suzuki Baleno', 'Demo purchase: Baleno');
SET @more_customer5 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Karan Shah', 'extra.karan.shah@example.com', '9100000006', 'Jaipur', 'Interested in Hyundai i20', 'Demo purchase: i20');
SET @more_customer6 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Isha Gupta', 'extra.isha.gupta@example.com', '9100000007', 'Lucknow', 'Interested in Honda Amaze', 'Demo purchase: Amaze');
SET @more_customer7 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Aditya Verma', 'extra.aditya.verma@example.com', '9100000008', 'Indore', 'Interested in Skoda Slavia', 'Demo purchase: Slavia');
SET @more_customer8 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Neha Reddy', 'extra.neha.reddy@example.com', '9100000009', 'Nagpur', 'Interested in Volkswagen Virtus', 'Demo purchase: Virtus');
SET @more_customer9 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Rahul Kapoor', 'extra.rahul.kapoor@example.com', '9100000010', 'Surat', 'Interested in Renault Kiger', 'Demo purchase: Kiger');
SET @more_customer10 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Pooja Iyer', 'extra.pooja.iyer@example.com', '9100000011', 'Chennai', 'Interested in Nissan Magnite', 'Demo purchase: Magnite');
SET @more_customer11 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Siddharth Jain', 'extra.siddharth.jain@example.com', '9100000012', 'Delhi', 'Interested in Tata Punch', 'Demo purchase: Punch');
SET @more_customer12 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Kavya Kumar', 'extra.kavya.kumar@example.com', '9100000013', 'Hyderabad', 'Interested in Mahindra Thar', 'Demo purchase: Thar');
SET @more_customer13 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Manish Bhat', 'extra.manish.bhat@example.com', '9100000014', 'Kochi', 'Interested in Toyota Glanza', 'Demo purchase: Glanza');
SET @more_customer14 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Ritika Sen', 'extra.ritika.sen@example.com', '9100000015', 'Ahmedabad', 'Interested in Kia Sonet', 'Demo purchase: Sonet');
SET @more_customer15 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Varun Malhotra', 'extra.varun.malhotra@example.com', '9100000016', 'Jaipur', 'Interested in Hyundai Venue', 'Demo purchase: Venue');
SET @more_customer16 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Divya Pillai', 'extra.divya.pillai@example.com', '9100000017', 'Lucknow', 'Interested in Maruti Suzuki Brezza', 'Demo purchase: Brezza');
SET @more_customer17 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Nikhil Sethi', 'extra.nikhil.sethi@example.com', '9100000018', 'Indore', 'Interested in Honda Elevate', 'Demo purchase: Elevate');
SET @more_customer18 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Aditi Kulkarni', 'extra.aditi.kulkarni@example.com', '9100000019', 'Nagpur', 'Interested in Skoda Kushaq', 'Demo purchase: Kushaq');
SET @more_customer19 = LAST_INSERT_ID();

INSERT INTO customer_db.customers (name, email, mobile, address, preferences, purchase_history)
VALUES ('Sanjay Bose', 'extra.sanjay.bose@example.com', '9100000020', 'Surat', 'Interested in Volkswagen Taigun', 'Demo purchase: Taigun');
SET @more_customer20 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000001', 'Tata', 'Nexon', 'White', 2026, 21, 1150000, 'NEW', 2, '2026-07-04');
SET @more_vehicle1 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000002', 'Mahindra', 'XUV700', 'Black', 2023, 22000, 2200000, 'USED', 3, '2026-07-07');
SET @more_vehicle2 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000003', 'Kia', 'Seltos', 'Silver', 2026, 23, 1650000, 'ON_SALE', 4, '2026-07-10');
SET @more_vehicle3 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000004', 'Toyota', 'Innova', 'Blue', 2026, 24, 2600000, 'NEW', 5, '2026-07-13');
SET @more_vehicle4 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000005', 'Maruti Suzuki', 'Baleno', 'Red', 2023, 25000, 850000, 'USED', 1, '2026-07-16');
SET @more_vehicle5 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000006', 'Hyundai', 'i20', 'White', 2026, 26, 950000, 'ON_SALE', 2, '2026-07-19');
SET @more_vehicle6 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000007', 'Honda', 'Amaze', 'Black', 2026, 27, 900000, 'NEW', 3, '2026-07-22');
SET @more_vehicle7 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000008', 'Skoda', 'Slavia', 'Silver', 2023, 28000, 1500000, 'USED', 4, '2026-07-25');
SET @more_vehicle8 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000009', 'Volkswagen', 'Virtus', 'Blue', 2026, 29, 1600000, 'ON_SALE', 5, '2026-07-28');
SET @more_vehicle9 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000010', 'Renault', 'Kiger', 'Red', 2026, 30, 800000, 'NEW', 1, '2026-07-31');
SET @more_vehicle10 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000011', 'Nissan', 'Magnite', 'White', 2023, 31000, 780000, 'USED', 2, '2026-08-03');
SET @more_vehicle11 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000012', 'Tata', 'Punch', 'Black', 2026, 32, 750000, 'ON_SALE', 3, '2026-08-06');
SET @more_vehicle12 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000013', 'Mahindra', 'Thar', 'Silver', 2026, 33, 1800000, 'NEW', 4, '2026-08-09');
SET @more_vehicle13 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000014', 'Toyota', 'Glanza', 'Blue', 2023, 34000, 920000, 'USED', 5, '2026-08-12');
SET @more_vehicle14 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000015', 'Kia', 'Sonet', 'Red', 2026, 35, 1100000, 'ON_SALE', 1, '2026-08-15');
SET @more_vehicle15 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000016', 'Hyundai', 'Venue', 'White', 2026, 36, 1200000, 'NEW', 2, '2026-08-18');
SET @more_vehicle16 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000017', 'Maruti Suzuki', 'Brezza', 'Black', 2023, 37000, 1250000, 'USED', 3, '2026-08-21');
SET @more_vehicle17 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000018', 'Honda', 'Elevate', 'Silver', 2026, 38, 1550000, 'ON_SALE', 4, '2026-08-24');
SET @more_vehicle18 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000019', 'Skoda', 'Kushaq', 'Blue', 2026, 39, 1750000, 'NEW', 5, '2026-08-27');
SET @more_vehicle19 = LAST_INSERT_ID();

INSERT INTO inventory_db.vehicles (vin, make, model, color, year, mileage, price, category, stock, date_added)
VALUES ('EXTRA000000000020', 'Volkswagen', 'Taigun', 'Red', 2023, 40000, 1700000, 'USED', 1, '2026-08-30');
SET @more_vehicle20 = LAST_INSERT_ID();

-- Stock values above represent remaining stock after these demo sales.
INSERT INTO sales_db.sales (customer_id, car_id, amount, sale_date) VALUES
(CAST(@more_customer1 AS CHAR), CAST(@more_vehicle1 AS CHAR), 1139500, '2026-08-04'),
(CAST(@more_customer2 AS CHAR), CAST(@more_vehicle2 AS CHAR), 2189000, '2026-08-07'),
(CAST(@more_customer3 AS CHAR), CAST(@more_vehicle3 AS CHAR), 1638500, '2026-08-10'),
(CAST(@more_customer4 AS CHAR), CAST(@more_vehicle4 AS CHAR), 2588000, '2026-08-13'),
(CAST(@more_customer5 AS CHAR), CAST(@more_vehicle5 AS CHAR), 837500, '2026-08-16'),
(CAST(@more_customer6 AS CHAR), CAST(@more_vehicle6 AS CHAR), 937000, '2026-08-19'),
(CAST(@more_customer7 AS CHAR), CAST(@more_vehicle7 AS CHAR), 886500, '2026-08-22'),
(CAST(@more_customer8 AS CHAR), CAST(@more_vehicle8 AS CHAR), 1486000, '2026-08-25'),
(CAST(@more_customer9 AS CHAR), CAST(@more_vehicle9 AS CHAR), 1585500, '2026-08-28'),
(CAST(@more_customer10 AS CHAR), CAST(@more_vehicle10 AS CHAR), 785000, '2026-08-31'),
(CAST(@more_customer11 AS CHAR), CAST(@more_vehicle11 AS CHAR), 764500, '2026-09-03'),
(CAST(@more_customer12 AS CHAR), CAST(@more_vehicle12 AS CHAR), 734000, '2026-09-06'),
(CAST(@more_customer13 AS CHAR), CAST(@more_vehicle13 AS CHAR), 1783500, '2026-09-09'),
(CAST(@more_customer14 AS CHAR), CAST(@more_vehicle14 AS CHAR), 903000, '2026-09-12'),
(CAST(@more_customer15 AS CHAR), CAST(@more_vehicle15 AS CHAR), 1082500, '2026-09-15'),
(CAST(@more_customer16 AS CHAR), CAST(@more_vehicle16 AS CHAR), 1182000, '2026-09-18'),
(CAST(@more_customer17 AS CHAR), CAST(@more_vehicle17 AS CHAR), 1231500, '2026-09-21'),
(CAST(@more_customer18 AS CHAR), CAST(@more_vehicle18 AS CHAR), 1531000, '2026-09-24'),
(CAST(@more_customer19 AS CHAR), CAST(@more_vehicle19 AS CHAR), 1730500, '2026-09-27'),
(CAST(@more_customer20 AS CHAR), CAST(@more_vehicle20 AS CHAR), 1680000, '2026-09-30');

INSERT INTO service_db.service_record (vehicle_id, service_type, appointment_date, status, cost, notes) VALUES
(@more_vehicle1, 'Oil change', '2026-10-21', 'Scheduled', 1750, 'Demo appointment for Nexon'),
(@more_vehicle2, 'General service', '2026-10-18', 'In Progress', 2000, 'Demo appointment for XUV700'),
(@more_vehicle3, 'Wheel alignment', '2026-10-04', 'Completed', 2250, 'Demo appointment for Seltos'),
(@more_vehicle4, 'Brake inspection', '2026-10-05', 'Cancelled', 2500, 'Demo appointment for Innova'),
(@more_vehicle5, 'AC service', '2026-10-25', 'Scheduled', 2750, 'Demo appointment for Baleno'),
(@more_vehicle6, 'Oil change', '2026-10-18', 'In Progress', 3000, 'Demo appointment for i20'),
(@more_vehicle7, 'General service', '2026-10-08', 'Completed', 3250, 'Demo appointment for Amaze'),
(@more_vehicle8, 'Wheel alignment', '2026-10-09', 'Cancelled', 3500, 'Demo appointment for Slavia'),
(@more_vehicle9, 'Brake inspection', '2026-10-29', 'Scheduled', 3750, 'Demo appointment for Virtus'),
(@more_vehicle10, 'AC service', '2026-10-18', 'In Progress', 4000, 'Demo appointment for Kiger'),
(@more_vehicle11, 'Oil change', '2026-10-12', 'Completed', 4250, 'Demo appointment for Magnite'),
(@more_vehicle12, 'General service', '2026-10-13', 'Cancelled', 4500, 'Demo appointment for Punch'),
(@more_vehicle13, 'Wheel alignment', '2026-11-02', 'Scheduled', 4750, 'Demo appointment for Thar'),
(@more_vehicle14, 'Brake inspection', '2026-10-18', 'In Progress', 5000, 'Demo appointment for Glanza'),
(@more_vehicle15, 'AC service', '2026-10-01', 'Completed', 5250, 'Demo appointment for Sonet'),
(@more_vehicle16, 'Oil change', '2026-10-02', 'Cancelled', 5500, 'Demo appointment for Venue'),
(@more_vehicle17, 'General service', '2026-11-06', 'Scheduled', 5750, 'Demo appointment for Brezza'),
(@more_vehicle18, 'Wheel alignment', '2026-10-18', 'In Progress', 6000, 'Demo appointment for Elevate'),
(@more_vehicle19, 'Brake inspection', '2026-10-05', 'Completed', 6250, 'Demo appointment for Kushaq'),
(@more_vehicle20, 'AC service', '2026-10-06', 'Cancelled', 6500, 'Demo appointment for Taigun');

-- File metadata only; these paths are placeholders, not actual uploaded files.
INSERT INTO files_db.files_document (file_name, file_type, file_path, upload_at) VALUES
('extra_invoice_01.pdf', 'application/pdf', 'uploads/extra_invoice_01.pdf', '2026-08-04 10:30:00'),
('extra_invoice_02.pdf', 'application/pdf', 'uploads/extra_invoice_02.pdf', '2026-08-07 10:30:00'),
('extra_invoice_03.pdf', 'application/pdf', 'uploads/extra_invoice_03.pdf', '2026-08-10 10:30:00'),
('extra_invoice_04.pdf', 'application/pdf', 'uploads/extra_invoice_04.pdf', '2026-08-13 10:30:00'),
('extra_invoice_05.pdf', 'application/pdf', 'uploads/extra_invoice_05.pdf', '2026-08-16 10:30:00'),
('extra_invoice_06.pdf', 'application/pdf', 'uploads/extra_invoice_06.pdf', '2026-08-19 10:30:00'),
('extra_invoice_07.pdf', 'application/pdf', 'uploads/extra_invoice_07.pdf', '2026-08-22 10:30:00'),
('extra_invoice_08.pdf', 'application/pdf', 'uploads/extra_invoice_08.pdf', '2026-08-25 10:30:00'),
('extra_invoice_09.pdf', 'application/pdf', 'uploads/extra_invoice_09.pdf', '2026-08-28 10:30:00'),
('extra_invoice_10.pdf', 'application/pdf', 'uploads/extra_invoice_10.pdf', '2026-08-31 10:30:00'),
('extra_invoice_11.pdf', 'application/pdf', 'uploads/extra_invoice_11.pdf', '2026-09-03 10:30:00'),
('extra_invoice_12.pdf', 'application/pdf', 'uploads/extra_invoice_12.pdf', '2026-09-06 10:30:00'),
('extra_invoice_13.pdf', 'application/pdf', 'uploads/extra_invoice_13.pdf', '2026-09-09 10:30:00'),
('extra_invoice_14.pdf', 'application/pdf', 'uploads/extra_invoice_14.pdf', '2026-09-12 10:30:00'),
('extra_invoice_15.pdf', 'application/pdf', 'uploads/extra_invoice_15.pdf', '2026-09-15 10:30:00'),
('extra_invoice_16.pdf', 'application/pdf', 'uploads/extra_invoice_16.pdf', '2026-09-18 10:30:00'),
('extra_invoice_17.pdf', 'application/pdf', 'uploads/extra_invoice_17.pdf', '2026-09-21 10:30:00'),
('extra_invoice_18.pdf', 'application/pdf', 'uploads/extra_invoice_18.pdf', '2026-09-24 10:30:00'),
('extra_invoice_19.pdf', 'application/pdf', 'uploads/extra_invoice_19.pdf', '2026-09-27 10:30:00'),
('extra_invoice_20.pdf', 'application/pdf', 'uploads/extra_invoice_20.pdf', '2026-09-30 10:30:00');

COMMIT;

-- Each result should show 20 rows from this additional dataset.
SELECT COUNT(*) AS added_users FROM security.`user` WHERE username LIKE 'extra\_%';
SELECT COUNT(*) AS added_customers FROM customer_db.customers WHERE email LIKE 'extra.%@example.com';
SELECT COUNT(*) AS added_vehicles FROM inventory_db.vehicles WHERE vin LIKE 'EXTRA%';
SELECT COUNT(*) AS added_sales FROM sales_db.sales WHERE CAST(customer_id AS UNSIGNED) IN (@more_customer1, @more_customer2, @more_customer3, @more_customer4, @more_customer5, @more_customer6, @more_customer7, @more_customer8, @more_customer9, @more_customer10, @more_customer11, @more_customer12, @more_customer13, @more_customer14, @more_customer15, @more_customer16, @more_customer17, @more_customer18, @more_customer19, @more_customer20);
SELECT COUNT(*) AS added_services FROM service_db.service_record WHERE vehicle_id IN (@more_vehicle1, @more_vehicle2, @more_vehicle3, @more_vehicle4, @more_vehicle5, @more_vehicle6, @more_vehicle7, @more_vehicle8, @more_vehicle9, @more_vehicle10, @more_vehicle11, @more_vehicle12, @more_vehicle13, @more_vehicle14, @more_vehicle15, @more_vehicle16, @more_vehicle17, @more_vehicle18, @more_vehicle19, @more_vehicle20);
SELECT COUNT(*) AS added_files FROM files_db.files_document WHERE file_name LIKE 'extra_invoice_%';
