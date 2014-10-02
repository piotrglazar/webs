package com.piotrglazar.webs.model.entities;

import javax.persistence.Entity;

@Entity
public final class BloombergNews extends WebsNews {

    public BloombergNews() {

    }

    public BloombergNews(String headline, String body, String url, String urlText, String imgContent) {
        super(headline, body, url, urlText, imgContent);
    }

    public static String getNewsName() {
        return "BloombergNews";
    }
}
