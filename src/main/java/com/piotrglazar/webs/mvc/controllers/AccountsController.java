package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDto;
import com.piotrglazar.webs.dto.PagerDto;
import com.piotrglazar.webs.dto.PagerDtoFactory;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.dto.WebsPageable;
import com.piotrglazar.webs.mvc.LoggedInUserProvider;
import com.piotrglazar.webs.mvc.forms.AccountCreationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountsController {

    private final AccountProvider accountProvider;
    private final LoggedInUserProvider loggedInUserProvider;
    private final MoneyTransferAuditProvider moneyTransferAuditProvider;
    private final PagerDtoFactory pagerDtoFactory;

    @Autowired
    public AccountsController(AccountProvider accountProvider, LoggedInUserProvider loggedInUserProvider,
                              MoneyTransferAuditProvider moneyTransferAuditProvider, PagerDtoFactory pagerDtoFactory) {
        this.accountProvider = accountProvider;
        this.loggedInUserProvider = loggedInUserProvider;
        this.moneyTransferAuditProvider = moneyTransferAuditProvider;
        this.pagerDtoFactory = pagerDtoFactory;
    }

    @RequestMapping("/accounts")
    public String accounts(final Model model) {
        final List<AccountDto> accountDtos = accountProvider.getUserAccounts(loggedInUserProvider.getLoggedInUserUsername());
        model.addAttribute("accounts", accountDtos);
        model.addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "accounts");
        return "accounts";
    }

    @RequestMapping("/accounts/{accountId}/")
    public String accountDetails(@PathVariable("accountId") final Long accountId, final Model model) {
        final Optional<SavingsAccountDto> account =
                accountProvider.getUserSavingsAccount(loggedInUserProvider.getLoggedInUserUsername(), accountId);
        if (account.isPresent()) {
            model.addAttribute("accountDetails", account.get());
            return "accountDetails";
        } else {
            return "redirect:/accounts";
        }
    }

    @RequestMapping(value = "/newAccount", method = RequestMethod.GET)
    public ModelAndView newAccount(final AccountCreationForm accountCreationForm) {
        final ModelAndView modelAndView = new ModelAndView("newAccount");
        modelAndView.addObject("accountCreationForm", accountCreationForm);
        modelAndView.addObject("allAccountTypes", AccountType.values());
        modelAndView.addObject("allCurrencies", Currency.values());
        return modelAndView;
    }

    @RequestMapping(value = "/newAccount", method = RequestMethod.POST)
    public String addAccount(@NotNull @Valid final AccountCreationForm accountCreationForm, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newAccount";
        }

        accountProvider.newAccount(loggedInUserProvider.getLoggedInUserUsername(), accountCreationForm.getType(),
                accountCreationForm.getCurrency());

        return "redirect:/accounts";
    }

    @RequestMapping(value = "/accountTransferHistory/{pageNumber}/", method = RequestMethod.GET)
    public String displayAccountTransferHistory(@PathVariable("pageNumber") final int pageNumber, final Model model) {
        final String loggedInUserUsername = loggedInUserProvider.getLoggedInUserUsername();
        final WebsPageable<MoneyTransferAuditUserDto> transferAuditData =
                moneyTransferAuditProvider.findPageForUsername(loggedInUserUsername, pageNumber);
        final List<PagerDto> pagers = pagerDtoFactory.createPagers(transferAuditData.getPageNumber(), transferAuditData.getPageCount(),
                "/accountTransferHistory/");
        final PagerDto leftPager = pagerDtoFactory.createLeftPager(transferAuditData.getPageNumber(), transferAuditData.isFirst(),
                "/accountTransferHistory/");
        final PagerDto rightPager = pagerDtoFactory.createRightPager(transferAuditData.getPageNumber(), transferAuditData.isLast(),
                "/accountTransferHistory/");

        model.addAttribute("moneyTransferAuditData", transferAuditData);
        model.addAttribute("pagers", pagers);
        model.addAttribute("leftPager", leftPager);
        model.addAttribute("rightPager", rightPager);

        return "accountTransferHistory";
    }
}
