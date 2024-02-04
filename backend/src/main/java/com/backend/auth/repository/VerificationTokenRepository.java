package com.backend.auth.repository;

import com.backend.auth.models.entity.User;
import com.backend.auth.models.entity.VerificationToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.stream.Stream;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

  @EntityGraph(attributePaths = {"user.roles", "user.userOrganisations"}, type =
          EntityGraph.EntityGraphType.LOAD)
  VerificationToken findByToken(String token);

  VerificationToken findByUser(User user);

  Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);

  void deleteByExpiryDateLessThan(Date now);

  @Modifying
  @Query("delete from verification_token t where t.expiryDate <= ?1")
  void deleteAllExpiredSince(Date now);
}
