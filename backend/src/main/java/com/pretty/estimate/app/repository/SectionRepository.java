package com.we.broke.app.repository;

import com.we.broke.app.model.entity.Section;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Transactional
public interface SectionRepository extends JpaRepository<Section, Long> {

    @Modifying
    @Query("update Section set modificationStatus='DELETED' where id in (?1)")
    void deleteSections(List<Long> id);

}
