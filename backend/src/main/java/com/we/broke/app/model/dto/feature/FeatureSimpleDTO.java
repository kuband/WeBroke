package com.we.broke.app.model.dto.feature;

import com.we.broke.app.model.dto.task.TaskSimpleDTO;
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
public class FeatureSimpleDTO {
    private Long id;
    private String name;
    private LinkedHashSet<TaskSimpleDTO> tasks;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
