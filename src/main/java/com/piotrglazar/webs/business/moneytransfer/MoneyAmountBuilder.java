package com.piotrglazar.webs.business.moneytransfer;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.piotrglazar.webs.config.BusinessConfiguration.INTEGRAL_PART_SHIFT;
import static com.piotrglazar.webs.config.BusinessConfiguration.MAXIMUM_FRACTION_NUMBER;

@Component
public class MoneyAmountBuilder {

    public BigDecimal fromIntegralAndFractionalParts(long integralPart, long fractionalPart) {
        Preconditions.checkArgument(fractionalPart >= 0 && fractionalPart <= MAXIMUM_FRACTION_NUMBER,
                "fractionalPart must be between 0 and 99");

        final long rawAmount = integralPart * INTEGRAL_PART_SHIFT + fractionalPart;
        return new BigDecimal(rawAmount).movePointLeft(2);
    }
}
