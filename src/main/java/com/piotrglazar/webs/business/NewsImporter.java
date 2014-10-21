package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.entities.WebsNews;
import rx.Observable;

public interface NewsImporter {

    Observable<? extends WebsNews> fetchNews();

    Class<? extends WebsNews> provides();

    String name();
}
