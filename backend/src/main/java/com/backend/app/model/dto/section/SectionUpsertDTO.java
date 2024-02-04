package com.backend.app.model.dto.section;

import com.backend.app.model.dto.module.ModuleUpsertDTO;
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
public class SectionUpsertDTO {
    private Long id;
    private String name;
    private LinkedHashSet<ModuleUpsertDTO> modules;
}
