package com.piotrglazar.webs.business;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.model.BloombergNews;
import com.piotrglazar.webs.util.WebsiteReader;
import com.piotrglazar.webs.util.WebsiteReaderFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BloombergNewsImporter implements NewsImporter {

    public static final String BLOOMBERG = "http://www.bloomberg.com/";

    private final WebsiteReader bloomberg;

    @Autowired
    public BloombergNewsImporter(final WebsiteReaderFactory factory) {
        this.bloomberg = factory.websiteReader(BLOOMBERG);
    }

    private String webPageContent() {
        return bloomberg.get();
    }

    protected String extractTickers(final String pageContent) {
        final Document document = Jsoup.parse(pageContent);
        final Elements tickers = document.select("#markets_snapshot > section > ul.tab.tickers > li");
        return tickers.stream()
                .map(e -> e.select(".name > a").text() + " " +
                        e.select(".day_change span").stream().map(elt -> elt.text()).collect(Collectors.joining(" ")) + " " +
                        e.select(".price").text())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<BloombergNews> fetchNews() {
        final String webPageContent = webPageContent();
        final String tickers = extractTickers(webPageContent);
        return Lists.newArrayList(bloombergNews(tickers));
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
