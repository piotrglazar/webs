package com.piotrglazar.webs.business;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyAmountBuilderTest {

    private MoneyAmountBuilder moneyAmountBuilder = new MoneyAmountBuilder();

    @Test
    public void shouldConstructBigDecimalFromFractionalAndIntegralPart() {
        // when
        final BigDecimal amount = moneyAmountBuilder.fromIntegralAndFractionalParts(123, 45);

        // then
        assertThat(amount).isEqualByComparingTo("123.45");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenFractionalPartIsGreaterThan99() {
        // expect
        moneyAmountBuilder.fromIntegralAndFractionalParts(123, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenFractionalPartIsLessThan0() {
        // expect
        moneyAmountBuilder.fromIntegralAndFractionalParts(123, -1);
    }
}
