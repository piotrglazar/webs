package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.business.utils.Currency;
import com.piotrglazar.webs.model.entities.Account;
import com.piotrglazar.webs.util.MoreCollectors;

import java.math.BigDecimal;
import java.util.List;

public class AccountDto {

    private final Long id;
    private final String number;
    private final BigDecimal balance;
    private final Currency currency;
    private final List<SubaccountDto> subaccounts;
    private final BigDecimal totalBalance;

    public AccountDto(final Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.currency = account.getCurrency();
        this.subaccounts = subaccounts(account);
        this.totalBalance = account.getTotalBalance();
    }

    private List<SubaccountDto> subaccounts(final Account account) {
        return account.getSubaccounts().stream()
                .map(subaccount -> new SubaccountDto(subaccount.getName(), subaccount.getBalance(), currency))
                .collect(MoreCollectors.toImmutableList());
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public List<SubaccountDto> getSubaccounts() {
        return subaccounts;
    }
}
