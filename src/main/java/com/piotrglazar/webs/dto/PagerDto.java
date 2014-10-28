package com.piotrglazar.webs.dto;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(currentPage, url);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final PagerDto other = (PagerDto) obj;
        return Objects.equals(this.currentPage, other.currentPage) && Objects.equals(this.url, other.url);
    }
}
