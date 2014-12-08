package com.piotrglazar.webs.business;

import com.piotrglazar.webs.model.entities.PasswordResetToken;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.entities.WebsUserBuilder;
import com.piotrglazar.webs.model.repositories.PasswordResetTokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import static com.piotrglazar.webs.model.PasswordResetTokenAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetTokenServiceTest {

    @Mock
    private UuidGenerator uuidGenerator;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private Supplier<LocalDateTime> dateSupplier;

    @InjectMocks
    private PasswordResetTokenService tokenGenerator;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldMarkAllOlderTokensAsExpiredBeforeInsertingNewToken() {
        // given
        final WebsUser websUser = new WebsUserBuilder().id(1).build();

        // when
        tokenGenerator.generateAndStoreTokenForUser(websUser);

        // then
        final InOrder inOrder = Mockito.inOrder(tokenRepository);
        inOrder.verify(tokenRepository).findByIsExpiredAndUserId(false, 1);
        inOrder.verify(tokenRepository).save(any(Iterable.class));
        inOrder.verify(tokenRepository).save(any(PasswordResetToken.class));
    }

    @Test
    public void shouldGenerateTokenForUser() {
        // given
        final WebsUser websUser = new WebsUserBuilder().id(1).build();
        given(dateSupplier.get()).willReturn(LocalDateTime.of(2014, 11, 26, 0, 0));
        given(uuidGenerator.generate()).willReturn("a9f4411a-7205-472b-8be5-ddd17108b45b");
        given(tokenRepository.save(any(PasswordResetToken.class))).willAnswer(invocation -> invocation.getArguments()[0]);

        // when
        final PasswordResetToken passwordResetToken = tokenGenerator.generateAndStoreTokenForUser(websUser);

        // then
        assertThat(passwordResetToken)
                .hasUserId(1)
                .hasCreatedAt(LocalDateTime.of(2014, 11, 26, 0, 0))
                .hasIsExpired(false)
                .hasTokenId("a9f4411a-7205-472b-8be5-ddd17108b45b");
    }

    @Test
    public void shouldGetTokenById() {
        // given
        final String tokenId = "a9f4411a-7205-472b-8be5-ddd17108b45b";

        // when
        tokenGenerator.getByTokenId(tokenId);

        // then
        verify(tokenRepository).getByTokenId(tokenId);
    }

    @Test
    public void shouldInvalidateToken() {
        // given
        final PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setIsExpired(false);

        // when
        tokenGenerator.invalidateToken(passwordResetToken);

        // then
        assertThat(passwordResetToken.getIsExpired()).isTrue();
    }
}
