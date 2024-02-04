package com.backend.auth.security.services;

import com.backend.auth.models.entity.User;
import com.backend.auth.models.entity.VerificationToken;
import com.backend.auth.repository.UserRepository;
import com.backend.auth.repository.VerificationTokenRepository;
import com.backend.common.model.entity.UserSecurityToken;
import com.backend.common.repository.UserSecurityTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserDetails {

  private VerificationTokenRepository verificationTokenRepository;
  private UserRepository userRepository;
  private UserSecurityTokenRepository userSecurityTokenRepository;

  public Optional<User> getUserByID(final long id) {
    return userRepository.findById(id);
  }

  public Optional<User> findUserByEmail(final String email) {
    return userRepository.findByEmail(email);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public void saveRegisteredUser(final User user) {
    userRepository.save(user);
  }

  public void deleteUser(final User user) {
    final VerificationToken verificationToken = verificationTokenRepository.findByUser(user);

    if (verificationToken != null) {
      verificationTokenRepository.delete(verificationToken);
    }

    List<UserSecurityToken> securityTokens = userSecurityTokenRepository.findByUser(user);

    if (!securityTokens.isEmpty()) {
      userSecurityTokenRepository.deleteAll(securityTokens);
    }

    userRepository.delete(user);
  }

  public Optional<User> getUserByUserSecurityToken(final String token) {
    return Optional.ofNullable(userSecurityTokenRepository.findByToken(token).getUser());
  }

//  public String generateQRUrl(User user) throws UnsupportedEncodingException {
//    return QR_PREFIX + URLEncoder.encode(String.format
//    ("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmail(), user.getSecret(),
//    APP_NAME), "UTF-8");
//  }
//
//  public User updateUser2FA(boolean use2FA) {
//    final Authentication curAuth = SecurityContextHolder.getContext()
//            .getAuthentication();
//    User currentUser = (User) curAuth.getPrincipal();
//    currentUser.setUsing2FA(use2FA);
//    currentUser = userRepository.save(currentUser);
//    final Authentication auth = new UsernamePasswordAuthenticationToken(currentUser,
//    currentUser.getPassword(), curAuth.getAuthorities());
//    SecurityContextHolder.getContext()
//            .setAuthentication(auth);
//    return currentUser;
//  }

}
