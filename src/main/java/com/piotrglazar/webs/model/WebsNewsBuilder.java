package com.piotrglazar.webs.model;

public class WebsNewsBuilder {
    private String headline = "";
    private String body = "";
    private String url = "";
    private String urlText = "";
    private String imgContent = "";

    public WebsNewsBuilder headline(final String headline) {
        this.headline = headline;
        return this;
    }

    public WebsNewsBuilder body(final String body) {
        this.body = body;
        return this;
    }

    public WebsNewsBuilder url(final String url) {
        this.url = url;
        return this;
    }

    public WebsNewsBuilder urlText(final String urlText) {
        this.urlText = urlText;
        return this;
    }

    public WebsNewsBuilder imgContent(final String imgContent) {
        this.imgContent = imgContent;
        return this;
    }

    public WebsNews build() {
        return new WebsNews(headline, body, url, urlText, imgContent);
    }
}
