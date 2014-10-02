package com.piotrglazar.webs;

import com.piotrglazar.webs.model.entities.WebsNews;

import java.util.Collection;

public interface WebsNewsProvider {

    Collection<WebsNews> getNews();

    void addNews(WebsNews news);

    void removeAll(Class<? extends WebsNews> newsType);

    void saveAll(Collection<? extends WebsNews> news);
}
