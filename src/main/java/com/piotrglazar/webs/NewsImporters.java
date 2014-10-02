package com.piotrglazar.webs;

import java.util.List;

public interface NewsImporters {
    List<String> getNewsImportersNames();

    void fetchNews(int newsImporterIndex);

    void fetchAllNews();
}
