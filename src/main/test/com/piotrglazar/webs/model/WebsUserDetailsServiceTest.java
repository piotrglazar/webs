package com.piotrglazar.webs.model;

import com.google.common.collect.Sets;
import com.piotrglazar.webs.UserProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class WebsUserDetailsServiceTest {

    private UserProvider userProvider;

    private WebsUserDetailsService websUserDetailsService;

    @Before
    public void setup() {
        userProvider = mock(UserProvider.class);
        websUserDetailsService = new WebsUserDetailsService(userProvider);
    }

    @Test
    public void shouldThrowExceptionWhenNoSuchUserExist() {
        // given

        // when
        catchException(websUserDetailsService).loadUserByUsername("non existing user");

        // then
        assertThat((Throwable) caughtException()).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void shouldConvertWebsUserToSpringUser() {
        // given
        final WebsUser user = new WebsUser("username", "password", Sets.newHashSet("role"));
        given(userProvider.findUser("username")).willReturn(user);

        // when
        final UserDetails userDetails = websUserDetailsService.loadUserByUsername("username");

        // then
        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("role");
    }
}
