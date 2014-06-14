package com.piotrglazar.webs.business;

import com.piotrglazar.webs.WebsNewsProvider;
import com.piotrglazar.webs.dto.NewsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class DefaultUiNewsProvider implements UiNewsProvider {

    private final WebsNewsProvider websNewsProvider;

    @Autowired
    public DefaultUiNewsProvider(final WebsNewsProvider websNewsProvider) {
        this.websNewsProvider = websNewsProvider;
    }

    @Override
    public Collection<NewsDto> getNews() {
        return websNewsProvider.getNews().stream().map(NewsDto::new).collect(Collectors.toList());
    }
}
