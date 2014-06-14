package com.piotrglazar.webs;

import com.piotrglazar.webs.model.WebsNews;

import java.util.Collection;

public interface WebsNewsProvider {

    Collection<WebsNews> getNews();

    void addNews(WebsNews news);
}
