package com.backend.app.model.entity;

import com.backend.app.model.ModificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "estimate")
@SQLDelete(sql = "UPDATE Estimate SET modification_status = 'DELETED' WHERE id=?")
@Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Estimate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
    @OrderColumn
    @Builder.Default
    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "estimate")
    @Builder.Default
    private Set<WorkType> estimateWorkTypes = new HashSet<>();

    @Enumerated(value = EnumType.STRING)
    private ModificationStatus modificationStatus;

    @Column(nullable = false, updatable = false)
    @CreatedBy
    private Long createdBy;

    @LastModifiedBy
    private Long lastModifyBy;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private long createdDate;

    @LastModifiedDate
    private long lastModifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estimate that = (Estimate) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
