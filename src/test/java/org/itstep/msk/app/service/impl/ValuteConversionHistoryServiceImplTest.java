package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.entity.ValuteConversion;
import org.itstep.msk.app.repository.ValuteConversionRepository;
import org.itstep.msk.app.service.ValuteConversionHistoryService;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ValuteConversionHistoryServiceImplTest {
    @Test
    public void getHistory() {
        User user = Mockito.mock(User.class);
        Pageable pageable = Mockito.mock(Pageable.class);

        ValuteConversion conversion1 = Mockito.mock(ValuteConversion.class);
        Mockito.when(conversion1.getId()).thenReturn(1);

        ValuteConversion conversion2 = Mockito.mock(ValuteConversion.class);
        Mockito.when(conversion2.getId()).thenReturn(2);

        List<ValuteConversion> content = new ArrayList<>();
        content.add(conversion1);
        content.add(conversion2);

        Page<ValuteConversion> expectedPage = new PageImpl<>(
                content,
                pageable,
                2L
        );

        ValuteConversionRepository conversionRepository = Mockito.mock(ValuteConversionRepository.class);
        Mockito
                .when(conversionRepository.findAllByUser(user, pageable))
                .thenReturn(expectedPage);

        ValuteConversionHistoryService service = new ValuteConversionHistoryServiceImpl(
                conversionRepository
        );

        Page<ValuteConversion> page = service.getHistory(user, pageable);

        assertSame(2L, page.getTotalElements());
        assertSame(expectedPage.getContent().size(), page.getContent().size());
        for (int i = 0; i < expectedPage.getSize(); i++) {
            assertSame(
                    expectedPage.getContent().get(i).getId(),
                    page.getContent().get(i).getId()
            );
        }
    }
}