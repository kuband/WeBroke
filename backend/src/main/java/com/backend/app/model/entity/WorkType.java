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

import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "work_type")
@SQLDelete(sql = "UPDATE work_type SET modification_status = 'DELETED' WHERE id=?")
@Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    @Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
    private Organisation organisation;

    @ManyToOne
    @JoinColumn(name = "estimate_id")
    @Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
    private Estimate estimate;

    @OneToMany(mappedBy = "workType")
    @Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
    @Builder.Default
    private Set<WorkTypeValue> workTypeValues = new HashSet<>();

    private String name;

    private Double price;

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
}
