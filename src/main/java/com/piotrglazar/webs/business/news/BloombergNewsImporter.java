package com.piotrglazar.webs.business.news;

import com.piotrglazar.webs.business.NewsImporter;
import com.piotrglazar.webs.dto.BloombergNewsBody;
import com.piotrglazar.webs.dto.BloombergNewsBodyFactory;
import com.piotrglazar.webs.model.entities.BloombergNews;
import com.piotrglazar.webs.util.MoreCollectors;
import com.piotrglazar.webs.util.readers.WebsiteReader;
import com.piotrglazar.webs.util.readers.WebsiteReaderFactory;
import com.piotrglazar.webs.util.templates.WebsTemplates;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.List;

@Component
public class BloombergNewsImporter implements NewsImporter {

    public static final String BLOOMBERG = "http://www.bloomberg.com/";

    private static final String OVERVIEW_NEWS_SELECTOR = "div.market-summary__securities.active > div";
    private static final String INDEX_NAME_SELECTOR = ".market-summary__security__name";
    private static final String INDEX_PRICE_CHANGE_SELECTOR = ".market-summary__security__price-change";
    private static final String INDEX_PRICE_SELECTOR = ".market-summary__security__price";
    private static final String INDEX_PERCENT_CHANGE_SELECTOR = ".market-summary__security__percent-change";

    private final WebsiteReader bloomberg;
    private final BloombergNewsBodyFactory bloombergNewsBodyFactory;
    private final WebsTemplates websTemplates;

    @Autowired
    public BloombergNewsImporter(WebsiteReaderFactory factory, BloombergNewsBodyFactory bloombergNewsBodyFactory,
                                 WebsTemplates websTemplates) {
        this.bloombergNewsBodyFactory = bloombergNewsBodyFactory;
        this.websTemplates = websTemplates;
        this.bloomberg = factory.websiteReader(BLOOMBERG);
    }

    private String webPageContent() {
        return bloomberg.get();
    }

    protected String bloombergNewsBodiesAsText(final String pageContent) {
        final List<BloombergNewsBody> bloombergNewsBodies = bloombergNewsBodies(pageContent);
        return websTemplates.bloombergNewsBody(bloombergNewsBodies);
    }

    protected List<BloombergNewsBody> bloombergNewsBodies(String pageContent) {
        final Document document = Jsoup.parse(pageContent);
        final Elements tickers = document.select(OVERVIEW_NEWS_SELECTOR);
        return tickers.stream()
                .map(e -> bloombergNewsBodyFactory.createNews(e.select(INDEX_NAME_SELECTOR).text(),
                        e.select(INDEX_PRICE_CHANGE_SELECTOR).text(),
                        e.select(INDEX_PRICE_SELECTOR).text(),
                        e.select(INDEX_PERCENT_CHANGE_SELECTOR).text()))
                .collect(MoreCollectors.toImmutableList());
    }

    @Override
    public Observable<BloombergNews> fetchNews() {
        return Observable.create((Observable.OnSubscribe<BloombergNews>) f -> {
            final String webPageContent = webPageContent();
            final String tickers = bloombergNewsBodiesAsText(webPageContent);
            f.onNext(bloombergNews(tickers));
            f.onCompleted();
        });
    }

    @Override
    public Class<BloombergNews> provides() {
        return BloombergNews.class;
    }

    @Override
    public String name() {
        return "Bloomberg News";
    }

    private BloombergNews bloombergNews(final String tickers) {
        return new BloombergNews("Latest news from Bloomberg", tickers, BLOOMBERG, "Read it",
                "#777:#7a7a7a/text:Bloomberg");
    }
}
