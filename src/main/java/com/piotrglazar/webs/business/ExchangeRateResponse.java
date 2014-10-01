package com.piotrglazar.webs.business;

import com.google.common.collect.ImmutableMap;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class ExchangeRateResponse {

    private String disclaimer;
    private String license;
    private long timestamp;
    private String base;
    private Map<String, BigDecimal> rates;

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(final String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(final String license) {
        this.license = license;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(final String base) {
        this.base = base;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(final Map<String, BigDecimal> rates) {
        this.rates = ImmutableMap.copyOf(rates);
    }

    public Set<Map.Entry<String, BigDecimal>> ratesAndValues() {
        return rates.entrySet();
    }
}
