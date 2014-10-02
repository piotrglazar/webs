package com.piotrglazar.webs.util.readers;

import org.springframework.stereotype.Component;

@Component
public class WebsiteReaderFactory {

    public WebsiteReader websiteReader(String target) {
        return new WebsiteReader(target);
    }
}
