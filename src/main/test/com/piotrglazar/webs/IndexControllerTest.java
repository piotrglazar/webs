package com.piotrglazar.webs;

import org.junit.Test;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Piotr Glazar
 * @since 23.02.14
 */
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
    }
}
