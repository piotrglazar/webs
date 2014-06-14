package com.piotrglazar.webs.business;

import com.piotrglazar.webs.dto.NewsDto;

import java.util.Collection;

public interface UiNewsProvider {

    Collection<NewsDto> getNews();
}
