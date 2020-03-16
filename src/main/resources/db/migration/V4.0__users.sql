CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id INT NOT NULL REFERENCES users (id),
    role VARCHAR(100) NOT NULL,
    PRIMARY KEY (user_id, role)
);
