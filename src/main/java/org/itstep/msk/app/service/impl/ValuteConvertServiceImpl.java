package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.Rate;
import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.repository.RateRepository;
import org.itstep.msk.app.service.ValuteConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValuteConvertServiceImpl implements ValuteConvertService {
    private RateRepository rateRepository;

    @Autowired
    public ValuteConvertServiceImpl(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    @Override
    public Double convert(Valute fromValute, Valute toValute, Double value) {
        if (fromValute.equals(toValute)) {
            return value;
        }

        Rate fromRate = rateRepository.findFirstByValuteOrderByDateDesc(fromValute);
        Rate toRate = rateRepository.findFirstByValuteOrderByDateDesc(toValute);

        if (fromRate == null || toRate == null) {
            return -2.0;
        }

        if (fromValute.getNominal().equals(0)
                || toRate.getValue().equals(0.0)
        ) {
            return -2.0;
        }

        return value
                * fromRate.getValue()
                * toValute.getNominal()
                / fromValute.getNominal()
                / toRate.getValue();
    }
}
