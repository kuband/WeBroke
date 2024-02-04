package com.we.broke.auth.repository;

import com.we.broke.auth.models.entity.RefreshToken;
import com.we.broke.auth.models.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
  Optional<RefreshToken> findByToken(String token);


  @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
  Optional<RefreshToken> findByUserId(Long userId);

  @Modifying
  int deleteByUser(User user);
}
