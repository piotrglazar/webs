package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.MoneyTransferAuditProvider;
import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.MoneyTransferAuditUserDto;
import com.piotrglazar.webs.dto.PagerDtoFactory;
import com.piotrglazar.webs.dto.PagersDto;
import com.piotrglazar.webs.dto.PrintableMoneyTransferAudit;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.dto.SubaccountDto;
import com.piotrglazar.webs.dto.UserDownloads;
import com.piotrglazar.webs.dto.WebsPageable;
import com.piotrglazar.webs.mvc.LoggedInUserProvider;
import com.piotrglazar.webs.mvc.forms.AccountCreationForm;
import com.piotrglazar.webs.mvc.forms.SubaccountCreationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import rx.Observable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountsController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
            LOG.info("No account with id {}", accountId);
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
        final PagersDto pagers = pagerDtoFactory.createPagers(transferAuditData.getPageNumber(), transferAuditData.getPageCount(),
                transferAuditData.isFirst(), transferAuditData.isLast(), "/accountTransferHistory/");

        model.addAttribute("moneyTransferAuditData", transferAuditData);
        model.addAttribute("pagers", pagers);

        return "accountTransferHistory";
    }

    @RequestMapping(value = "/downloadTransferHistory", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserDownloads moneyTransferAuditData() {
        final String loggedInUserUsername = loggedInUserProvider.getLoggedInUserUsername();
        final Observable<MoneyTransferAuditUserDto> transferHistory = moneyTransferAuditProvider.findTransferHistory(loggedInUserUsername);
        final List<String> auditData = transferHistory
                .map(PrintableMoneyTransferAudit::from)
                .map(PrintableMoneyTransferAudit::getMessage)
                .map(m -> m.concat("\n"))
                .toList()
                .toBlocking()
                .first();
        return new UserDownloads(loggedInUserUsername + ".txt", auditData);
    }

    @RequestMapping("/subaccounts/{accountId}/{subaccountName}/")
    public ModelAndView subaccountDetails(@PathVariable("accountId") Long accountId, @PathVariable("subaccountName") String subaccName) {
        final Optional<SubaccountDto> subaccount = accountProvider.getSubaccount(loggedInUserProvider.getLoggedInUserUsername(),
                accountId, subaccName);
        if (subaccount.isPresent()) {
            return new ModelAndView("subaccounts").addObject("subaccountDetails", subaccount.get());
        } else {
            LOG.info("No subaccount {} for account with id {}", subaccName, accountId);
            return new ModelAndView("redirect:/accounts/" + accountId + "/");
        }
    }

    @RequestMapping(value = "/subaccountsCreate/{accountId}/", method = RequestMethod.GET)
    public String newSubaccount(@PathVariable("accountId") final Long accountId, final SubaccountCreationForm subaccountCreationForm) {
        final Optional<SavingsAccountDto> account = accountProvider.getUserSavingsAccount(loggedInUserProvider.getLoggedInUserUsername(),
                accountId);
        if (account.isPresent()) {
            subaccountCreationForm.setAccountId(accountId);
            return "newSubaccount";
        } else {
            return "redirect:/accounts";
        }
    }

    @RequestMapping(value = "/subaccountsCreate/{accountId}/", method = RequestMethod.POST)
    public String createSubaccount(@PathVariable("accountId") final Long accountId, final SubaccountCreationForm subaccountCreationForm) {
        accountProvider.newSubaccount(loggedInUserProvider.getLoggedInUserUsername(), subaccountCreationForm.getAccountId(),
                subaccountCreationForm.getSubaccountAmount(), subaccountCreationForm.getSubaccountName());
        return "redirect:/accounts/" + accountId + "/";
    }

    @RequestMapping(value = "/subaccountsDelete/{accountId}/{subaccountName}/", method = RequestMethod.POST)
    public String deleteSubaccount(@PathVariable("accountId") Long accountId, @PathVariable("subaccountName") String subaccountName) {
        accountProvider.deleteSubaccount(loggedInUserProvider.getLoggedInUserUsername(), accountId, subaccountName);
        return "redirect:/accounts/" + accountId + "/";
    }
}
