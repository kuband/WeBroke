package com.backend.app.repository;

import com.backend.app.model.entity.Feature;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Transactional
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    @Modifying
    @Query("update Feature set modificationStatus='DELETED' where id in (?1)")
    void deleteFeatures(List<Long> id);

}
