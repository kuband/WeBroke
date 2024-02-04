package com.backend.app.payload.request;

import com.backend.app.model.dto.worktype.WorkTypeUpsertDTO;
import com.backend.app.model.type.Operation;
import com.backend.app.model.type.WorkTypeEnum;
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
