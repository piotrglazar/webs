package com.piotrglazar.webs.util.beans;

import com.piotrglazar.webs.NewsImporters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "webs:name=NewsImporting")
public class NewsImportingBean {

    private final NewsImporters newsImporters;

    @Autowired
    public NewsImportingBean(final NewsImporters newsImporters) {
        this.newsImporters = newsImporters;
    }

    @ManagedOperation
    public void importNews() {
        newsImporters.fetchAllNews();
    }

    @ManagedOperation
    public void importNews(@ManagedOperationParameter(name = "newsImporter", description = "the index of news importer") int newsImporter) {
        newsImporters.fetchNews(newsImporter);
    }
}
