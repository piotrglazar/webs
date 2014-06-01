package com.piotrglazar.webs.mvc;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.dto.MoneyTransferAuditDto;
import com.piotrglazar.webs.model.MoneyTransferAudit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

    @Mock
    private Model model;

    @Mock
    private MoneyTransferAuditProvider provider;

    @InjectMocks
    private AdminController controller;

    @Test
    public void shouldDisplayAllMoneyTransferAuditToDto() {
        // given
        final List<MoneyTransferAuditDto> dtos = someMoneyTransferAuditDtos();
        given(provider.findAll()).willReturn(dtos);

        // when
        controller.showMoneyTransferAudit(model);

        // then
        verify(model).addAttribute("moneyTransferAudits", dtos);
    }

    private List<MoneyTransferAuditDto> someMoneyTransferAuditDtos() {
        return Lists.newArrayList(
                new MoneyTransferAuditDto(new MoneyTransferAudit(1L, 10L, 11L, BigDecimal.TEN, true, LocalDateTime.of(2014, 5, 25, 22, 3))),
                new MoneyTransferAuditDto(new MoneyTransferAudit(5L, 20L, 25L, BigDecimal.ONE, false, LocalDateTime.of(2014, 5, 2, 2, 3))));
    }
}