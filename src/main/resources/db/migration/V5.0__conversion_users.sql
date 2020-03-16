TRUNCATE TABLE valute_conversions;

ALTER TABLE valute_conversions
ADD COLUMN user_id INT NOT NULL REFERENCES users (id);
