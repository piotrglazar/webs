package com.piotrglazar.webs.business.news;

import com.piotrglazar.webs.NewsImporters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledNewsImporterTest {

    @Mock
    private NewsImporters newsImporters;

    @InjectMocks
    private ScheduledNewsImporter scheduledNewsImporter;

    @Test
    public void shouldImportNews() {
        // when
        scheduledNewsImporter.importNews();

        // then
        verify(newsImporters).fetchAllNews();
    }
}
