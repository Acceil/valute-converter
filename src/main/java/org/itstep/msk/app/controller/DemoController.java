package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.DemoJson;
import org.itstep.msk.app.repository.DemoJsonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {
    @GetMapping("/demo")
    public String demo() {
        return "demo";
    }

    @PostMapping("/demo")
    public @ResponseBody DemoJson handle(@RequestBody DemoJson demo) {
        DemoJsonRepository repository = new DemoJsonRepository();
        DemoJson result = repository.getOne(demo.getId());

        if (result == null) {
            result = demo;
        }

        return result;
    }
}
