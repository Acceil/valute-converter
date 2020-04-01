package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.Rate;
import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.repository.RateRepository;
import org.itstep.msk.app.service.ValuteConvertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ValuteConvertServiceImplTest {
    private Integer valuteFromNominal;
    private Double rateFromValue;
    private Integer valuteToNominal;
    private Double rateToValue;
    private Double value;
    private Double expectedResult;

    public ValuteConvertServiceImplTest(
            Integer valuteFromNominal,
            Double rateFromValue,
            Integer valuteToNominal,
            Double rateToValue,
            Double value,
            Double expectedResult
    ) {
        this.valuteFromNominal = valuteFromNominal;
        this.rateFromValue = rateFromValue;
        this.valuteToNominal = valuteToNominal;
        this.rateToValue = rateToValue;
        this.value = value;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[] {1, 30.0, 1, 1.0, 5.0, 150.0});
        data.add(new Object[] {100, 50.0, 1, 1.0, 1000.0, 500.0});

        return data;
    }

    @Test
    public void convert() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Arrange
        Valute valuteFrom = Mockito.mock(Valute.class);
        Mockito.when(valuteFrom.getId()).thenReturn(1);
        Mockito.when(valuteFrom.getNominal()).thenReturn(valuteFromNominal);
        Rate rateFrom = new Rate();
        rateFrom.setValute(valuteFrom);
        rateFrom.setValue(rateFromValue);

        Valute valuteTo = Mockito.mock(Valute.class);
        Mockito.when(valuteTo.getId()).thenReturn(2);
        Mockito.when(valuteTo.getNominal()).thenReturn(valuteToNominal);
        Rate rateTo = new Rate();
        rateTo.setValute(valuteTo);
        rateTo.setValue(rateToValue);

        RateRepository rateRepository = Mockito.mock(RateRepository.class);
        Mockito.when(
                rateRepository.findFirstByValuteOrderByDateDesc(valuteFrom)
        ).thenReturn(rateFrom);
        Mockito.when(
                rateRepository.findFirstByValuteOrderByDateDesc(valuteTo)
        ).thenReturn(rateTo);

        ValuteConvertService service = new ValuteConvertServiceImpl(
                rateRepository
        );

        // Act
        Double result = service.convert(valuteFrom, valuteTo, value);

        // Assert
        assertEquals(expectedResult, result); // *.equals()

//        Object[] data = {1, 30.0, 1, 1.0, 5.0, 150.0};
//        ValuteConvertServiceImplTest.class
//                .getConstructor(Integer.class, Double.class, Integer.class, Double.class, Double.class, Double.class)
//                .newInstance(data);
    }
}
