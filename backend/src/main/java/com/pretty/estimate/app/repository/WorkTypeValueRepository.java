package com.we.broke.app.repository;

import com.we.broke.app.model.entity.WorkTypeValue;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Transactional
public interface WorkTypeValueRepository extends JpaRepository<WorkTypeValue, Long> {

    @Modifying
    @Query("update WorkTypeValue set modificationStatus='DELETED' where id in (?1)")
    void deleteWtvs(List<Long> id);
}
