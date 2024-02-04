package com.we.broke.app.model.dto.section;

import com.we.broke.app.model.dto.module.ModuleSimpleDTO;
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
public class SectionSimpleDTO {
    private Long id;
    private String name;
    private LinkedHashSet<ModuleSimpleDTO> modules;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
