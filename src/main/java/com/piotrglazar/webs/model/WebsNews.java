package com.piotrglazar.webs.model;

import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public final class WebsNews {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String headline;

    private String body;

    private String url;

    private String urlText;

    private String imgContent;

    public WebsNews() {

    }

    public WebsNews(String headline, String body, String url, String urlText, String imgContent) {
        this.headline = headline;
        this.body = body;
        this.url = url;
        this.urlText = urlText;
        this.imgContent = imgContent;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final WebsNews websNews = (WebsNews) o;

        return Objects.equal(id, websNews.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(final String headline) {
        this.headline = headline;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getUrlText() {
        return urlText;
    }

    public void setUrlText(final String urlText) {
        this.urlText = urlText;
    }

    public String getImgContent() {
        return imgContent;
    }

    public void setImgContent(final String imgContent) {
        this.imgContent = imgContent;
    }

    public static WebsNewsBuilder builder() {
        return new WebsNewsBuilder();
    }

    public Long getId() {
        return id;
    }
}
