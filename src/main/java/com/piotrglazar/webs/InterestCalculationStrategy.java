package com.piotrglazar.webs;

import java.math.BigDecimal;

public interface InterestCalculationStrategy {

    BigDecimal interest(BigDecimal amount, BigDecimal interestRate);
}
