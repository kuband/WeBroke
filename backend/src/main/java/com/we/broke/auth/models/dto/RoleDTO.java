package com.we.broke.auth.models.dto;

import com.we.broke.auth.models.ERole;
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