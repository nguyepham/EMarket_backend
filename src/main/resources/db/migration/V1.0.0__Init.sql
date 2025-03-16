CREATE SCHEMA IF NOT EXISTS emarket;

CREATE TABLE IF NOT EXISTS emarket.password (
    id SERIAL PRIMARY KEY,

    text VARCHAR(60) NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_password_user
        FOREIGN KEY (id) REFERENCES emarket.user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.profile (
    id SERIAL PRIMARY KEY,

    is_merchant BOOLEAN NOT NULL DEFAULT FAULT,
    first_name VARCHAR(28),
    last_name VARCHAR(28),
    email VARCHAR(100),
    age INT,
    sex CHAR(1),

    CONSTRAINT fk_profile_user
        FOREIGN KEY (id) REFERENCES emarket.user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.user (
    id SERIAL PRIMARY KEY,

    image_url TEXT,
    username VARCHAR(36) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS emarket.product_details (
    id SERIAL PRIMARY KEY,

    shop_id INTEGER NOT NULL,
    short_description VARCHAR(100) NOT NULL,
    description TEXT,
    qty_in_stock INT NOT NULL,

    CONSTRAINT fk_product_details_product
        FOREIGN KEY (product_id) REFERENCES emarket.product(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_details_shop
        FOREIGN KEY (shop_id) REFERENCES emarket.shop(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.product (
    id SERIAL PRIMARY KEY,

    name VARCHAR(50) NOT NULL,
    image_url TEXT,
    unit_price BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS emarket.cart_item (
    id SERIAL PRIMARY KEY,

    product_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    qty INT NOT NULL,

    CONSTRAINT fk_cart_item_product
         FOREIGN KEY (product_id) REFERENCES emarket.product(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_customer
        FOREIGN KEY (customer_id) REFERENCES emarket.customer(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.order_item (
    id SERIAL PRIMARY KEY,

    product_id INTEGER NOT NULL,
    order_id INTEGER NOT NULL,
    purchased BOOLEAN NOT NULL DEFAULT FAULT,
    purchasedAt TIMESTAMP,
    qty INT NOT NULL,

    CONSTRAINT fk_order_item_product
          FOREIGN KEY (product_id) REFERENCES emarket.product(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id) REFERENCES emarket.order(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.order_details (
    id SERIAL PRIMARY KEY,

    customer_id INTEGER NOT NULL,
    created_at TIMESTAMP,
    completed_at TIMESTAMP,
    status CHAR(16) NOT NULL,
    status_updated_at TIMESTAMP,

    CONSTRAINT fk_order_customer
        FOREIGN KEY (customer_id) REFERENCES emarket.customer(id),
    CONSTRAINT fk_order_details_order
        FOREIGN KEY (id) REFERENCES emarket.order(id)
);

CREATE TABLE IF NOT EXISTS emarket.order (
     id SERIAL PRIMARY KEY,

     shop_id INTEGER NOT NULL,

     CONSTRAINT fk_order_shop
        FOREIGN KEY (shop_id) REFERENCES emarket.shop(id)
);

CREATE TABLE IF NOT EXISTS emarket.shop_details (
    id SERIAL PRIMARY KEY,

    owner_id INT NOT NULL,
    rating INT,

    CONSTRAINT fk_shop_details_shop
        FOREIGN KEY (id) REFERENCES emarket.shop(id) ON DELETE CASCADE,
    CONSTRAINT fk_shop_user
        FOREIGN KEY (owner_id) REFERENCES emarket.user(id)
);

CREATE TABLE IF NOT EXISTS emarket.shop (
    id SERIAL PRIMARY KEY,

    image_url TEXT
    name VARCHAR(50) NOT NULL,
);

CREATE TABLE IF NOT EXISTS emarket.business_performance (
    id SERIAL PRIMARY KEY,

    average_weekly_rating NUMERIC,
    average_monthly_rating NUMERIC,
    average_quarterly_rating NUMERIC,
    average_yearly_rating NUMERIC,

    average_weekly_sales NUMERIC,
    average_monthly_sales NUMERIC,
    average_quarterly_sales NUMERIC,
    average_yearly_sales NUMERIC,

    average_weekly_revenue NUMERIC,
    average_monthly_revenue NUMERIC,
    average_quarterly_revenue NUMERIC,
    average_yearly_revenue NUMERIC,

    total_weekly_sales BIGINT,
    total_monthly_sales BIGINT,
    total_quarterly_sales BIGINT,
    total_yearly_sales BIGINT,

    total_weekly_revenue BIGINT,
    total_monthly_revenue BIGINT,
    total_quarterly_revenue BIGINT,
    total_yearly_revenue BIGINT,

    CONSTRAINT fk_business_performance_shop
        FOREIGN KEY (id) REFERENCES emarket.shop(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.product_review (
    id SERIAL PRIMARY KEY,

    customer_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    rating INTEGER,
    image_url TEXT,
    text TEXT,

    CONSTRAINT fk_product_review_customer
        FOREIGN KEY (customer_id) REFERENCES emarket.user(id),
    CONSTRAINT fk_product_review_product
        FOREIGN KEY (product_id) REFERENCES emarket.product(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.product_review (
    id SERIAL PRIMARY KEY,

    customer_id INTEGER NOT NULL,
    shop_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    rating INTEGER,
    image_url TEXT,
    text TEXT,

    CONSTRAINT fk_product_review_customer
        FOREIGN KEY (customer_id) REFERENCES emarket.user(id),
    CONSTRAINT fk_shop_review_shop
        FOREIGN KEY (shop_id) REFERENCES emarket.shop(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.user_address (
    id SERIAL PRIMARY KEY,

    province VARCHAR(40) NOT NULL,
    district VARCHAR(40) NOT NULL,
    street_and_number VARCHAR(40) NOT NULL,

    CONSTRAINT fk_user_address_user
        FOREIGN KEY (id) REFERENCES emarket.user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.shop_address (
    id SERIAL PRIMARY KEY,

    province VARCHAR(40) NOT NULL,
    district VARCHAR(40) NOT NULL,
    street_and_number VARCHAR(40) NOT NULL,

    CONSTRAINT fk_shop_address_shop
       FOREIGN KEY (id) REFERENCES emarket.shop(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.shipment_address (
    id SERIAL PRIMARY KEY,

    province VARCHAR(40) NOT NULL,
    district VARCHAR(40) NOT NULL,
    street_and_number VARCHAR(40) NOT NULL,

    CONSTRAINT fk_shipment_address_order
        FOREIGN KEY (id) REFERENCES emarket.order(id) ON DELETE CASCADE
);
