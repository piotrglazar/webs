package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.entities.WebsNews;

import java.util.List;

public interface NewsImporter {

    List<? extends WebsNews> fetchNews();

    Class<? extends WebsNews> provides();

    String name();
}
