package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.WebsNews;

import java.util.List;

public interface NewsImportingStrategy {

    void saveNews(NewsImporter newsImporter, List<? extends WebsNews> news);
}
