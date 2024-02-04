package com.we.broke.app.model.dto;

import com.we.broke.app.model.dto.organisation.OrganisationFullDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientDTO {
    private Long id;
    private String email;
    private OrganisationFullDTO organisation;
    private String fullName;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
