package com.backend.app.model.dto.project;

import com.backend.app.model.dto.TagDTO;
import com.backend.app.model.dto.estimate.EstimateSimpleDTO;
import com.backend.app.model.dto.worktype.WorkTypeSimpleDTO;
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
public class ProjectSimpleDTO {
    private Long id;
    private String name;
    private String currency;
    private LinkedHashSet<EstimateSimpleDTO> estimates;
    private Set<WorkTypeSimpleDTO> projectWorkTypes;
    private Set<TagDTO> tags;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
