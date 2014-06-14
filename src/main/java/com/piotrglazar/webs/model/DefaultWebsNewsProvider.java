package com.piotrglazar.webs.model;

import com.piotrglazar.webs.WebsNewsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
class DefaultWebsNewsProvider implements WebsNewsProvider {

    private final WebsNewsRepository websNewsRepository;

    @Autowired
    public DefaultWebsNewsProvider(final WebsNewsRepository websNewsRepository) {
        this.websNewsRepository = websNewsRepository;
    }

    @Override
    public Collection<WebsNews> getNews() {
        return websNewsRepository.findAll();
    }

    @Override
    public void addNews(final WebsNews websNews) {
        websNewsRepository.saveAndFlush(websNews);
    }
}
