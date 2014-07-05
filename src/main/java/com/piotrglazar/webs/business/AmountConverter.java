package com.piotrglazar.webs.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class AmountConverter {

    private final int scale;
    private final RoundingMode roundingMode;

    @Autowired
    public AmountConverter(@Value("#{businessProperties['money.scale']?:2}") int scale,
                           @Value("#{businessProperties['money.rounding.type']?:HALF_UP}") String roundingMode) {
        this.scale = scale;
        this.roundingMode = RoundingMode.valueOf(roundingMode);
    }

    public BigDecimal convert(BigDecimal amount) {
        return amount.setScale(scale, roundingMode);
    }

    public int getScale() {
        return scale;
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }
}
