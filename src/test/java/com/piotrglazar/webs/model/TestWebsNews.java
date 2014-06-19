package com.piotrglazar.webs.model;

import org.springframework.context.annotation.Profile;

import javax.persistence.Entity;

@Entity
@Profile("test")
public class TestWebsNews extends WebsNews {

    public static String classSimpleName() {
        return TestWebsNews.class.getSimpleName();
    }
}
