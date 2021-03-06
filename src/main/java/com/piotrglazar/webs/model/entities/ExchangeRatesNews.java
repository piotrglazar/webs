package com.piotrglazar.webs.model.entities;

import javax.persistence.Entity;

@Entity
public final class ExchangeRatesNews extends WebsNews {

    public ExchangeRatesNews() {

    }

    public ExchangeRatesNews(String headline, String body) {
        super(headline, body, "#", "Latest exchange rates", "#777:#7a7a7a/text:ExchangeRates");
    }

    public static String getNewsName() {
        return "ExchangeRatesNews";
    }
}
