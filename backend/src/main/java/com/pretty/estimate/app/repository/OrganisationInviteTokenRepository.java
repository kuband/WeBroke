package com.we.broke.app.repository;

import com.we.broke.app.model.entity.OrganisationInviteToken;
import com.we.broke.auth.models.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.stream.Stream;


@Repository
@Transactional
public interface OrganisationInviteTokenRepository extends JpaRepository<OrganisationInviteToken,
        Long> {

    @EntityGraph(attributePaths = {"user.roles"}, type = EntityGraph.EntityGraphType.LOAD)
    OrganisationInviteToken findByToken(String token);

    OrganisationInviteToken findByUser(User user);

    OrganisationInviteToken findByUserId(Long userId);

    Stream<OrganisationInviteToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    void deleteByToken(String token);

    @Modifying
    @Query("delete from OrganisationInviteToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);

}
