package com.piotrglazar.webs.dto;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class PagerDto {

    private final int currentPage;
    private final String url;

    private PagerDto() {
        this.currentPage = 0;
        this.url = "#";
    }

    public PagerDto(int currentPage, String url) {
        this.currentPage = currentPage;
        this.url = url + currentPage + "/";
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getUrl() {
        return url;
    }

    public int getCurrentPageForUi() {
        return currentPage + 1;
    }

    public static PagerDto emptyPager() {
        return new PagerDto();
    }
}
