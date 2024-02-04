package com.backend.app.model.entity;

import com.backend.auth.models.entity.Role;
import com.backend.auth.models.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLJoinTableRestriction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_organisation")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class UserOrganisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "organisation_id", referencedColumnName = "id")
    @SQLJoinTableRestriction("modification_status <> 'DELETED'")
    private Organisation organisation;

    @ManyToMany
    @JoinTable(name = "organisation_roles",
            joinColumns = @JoinColumn(name = "user_organisation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @BatchSize(size = 10)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    //currently active/selected org for a user
    private boolean active;

    //has the user joined/accepted the invite
    private boolean joined;

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

    public UserOrganisation(User user, Organisation organisation) {
        this.user = user;
        this.organisation = organisation;
    }
}