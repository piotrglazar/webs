package com.piotrglazar.webs;

import com.piotrglazar.webs.model.entities.PasswordResetToken;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

import static com.piotrglazar.webs.config.UtilityConfiguration.HOST;
import static com.piotrglazar.webs.config.UtilityConfiguration.PASSWORD_RESET_ROOT_URL;
import static com.piotrglazar.webs.config.UtilityConfiguration.PORT;

@Component
public class PasswordResetUrlGenerator {

    public URL generateUrlForToken(final PasswordResetToken passwordResetToken) {
        try {
            return new URL(String.format("http://%s:%s/%s/%s/", HOST, PORT, PASSWORD_RESET_ROOT_URL, passwordResetToken.getTokenId()));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Failed to create password reset url", e);
        }
    }
}
