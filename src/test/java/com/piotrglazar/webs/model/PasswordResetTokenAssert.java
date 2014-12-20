package com.piotrglazar.webs.model;

import com.piotrglazar.webs.model.entities.PasswordResetToken;
import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;

public final class PasswordResetTokenAssert {

    private final PasswordResetToken that;

    private PasswordResetTokenAssert(final PasswordResetToken that) {
        this.that = that;
    }

    public static PasswordResetTokenAssert assertThat(PasswordResetToken passwordResetToken) {
        return new PasswordResetTokenAssert(passwordResetToken);
    }

    public PasswordResetTokenAssert hasCreatedAt(LocalDateTime createdAt) {
        Assertions.assertThat(that.getCreatedAt()).isEqualTo(createdAt);
        return this;
    }

    public PasswordResetTokenAssert hasTokenId(String tokenId) {
        Assertions.assertThat(that.getTokenId()).isEqualTo(tokenId);
        return this;
    }

    public PasswordResetTokenAssert hasUserId(long userId) {
        Assertions.assertThat(that.getUserId()).isEqualTo(userId);
        return this;
    }

    public PasswordResetTokenAssert hasIsExpired(boolean isExpired) {
        Assertions.assertThat(that.getIsExpired()).isEqualTo(isExpired);
        return this;
    }
}
