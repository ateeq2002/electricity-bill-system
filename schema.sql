-- =============================================
-- Electricity Bill Management System - Schema
-- =============================================

CREATE DATABASE IF NOT EXISTS electricity_db;
USE electricity_db;

-- Admin Table
CREATE TABLE IF NOT EXISTS admin (
    admin_id   INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL
);

-- Consumer Table
CREATE TABLE IF NOT EXISTS consumer (
    consumer_id     INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    address         VARCHAR(255) NOT NULL,
    meter_number    VARCHAR(50)  NOT NULL UNIQUE,
    connection_type ENUM('Residential','Commercial','Industrial') NOT NULL,
    password        VARCHAR(255) NOT NULL,
    status          ENUM('Active','Inactive') NOT NULL DEFAULT 'Active'
);

-- Bill Table
CREATE TABLE IF NOT EXISTS bill (
    bill_id         INT AUTO_INCREMENT PRIMARY KEY,
    consumer_id     INT            NOT NULL,
    month           VARCHAR(20)    NOT NULL,
    units_consumed  DECIMAL(10,2)  NOT NULL,
    rate_per_unit   DECIMAL(10,2)  NOT NULL,
    total_amount    DECIMAL(10,2)  NOT NULL,
    due_date        DATE           NOT NULL,
    status          ENUM('Pending','Paid','Overdue') NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (consumer_id) REFERENCES consumer(consumer_id) ON DELETE CASCADE
);

-- Payment Table
CREATE TABLE IF NOT EXISTS payment (
    payment_id      INT AUTO_INCREMENT PRIMARY KEY,
    bill_id         INT            NOT NULL,
    payment_date    DATE           NOT NULL,
    amount_paid     DECIMAL(10,2)  NOT NULL,
    payment_method  VARCHAR(50)    NOT NULL,
    transaction_id  VARCHAR(100)   NOT NULL UNIQUE,
    FOREIGN KEY (bill_id) REFERENCES bill(bill_id) ON DELETE CASCADE
);

-- =============================================
-- Default admin (username: admin, password: admin123)
-- =============================================
INSERT IGNORE INTO admin (username, password) VALUES ('admin', 'admin123');

-- Sample consumers
INSERT IGNORE INTO consumer (name, address, meter_number, connection_type, password, status) VALUES
('Ali Raza',    'House 12, Block A, Sukkur', 'MTR-001', 'Residential', 'pass123', 'Active'),
('Sara Khan',   'Shop 5, Main Market, Sukkur','MTR-002', 'Commercial',  'pass456', 'Active'),
('Zaid Ahmed',  'Plot 7, Industrial Zone',   'MTR-003', 'Industrial',  'pass789', 'Active');

