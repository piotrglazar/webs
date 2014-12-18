package com.piotrglazar.webs.business.passwordreset;

import com.piotrglazar.webs.PasswordResetUrlGenerator;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.business.mail.BusinessMailSender;
import com.piotrglazar.webs.model.WebsUserNotFoundException;
import com.piotrglazar.webs.model.entities.PasswordResetToken;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.entities.WebsUserBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetServiceTest {

    @Mock
    private UserProvider userProvider;

    @Mock
    private BusinessMailSender mailSender;

    @Mock
    private PasswordResetUrlGenerator urlGenerator;

    @Mock
    private PasswordResetTokenService tokenService;

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Test(expected = WebsUserNotFoundException.class)
    public void shouldThrowExceptionWhenThereIsNoSuchUser() {
        // expect
        passwordResetService.sendUserPasswordResetMessage("there is no such user");
    }

    @Test
    public void shouldCreateTokenAndSendEmailForUser() throws MalformedURLException {
        // given
        final WebsUser user = new WebsUserBuilder().id(1).email("email").build();
        given(userProvider.findUserByEmail("email")).willReturn(user);
        final PasswordResetToken passwordResetToken = new PasswordResetToken();
        given(tokenService.generateAndStoreTokenForUser(user)).willReturn(passwordResetToken);
        given(urlGenerator.generateUrlForToken(passwordResetToken)).willReturn(new URL("http://localhost"));

        // when
        passwordResetService.sendUserPasswordResetMessage("email");

        // then
        verify(mailSender).sendPasswordResetMessage(user, new URL("http://localhost"));
    }

    @Test
    public void shouldResetUserPasswordAndInvalidateToken() {
        // given
        final PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUserId(101L);
        given(tokenService.getByTokenId("tokenId")).willReturn(passwordResetToken);
        final WebsUser websUser = new WebsUserBuilder().build();
        given(userProvider.getUserById(101L)).willReturn(websUser);

        // when
        passwordResetService.resetUserPassword("tokenId", "newPassword");

        // then
        verify(userProvider).updateUserPassword(websUser, "newPassword");
        verify(tokenService).invalidateToken(passwordResetToken);
    }
}
