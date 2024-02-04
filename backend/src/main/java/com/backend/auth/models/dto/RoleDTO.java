package com.backend.auth.models.dto;

import com.backend.auth.models.ERole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTO {
    private Integer id;
    private ERole name;
}