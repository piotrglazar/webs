package com.piotrglazar.webs.mvc;

import com.google.common.collect.Lists;
import com.piotrglazar.webs.business.UiNewsProvider;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.dto.NewsDto;
import com.piotrglazar.webs.model.entities.InternalWebsNews;
import com.piotrglazar.webs.mvc.controllers.IndexController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IndexControllerTest {

    @Mock
    private Model model;

    @Mock
    private UiNewsProvider uiNewsProvider;

    @InjectMocks
    private IndexController controller;

    @Test
    public void shouldShowNewsAndWelcomeMessage() {
        // given
        final List<NewsDto> news = Lists.newArrayList(new NewsDto(InternalWebsNews.builder().build()));
        given(uiNewsProvider.getNews()).willReturn(news);

        // when
        final String view = controller.index(model);

        // then
        assertThat(view).isEqualTo("index");
        verify(model).addAttribute("message", "Welcome to the brand-new online bank!");
        verify(model).addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "index");
        verify(model).addAttribute("websNews", news);
    }
}
