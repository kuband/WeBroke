package com.we.broke.auth.security.services;

import com.we.broke.auth.models.entity.User;
import com.we.broke.auth.models.entity.VerificationToken;
import com.we.broke.auth.repository.UserRepository;
import com.we.broke.auth.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class VerificationTokenService {

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    private VerificationTokenRepository verificationTokenRepository;
    private UserRepository userRepository;

    public User getUser(final String verificationToken) {
        final VerificationToken token = verificationTokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    public void deleteUserVerificationToken(final User user) {
        final VerificationToken verificationToken = verificationTokenRepository.findByUser(user);

        if (verificationToken != null) {
            verificationTokenRepository.delete(verificationToken);
        }
    }

    public VerificationToken getVerificationToken(final String VerificationToken) {
        return verificationTokenRepository.findByToken(VerificationToken);
    }

    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken =
                verificationTokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
                .toString());
        vToken = verificationTokenRepository.save(vToken);
        return vToken;
    }

    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        userRepository.save(user);
        return TOKEN_VALID;
    }


//    public List<String> getUsersFromSessionRegistry() {
//        return sessionRegistry.getAllPrincipals()
//                .stream()
//                .filter((u) -> !sessionRegistry.getAllSessions(u, false)
//                        .isEmpty())
//                .map(o -> {
//                    if (o instanceof User) {
//                        return ((User) o).getEmail();
//                    } else {
//                        return o.toString()
//                                ;
//                    }
//                }).collect(Collectors.toList());
//    }
}
