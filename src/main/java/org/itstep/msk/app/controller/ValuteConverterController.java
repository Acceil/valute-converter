package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.entity.ValuteConversion;
import org.itstep.msk.app.exception.NotFoundException;
import org.itstep.msk.app.model.ValuteConversionFilter;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.repository.ValuteConversionRepository;
import org.itstep.msk.app.repository.ValuteRepository;
import org.itstep.msk.app.service.ValuteConversionHistoryService;
import org.itstep.msk.app.service.ValuteConvertService;
import org.itstep.msk.app.service.ValuteImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class ValuteConverterController {
    final private static int VALUTES_PAGE_SIZE = 10;
    final private static int CONVERSIONS_PAGE_SIZE = 20;

    @Autowired
    private ValuteImportService valuteImportService;

    @Autowired
    private ValuteConvertService valuteConvertService;

    @Autowired
    private ValuteConversionHistoryService historyService;

    @Autowired
    private ValuteRepository valuteRepository;

    @Autowired
    private UserRepository userRepository;

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
            @RequestParam String value,
            Principal principal
    ) {
        Double result = valuteConvertService.convert(
                valuteFrom,
                valuteTo,
                Double.valueOf(value)
        );

        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName());
            historyService.save(
                    user,
                    valuteFrom,
                    valuteTo,
                    Double.valueOf(value),
                    result
            );
        }

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
        model.addAttribute("valutesPage", valutes);

        return "valutes";
    }

    @GetMapping("/conversions")
    public String conversions(
            Model model,
            @PageableDefault(
                    page = 0,
                    size = CONVERSIONS_PAGE_SIZE,
                    sort = "date",
                    direction = Sort.Direction.DESC
            )
            Pageable pagination,
            @ModelAttribute ValuteConversionFilter filter,
            Principal principal
    ) {
        User user = userRepository.findByEmail(principal.getName());

        Specification<ValuteConversion> specification = null;

        if (filter.getValuteFrom() != null) {
            specification = ValuteConversionRepository.hasValuteFrom(filter.getValuteFrom());
        }

        if (filter.getValuteTo() != null) {
            specification = specification == null
                    ? ValuteConversionRepository.hasValuteTo(filter.getValuteTo())
                    : specification.and(
                            ValuteConversionRepository.hasValuteTo(filter.getValuteTo())
                    );
        }

        int valueFrom = filter.getValueFrom() != null ? filter.getValueFrom() : -1;
        int valueTo = filter.getValueTo() != null ? filter.getValueTo() : -1;

        int resultFrom = filter.getResultFrom() != null ? filter.getResultFrom() : -1;
        int resultTo = filter.getResultTo() != null ? filter.getResultTo() : -1;

        if (valueFrom >= 0 || valueTo >= 0) {
            specification = specification == null
                    ? ValuteConversionRepository.hasValue(valueFrom, valueTo)
                    : specification.and(
                            ValuteConversionRepository.hasValue(valueFrom, valueTo)
                    );
        }

        if (resultFrom >= 0 || resultTo >= 0) {
            specification = specification == null
                    ? ValuteConversionRepository.hasResult(resultFrom, resultTo)
                    : specification.and(
                            ValuteConversionRepository.hasResult(resultFrom, resultTo)
                    );
        }

        Page<ValuteConversion> conversions = historyService.getHistory(
                user,
                specification,
                pagination
        );

        int page = pagination.getPageNumber() + 1;

        if (conversions.getTotalPages() < page || page < 1) {
            throw new NotFoundException();
        }

        model.addAttribute("conversions", conversions.getContent());
        model.addAttribute("pages", conversions.getTotalPages());
        model.addAttribute("total", conversions.getTotalElements());
        model.addAttribute("pagination", pagination);
        model.addAttribute("valutes", valuteRepository.findAll(
                Sort.by("name")
        ));
        model.addAttribute("filter", filter);

        return "conversions";
    }
}
