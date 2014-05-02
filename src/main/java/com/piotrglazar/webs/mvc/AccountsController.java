package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.model.AccountType;
import com.piotrglazar.webs.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountsController {

    private final AccountProvider accountProvider;

    private final LoggedInUserProvider loggedInUserProvider;

    @Autowired
    public AccountsController(final AccountProvider accountProvider, final LoggedInUserProvider loggedInUserProvider) {
        this.accountProvider = accountProvider;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    @RequestMapping("/accounts")
    public String accounts(final Model model) {
        final List<AccountDto> accountDtos = accountProvider.getUserAccounts(getUsername());
        model.addAttribute("accounts", accountDtos);
        model.addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "accounts");
        return "accounts";
    }

    private String getUsername() {
        return loggedInUserProvider.getLoggedInUser().getUsername();
    }

    @RequestMapping("/accounts/{accountId}/")
    public String accountDetails(@PathVariable("accountId") final Long accountId, final Model model) {
        final Optional<SavingsAccountDto> account = accountProvider.getUserSavingsAccount(getUsername(), accountId);
        if (account.isPresent()) {
            model.addAttribute("accountDetails", account.get());
            return "accountDetails";
        } else {
            return "redirect:/accounts";
        }
    }

    @RequestMapping(value = "/newAccount/", method = RequestMethod.GET)
    public ModelAndView newAccount(final AccountCreationForm newAccountForm) {
        final ModelAndView modelAndView = new ModelAndView("newAccount");
        modelAndView.addObject("newAccountForm", newAccountForm);
        modelAndView.addObject("allAccountTypes", AccountType.values());
        modelAndView.addObject("allCurrencies", Currency.values());
        return modelAndView;
    }

    @RequestMapping(value = "/newAccount/", method = RequestMethod.POST)
    public String addAccount(@Valid final AccountCreationForm form, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newAccount";
        }

        accountProvider.newAccount(getUsername(), form.getType(), form.getCurrency());

        return "redirect:/accounts";
    }
}
