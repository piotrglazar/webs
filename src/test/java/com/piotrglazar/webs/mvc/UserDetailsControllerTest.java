package com.piotrglazar.webs.mvc;

import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.config.MvcConfiguration;
import com.piotrglazar.webs.dto.UserDetailsDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsControllerTest {

    @Mock
    private Model model;

    @Mock
    private LoggedInUserProvider loggedInUserProvider;

    @Mock
    private UserProvider userProvider;

    @InjectMocks
    private UserDetailsController controller;

    @Test
    public void shouldFetchUserDetailsFromDb() {
        // given
        final UserDetailsDto userDetails = mock(UserDetailsDto.class);
        final User user = mock(User.class);
        given(user.getUsername()).willReturn("user");
        given(loggedInUserProvider.getLoggedInUser()).willReturn(user);
        given(userProvider.getUserDetails("user")).willReturn(userDetails);

        // when
        final String page = controller.userDetails(model);

        // then
        assertThat(page).isEqualTo("userDetails");
        verify(model).addAttribute(MvcConfiguration.PAGE_NAME_ATTRIBUTE, "userDetails");
        verify(model).addAttribute("welcomeMessage", "Welcome user!");
        verify(model).addAttribute("userDetails", userDetails);
    }
}
