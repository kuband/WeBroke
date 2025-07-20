package com.backend.auth.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.backend.app.model.entity.UserOrganisation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @BatchSize(size = 10)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<UserOrganisation> userOrganisations = new HashSet<>();

    @NotBlank
    @Size(max = 50)
    private String fullName;

    private String avatarUrl;

    private Integer age;

    private boolean enabled;

    private boolean using2FA;

    public User(String email, String password, String fullName, String avatarUrl, Integer age,
                boolean enabled, boolean using2FA) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.age = age;
        this.enabled = enabled;
        this.using2FA = using2FA;
    }

    public User(String email, String password, String fullName,
                boolean enabled, boolean using2FA) {
        this(email, password, fullName, null, null, enabled, using2FA);
    }
}
