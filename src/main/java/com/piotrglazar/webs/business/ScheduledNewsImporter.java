package com.piotrglazar.webs.business;

import com.piotrglazar.webs.NewsImporters;
import com.piotrglazar.webs.config.BusinessConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ScheduledNewsImporter {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NewsImporters newsImporters;

    @Autowired
    public ScheduledNewsImporter(final NewsImporters newsImporters) {
        this.newsImporters = newsImporters;
    }

    @Scheduled(cron = BusinessConfiguration.NEWS_IMPORTING_CRON_EXPRESSION)
    public void importNews() {
        LOG.info("Started - fetching news");

        newsImporters.fetchAllNews();

        LOG.info("Ended - fetching news");
    }
}
