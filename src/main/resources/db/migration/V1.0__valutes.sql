CREATE TABLE valutes (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cbr_id VARCHAR(50) NOT NULL UNIQUE,
    num_code INT NOT NULL,
    char_code VARCHAR(3) NOT NULL,
    nominal INT NOT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE rates (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    valute_id INT NOT NULL REFERENCES valutes (id),
    rate_date DATE NOT NULL,
    rate_value DOUBLE NOT NULL,
    UNIQUE INDEX valute_rate_date (valute_id, rate_date)
);
