package com.backend.app.model.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.backend.app.model.Status;
import com.backend.app.model.dto.TagDTO;
import com.backend.app.model.dto.estimate.EstimateFullDTO;
import com.backend.app.model.dto.organisation.OrganisationFullDTO;
import com.backend.app.model.dto.worktype.WorkTypeFullDTO;
import com.backend.app.swagger.KeepSwaggerJson;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectFullDTO {
    private Long id;
    private String name;
    private String currency;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"project"})
    private LinkedHashSet<EstimateFullDTO> estimates;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"organisation", "estimate", "project"})
    private Set<WorkTypeFullDTO> projectWorkTypes;
    private Set<TagDTO> tags;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"organisationProjects"})
    private OrganisationFullDTO organisation;
    private Status status;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
