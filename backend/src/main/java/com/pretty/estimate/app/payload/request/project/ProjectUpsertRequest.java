package com.we.broke.app.payload.request.project;

import com.we.broke.app.model.dto.project.ProjectUpsertDTO;
import com.we.broke.app.model.type.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectUpsertRequest {

    private ProjectUpsertDTO project;

    @NotNull
    private Operation operation;

}
