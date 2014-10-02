package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.entities.WebsNews;

import java.util.Collection;

public interface NewsImportingStrategy {

    void saveNews(NewsImporter newsImporter, Collection<? extends WebsNews> news);
}
