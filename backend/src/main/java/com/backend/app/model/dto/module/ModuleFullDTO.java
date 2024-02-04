package com.backend.app.model.dto.module;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.backend.app.model.dto.feature.FeatureFullDTO;
import com.backend.app.model.dto.section.SectionFullDTO;
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
public class ModuleFullDTO {
    private Long id;
    private String name;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"estimate", "modules"})
    private SectionFullDTO section;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"module"})
    private LinkedHashSet<FeatureFullDTO> features;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
