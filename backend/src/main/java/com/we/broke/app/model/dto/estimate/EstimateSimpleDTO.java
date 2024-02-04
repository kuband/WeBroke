package com.we.broke.app.model.dto.estimate;

import com.we.broke.app.model.dto.section.SectionSimpleDTO;
import com.we.broke.app.model.dto.worktype.WorkTypeSimpleDTO;
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
public class EstimateSimpleDTO {
    private Long id;
    private String name;
    private LinkedHashSet<SectionSimpleDTO> sections;
    private Set<WorkTypeSimpleDTO> estimateWorkTypes;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
