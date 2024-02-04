package com.we.broke.app.repository;

import com.we.broke.app.model.entity.Organisation;
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


    @Query("select id from Organisation where id not in (?1) and (subscriptionStatus<>'null' or subscriptionStatus<>'CANCELED')")
    List<Long> findAllToCancel(List<Long> id);

    @Modifying
    @Query("update Organisation set modificationStatus='DELETED' where id in (?1)")
    void deleteOrgs(List<Long> id);

}
