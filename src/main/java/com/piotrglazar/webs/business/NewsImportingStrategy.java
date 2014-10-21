package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.entities.WebsNews;
import rx.Observable;

public interface NewsImportingStrategy {

    void saveNews(NewsImporter newsImporter, Observable<? extends WebsNews> news);

    void saveAllNews(Observable<? extends WebsNews> allNews);
}
