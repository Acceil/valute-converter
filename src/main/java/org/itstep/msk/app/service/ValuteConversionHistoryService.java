package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.entity.ValuteConversion;

import java.util.List;

public interface ValuteConversionHistoryService {
    void save(Valute valuteFrom, Valute valuteTo, Double value, Double result);

    List<ValuteConversion> getHistory();
}
