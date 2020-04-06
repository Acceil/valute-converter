CREATE TABLE uploads (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    original_filename VARCHAR(2550) NOT NULL,
    content_type VARCHAR(255) NOT NULL
);
