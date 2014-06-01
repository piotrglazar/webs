package com.piotrglazar.webs.business;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MoneyAmountBuilder {

    public BigDecimal fromIntegralAndFractionalParts(long integralPart, long fractionalPart) {
        Preconditions.checkArgument(fractionalPart >= 0 && fractionalPart <= 99, "fractionalPart must be between 0 and 99");

        final long rawAmount = integralPart * 100 + fractionalPart;
        final BigDecimal amount = new BigDecimal(rawAmount).movePointLeft(2);
        return amount;
    }
}
