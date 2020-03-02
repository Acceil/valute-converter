package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.exception.NotFoundException;
import org.itstep.msk.app.repository.ValuteRepository;
import org.itstep.msk.app.service.ValuteConversionHistoryService;
import org.itstep.msk.app.service.ValuteConvertService;
import org.itstep.msk.app.service.ValuteImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ValuteConverterController {
    final private static int VALUTES_PAGE_SIZE = 10;

    @Autowired
    private ValuteImportService valuteImportService;

    @Autowired
    private ValuteConvertService valuteConvertService;

    @Autowired
    private ValuteConversionHistoryService historyService;

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
            @RequestParam Valute valuteFrom,
            @RequestParam Valute valuteTo,
            @RequestParam String value
    ) {
        Double result = valuteConvertService.convert(
                valuteFrom,
                valuteTo,
                Double.valueOf(value)
        );

        historyService.save(
                valuteFrom,
                valuteTo,
                Double.valueOf(value),
                result
        );

        return result;
    }

    // http://localhost:9999/valutes?page=3
    @GetMapping("/valutes")
    public String valutes(
            Model model,
            @PageableDefault(
                    page = 0,
                    size = VALUTES_PAGE_SIZE,
                    sort = "name",
                    direction = Sort.Direction.ASC
            )
            // Объект с настройками пагинации
            Pageable pagination
    ) {
        // "Страница" с валютами
        Page<Valute> valutes = valuteRepository.findAll(pagination);

        int page = pagination.getPageNumber() + 1;

        if (valutes.getTotalPages() < page || page < 1) {
            throw new NotFoundException();
        }

        // Коллекция валют
        model.addAttribute("valutes", valutes.getContent());
        // Общее кол-во страниц
        model.addAttribute("pages", valutes.getTotalPages());
        // Общее число валют
        model.addAttribute("total", valutes.getTotalElements());
        model.addAttribute("pagination", pagination);

        return "valutes";
    }
}
