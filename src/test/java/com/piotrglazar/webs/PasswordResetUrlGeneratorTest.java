package com.piotrglazar.webs;

import com.piotrglazar.webs.model.entities.PasswordResetToken;
import com.piotrglazar.webs.model.entities.PasswordResetTokenBuilder;
import org.junit.Test;

import java.net.URL;
import java.net.UnknownHostException;

import static com.piotrglazar.webs.config.UtilityConfiguration.HOST;
import static com.piotrglazar.webs.config.UtilityConfiguration.PASSWORD_RESET_ROOT_URL;
import static com.piotrglazar.webs.config.UtilityConfiguration.PORT;
import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

public class PasswordResetUrlGeneratorTest {

    private PasswordResetUrlGenerator urlGenerator = new PasswordResetUrlGenerator();

    @Test
    public void shouldGenerateUrlForToken() throws UnknownHostException {
        // given
        final PasswordResetToken token = new PasswordResetTokenBuilder().tokenId("tokenId").build();

        // when
        final URL url = urlGenerator.generateUrlForToken(token);

        // then
        assertThat(url.toString()).contains(HOST, valueOf(PORT), PASSWORD_RESET_ROOT_URL, "/tokenId/");
    }
}
