package org.itstep.msk.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    private JdbcTemplate template;

    @Autowired
    private NamedParameterJdbcTemplate namedTemplate;

    private class Valute {
        public Integer id;
        public String cbrId;
        public Integer numCode;
        public String charCode;
        public Integer nominal;
        public String name;
    }

    private class ValuteMapper implements RowMapper<Valute> {
        @Override
        public Valute mapRow(ResultSet resultSet, int i) throws SQLException {
            Valute valute = new Valute();
            valute.id = resultSet.getInt("id");
            valute.cbrId = resultSet.getString("cbr_id");
            valute.numCode = resultSet.getInt("num_code");
            valute.charCode = resultSet.getString("char_code");
            valute.nominal = resultSet.getInt("nominal");
            valute.name = resultSet.getString("name");

            return valute;
        }
    }

    @GetMapping("/test")
    public List<Valute> test() {
        // Получить коллекцию объектов
//        String query = "SELECT * FROM valutes";
//        List<Valute> result = template.query(query, new ValuteMapper());

        // Получить один объект
        // Количество записей
//        String query = "SELECT COUNT(*) FROM valutes";
//        Integer result = template.queryForObject(query, Integer.class);

        // Параметры
//        String query = "SELECT COUNT(*) FROM valutes WHERE nominal > ? ORDER BY nominal LIMIT ?";
//        Integer result = template.queryForObject(query, Integer.class, 10, 4);

        // SQL-инъекция
//        String name = "'; DELETE FROM valutes WHERE name != '";
//        String nameQuery =
//                "SELECT * FROM valutes WHERE name = '" + name + "'";
//        System.out.println(nameQuery);

//        String query = "SELECT * FROM valutes WHERE nominal > ? ORDER BY nominal LIMIT ?";
//        List<Valute> result = template.query(query, new ValuteMapper(), 10, 4);

        // Именованные параметры
        String query = "SELECT * FROM valutes WHERE nominal >= :nominal LIMIT :limit";
        Map<String, Object> params = new HashMap<>();
        params.put("nominal", 10);
        params.put("limit", 5);
        List<Valute> result = namedTemplate.query(query, params, new ValuteMapper());

//        String query = "SELECT * FROM valute_conversions " +
//                "WHERE valute_from_id IN (:from1, :from2, :from3) " +
//                "AND valute_to_id IN (:to1, :to2, :to3) " +
//                "AND conversion_date BETWEEN :dateFrom AND :dateTo " +
//                "AND (conversion_value > :valueFrom OR conversion_value < :valueTo)";
//        params.put("from1", 1);
//        ...
//        params.put("dateFrom", "2020-04-01");
//        ...
//        params.put("valueTo", 15);

        return result;
    }
}
