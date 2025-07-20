package com.backend.app.repository;

import com.backend.app.model.entity.Organisation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
@Transactional
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

    Integer countAllByIdIn(Set<Long> ids);


    @Query("select o.id from Organisation o where o.id not in :ids and o.subscriptionStatus is not null and o.subscriptionStatus <> 'CANCELED'")
    List<Long> findAllToCancel(@org.springframework.data.repository.query.Param("ids") List<Long> ids);

    @Modifying
    @Query("update Organisation set modificationStatus='DELETED' where id in (?1)")
    void deleteOrgs(List<Long> id);

}
