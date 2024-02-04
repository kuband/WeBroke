package com.backend.app.payload.request.userorganisation;

import com.backend.app.model.dto.organisation.OrganisationUpsertDTO;
import com.backend.app.model.type.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserOrganisationUpsertRequest {

    private OrganisationUpsertDTO organisation;

    @NotNull
    private Operation operation;

}
