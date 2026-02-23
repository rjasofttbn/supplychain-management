-- =========================
-- USERS
-- =========================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    clear_password VARCHAR(255),
    role ENUM('ADMIN','FARMER','DEALER','SHOPKEEPER','CUSTOMER') NOT NULL,
    enabled BOOLEAN DEFAULT FALSE,
    verification_token VARCHAR(255),
    token_expiry DATETIME,
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL
) ENGINE=InnoDB;


-- =========================
-- FARMERS
-- =========================
CREATE TABLE farmers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    created_by BIGINT NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_farmer_user
        FOREIGN KEY (created_by) REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;



-- =========================
-- SHOPKEEPERS
-- =========================
CREATE TABLE shopkeepers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    created_by BIGINT NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_shopkeeper_user
        FOREIGN KEY (created_by) REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;



-- =========================
-- CUSTOMERS
-- =========================
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    created_by BIGINT NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_customer_user
        FOREIGN KEY (created_by) REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;



-- =========================
-- DEALERS
-- =========================
CREATE TABLE dealers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    created_by BIGINT NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_dealer_user
        FOREIGN KEY (created_by) REFERENCES users(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- V1__create_items_table.sql

CREATE TABLE items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    name_bn VARCHAR(255),
    unit VARCHAR(50),
    sku VARCHAR(50),
    purchase_price DECIMAL(10,2),
    selling_price DECIMAL(10,2),
    min_stock_level INT,
    created_by BIGINT,
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
) ENGINE=InnoDB;


-- =========================
-- PURCHASES
-- =========================
CREATE TABLE purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_by BIGINT NOT NULL,
    farmer_id BIGINT NOT NULL,
    dealer_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity DECIMAL(12,2) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    total_amount DECIMAL(14,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (farmer_id) REFERENCES farmers(id),
    FOREIGN KEY (dealer_id) REFERENCES dealers(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
) ENGINE=InnoDB;



-- =========================
-- SALES
-- =========================
CREATE TABLE sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_by BIGINT NOT NULL,
    dealer_id BIGINT NOT NULL,
    shopkeeper_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity DECIMAL(12,2) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    total_amount DECIMAL(14,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (dealer_id) REFERENCES dealers(id),
    FOREIGN KEY (shopkeeper_id) REFERENCES shopkeepers(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
) ENGINE=InnoDB;



-- =========================
-- RETURNS
-- =========================
CREATE TABLE returns (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_by BIGINT NOT NULL,
    sale_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity DECIMAL(12,2) NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (sale_id) REFERENCES sales(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
) ENGINE=InnoDB;

-- V4__Create_PreOrders_Tables.sql
-- Flyway migration for Pre-Order tables

-- =========================================
-- Table: pre_orders_customer
-- =========================================
CREATE TABLE IF NOT EXISTS pre_orders_customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity DOUBLE(12,2) NOT NULL,
    date_of_order DATE NOT NULL,
    delivery_date DATE NOT NULL,
    status ENUM('Pending','Confirmed','Delivered','Cancelled') NOT NULL DEFAULT 'Pending',
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: pre_orders_dealer
-- =========================================
CREATE TABLE IF NOT EXISTS pre_orders_dealer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dealer_id BIGINT NOT NULL,
    farmer_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity DOUBLE(12,2) NOT NULL,
    date_of_order DATE NOT NULL,
    delivery_date DATE NOT NULL,
    status ENUM('Pending','Delivered','Cancelled') NOT NULL DEFAULT 'Pending',
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================
-- Table: pre_orders_shopkeeper
-- =========================================
CREATE TABLE IF NOT EXISTS pre_orders_shopkeeper (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shopkeeper_id BIGINT NOT NULL,
    dealer_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity DOUBLE(12,2) NOT NULL,
    date_of_order DATE NOT NULL,
    delivery_date DATE NOT NULL,
    status ENUM('Pending','Delivered','Cancelled') NOT NULL DEFAULT 'Pending',
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;