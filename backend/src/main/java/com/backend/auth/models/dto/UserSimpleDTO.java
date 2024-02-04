package com.backend.auth.models.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSimpleDTO {
    private Long id;
    private String email;
    private Set<RoleDTO> roles;
    private String fullName;
}
