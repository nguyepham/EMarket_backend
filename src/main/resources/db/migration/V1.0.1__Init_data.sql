-- Insert users
INSERT INTO emarket.user (id, username, image_url) VALUES
   (1, 'admin', NULL),
   (2, 'user1', NULL),
   (3, 'user2', NULL);

-- Insert passwords
INSERT INTO emarket.password (id, text, updated_at) VALUES
    (1, '$2b$12$y/Mro7OppORA93qe4FX0ZegFzzODl3B81vhJ76ylkNxSUC1JgwCQq', NOW()),
    (2, '$2b$12$FX7jPmstGhucj3PAwRocFuNNn/kyDV4DbJ3Pn08Y/mvGy.tP.D3Ma', NOW()),
    (3, '$2b$12$zTMUBZlP2/.RbzemLMmV3O4sX/wqfRoPmfrFaIqc56amAdLFZFxqu', NOW());

-- Insert profiles
INSERT INTO emarket.profile (id, age, authority) VALUES
    (1, 30, 'ROLE_ADMIN'),
    (2, 25, 'ROLE_USER'),
    (3, 20, 'ROLE_USER');

-- Insert user addresses
INSERT INTO emarket.address (id, province, district, street_and_number) VALUES
    (1, 'admin', 'amin', 'admin street'),
    (2, 'CA', 'Los Angeles', 'Hollywood Boulevard'),
    (3, 'TX', 'Houston', 'NASA Parkway');

-- Insert default category
INSERT INTO emarket.category (id, name, level, path, parent_name) VALUES
    (1, 'Electronics', 1, '/Electronics/', 'default'),
    (2, 'Phone', 2, '/Electronics/Phone/', 'Electronics'),
    (3, 'Clothing', 1, '/Clothing/', 'default'),
    (4, 'Shoes', 2, '/Clothing/Shoes/', 'Clothing'),
    (5, 'Smartphone', 3, '/Electronics/Phone/Smartphone', 'Phone'),
    (6, 'Laptop', 2, '/Electronics/Laptop', 'Electronics'),
    (7, 'T-shirt', 2, '/Clothing/T-shirt/', 'Clothing'),
    (8, 'Jeans', 2, '/Clothing/Jeans/', 'Clothing'),
    (9, 'Sneakers', 3, '/Clothing/Shoes/Sneakers/', 'Shoes'),
    (10, 'Boots', 3, '/Clothing/Shoes/Boots/', 'Shoes');

-- Insert products with correct schema
INSERT INTO emarket.product (
    id, seller_username, name, cover_image_url, unit_price,
    updated_at, short_description, description, qty_in_stock
) VALUES
    (1, 'user1', 'iPhone 13', NULL, 79900, NOW(), 'Apple A15 smartphone', 'Apple smartphone with A15 Bionic chip and OLED display.', 50),
    (2, 'user1', 'Nike Air Max', NULL, 12000, NOW(), 'Nike running shoes', 'Popular and comfortable running shoes by Nike.', 100),
    (3, 'user1', 'Samsung Galaxy S21', NULL, 69900, NOW(), 'Flagship Samsung phone', 'Top-tier Android phone with Snapdragon processor.', 75),
    (4, 'user2', 'Levi Jeans', NULL, 5999, NOW(), 'Classic blue jeans', 'Comfortable and durable blue denim jeans.', 200),
    (5, 'user1', 'Adidas Sneakers', NULL, 9000, NOW(), 'Stylish sneakers', 'Adidas sneakers for everyday use.', 150),
    (6, 'user2', 'Timberland Boots', NULL, 14000, NOW(), 'Durable boots', 'Rugged leather boots perfect for hiking and outdoor work.', 80),
    (7, 'user1', 'Dell XPS 13', NULL, 99900, NOW(), 'Compact laptop', '13-inch ultrabook with Intel i7 and 16GB RAM.', 60),
    (8, 'user1', 'Uniqlo T-Shirt', NULL, 1999, NOW(), 'Cotton T-shirt', 'Soft and breathable cotton T-shirt for everyday wear.', 300),
    (9, 'user2', 'Converse All Star', NULL, 4500, NOW(), 'Classic sneakers', 'Iconic Converse sneakers in various colors.', 120);

-- Insert category relationships
INSERT INTO emarket.category_product (id, product_id, category_id) VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 2, 3),
    (4, 2, 4),
    (5, 3, 1),
    (6, 3, 2),
    (7, 3, 5),
    (8, 4, 3),
    (9, 4, 8),
    (10, 5, 3),
    (11, 5, 4),
    (12, 5, 9),
    (13, 6, 3),
    (14, 6, 4),
    (15, 6, 10),
    (16, 7, 1),
    (17, 7, 6),
    (18, 8, 3),
    (19, 8, 7),
    (20, 9, 3),
    (21, 9, 4),
    (22, 9, 9);