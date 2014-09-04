package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.converters.IntegerListConverter;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.ConvertParam;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class PagerDtoFactoryTest {

    @Test
    @Parameters({
            "0 | [0;1;2;3;4]",
            "1 | [0;1;2;3;4]",
            "2 | [0;1;2;3;4]",
    })
    public void shouldConstructLeftBorderForPageCountGreaterThanPagerSize(int pageNumber,
                    @ConvertParam(IntegerListConverter.class) List<Integer> expectedPageIds) {
        // given
        final int pagerSize = 5;
        final int pageCount = 10;
        final PagerDtoFactory factory = new PagerDtoFactory(pagerSize);

        // when
        final List<PagerDto> pagers = factory.createPagers(pageNumber, pageCount, "abc");

        // then
        assertThat(pagers)
                .hasSize(pagerSize)
                .extracting("currentPage")
                    .containsOnly(expectedPageIds.toArray());
    }

    @Test
    @Parameters({
            "9 | [5;6;7;8;9]",
            "8 | [5;6;7;8;9]",
            "7 | [5;6;7;8;9]",
    })
    public void shouldConstructRightBorderForPageCountGreaterThanPagerSize(int pageNumber,
                   @ConvertParam(IntegerListConverter.class) List<Integer> expectedPageIds) {
        // given
        final int pagerSize = 5;
        final int pageCount = 10;
        final PagerDtoFactory factory = new PagerDtoFactory(pagerSize);

        // when
        final List<PagerDto> pagers = factory.createPagers(pageNumber, pageCount, "abc");

        // then
        assertThat(pagers)
                .hasSize(pagerSize)
                .extracting("currentPage")
                    .containsOnly(expectedPageIds.toArray());
    }

    @Test
    @Parameters({
            "3 | [1;2;3;4;5]",
            "4 | [2;3;4;5;6]",
            "5 | [3;4;5;6;7]",
            "6 | [4;5;6;7;8]"
    })
    public void shouldConstructMiddlePagerForPageCountGreaterThanPagerSize(int pageNumber,
                   @ConvertParam(IntegerListConverter.class) List<Integer> expectedPageIds) {
        // given
        final int pagerSize = 5;
        final int pageCount = 10;
        final PagerDtoFactory factory = new PagerDtoFactory(pagerSize);

        // when
        final List<PagerDto> pagers = factory.createPagers(pageNumber, pageCount, "abc");

        // then
        assertThat(pagers)
                .hasSize(pagerSize)
                .extracting("currentPage")
                    .containsOnly(expectedPageIds.toArray());
    }

    @Test
    @Parameters({
            "0 | [0;1]",
            "1 | [0;1]",
    })
    public void shouldConstructPagerForPageCountSmallerThanPagerSize(int pageNumber,
                  @ConvertParam(IntegerListConverter.class) List<Integer> expectedPageIds) {
        // given
        final int pagerSize = 3;
        final int pageCount = 2;
        final PagerDtoFactory factory = new PagerDtoFactory(pagerSize);

        // when
        final List<PagerDto> pagers = factory.createPagers(pageNumber, pageCount, "abc");

        // then
        assertThat(pagers)
                .hasSize(pageCount)
                .extracting("currentPage")
                .containsOnly(expectedPageIds.toArray());
    }

    @Test
    @Parameters({
            "3, true, abc | 0, #",
            "3, false, abc | 2, abc2/"
    })
    public void shouldConstructLeftPager(int pageNumber, boolean isFirst, String url, int expectedPageNumber, String expectedUrl) {
        // given
        final PagerDtoFactory factory = new PagerDtoFactory(10);

        // when
        final PagerDto leftPager = factory.createLeftPager(pageNumber, isFirst, url);

        // then
        assertThat(leftPager.getUrl()).isEqualTo(expectedUrl);
        assertThat(leftPager.getCurrentPage()).isEqualTo(expectedPageNumber);
    }

    @Test
    @Parameters({
            "3, true, abc | 0, #",
            "3, false, abc | 4, abc4/"
    })
    public void shouldConstructRightPager(int pageNumber, boolean isLast, String url, int expectedPageNumber, String expectedUrl) {
        // given
        final PagerDtoFactory factory = new PagerDtoFactory(10);

        // when
        final PagerDto rightPager = factory.createRightPager(pageNumber, isLast, url);

        // then
        assertThat(rightPager.getUrl()).isEqualTo(expectedUrl);
        assertThat(rightPager.getCurrentPage()).isEqualTo(expectedPageNumber);
    }
}