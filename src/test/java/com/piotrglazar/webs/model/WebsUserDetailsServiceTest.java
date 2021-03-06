package com.piotrglazar.webs.model;

import com.google.common.collect.Sets;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.entities.WebsUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class WebsUserDetailsServiceTest {

    @Mock
    private UserProvider userProvider;

    @InjectMocks
    private WebsUserDetailsService websUserDetailsService;

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowExceptionWhenNoSuchUserExist() {
        // when
        websUserDetailsService.loadUserByUsername("non existing user");
    }

    @Test
    public void shouldConvertWebsUserToSpringUser() {
        // given
        final WebsUser user = WebsUser.builder().username("username").password("password").roles(Sets.newHashSet("role")).build();
        given(userProvider.findUserByUsername("username")).willReturn(user);

        // when
        final UserDetails userDetails = websUserDetailsService.loadUserByUsername("username");

        // then
        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("role");
    }
}
