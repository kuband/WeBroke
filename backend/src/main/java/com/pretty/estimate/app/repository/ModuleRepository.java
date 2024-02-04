package com.we.broke.app.repository;

import com.we.broke.app.model.entity.Module;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Transactional
public interface ModuleRepository extends JpaRepository<Module, Long> {

    @Modifying
    @Query("update Module set modificationStatus='DELETED' where id in (?1)")
    void deleteModules(List<Long> id);

}
