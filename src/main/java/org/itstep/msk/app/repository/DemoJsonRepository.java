package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.DemoJson;

import java.util.ArrayList;
import java.util.List;

public class DemoJsonRepository {
    public List<DemoJson> findAll() {
        List<DemoJson> list = new ArrayList<>();
        DemoJson demo = new DemoJson();
        demo.setId(1);
        demo.setTitle("Title 1");
        list.add(demo);

        demo = new DemoJson();
        demo.setId(2);
        demo.setTitle("Title 2");
        list.add(demo);

        demo = new DemoJson();
        demo.setId(3);
        demo.setTitle("Title 3");
        list.add(demo);

        return list;
    }

    public DemoJson getOne(Integer id) {
        return findAll().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
