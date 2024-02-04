package com.we.broke.app.model.entity;

import com.we.broke.app.model.ModificationStatus;
import com.we.broke.payment.model.enumeration.SubscriptionStatus;
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
@Table(name = "organisation")
@SQLDelete(sql = "UPDATE Organisation SET modification_status = 'DELETED' WHERE id=?")
@Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    @OneToMany(mappedBy = "organisation")
    @Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
    @Builder.Default
    private Set<WorkType> organisationWorkTypes = new HashSet<>();

    @OneToMany(mappedBy = "organisation")
    @Filter(name = "deletedFilter", condition = "modification_status <> :isDeleted")
    @Builder.Default
    private Set<Project> organisationProjects = new HashSet<>();

    @OneToMany(mappedBy = "organisation")
    @Builder.Default
    private Set<UserOrganisation> userOrganisations = new HashSet<>();

    @OneToMany(mappedBy = "organisation")
    @Builder.Default
    private Set<Client> clients = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "organisation_tags",
            joinColumns = @JoinColumn(name = "organisation_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    private boolean organisationWorktypesAllowed;

    private boolean projectWorktypesAllowed;

    private boolean estimateWorktypesAllowed;

    @Enumerated(value = EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    @Enumerated(value = EnumType.STRING)
    private ModificationStatus modificationStatus;

    public Organisation(String name) {
        this.name = name;
        this.estimateWorktypesAllowed = true;
        this.modificationStatus = ModificationStatus.ACTIVE;
    }

    public Organisation(Long id) {
        this.id = id;
    }

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