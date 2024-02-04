package com.we.broke.app.payload.request;

import com.we.broke.app.model.dto.worktype.WorkTypeUpsertDTO;
import com.we.broke.app.model.type.Operation;
import com.we.broke.app.model.type.WorkTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkTypeUpsertRequest {

    private WorkTypeUpsertDTO workTypeDTO;

    @NotNull
    private Operation operation;

    @NotNull
    private WorkTypeEnum workType;

}
