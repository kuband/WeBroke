package com.backend.app.repository;

import com.backend.app.model.entity.WorkType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface WorkTypeRepository extends JpaRepository<WorkType, Long> {

    Optional<List<WorkType>> findAllByOrganisationIdIn(@Param("ids") Iterable<Long> ids);


    Optional<List<WorkType>> findAllByProjectIdIn(@Param("ids") Iterable<Long> ids);


    Optional<List<WorkType>> findAllByEstimateIdIn(@Param("ids") Iterable<Long> ids);

    @Modifying
    @Query("update WorkType set modificationStatus='DELETED' where id in (?1)")
    void deleteWorkTypes(List<Long> id);
}
