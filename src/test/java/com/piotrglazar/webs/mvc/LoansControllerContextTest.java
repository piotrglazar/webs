package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.LoanProvider;
import com.piotrglazar.webs.commons.Utils;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.config.Settings;
import com.piotrglazar.webs.model.entities.Loan;
import com.piotrglazar.webs.model.repositories.LoanRepository;
import com.piotrglazar.webs.mvc.forms.LoanCreationForm;
import com.piotrglazar.webs.util.LoanOption;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

public class LoansControllerContextTest extends AbstractContextTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanProvider loanProvider;

    @Test
    public void shouldDisplayActiveLoans() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/loans").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(model().attribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "loans"))
            .andExpect(model().attributeExists("loans"));
    }

    @Test
    public void shouldDisplayArchiveLoans() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/archiveLoans").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(model().attribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "loans"))
            .andExpect(model().attributeExists("archiveLoans"));
    }

    @Test
    public void shouldDisplayLoanCreationForm() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(get("/newLoan").session(authenticate))

        // then
            .andExpect(status().isOk())
            .andExpect(model().attribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "loans"))
            .andExpect(model().attributeExists("loanCreationForm", "allLoanOptions", "allAccounts"))
            .andExpect(xpath("//input[@id='amountLoaned']").exists());
    }

    @Test
    public void shouldCreateLoan() throws Exception {
        // given
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(Utils.addCsrf(post("/newLoan/").session(authenticate))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amountLoaned", "1000")
                .param("accountId", "1")
                .param("loanOption", LoanOption.LONG.name()))
        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/loans"));

        assertThat(loanRepository.count()).isEqualTo(1);

        // cleanup
        loanRepository.deleteAll();
    }

    @Test
    public void shouldPostponeLoan() throws Exception {
        // given
        final Long loanId = createLoan();
        final MockHttpSession authenticate = Utils.authenticate(mockMvc);

        // when
        mockMvc.perform(Utils.addCsrf(post("/postponeLoan/" + loanId + "/").session(authenticate)))

        // then
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andExpect(redirectedUrl("/loans"));

        final Loan loan = loanRepository.findOne(loanId);
        assertThat(loan.getPostpones()).hasSize(1);

        // cleanup
        loanRepository.deleteAll();
    }

    private Long createLoan() {
        final LoanCreationForm form = new LoanCreationForm();
        form.setAmountLoaned(new BigDecimal("1000"));
        form.setLoanOption(LoanOption.LONG);
        form.setAccountId(1L);
        loanProvider.createLoan(form, Settings.USERNAME);

        final List<Loan> allLoans = loanRepository.findAll();
        assertThat(allLoans).hasSize(1);
        return allLoans.get(0).getId();
    }
}
