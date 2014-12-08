package com.piotrglazar.webs.model.entities;

import java.time.LocalDateTime;

public class PasswordResetTokenBuilder {

    private Long id;
    private LocalDateTime createdAt = LocalDateTime.of(2014, 11, 25, 0, 0);
    private String tokenId = "";
    private long userId;
    private boolean isExpired;

    public PasswordResetTokenBuilder id(final Long id) {
        this.id = id;
        return this;
    }

    public PasswordResetTokenBuilder createdAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public PasswordResetTokenBuilder tokenId(final String tokenId) {
        this.tokenId = tokenId;
        return this;
    }

    public PasswordResetTokenBuilder userId(final Long userId) {
        this.userId = userId;
        return this;
    }

    public PasswordResetTokenBuilder isExpired(final Boolean isExpired) {
        this.isExpired = isExpired;
        return this;
    }

    public PasswordResetToken build() {
        final PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setId(id);
        passwordResetToken.setCreatedAt(createdAt);
        passwordResetToken.setIsExpired(isExpired);
        passwordResetToken.setTokenId(tokenId);
        passwordResetToken.setUserId(userId);
        return passwordResetToken;
    }
}
