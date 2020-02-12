INSERT INTO valutes (cbr_id, num_code, char_code, nominal, name)
VALUES ("", 0, "RUB", 1, "Российсий рубль");

INSERT INTO rates (valute_id, rate_date, rate_value)
SELECT
    id AS valute_id, "2020-01-01" AS rate_date, 1 AS rate_value
FROM
    valutes
WHERE
    char_code = "RUB";
