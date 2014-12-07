package com.piotrglazar.webs.model.repositories;

import com.piotrglazar.webs.model.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    List<PasswordResetToken> findByIsExpiredAndUserId(boolean isExpired, long userId);

    PasswordResetToken getByTokenId(String tokenId);
}
