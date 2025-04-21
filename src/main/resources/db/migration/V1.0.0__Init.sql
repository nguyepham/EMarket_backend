CREATE SCHEMA IF NOT EXISTS emarket;

CREATE TABLE IF NOT EXISTS emarket.user (
    id SERIAL PRIMARY KEY,

    image_url TEXT,
    username VARCHAR(36) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS emarket.password (
    id SERIAL PRIMARY KEY,

    text VARCHAR(60) NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_password_user
        FOREIGN KEY (id) REFERENCES emarket.user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.profile (
    id SERIAL PRIMARY KEY,

    authority VARCHAR(16) NOT NULL,
    first_name VARCHAR(28),
    last_name VARCHAR(28),
    email VARCHAR(100),
    age INT,
    gender VARCHAR(7),

    CONSTRAINT fk_profile_user
        FOREIGN KEY (id) REFERENCES emarket.user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.product (
    id SERIAL PRIMARY KEY,
    seller_username VARCHAR(36) NOT NULL,

    name VARCHAR(50) NOT NULL,
    cover_image_url TEXT DEFAULT NULL,
    unit_price BIGINT NOT NULL,

    updated_at TIMESTAMP NOT NULL,
    short_description VARCHAR(100) NOT NULL,
    description TEXT,
    qty_in_stock INT NOT NULL,

    CONSTRAINT fk_product_seller
        FOREIGN KEY (seller_username) REFERENCES emarket.user(username) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.product_image (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL,
    image_url TEXT NOT NULL,

    CONSTRAINT fk_product_image_product
    FOREIGN KEY (product_id) REFERENCES emarket.product(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.category (
    id SERIAL PRIMARY KEY,

    name VARCHAR(50) NOT NULL,
    level INT NOT NULL,
    path VARCHAR(255) NOT NULL,
    parent_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS emarket.category_product (
    id SERIAL,
    category_id INT NOT NULL,
    product_id INT NOT NULL,
    PRIMARY KEY (id, category_id, product_id),

    CONSTRAINT fk_category_product_product
        FOREIGN KEY (product_id) REFERENCES emarket.product(id) ON DELETE CASCADE,
    CONSTRAINT fk_category_product_category
        FOREIGN KEY (category_id) REFERENCES emarket.category(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.order (
    id SERIAL PRIMARY KEY,

    seller_username VARCHAR(36) NOT NULL,
    customer_username VARCHAR(36) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    status VARCHAR(16) NOT NULL,
    status_updated_at TIMESTAMP,

    CONSTRAINT fk_order_seller
        FOREIGN KEY (seller_username) REFERENCES emarket.user(username),
    CONSTRAINT fk_order_customer
        FOREIGN KEY (customer_username) REFERENCES emarket.user(username)
);

CREATE TABLE IF NOT EXISTS emarket.cart_item (
    id SERIAL PRIMARY KEY,

    product_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    qty INT NOT NULL,

    CONSTRAINT fk_cart_item_product
         FOREIGN KEY (product_id) REFERENCES emarket.product(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_customer
        FOREIGN KEY (customer_id) REFERENCES emarket.user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.order_item (
    id SERIAL PRIMARY KEY,

    product_id INTEGER NOT NULL,
    order_id INTEGER NOT NULL,
    purchased BOOLEAN NOT NULL,
    purchased_at TIMESTAMP,
    qty INT NOT NULL,

    CONSTRAINT fk_order_item_product
          FOREIGN KEY (product_id) REFERENCES emarket.product(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id) REFERENCES emarket.order(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.product_review (
    id SERIAL PRIMARY KEY,

    customer_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    rating INTEGER,
    text TEXT,

    CONSTRAINT fk_product_review_customer
        FOREIGN KEY (customer_id) REFERENCES emarket.user(id),
    CONSTRAINT fk_product_review_product
        FOREIGN KEY (product_id) REFERENCES emarket.product(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.seller_review (
    id SERIAL PRIMARY KEY,

    customer_id INTEGER NOT NULL,
    seller_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    rating INTEGER,
    text TEXT,

    CONSTRAINT fk_seller_review_customer
        FOREIGN KEY (customer_id) REFERENCES emarket.user(id),
    CONSTRAINT fk_seller_review_seller
        FOREIGN KEY (seller_id) REFERENCES emarket.user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.product_review_image (
    id SERIAL PRIMARY KEY,

    image_url TEXT,

    CONSTRAINT fk_product_review_image_review
        FOREIGN KEY (id) REFERENCES emarket.product_review(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.seller_review_image (
    id SERIAL PRIMARY KEY,

    image_url TEXT,

    CONSTRAINT fk_seller_review_image_review
        FOREIGN KEY (id) REFERENCES emarket.seller_review(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emarket.address (
    id SERIAL PRIMARY KEY,

    province VARCHAR(40) NOT NULL,
    district VARCHAR(40) NOT NULL,
    street_and_number VARCHAR(40) NOT NULL,

    CONSTRAINT fk_address_user
        FOREIGN KEY (id) REFERENCES emarket.user(id) ON DELETE CASCADE
);