package com.we.broke.app.model.entity;

import com.we.broke.app.model.ModificationStatus;
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

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "work_type_value")
@SQLDelete(sql = "UPDATE work_type_value SET modification_status = 'DELETED' WHERE id=?")
@Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkTypeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_type_id")
    private WorkType workType;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    private Long value;

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
