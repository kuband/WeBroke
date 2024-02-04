package com.backend.app.model.dto.organisation;

import com.backend.app.model.dto.TagDTO;
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
public class OrganisationUpsertDTO {
    private Long id;
    private String name;
    private Set<TagDTO> tags;
    private boolean organisationWorktypesAllowed;
    private boolean projectWorktypesAllowed;
    private boolean estimateWorktypesAllowed;
}