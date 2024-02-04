package com.backend.app.payload.request.userorganisation;

import com.backend.app.model.dto.userorganisation.UserOrganisationUpsertDTO;
import com.backend.app.model.type.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserOrganisationRequest {

    private UserOrganisationUpsertDTO userOrganisation;

    @NotNull
    private Operation operation;

}
