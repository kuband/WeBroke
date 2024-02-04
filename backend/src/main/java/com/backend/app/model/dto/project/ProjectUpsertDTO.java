package com.backend.app.model.dto.project;

import com.backend.app.model.dto.TagDTO;
import com.backend.app.model.dto.estimate.EstimateUpsertDTO;
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
public class ProjectUpsertDTO {
    private Long id;
    private String name;
    private String currency;
    private LinkedHashSet<EstimateUpsertDTO> estimates;
    private Set<TagDTO> tags;
}
