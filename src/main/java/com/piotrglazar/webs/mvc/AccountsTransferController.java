package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.AccountProvider;
import com.piotrglazar.webs.business.AccountMoneyTransfer;
import com.piotrglazar.webs.business.MoneyTransferParams;
import com.piotrglazar.webs.business.MoneyTransferParamsFactory;
import com.piotrglazar.webs.dto.SavingsAccountDto;
import com.piotrglazar.webs.mvc.validators.TransferFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AccountsTransferController {

    private final AccountProvider accountProvider;

    private final TransferFormValidator validator;

    private final AccountMoneyTransfer accountMoneyTransfer;

    private final MoneyTransferParamsFactory factory;

    private final LoggedInUserProvider loggedInUserProvider;

    @Autowired
    public AccountsTransferController(AccountProvider accountProvider, TransferFormValidator validator,
                                      AccountMoneyTransfer accountMoneyTransfer, MoneyTransferParamsFactory factory,
                                      LoggedInUserProvider loggedInUserProvider) {
        this.accountProvider = accountProvider;
        this.validator = validator;
        this.accountMoneyTransfer = accountMoneyTransfer;
        this.factory = factory;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    @InitBinder
    public void initBinder(final DataBinder dataBinder) {
        dataBinder.addValidators(validator);
    }

    @RequestMapping(value = "/accountsTransfer/{accountId}/", method = RequestMethod.GET)
    public String showAccountsTransfer(@PathVariable("accountId") final Long accountId, final TransferForm transferForm) {
        if (userAccessesNotHisAccount(accountId) || violationAttempt(accountId, transferForm)) {
            return "redirect:/accounts";
        }

        transferForm.setAccountId(accountId);
        return "accountsTransfer";
    }

    @RequestMapping(value = "/accountsTransfer/{accountId}/", method = RequestMethod.POST)
    public String doTransfer(@PathVariable("accountId") final Long accountId, @Valid final TransferForm transferForm,
                             final BindingResult bindingResult) {
        if (userAccessesNotHisAccount(accountId) || violationAttempt(accountId, transferForm)) {
            return "redirect:/accounts";
        }

        if (bindingResult.hasErrors()) {
            return "accountsTransfer";
        }

        final MoneyTransferParams moneyTransferParams = factory.create(loggedInUserProvider.getLoggedInUser().getUsername(), transferForm);

        accountMoneyTransfer.transferMoney(moneyTransferParams);

        return "redirect:/accounts/{accountId}/";
    }

    private boolean userAccessesNotHisAccount(final Long accountId) {
        final Optional<SavingsAccountDto> account = accountProvider.getUserSavingsAccount(getUsername(), accountId);
        return !account.isPresent();
    }

    private String getUsername() {
        return loggedInUserProvider.getLoggedInUser().getUsername();
    }

    private boolean violationAttempt(final Long accountId, final TransferForm transferForm) {
        return accountId.longValue() != transferForm.getAccountId();
    }
}
