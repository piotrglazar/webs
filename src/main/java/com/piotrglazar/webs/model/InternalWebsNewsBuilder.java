package com.piotrglazar.webs.model;

public class InternalWebsNewsBuilder {
    private String headline = "";
    private String body = "";
    private String url = "";
    private String urlText = "";
    private String imgContent = "";

    public InternalWebsNewsBuilder headline(final String headline) {
        this.headline = headline;
        return this;
    }

    public InternalWebsNewsBuilder body(final String body) {
        this.body = body;
        return this;
    }

    public InternalWebsNewsBuilder url(final String url) {
        this.url = url;
        return this;
    }

    public InternalWebsNewsBuilder urlText(final String urlText) {
        this.urlText = urlText;
        return this;
    }

    public InternalWebsNewsBuilder imgContent(final String imgContent) {
        this.imgContent = imgContent;
        return this;
    }

    public InternalWebsNews build() {
        return new InternalWebsNews(headline, body, url, urlText, imgContent);
    }
}
