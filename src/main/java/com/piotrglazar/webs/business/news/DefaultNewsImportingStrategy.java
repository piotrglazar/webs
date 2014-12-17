package com.piotrglazar.webs.business.news;

import com.piotrglazar.webs.WebsNewsProvider;
import com.piotrglazar.webs.business.NewsImporter;
import com.piotrglazar.webs.business.NewsImportingStrategy;
import com.piotrglazar.webs.model.entities.WebsNews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultNewsImportingStrategy implements NewsImportingStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final WebsNewsProvider websNewsProvider;

    @Autowired
    public DefaultNewsImportingStrategy(final WebsNewsProvider websNewsProvider) {
        this.websNewsProvider = websNewsProvider;
    }

    @Override
    public void saveNews(final NewsImporter newsImporter, final Observable<? extends WebsNews> rawNews) {
        rawNews.toList().subscribe(
                news -> {
                    if (news.isEmpty()) {
                        LOG.info("No news from {} to save", newsImporter);
                    } else {
                        LOG.debug("Saving news {} {}", newsImporter, news);
                        websNewsProvider.removeAll(newsImporter.provides());
                        websNewsProvider.saveAll(news);
                    }
                },
                e -> LOG.error("Failed to save news {}", newsImporter, e),
                () -> LOG.debug("Finished saving from NewsImporter {}", newsImporter));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void saveAllNews(final Observable<? extends WebsNews> allRawNews) {
        allRawNews.toList().subscribe(
                news -> {
                    if (news.isEmpty()) {
                        LOG.info("No news to save");
                    } else {
                        LOG.debug("Saving all news {}", news);
                        ((List<Class<? extends WebsNews>>) news.stream().map(WebsNews::getClass).distinct().collect(Collectors.toList()))
                                .forEach(websNewsProvider::removeAll);
                        websNewsProvider.saveAll(news);
                    }
                },
                e -> LOG.error("Failed to save all news", e),
                () -> LOG.debug("Finished saving all news"));
    }
}
