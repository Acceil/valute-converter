CREATE TABLE likes (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users (user_id)
);

CREATE TABLE valute_likes (
    id INT NOT NULL PRIMARY KEY REFERENCES likes (id),
    valute_id INT NOT NULL REFERENCES valutes (id)
);

CREATE TABLE conversion_likes (
    id INT NOT NULL PRIMARY KEY REFERENCES likes (id),
    conversion_id INT NOT NULL REFERENCES valute_conversions (id)
);
