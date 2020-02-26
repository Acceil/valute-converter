package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.exception.NotFoundException;
import org.itstep.msk.app.repository.ValuteRepository;
import org.itstep.msk.app.service.ValuteConvertService;
import org.itstep.msk.app.service.ValuteImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ValuteConverterController {
    final private static int VALUTES_PAGE_SIZE = 10;

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

    // http://localhost:9999/valutes?page=3
    @GetMapping("/valutes")
    public String valutes(
            Model model,
            @RequestParam(required = false, defaultValue = "1")
            Integer page
    ) {
        // Объект с настройками пагинации
        Pageable pagination = PageRequest.of(
                page - 1, // Номер страницы (начинается с 0)
                VALUTES_PAGE_SIZE, // Кол-во объектов на странице
                Sort.by("name").ascending() // Сортировка
        );
        // "Страница" с валютами
        Page<Valute> valutes = valuteRepository.findAll(pagination);

        if (valutes.getTotalPages() < page || page < 1) {
            throw new NotFoundException();
        }

        // Коллекция валют
        model.addAttribute("valutes", valutes.getContent());
        // Текущий номер страницы
        model.addAttribute("page", valutes.getNumber() + 1);
        // Общее кол-во страниц
        model.addAttribute("pages", valutes.getTotalPages());
        // Общее число валют
        model.addAttribute("total", valutes.getTotalElements());

        return "valutes";
    }
}
