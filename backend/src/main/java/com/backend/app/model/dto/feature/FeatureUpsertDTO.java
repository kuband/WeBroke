package com.backend.app.model.dto.feature;

import com.backend.app.model.dto.task.TaskUpsertDTO;
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
public class FeatureUpsertDTO {
    private Long id;
    private String name;
    private LinkedHashSet<TaskUpsertDTO> tasks;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
