package com.piotrglazar.webs.business;

import com.piotrglazar.webs.business.utils.Currency;
import org.assertj.core.api.Assertions;

public class MoneyTransferParamsAssert {

    private final MoneyTransferParams that;

    private MoneyTransferParamsAssert(final MoneyTransferParams that) {
        this.that = that;
    }

    public static MoneyTransferParamsAssert assertThat(MoneyTransferParams that) {
        return new MoneyTransferParamsAssert(that);
    }

    public MoneyTransferParamsAssert hasFromAccount(long accountId) {
        Assertions.assertThat(that.getFromAccount()).isEqualTo(accountId);
        return this;
    }

    public MoneyTransferParamsAssert hasToAccount(long accountId) {
        Assertions.assertThat(that.getToAccount()).isEqualTo(accountId);
        return this;
    }

    public MoneyTransferParamsAssert hasUsername(String username) {
        Assertions.assertThat(that.getUsername()).isEqualTo(username);
        return this;
    }

    public MoneyTransferParamsAssert hasAmount(String amount) {
        Assertions.assertThat(that.getAmount()).isEqualByComparingTo(amount);
        return this;
    }

    public MoneyTransferParamsAssert hasReceivingUserId(long userId) {
        Assertions.assertThat(that.getReceivingUserId()).isEqualTo(userId);
        return this;
    }

    public MoneyTransferParamsAssert hasCurrency(Currency currency) {
        Assertions.assertThat(that.getCurrency()).isEqualTo(currency);
        return this;
    }
}
