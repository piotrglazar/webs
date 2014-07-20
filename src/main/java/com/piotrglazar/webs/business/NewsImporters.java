package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.WebsNews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NewsImporters {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final List<NewsImporter> newsImporters;

    private final NewsImportingStrategy newsImportingStrategy;

    @Autowired
    public NewsImporters(final List<NewsImporter> newsImporters, final NewsImportingStrategy newsImportingStrategy) {
        this.newsImporters = newsImporters;
        this.newsImportingStrategy = newsImportingStrategy;
    }

    public List<String> getNewsImportersNames() {
        return newsImporters.stream().map(NewsImporter::name).collect(Collectors.toList());
    }

    public void fetchNews(final int newsImporterIndex) {
        final NewsImporter newsImporter = getNewsImporter(newsImporterIndex);
        final List<? extends WebsNews> news = newsImporter.fetchNews();

        LOG.info("NewsImporter {} fetching news {}", newsImporter, news);

        newsImportingStrategy.saveNews(newsImporter, news);
    }

    private NewsImporter getNewsImporter(final int newsImporterIndex) {
        if (newsImporterIndex >= 0 && newsImporterIndex < newsImporters.size()) {
            return newsImporters.get(newsImporterIndex);
        } else {
            throw new ArrayIndexOutOfBoundsException(String.format("Illegal news importer index %s, current size is %s", newsImporterIndex,
                    newsImporters.size()));
        }
    }

    public void fetchAllNews() {
        for (int i = 0; i < newsImporters.size(); ++i) {
            fetchNews(i);
        }
    }
}
