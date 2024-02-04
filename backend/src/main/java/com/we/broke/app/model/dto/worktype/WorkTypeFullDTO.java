package com.we.broke.app.model.dto.worktype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.we.broke.app.model.dto.estimate.EstimateFullDTO;
import com.we.broke.app.model.dto.organisation.OrganisationFullDTO;
import com.we.broke.app.model.dto.project.ProjectFullDTO;
import com.we.broke.app.model.dto.worktypevalue.WorkTypeValueFullDTO;
import com.we.broke.app.swagger.KeepSwaggerJson;
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
public class WorkTypeFullDTO {
    private Long id;
    private String name;
    private Double price;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = "estimateWorkTypes")
    private EstimateFullDTO estimate;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = "organisationWorkTypes")
    private OrganisationFullDTO organisation;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = "projectWorkTypes")
    private ProjectFullDTO project;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"workType"})
    private Set<WorkTypeValueFullDTO> workTypeValues;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
