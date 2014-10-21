package com.piotrglazar.webs.model;

import com.piotrglazar.webs.dto.UserDetailsDto;
import com.piotrglazar.webs.model.entities.Address;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.entities.WebsUserDetails;
import com.piotrglazar.webs.model.repositories.WebsUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserProviderTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WebsUserRepository websUserRepository;

    @InjectMocks
    private DefaultUserProvider defaultUserProvider;

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

    @Test
    public void shouldBuildUserDetailsDto() {
        // given
        final WebsUserDetails userDetails = new WebsUserDetails(LocalDateTime.of(2014, 7, 1, 0, 0), new Address("Warsaw", "PL"));
        final WebsUser user = WebsUser.builder().username("username").email("email").details(userDetails).build();
        given(websUserRepository.findByUsername("username")).willReturn(user);

        // when
        final UserDetailsDto dto = defaultUserProvider.getUserDetails("username");

        // then
        assertThat(dto.getEmail()).isEqualTo("email");
        assertThat(dto.getMemberSince()).isEqualTo(LocalDateTime.of(2014, 7, 1, 0, 0));
        assertThat(dto.getUsername()).isEqualTo("username");
        assertThat(dto.getAddress().getCity()).isEqualTo("Warsaw");
        assertThat(dto.getAddress().getCountry()).isEqualTo("PL");
    }

    @Test(expected = WebsUserNotFoundException.class)
    public void shouldGetThrowExceptionWhenUserNotFound() {
        // expect
        defaultUserProvider.getUserByUsername("abc");
    }
}
