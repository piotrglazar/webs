package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.config.MvcConfig;
import com.piotrglazar.webs.mvc.IndexController;
import org.junit.Test;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class IndexControllerTest {

    @Test
    public void shouldServeWelcomeMessage() {
        // given
        final Model model = mock(Model.class);
        final IndexController controller = new IndexController();

        // when
        final String view = controller.index(model);

        // then
        assertThat(view).isEqualTo("index");
        verify(model).addAttribute("message", "Welcome to the brand-new online bank!");
        verify(model).addAttribute(MvcConfig.PAGE_NAME_ATTRIBUTE, "index");
    }
}
