package com.we.broke.app.model.dto.worktypevalue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.we.broke.app.model.dto.task.TaskFullDTO;
import com.we.broke.app.model.dto.worktype.WorkTypeSimpleDTO;
import com.we.broke.app.swagger.KeepSwaggerJson;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkTypeValueFullDTO {
    private Long id;
    private WorkTypeSimpleDTO workType;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"workTypeValues", "feature"})
    private TaskFullDTO task;
    private Long value;
    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}
