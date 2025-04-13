-- Insert users
INSERT INTO emarket.user (id, username, image_url) VALUES
   (1000, 'admin', NULL),
   (2000, 'merchant', NULL),
   (3000, 'customer', NULL);

-- Insert passwords
INSERT INTO emarket.password (id, text, updated_at) VALUES
    (1000, '$2b$12$NHV0A8e6nz1LCKmLF9mjC.2ZibHkEGIMiY8.lh8c77nxDvNtkUNnW', NOW()),
    (2000, '$2b$12$EY.Aqv57XBIp5Bim4DDnsO7nZJvbW9ZSOWGxINentNSkyOLrq6t6S', NOW()),
    (3000, '$2b$12$4kmKPKZYNh1SCT9sjLHW4.b0.h/J2GCRACq5FHLR1rRJOkgFkVDTu', NOW());

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