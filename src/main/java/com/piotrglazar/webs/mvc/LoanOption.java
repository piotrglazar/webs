package com.piotrglazar.webs.mvc;

import java.math.BigDecimal;

public enum LoanOption {

    SHORT((byte) 4, new BigDecimal("5.0")),

    MEDIUM((byte) 8, new BigDecimal("8.0")),

    LONG((byte) 12, new BigDecimal("10.0"));

    private byte weeks;

    private BigDecimal percentage;

    private LoanOption(byte weeks, BigDecimal percentage) {
        this.weeks = weeks;
        this.percentage = percentage;
    }

    public byte getWeeks() {
        return weeks;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public String getDescription() {
        return String.format("%s weeks, %s %%", weeks, percentage);
    }
}
