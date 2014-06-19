package com.piotrglazar.webs.model;

import javax.persistence.Entity;

@Entity
public final class InternalWebsNews extends WebsNews {

    public InternalWebsNews() {

    }

    public InternalWebsNews(String headline, String body, String url, String urlText, String imgContent) {
        super(headline, body, url, urlText, imgContent);
    }

    public static InternalWebsNewsBuilder builder() {
        return new InternalWebsNewsBuilder();
    }
}
