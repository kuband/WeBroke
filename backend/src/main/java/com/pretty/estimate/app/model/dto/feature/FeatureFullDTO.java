package com.we.broke.app.model.dto.feature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.we.broke.app.model.dto.module.ModuleFullDTO;
import com.we.broke.app.model.dto.task.TaskFullDTO;
import com.we.broke.app.swagger.KeepSwaggerJson;
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
public class FeatureFullDTO {
    private Long id;
    private String name;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"features", "section"})
    private ModuleFullDTO module;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"feature"})
    private LinkedHashSet<TaskFullDTO> tasks;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
