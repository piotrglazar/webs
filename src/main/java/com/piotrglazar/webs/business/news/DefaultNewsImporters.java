package com.piotrglazar.webs.business.news;

import com.piotrglazar.webs.NewsImporters;
import com.piotrglazar.webs.business.NewsImporter;
import com.piotrglazar.webs.business.NewsImportingStrategy;
import com.piotrglazar.webs.model.entities.WebsNews;
import com.piotrglazar.webs.util.MoreCollectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class DefaultNewsImporters implements NewsImporters {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final List<NewsImporter> newsImporters;

    private final NewsImportingStrategy newsImportingStrategy;

    @Autowired
    public DefaultNewsImporters(final List<NewsImporter> newsImporters, final NewsImportingStrategy newsImportingStrategy) {
        this.newsImporters = newsImporters;
        this.newsImportingStrategy = newsImportingStrategy;
    }

    @Override
    public List<String> getNewsImportersNames() {
        return newsImporters.stream().map(NewsImporter::name).collect(MoreCollectors.toImmutableList());
    }

    @Override
    public void fetchNews(final int newsImporterIndex) {
        final NewsImporter newsImporter = getNewsImporter(newsImporterIndex);
        final Observable<? extends WebsNews> news = newsImporter.fetchNews();

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

    @Override
    public void fetchAllNews() {
        Observable<? extends WebsNews> allNews = newsImporters.stream()
                .map(NewsImporter::fetchNews)
                .reduce(Observable.empty(), Observable::concat);
        newsImportingStrategy.saveAllNews(allNews);
    }
}
