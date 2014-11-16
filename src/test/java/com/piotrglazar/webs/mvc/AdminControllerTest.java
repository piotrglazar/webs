package com.piotrglazar.webs.mvc;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.NewsImporters;
import com.piotrglazar.webs.business.InterestAccruer;
import com.piotrglazar.webs.business.LoanRepays;
import com.piotrglazar.webs.dto.MoneyTransferAuditAdminDto;
import com.piotrglazar.webs.model.entities.MoneyTransferAudit;
import com.piotrglazar.webs.mvc.controllers.AdminController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.piotrglazar.webs.business.utils.Currency.GBP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

    @Mock
    private LoanRepays loanRepays;

    @Mock
    private InterestAccruer interestAccruer;

    @Mock
    private NewsImporters newsImporters;

    @Mock
    private Model model;

    @Mock
    private MoneyTransferAuditProvider provider;

    @InjectMocks
    private AdminController controller;

    @Test
    public void shouldDisplayAllMoneyTransferAuditDtoAndNewsImporters() {
        // given
        final List<MoneyTransferAuditAdminDto> dtos = someMoneyTransferAuditDtos();
        final List<String> newsImporterNames = Lists.newArrayList("A", "B", "C");
        given(provider.findAll()).willReturn(dtos);
        given(newsImporters.getNewsImportersNames()).willReturn(newsImporterNames);

        // when
        controller.showMoneyTransferAudit(model);

        // then
        verify(model).addAttribute("moneyTransferAudits", dtos);
        verify(model).addAttribute("newsImporters", newsImporterNames);
    }

    @Test
    public void shouldRedirectToItselfAfterFetchingNews() {
        // when
        final String view = controller.importNews(0, model);

        // then
        assertThat(view).contains("admin");
        assertThat(model.addAttribute(eq("moneyTransferAudits"), anyObject()));
        assertThat(model.addAttribute(eq("newsImporters"), anyObject()));
        // successful message is shown
        assertThat(model.addAttribute("uiMessage", "News successfully imported"));
    }

    @Test
    public void shouldFetchNews() {
        // when
        controller.importNews(0, model);

        // then
        verify(newsImporters).fetchNews(0);
    }

    @Test
    public void shouldAccrueInterest() {
        // when
        controller.accrueInterest(model);

        // then
        verify(interestAccruer).accrueInterest();
    }

    @Test
    public void shouldRedirectToItselfAfterAccruingInterest() {
        // when
        final String view = controller.accrueInterest(model);

        // then
        assertThat(view).contains("admin");
        assertThat(model.addAttribute(eq("moneyTransferAudits"), anyObject()));
        assertThat(model.addAttribute(eq("newsImporters"), anyObject()));
        // successful message is shown
        assertThat(model.addAttribute("uiMessage", "Interest accrued successfully"));
    }

    @Test
    public void shouldRepayLoans() {
        // when
        controller.repayLoans(model);

        // then
        verify(loanRepays).repayAllLoans();
    }

    @Test
    public void shouldRedirectToItselfAfterRepayingLoans() {
        // when
        final String view = controller.repayLoans(model);

        // then
        assertThat(view).contains("admin");
        assertThat(model.addAttribute(eq("moneyTransferAudits"), anyObject()));
        assertThat(model.addAttribute(eq("newsImporters"), anyObject()));
        // successful message is shown
        assertThat(model.addAttribute("uiMessage", "Loans repaid successfully"));
    }

    @Test
    public void shouldImportAllNews() {
        // when
        controller.importAllNews(model);

        // then
        verify(newsImporters).fetchAllNews();
    }

    private List<MoneyTransferAuditAdminDto> someMoneyTransferAuditDtos() {
        return Lists.newArrayList(
                new MoneyTransferAuditAdminDto(
                        new MoneyTransferAudit(1L, 10L, 11L, BigDecimal.TEN, true, LocalDateTime.of(2014, 5, 25, 22, 3), 100L, GBP)),
                new MoneyTransferAuditAdminDto(
                        new MoneyTransferAudit(5L, 20L, 25L, BigDecimal.ONE, false, LocalDateTime.of(2014, 5, 2, 2, 3), 200L, GBP)));
    }
}
