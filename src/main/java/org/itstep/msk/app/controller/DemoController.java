package org.itstep.msk.app.controller;

import org.itstep.msk.app.service.ValuteImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
    @Autowired
    private ValuteImportService valuteImportService;

    @GetMapping("/")
    public String index(Model model) {
        return "demo";
    }
}
