package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.dto.AccountDto;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
