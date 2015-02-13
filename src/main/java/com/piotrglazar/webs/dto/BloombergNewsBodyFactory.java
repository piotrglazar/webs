package com.piotrglazar.webs.dto;

import org.springframework.stereotype.Component;

@Component
public class BloombergNewsBodyFactory {

    public BloombergNewsBody createNews(String name, String priceChange, String price, String percentChange) {
        final boolean isUp = isUp(priceChange);

        return new BloombergNewsBody(name, priceChange, isUp, price, percentChange);
    }

    private boolean isUp(final String growth) {
        return growth.contains("+");
    }
}
