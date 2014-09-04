package com.piotrglazar.webs.dto;

import com.google.common.collect.ImmutableList;
import org.springframework.data.domain.Page;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Immutable
public final class WebsPageable<T> {

    private final List<T> content;
    private final boolean first;
    private final boolean last;
    private final int pageNumber;
    private final int pageCount;

    public WebsPageable(Page<T> page) {
        this(page.getContent(), page.isFirst(), page.isLast(), page.getNumber(), page.getTotalPages());
    }

    private WebsPageable(List<T> content, boolean first, boolean last, int pageNumber, int pageCount) {
        this.content = ImmutableList.copyOf(content);
        this.first = first;
        this.last = last;
        this.pageNumber = pageNumber;
        this.pageCount = pageCount;
    }

    public <S> WebsPageable<S> transform(Function<? super T, ? extends S> function) {
        final List<S> transformedContent = content.stream().map(function).collect(Collectors.toList());
        return new WebsPageable<>(transformedContent, first, last, pageNumber, pageCount);
    }

    public boolean hasContent() {
        return !content.isEmpty();
    }

    public List<T> getContent() {
        return content;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageCount() {
        return pageCount;
    }
}
