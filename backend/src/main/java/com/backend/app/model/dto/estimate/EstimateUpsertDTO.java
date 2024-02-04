package com.backend.app.model.dto.estimate;

import com.backend.app.model.dto.section.SectionUpsertDTO;
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
public class EstimateUpsertDTO {
    private Long id;
    private String name;
    private LinkedHashSet<SectionUpsertDTO> sections;
}
