package com.we.broke.app.model.dto.userorganisation;

import com.we.broke.app.model.dto.organisation.OrganisationSimpleDTO;
import com.we.broke.auth.models.dto.RoleDTO;
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
public class UserOrganisationSimpleDTO {
    private Long id;
    private Set<RoleDTO> roles;
    private boolean active;
    private boolean joined;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
    private OrganisationSimpleDTO organisation;
}