package com.piotrglazar.webs.model;

import com.google.common.collect.Sets;
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
        // given

        // when
        defaultUserProvider.createUser("username", "password");

        // then
        verify(websUserRepository).save(any(WebsUser.class));
    }

    @Test
    public void shouldFindExistingUser() {
        // given
        final WebsUser user = new WebsUser("username", "password", Sets.newHashSet("role"));
        given(websUserRepository.findByUsername("username")).willReturn(user);

        // when
        final WebsUser foundUser = defaultUserProvider.findUser("username");

        // then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void shouldReturnNullWhenThereIsNoUserForGivenName() {
        // given

        // when
        final WebsUser foundUser = defaultUserProvider.findUser("non existing user");

        // then
        assertThat(foundUser).isNull();
    }
}
