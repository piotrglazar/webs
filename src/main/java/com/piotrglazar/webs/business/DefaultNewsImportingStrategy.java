package com.piotrglazar.webs.business;

import com.google.common.base.Preconditions;
import com.piotrglazar.webs.WebsNewsProvider;
import com.piotrglazar.webs.model.WebsNews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

@Component
class DefaultNewsImportingStrategy implements NewsImportingStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final WebsNewsProvider websNewsProvider;

    @Autowired
    public DefaultNewsImportingStrategy(final WebsNewsProvider websNewsProvider) {
        this.websNewsProvider = websNewsProvider;
    }

    @Override
    public void saveNews(final NewsImporter newsImporter, final Collection<? extends WebsNews> news) {
        Preconditions.checkArgument(!news.isEmpty(), "No news to save.");

        websNewsProvider.removeAll(newsImporter.provides());
        websNewsProvider.saveAll(news);

        LOG.debug("NewsImporter {} saved {}", newsImporter, news);
    }
}
