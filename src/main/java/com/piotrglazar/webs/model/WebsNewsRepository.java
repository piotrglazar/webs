package com.piotrglazar.webs.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface WebsNewsRepository extends JpaRepository<WebsNews, Long> {

    @Modifying
    @Query(value = "delete from WebsNews where news_source = ?1", nativeQuery = true)
    void deleteAllNews(String newsName);
}
