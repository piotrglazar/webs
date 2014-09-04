package com.piotrglazar.webs.dto;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PagerDtoTest {

    @Test
    public void shouldPageWithNumber0BeDisplayedAsPageWithNumber1OnUi() {
        // given
        final int pageNumber = 0;

        // when
        final PagerDto pagerDto = new PagerDto(pageNumber, "");

        // then
        assertThat(pagerDto.getCurrentPage()).isEqualTo(pageNumber);
        assertThat(pagerDto.getCurrentPageForUi()).isEqualTo(pageNumber + 1);
    }

    @Test
    public void shouldEmptyPagerHaveDefaultUrlAndPageNumber() {
        // when
        final PagerDto emptyPager = PagerDto.emptyPager();

        // then
        assertThat(emptyPager.getCurrentPage()).isEqualTo(0);
        assertThat(emptyPager.getUrl()).isEqualTo("#");
    }
}