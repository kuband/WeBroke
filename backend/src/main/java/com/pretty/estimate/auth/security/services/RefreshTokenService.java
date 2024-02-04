package com.we.broke.auth.security.services;

import com.we.broke.auth.exception.TokenException;
import com.we.broke.auth.models.entity.RefreshToken;
import com.we.broke.auth.repository.RefreshTokenRepository;
import com.we.broke.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    @Value("${we.broke.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Value("${we.broke.app.jwtRefreshExpirationRememberMeMs}")
    private Long jwtRefreshExpirationRememberMeMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId, boolean rememberMe) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(userId);

        RefreshToken refreshToken;
        if (optionalRefreshToken.isEmpty()) {
            refreshToken = new RefreshToken();
            refreshToken.setUser(userRepository.findById(userId).get());
            refreshToken.setExpiryDate(Instant.now().plusMillis(rememberMe ?
                    jwtRefreshExpirationRememberMeMs : refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        } else {
            return optionalRefreshToken.get();
        }
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            log.error("Refresh token was expired. Please " +
                    "make a new signin request");
            throw new TokenException("REFRESH_TOKEN_EXPIRED");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
