package com.backend.common.services;

import com.backend.auth.models.entity.User;
import com.backend.common.model.UserSecurityTokenType;
import com.backend.common.model.entity.UserSecurityToken;
import com.backend.common.repository.UserSecurityTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
@AllArgsConstructor
public class UserSecurityTokenService {

    private UserSecurityTokenRepository userSecurityTokenRepository;

    public void createSecurityTokenForUser(final User user, final String token,
                                           final UserSecurityTokenType type,
                                           final Long securityId) {
        final UserSecurityToken myToken = new UserSecurityToken(token, user, type, securityId);
        userSecurityTokenRepository.save(myToken);
    }

    public List<UserSecurityToken> getSecurityTokenByUser(final User user,
                                                          UserSecurityTokenType type) {
        return userSecurityTokenRepository.findByUserAndType(user, type);
    }

    public List<UserSecurityToken> getSecurityTokenByUserId(final Long userId,
                                                            UserSecurityTokenType type) {
        return userSecurityTokenRepository.findByUserIdAndType(userId, type);
    }

    public UserSecurityToken getSecurityToken(final String token) {
        return userSecurityTokenRepository.findByToken(token);
    }

    public void deleteByToken(String token) {
        userSecurityTokenRepository.deleteByToken(token);
    }

    public void delete(UserSecurityToken userSecurityToken) {
        userSecurityTokenRepository.delete(userSecurityToken);
    }

    public String validateToken(String token, UserSecurityTokenType type) {
        final UserSecurityToken passToken = userSecurityTokenRepository.findByToken(token);


        return !isTokenFound(passToken) ? "invalidToken" : (type == passToken.getType() ?
                (isTokenExpired(passToken) ? "expired" : null)
                : null);
    }

    private boolean isTokenFound(UserSecurityToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(UserSecurityToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
