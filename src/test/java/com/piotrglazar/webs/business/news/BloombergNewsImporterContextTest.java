package com.piotrglazar.webs.business.news;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.model.entities.BloombergNews;
import com.piotrglazar.webs.model.repositories.WebsNewsRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rx.Observable;

import java.util.List;

import static com.piotrglazar.webs.TestUtilities.toListToBlocking;
import static org.assertj.core.api.Assertions.assertThat;

public class BloombergNewsImporterContextTest extends AbstractContextTest {

    @Autowired
    private BloombergNewsImporter bloombergNewsImporter;

    @Autowired
    private WebsNewsRepository websNewsRepository;

    @Test
    @Ignore("Bloomberg main page has changed")
    public void shouldCreateBloombergNews() {
        // when
        final Observable<BloombergNews> news = bloombergNewsImporter.fetchNews();

        // then
        final List<BloombergNews> newsList = toListToBlocking(news);
        assertThat(newsList).hasSize(1);
        // five indices from stock exchange and others
        assertThat(newsList.get(0).getBody().split("\n")).hasSize(15);

        // cleanup
        newsList.forEach(websNewsRepository::delete);
    }
}
