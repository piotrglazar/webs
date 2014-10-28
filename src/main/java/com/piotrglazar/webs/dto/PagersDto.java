package com.piotrglazar.webs.dto;

import com.google.common.collect.ImmutableList;

import javax.annotation.concurrent.Immutable;
import java.util.List;

@Immutable
public final class PagersDto {

    private final PagerDto left;
    private final List<PagerDto> middle;
    private final PagerDto right;

    public PagersDto(PagerDto left, List<PagerDto> middle, PagerDto right) {
        this.left = left;
        this.middle = ImmutableList.copyOf(middle);
        this.right = right;
    }

    public PagerDto getLeft() {
        return left;
    }

    public List<PagerDto> getMiddle() {
        return middle;
    }

    public PagerDto getRight() {
        return right;
    }
}
