package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.Valute;

public interface ValuteConvertService {
    Double convert(Valute fromValute, Valute toValute, Double value);
}
