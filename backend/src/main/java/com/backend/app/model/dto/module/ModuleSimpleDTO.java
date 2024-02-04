package com.backend.app.model.dto.module;

import com.backend.app.model.dto.feature.FeatureSimpleDTO;
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
public class ModuleSimpleDTO {
    private Long id;
    private String name;
    private LinkedHashSet<FeatureSimpleDTO> features;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
