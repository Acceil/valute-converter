package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.entity.ValuteConversion;
import org.itstep.msk.app.repository.ValuteConversionRepository;
import org.itstep.msk.app.service.ValuteConversionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ValuteConversionHistoryServiceImpl implements ValuteConversionHistoryService {
    private ValuteConversionRepository conversionRepository;

    @Autowired
    public ValuteConversionHistoryServiceImpl(ValuteConversionRepository conversionRepository) {
        this.conversionRepository = conversionRepository;
    }

    @Override
    public void save(User user, Valute valuteFrom, Valute valuteTo, Double value, Double result) {
        ValuteConversion conversion = new ValuteConversion();
        conversion.setUser(user);
        conversion.setValuteFrom(valuteFrom);
        conversion.setValue(value);
        conversion.setValuteTo(valuteTo);
        conversion.setResult(result);
        conversion.setDate(new Date());

        conversionRepository.save(conversion);
        conversionRepository.flush();
    }

    @Override
    public Page<ValuteConversion> getHistory(User user, Pageable pagination) {
        return conversionRepository.findAllByUser(user, pagination);
    }

    @Override
    public Page<ValuteConversion> getHistory(User user, Specification<ValuteConversion> specification, Pageable pagination) {
        return conversionRepository.findAllByUser(user, specification, pagination);
    }
}
