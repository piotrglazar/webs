package com.piotrglazar.webs.business;

import org.springframework.stereotype.Component;

@Component
public class BloombergNewsBodyFactory {

    public BloombergNewsBody createNews(String name, String growth, String price) {
        final boolean isUp = isUp(growth);

        return new BloombergNewsBody(name, growth, isUp, price);
    }

    private boolean isUp(final String growth) {
        return growth.contains("+");
    }
}
