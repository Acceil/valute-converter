package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.entity.ValuteConversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ValuteConversionHistoryService {
    void save(User user, Valute valuteFrom, Valute valuteTo, Double value, Double result);

    Page<ValuteConversion> getHistory(User user, Pageable pagination);

    Page<ValuteConversion> getHistory(User user, Specification<ValuteConversion> specification, Pageable pagination);
}
