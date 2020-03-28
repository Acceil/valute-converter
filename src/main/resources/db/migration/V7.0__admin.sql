INSERT INTO users (email, password)
VALUES ("admin", "$2a$10$VYWBkrfWdt6Q5kONlLkJkeMZ9nAXkNaC8GUgSD1SBQdSbQebh8J62");

INSERT INTO user_roles (user_id, role)
SELECT id, "ROLE_USER"
FROM users
WHERE email = "admin";

INSERT INTO user_roles (user_id, role)
SELECT id, "ROLE_ADMIN"
FROM users
WHERE email = "admin";
