package com.piotrglazar.webs.business.passwordreset;

import com.piotrglazar.webs.business.UuidGenerator;
import com.piotrglazar.webs.model.entities.PasswordResetToken;
import com.piotrglazar.webs.model.entities.PasswordResetTokenBuilder;
import com.piotrglazar.webs.model.entities.WebsUser;
import com.piotrglazar.webs.model.repositories.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Component
public class PasswordResetTokenService {

    private final UuidGenerator uuidGenerator;
    private final PasswordResetTokenRepository tokenRepository;
    private final Supplier<LocalDateTime> dateSupplier;

    @Autowired
    public PasswordResetTokenService(UuidGenerator uuidGenerator, PasswordResetTokenRepository tokenRepository,
                                     Supplier<LocalDateTime> dateSupplier) {
        this.uuidGenerator = uuidGenerator;
        this.tokenRepository = tokenRepository;
        this.dateSupplier = dateSupplier;
    }

    public PasswordResetToken generateAndStoreTokenForUser(WebsUser websUser) {
        invalidateOldTokens(websUser);
        final PasswordResetToken passwordResetToken = generateNewToken(websUser);
        return tokenRepository.save(passwordResetToken);
    }

    private PasswordResetToken generateNewToken(final WebsUser websUser) {
        return new PasswordResetTokenBuilder()
                .createdAt(dateSupplier.get())
                .isExpired(false)
                .userId(websUser.getId())
                .tokenId(uuidGenerator.generate())
                .build();
    }

    private void invalidateOldTokens(final WebsUser websUser) {
        final List<PasswordResetToken> tokens = tokenRepository.findByIsExpiredAndUserId(false, websUser.getId()).stream().map(t -> {
            t.setIsExpired(true);
            return t;
        }).collect(toList());
        tokenRepository.save(tokens);
    }

    public PasswordResetToken getByTokenId(final String tokenId) {
        return tokenRepository.getByTokenId(tokenId);
    }

    public void invalidateToken(final PasswordResetToken passwordResetToken) {
        passwordResetToken.setIsExpired(true);
        tokenRepository.saveAndFlush(passwordResetToken);
    }
}
