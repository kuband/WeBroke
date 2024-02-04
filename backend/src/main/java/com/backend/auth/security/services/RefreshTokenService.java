package com.backend.auth.security.services;

import com.backend.auth.exception.TokenException;
import com.backend.auth.models.entity.RefreshToken;
import com.backend.auth.repository.RefreshTokenRepository;
import com.backend.auth.repository.UserRepository;
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
    @Value("${backend.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Value("${backend.app.jwtRefreshExpirationRememberMeMs}")
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
