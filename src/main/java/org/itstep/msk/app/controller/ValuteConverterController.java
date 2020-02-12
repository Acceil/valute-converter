package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.repository.ValuteRepository;
import org.itstep.msk.app.service.ValuteConvertService;
import org.itstep.msk.app.service.ValuteImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ValuteConverterController {
    @Autowired
    private ValuteImportService valuteImportService;

    @Autowired
    private ValuteConvertService valuteConvertService;

    @Autowired
    private ValuteRepository valuteRepository;

    @GetMapping("/")
    public String index(Model model) {
        valuteImportService.importValutes();

//        Double result = valuteConvertService.convert(
//                valuteRepository.findByCharCode("CAD"),
//                valuteRepository.findByCharCode("USD"),
//                150.0
//        );
//
//        DecimalFormat df = new DecimalFormat("#.####");
//        model.addAttribute("result", df.format(result));

        List<Valute> valutes = valuteRepository.findAll(
                Sort.by("name")
        );
        model.addAttribute("valutes", valutes);

        return "index";
    }

    @PostMapping("/converter")
    @ResponseBody
    public Double converter(
            @RequestParam Valute fromValute,
            @RequestParam Valute toValute,
            @RequestParam String value
    ) {
        return valuteConvertService.convert(
                fromValute,
                toValute,
                Double.valueOf(value)
        );
    }
}
