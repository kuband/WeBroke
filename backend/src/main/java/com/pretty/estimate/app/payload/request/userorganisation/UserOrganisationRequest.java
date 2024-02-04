package com.we.broke.app.payload.request.userorganisation;

import com.we.broke.app.model.dto.userorganisation.UserOrganisationUpsertDTO;
import com.we.broke.app.model.type.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserOrganisationRequest {

    private UserOrganisationUpsertDTO userOrganisation;

    @NotNull
    private Operation operation;

}
