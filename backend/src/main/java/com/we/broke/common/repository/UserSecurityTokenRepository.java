package com.we.broke.common.repository;

import com.we.broke.auth.models.entity.User;
import com.we.broke.common.model.UserSecurityTokenType;
import com.we.broke.common.model.entity.UserSecurityToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.stream.Stream;

@Repository
@Transactional
public interface UserSecurityTokenRepository extends JpaRepository<UserSecurityToken, Long> {

    @EntityGraph(attributePaths = {"user.roles"}, type = EntityGraph.EntityGraphType.LOAD)
    UserSecurityToken findByToken(String token);

    List<UserSecurityToken> findByUser(User user);

    List<UserSecurityToken> findByUserAndType(User user, UserSecurityTokenType type);

    List<UserSecurityToken> findByUserIdAndType(Long userId, UserSecurityTokenType type);

    Stream<UserSecurityToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    void deleteByToken(String token);

    @Modifying
    @Query("delete from UserSecurityToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
