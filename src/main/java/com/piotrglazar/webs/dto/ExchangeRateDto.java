package com.piotrglazar.webs.dto;

import com.google.common.collect.ImmutableMap;
import com.piotrglazar.webs.business.ExchangeRateResponse;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExchangeRateDto {

    private final String base;
    private final Map<String, BigDecimal> rates;

    public ExchangeRateDto(final String base, final Map<String, BigDecimal> rates) {
        this.base = base;
        this.rates = ImmutableMap.copyOf(rates);
    }

    public static ExchangeRateDto from(ExchangeRateResponse response, Predicate<? super Map.Entry<String, BigDecimal>> ratesFilter) {
        final Map<String, BigDecimal> rates = response.ratesAndValues().stream()
                .filter(ratesFilter::test)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ExchangeRateDto(response.getBase(), rates);
    }

    public String getBase() {
        return base;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }
}
