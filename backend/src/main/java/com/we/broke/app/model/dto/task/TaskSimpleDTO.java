package com.we.broke.app.model.dto.task;

import com.we.broke.app.model.dto.worktypevalue.WorkTypeValueSimpleDTO;
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
public class TaskSimpleDTO {
    private Long id;
    private String name;
    private LinkedHashSet<WorkTypeValueSimpleDTO> workTypeValues;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
