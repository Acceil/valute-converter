ALTER TABLE users
ADD COLUMN avatar_id INT NULL REFERENCES uploads (id);
