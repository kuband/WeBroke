package com.backend.app.model.dto.task;

import com.backend.app.model.dto.worktypevalue.WorkTypeValueUpsertDTO;
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
public class TaskUpsertDTO {
    private Long id;
    private String name;
    private LinkedHashSet<WorkTypeValueUpsertDTO> workTypeValues;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
