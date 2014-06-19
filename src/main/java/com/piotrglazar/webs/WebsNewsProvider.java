package com.piotrglazar.webs;

import com.piotrglazar.webs.model.WebsNews;

import java.util.Collection;
import java.util.List;

public interface WebsNewsProvider {

    Collection<WebsNews> getNews();

    void addNews(WebsNews news);

    void removeAll(Class<? extends WebsNews> newsType);

    void saveAll(List<? extends WebsNews> news);
}
