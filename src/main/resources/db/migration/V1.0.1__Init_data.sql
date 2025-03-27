-- Insert users
INSERT INTO emarket.user (id, username, image_url) VALUES
   (1000, 'admin', NULL),
   (2000, 'merchant', NULL),
   (3000, 'customer', NULL);

-- Insert passwords
INSERT INTO emarket.password (id, text, updated_at) VALUES
    (1000, '$2b$12$wWpuvflxLi7SbIT2bcYQkOElPueQMkberbZOyf/PGv2uCBk/PCuLq', NOW()),
    (2000, '$2b$12$FoTEGsalDG8Z1uFS6KdKMeC1gWWGOpHMmvQFOZ16O41u8JnpD2jOe', NOW()),
    (3000, '$2a$12$UOnAlNIAZ0zcRJt5t7vGT.UM8k7kOcxUgs7FzL1pAVbSSllINHIsG', NOW());

-- Insert profiles
INSERT INTO emarket.profile (id, age, authority) VALUES
    (1000, 30, 'ROLE_ADMIN'),
    (2000, 25, 'ROLE_MERCHANT'),
    (3000, 20, 'ROLE_CUSTOMER');

-- Insert user addresses
INSERT INTO emarket.user_address (id, province, district, street_and_number) VALUES
    (1000, 'admin', 'amin', 'admin street'),
    (2000, 'CA', 'Los Angeles', 'Hollywood Boulevard'),
    (3000, 'TX', 'Houston', 'NASA Parkway');