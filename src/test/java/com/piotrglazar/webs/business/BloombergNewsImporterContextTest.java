package com.piotrglazar.webs.business;

import com.piotrglazar.webs.AbstractContextTest;
import com.piotrglazar.webs.model.BloombergNews;
import com.piotrglazar.webs.model.WebsNewsRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BloombergNewsImporterContextTest extends AbstractContextTest {

    @Autowired
    private BloombergNewsImporter bloombergNewsImporter;

    @Autowired
    private WebsNewsRepository websNewsRepository;

    @Test
    public void shouldCreateBloombergNews() {
        // when
        final List<BloombergNews> news = bloombergNewsImporter.fetchNews();

        // then
        assertThat(news).hasSize(1);
        // five indices from stock exchange and others
        assertThat(news.get(0).getBody().split("\n")).hasSize(15);

        // cleanup
        news.stream().forEach(websNewsRepository::delete);
    }
}
