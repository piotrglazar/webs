package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.model.WebsNews;

public class NewsDto {

    private String headline;
    private String body;
    private String url;
    private String urlText;
    private String imgContent;

    public NewsDto(final WebsNews websNews) {
        this.headline = websNews.getHeadline();
        this.body = websNews.getBody();
        this.url = websNews.getUrl();
        this.urlText = websNews.getUrlText();
        this.imgContent = websNews.getImgContent();
    }

    public String getHeadline() {
        return headline;
    }

    public String getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlText() {
        return urlText;
    }

    public String getImgContent() {
        return imgContent;
    }
}
