package com.piotrglazar.webs.model;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultUserProviderTest {

    private WebsUserRepository websUserRepository;

    private DefaultUserProvider defaultUserProvider;

    @Before
    public void setup() {
        websUserRepository = mock(WebsUserRepository.class);
        defaultUserProvider = new DefaultUserProvider(websUserRepository, mock(PasswordEncoder.class));
    }

    @Test
    public void shouldCreateUser() {
        // when
        final WebsUser user = defaultUserProvider.createUser("username", "password");

        // then
        verify(websUserRepository).save(any(WebsUser.class));
        assertThat(user.getUsername()).isEqualTo("username");
        // password must be encrypted, not plain text
        assertThat(user.getPassword()).isNotEqualTo("password");
    }

    @Test
    public void shouldFindExistingUser() {
        // given
        final WebsUser user = WebsUser.builder().username("username").password("password").build();
        given(websUserRepository.findByUsername("username")).willReturn(user);

        // when
        final WebsUser foundUser = defaultUserProvider.findUserByUsername("username");

        // then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void shouldReturnNullWhenThereIsNoUserForGivenName() {
        // when
        final WebsUser foundUser = defaultUserProvider.findUserByUsername("non existing user");

        // then
        assertThat(foundUser).isNull();
    }
}
