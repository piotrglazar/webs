package com.piotrglazar.webs.business;

import com.piotrglazar.webs.dto.NewsDto;
import org.assertj.core.api.Assertions;

public final class NewsDtoAssert {

    private final NewsDto that;

    private NewsDtoAssert(final NewsDto that) {
        this.that = that;
    }

    public static NewsDtoAssert assertThat(NewsDto that) {
        return new NewsDtoAssert(that);
    }

    public NewsDtoAssert hasHeadline(String headline) {
        Assertions.assertThat(that.getHeadline()).isEqualTo(headline);
        return this;
    }

    public NewsDtoAssert hasBody(String body) {
        Assertions.assertThat(that.getBody()).isEqualTo(body);
        return this;
    }

    public NewsDtoAssert hasUrl(String url) {
        Assertions.assertThat(that.getUrl()).isEqualTo(url);
        return this;
    }

    public NewsDtoAssert hasUrlText(String urlText) {
        Assertions.assertThat(that.getUrlText()).isEqualTo(urlText);
        return this;
    }

    public NewsDtoAssert hasImgContent(String imgContent) {
        Assertions.assertThat(that.getImgContent()).isEqualTo(imgContent);
        return this;
    }
}
