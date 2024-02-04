package com.backend.app.repository;

import com.backend.app.model.entity.UserOrganisation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface UserOrganisationRepository extends JpaRepository<UserOrganisation, Long> {

    Optional<List<UserOrganisation>> findAllByOrganisationIdIn(@Param("ids") Iterable<Long> ids);

    Optional<List<UserOrganisation>> findAllByUserIdIn(@Param("ids") Iterable<Long> ids);

    Optional<UserOrganisation> findAllByOrganisationIdAndUserId(@Param("organisationId") Long organisationId, @Param("userId") Long userId);

}
