package com.we.broke.app.model.dto.worktypevalue;

import com.we.broke.app.model.dto.worktype.WorkTypeUpsertDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkTypeValueUpsertDTO {
    private Long id;
    private Long value;
    private WorkTypeUpsertDTO workType;
}
