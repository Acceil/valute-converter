package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.entity.ValuteConversion;
import org.itstep.msk.app.repository.ValuteConversionRepository;
import org.itstep.msk.app.service.ValuteConversionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ValuteConversionHistoryServiceImpl implements ValuteConversionHistoryService {
    private ValuteConversionRepository conversionRepository;

    @Autowired
    public ValuteConversionHistoryServiceImpl(ValuteConversionRepository conversionRepository) {
        this.conversionRepository = conversionRepository;
    }

    @Override
    public void save(Valute valuteFrom, Valute valuteTo, Double value, Double result) {
        ValuteConversion conversion = new ValuteConversion();
        conversion.setValuteFrom(valuteFrom);
        conversion.setValue(value);
        conversion.setValuteTo(valuteTo);
        conversion.setResult(result);
        conversion.setDate(new Date());

        conversionRepository.save(conversion);
        conversionRepository.flush();
    }

    @Override
    public List<ValuteConversion> getHistory() {
        return null;
    }
}
