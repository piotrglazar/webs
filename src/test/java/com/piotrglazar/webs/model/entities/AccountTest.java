package com.piotrglazar.webs.model.entities;

import com.piotrglazar.webs.business.utils.AccountType;
import com.piotrglazar.webs.business.utils.Currency;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {

    @Test
    public void shouldIncludeSubaccountsBalanceIntoTotalBalance() {
        // given
        final Subaccount firstSubaccount = subaccountWithBalance(BigDecimal.valueOf(65));
        final Subaccount secondSubaccount = subaccountWithBalance(BigDecimal.valueOf(35));
        final Account testAccount = new TestAccount(BigDecimal.valueOf(100), firstSubaccount, secondSubaccount);

        // when
        final BigDecimal balance = testAccount.getBalance();
        final BigDecimal totalBalance = testAccount.getTotalBalance();

        // then
        assertThat(balance).isEqualByComparingTo("100");
        assertThat(totalBalance).isEqualByComparingTo("200");
    }

    @Test
    public void shouldTotalBalanceBeEqualToBalanceWhenThereAreNoSubaccounts() {
        // given
        final Account testAccount = new TestAccount(BigDecimal.valueOf(123));

        // when
        final BigDecimal balance = testAccount.getBalance();
        final BigDecimal totalBalance = testAccount.getTotalBalance();

        // then
        assertThat(balance).isEqualByComparingTo("123");
        assertThat(totalBalance).isEqualByComparingTo("123");
    }

    private static final class TestAccount extends Account {

        public TestAccount(BigDecimal balance, Subaccount... subaccounts) {
            super("number", Currency.GBP, balance);
            Arrays.stream(subaccounts).forEach(this::addSubaccount);
        }

        @Override
        public AccountType accountType() {
            return null;
        }
    }

    private Subaccount subaccountWithBalance(BigDecimal balance) {
        return new Subaccount("name", balance);
    }
}
