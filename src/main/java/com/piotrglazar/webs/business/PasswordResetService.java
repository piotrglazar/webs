package com.piotrglazar.webs.business;

import com.piotrglazar.webs.PasswordResetUrlGenerator;
import com.piotrglazar.webs.UserProvider;
import com.piotrglazar.webs.model.WebsUserNotFoundException;
import com.piotrglazar.webs.model.entities.PasswordResetToken;
import com.piotrglazar.webs.model.entities.WebsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Optional;

@Component
public class PasswordResetService {

    private final UserProvider userProvider;
    private final BusinessMailSender mailSender;
    private final PasswordResetUrlGenerator urlGenerator;
    private final PasswordResetTokenService tokenService;

    @Autowired
    public PasswordResetService(UserProvider userProvider, BusinessMailSender mailSender, PasswordResetUrlGenerator urlGenerator,
                                PasswordResetTokenService tokenService) {
        this.userProvider = userProvider;
        this.mailSender = mailSender;
        this.urlGenerator = urlGenerator;
        this.tokenService = tokenService;
    }

    public void sendUserPasswordResetMessage(final String email) {
        final WebsUser websUser = getUser(email);

        final PasswordResetToken passwordResetToken = tokenService.generateAndStoreTokenForUser(websUser);

        final URL passwordResetUrl = urlGenerator.generateUrlForToken(passwordResetToken);

        mailSender.sendPasswordResetMessage(websUser, passwordResetUrl);
    }

    @Transactional
    public void resetUserPassword(final String tokenId, final String password) {
        final PasswordResetToken passwordResetToken = tokenService.getByTokenId(tokenId);

        final WebsUser websUser = userProvider.getUserById(passwordResetToken.getUserId());
        userProvider.updateUserPassword(websUser, password);

        tokenService.invalidateToken(passwordResetToken);
    }

    private WebsUser getUser(final String email) {
        return Optional.ofNullable(userProvider.findUserByEmail(email)).orElseThrow(() -> new WebsUserNotFoundException(email));
    }
}
