ALTER TABLE users
ADD COLUMN active BIT NOT NULL DEFAULT 0,
ADD COLUMN confirm_code VARCHAR(255) NOT NULL DEFAULT "";

UPDATE users SET active = 1 WHERE email = "admin";
