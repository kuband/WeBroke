package com.backend.app.model.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.backend.app.model.dto.feature.FeatureFullDTO;
import com.backend.app.model.dto.worktypevalue.WorkTypeValueFullDTO;
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
public class TaskFullDTO {
    private Long id;
    private String name;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"tasks", "module"})
    private FeatureFullDTO feature;
    @KeepSwaggerJson
    @JsonIgnoreProperties({"task"})
    private LinkedHashSet<WorkTypeValueFullDTO> workTypeValues;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
