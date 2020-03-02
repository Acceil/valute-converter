CREATE TABLE valute_conversions (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    valute_from_id INT NOT NULL REFERENCES valutes (id),
    valute_to_id INT NOT NULL REFERENCES valutes (id),
    conversion_date TIMESTAMP NOT NULL,
    conversion_value DOUBLE NOT NULL,
    conversion_result DOUBLE NOT NULL
);
