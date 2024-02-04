package com.we.broke.app.repository;

import com.we.broke.app.model.entity.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<List<Project>> findAllByOrganisationIdIn(@Param("ids") Iterable<Long> ids);

    Page<Project> findAllByOrganisationIdIn(@Param("ids") Iterable<Long> ids, Pageable pageable);

    //set REMOVED STATUS
    @Modifying
    @Query("update Project set modificationStatus='DELETED' where id in (?1)")
    void deleteProjects(List<Long> id);


    @Query("select p from Project p where id in (?1)")
    List<Project> getProjects(@Param("ids") Iterable<Long> ids);
}
