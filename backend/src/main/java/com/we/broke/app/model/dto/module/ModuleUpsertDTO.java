package com.we.broke.app.model.dto.module;

import com.we.broke.app.model.dto.feature.FeatureUpsertDTO;
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
public class ModuleUpsertDTO {
    private Long id;
    private String name;
    private LinkedHashSet<FeatureUpsertDTO> features;
}
