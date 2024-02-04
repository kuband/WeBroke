package com.backend.app.model.dto.estimate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.backend.app.model.dto.project.ProjectFullDTO;
import com.backend.app.model.dto.section.SectionFullDTO;
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
public class EstimateFullDTO {
    private Long id;
    private String name;
    private ProjectFullDTO project;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"estimate"})
    private LinkedHashSet<SectionFullDTO> sections;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"organisation", "estimate", "project"})
    private Set<WorkTypeFullDTO> estimateWorkTypes;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
