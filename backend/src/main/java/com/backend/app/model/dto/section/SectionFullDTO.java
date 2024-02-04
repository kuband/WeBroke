package com.backend.app.model.dto.section;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.backend.app.model.dto.estimate.EstimateFullDTO;
import com.backend.app.model.dto.module.ModuleFullDTO;
import com.backend.app.swagger.KeepSwaggerJson;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashSet;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionFullDTO {
    private Long id;
    private String name;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"sections", "project"})
    private EstimateFullDTO estimate;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"section"})
    private LinkedHashSet<ModuleFullDTO> modules;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
