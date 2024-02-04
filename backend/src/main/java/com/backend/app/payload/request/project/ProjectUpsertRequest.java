package com.backend.app.payload.request.project;

import com.backend.app.model.dto.project.ProjectUpsertDTO;
import com.backend.app.model.type.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectUpsertRequest {

    private ProjectUpsertDTO project;

    @NotNull
    private Operation operation;

}
