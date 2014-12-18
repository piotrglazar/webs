package com.piotrglazar.webs.business.news;

import com.piotrglazar.webs.WebsNewsProvider;
import com.piotrglazar.webs.business.UiNewsProvider;
import com.piotrglazar.webs.dto.NewsDto;
import com.piotrglazar.webs.util.MoreCollectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class DefaultUiNewsProvider implements UiNewsProvider {

    private final WebsNewsProvider websNewsProvider;

    @Autowired
    public DefaultUiNewsProvider(final WebsNewsProvider websNewsProvider) {
        this.websNewsProvider = websNewsProvider;
    }

    @Override
    public Collection<NewsDto> getNews() {
        return websNewsProvider.getNews().stream().map(NewsDto::new).collect(MoreCollectors.toImmutableList());
    }
}
