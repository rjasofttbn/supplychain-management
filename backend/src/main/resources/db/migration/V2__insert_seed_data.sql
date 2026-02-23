-- src/main/resources/db/migration/V3__Seed_Initial_Data.sql

-- =========================
-- USERS
-- =========================
INSERT INTO users
(name, email, password, clear_password, role, enabled, created_at)
VALUES
('Admin User', 'admin@supply.com', '$2a$10$hashedpassword', 'admin123', 'ADMIN', TRUE, NOW()),
('Farmer User', 'farmer@supply.com', '$2a$10$hashedpassword', 'farmer123', 'FARMER', TRUE, NOW()),
('Dealer User', 'dealer@supply.com', '$2a$10$hashedpassword', 'dealer123', 'DEALER', TRUE, NOW()),
('Shopkeeper User', 'shop@supply.com', '$2a$10$hashedpassword', 'shop123', 'SHOPKEEPER', TRUE, NOW()),
('Customer User', 'customer@supply.com', '$2a$10$hashedpassword', 'customer123', 'CUSTOMER', TRUE, NOW());

-- =========================
-- FARMERS (created_by = 2 -> Farmer User)
-- =========================
INSERT INTO farmers
(name, created_by, phone, address, status, created_at)
VALUES
('Abdul Karim', 2, '01711111111', 'Rajshahi', 'active', NOW()),
('Rahim Uddin', 2, '01722222222', 'Natore', 'active', NOW());

-- =========================
-- DEALERS (created_by = 3 -> Dealer User)
-- =========================
INSERT INTO dealers
(name, created_by, phone, address, status, created_at)
VALUES
('Rosenbaum Dealer', 3, '01874027897', 'Dhaka Market', 'active', NOW()),
('Ortiz Dealer', 3, '01823270764', 'Gazipur Market', 'active', NOW());

-- =========================
-- SHOPKEEPERS (created_by = 4 -> Shopkeeper User)
-- =========================
INSERT INTO shopkeepers
(name, created_by, phone, address, status, created_at)
VALUES
('Karim Store', 4, '01911111111', 'Dhaka Mirpur', 'active', NOW()),
('Bismillah Store', 4, '01922222222', 'Gazipur', 'active', NOW());


-- =========================
-- CUSTOMERS (created_by = 5 -> Customer User)
-- =========================
INSERT INTO customers
(name, created_by, phone, address, status, created_at)
VALUES
('Sakib Hasan', 5, '01611111111', 'Dhaka', 'active', NOW()),
('Tamim Iqbal', 5, '01622222222', 'Chattogram', 'active', NOW());


-- =========================
-- ITEMS (created_by = 3 -> Dealer User)
-- =========================

INSERT INTO items
(name, name_bn, unit, sku, purchase_price, selling_price, min_stock_level, created_by, status, created_at)
VALUES
('Ridge Gourd', 'ঝিঙ্গা', 'Piece', 'SKU-RG-001', 30.00, 50.00, 10, 3, 'active', NOW()),
('Tomato', 'টমেটো', 'Kg', 'SKU-TM-001', 20.00, 40.00, 15, 3, 'active', NOW());

-- =========================
-- PURCHASES
-- Farmer (1) -> Dealer (1) -> Item (1)
-- =========================
INSERT INTO purchases
(created_by, farmer_id, dealer_id, item_id, quantity, price, total_amount, created_at)
VALUES
(3, 1, 1, 1, 500, 40, 20000, NOW());

-- =========================
-- SALES
-- Dealer (1) -> Shopkeeper (1) -> Item (1)
-- =========================
INSERT INTO sales
(created_by, dealer_id, shopkeeper_id, item_id, quantity, price, total_amount, created_at)
VALUES
(3, 1, 1, 1, 200, 45, 9000, NOW());

-- =========================
-- RETURNS
-- Sale (1) -> Item (1)
-- =========================
INSERT INTO returns
(created_by, sale_id, item_id, quantity, reason, created_at)
VALUES
(3, 1, 1, 5, 'Damaged product', NOW());

ALTER TABLE users AUTO_INCREMENT = 6;
ALTER TABLE farmers AUTO_INCREMENT = 3;
ALTER TABLE shopkeepers AUTO_INCREMENT = 3;
ALTER TABLE customers AUTO_INCREMENT = 3;
ALTER TABLE items AUTO_INCREMENT = 4;

