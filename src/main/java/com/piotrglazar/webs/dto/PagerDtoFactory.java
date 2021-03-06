package com.piotrglazar.webs.dto;

import com.piotrglazar.webs.util.MoreCollectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class PagerDtoFactory {

    private final int pagerSize;
    private final int borderSize;

    @Autowired
    public PagerDtoFactory(@Value("#{businessProperties['pager.size']?:2}") int pagerSize) {
        this.pagerSize = pagerSize;
        this.borderSize = pagerSize / 2;
    }

    public PagersDto createPagers(int pageNumber, int pageCount, boolean isFirst, boolean isLast, String url) {
        final PagerDto leftPager = createLeftPager(pageNumber, isFirst, url);
        final PagerDto rightPager = createRightPager(pageNumber, isLast, url);
        final List<PagerDto> middlePagers = createMiddlePagers(pageNumber, pageCount, url);

        return new PagersDto(leftPager, middlePagers, rightPager);
    }

    public List<PagerDto> createMiddlePagers(int pageNumber, int pageCount, String url) {
        if (isSinglePiecePager(pageCount, pagerSize)) {
            return singlePiecePager(pageCount, url);
        } else if (isLeftBorder(pageNumber, borderSize)) {
            return constructLeftBorder(pageCount, pagerSize, url);
        } else if (isRightBorder(pageNumber, pageCount, borderSize)) {
            return constructRightBorder(pageCount, pagerSize, url);
        } else {
            return constructMiddle(pageNumber, borderSize, pagerSize, url);
        }
    }

    private List<PagerDto> singlePiecePager(final int pageCount, final String url) {
        return IntStream.range(0, pageCount).mapToObj(i -> new PagerDto(i, url)).collect(MoreCollectors.toImmutableList());
    }

    private boolean isSinglePiecePager(final int pageCount, final int pagerSize) {
        return pageCount <= pagerSize;
    }

    private List<PagerDto> constructMiddle(final int pageNumber, final int borderSize, final int pagerSize, final String url) {
        final int start = pageNumber - borderSize;
        final int end = start + pagerSize;
        return IntStream.range(start, end).mapToObj(i -> new PagerDto(i, url)).collect(MoreCollectors.toImmutableList());
    }

    private List<PagerDto> constructRightBorder(final int pageCount, final int size, final String url) {
        return IntStream.range(pageCount - size, pageCount).mapToObj(i -> new PagerDto(i, url)).collect(MoreCollectors.toImmutableList());
    }

    private boolean isRightBorder(final int pageNumber, final int pageCount, final int size) {
        return pageCount < pageNumber + size + 1;
    }

    private List<PagerDto> constructLeftBorder(final int pageCount, final int size, final String url) {
        final int howMany = Math.min(pageCount, size);
        return IntStream.range(0, howMany).mapToObj(i -> new PagerDto(i, url)).collect(MoreCollectors.toImmutableList());
    }

    private boolean isLeftBorder(final int pageNumber, final int size) {
        return pageNumber < size;
    }

    public PagerDto createLeftPager(final int pageNumber, final boolean isFirst, final String url) {
        if (isFirst) {
            return PagerDto.emptyPager();
        } else {
            return new PagerDto(pageNumber - 1, url);
        }
    }

    public PagerDto createRightPager(final int pageNumber, final boolean isLast, final String url) {
        if (isLast) {
            return PagerDto.emptyPager();
        } else {
            return new PagerDto(pageNumber + 1, url);
        }
    }
}
