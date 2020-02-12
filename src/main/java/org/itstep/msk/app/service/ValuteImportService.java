package org.itstep.msk.app.service;

import java.util.GregorianCalendar;

public interface ValuteImportService {
    void importValutes();

    void importValutes(GregorianCalendar date);
}
