package com.we.broke.app.payload.request.userorganisation;

import com.we.broke.app.model.dto.organisation.OrganisationUpsertDTO;
import com.we.broke.app.model.type.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserOrganisationUpsertRequest {

    private OrganisationUpsertDTO organisation;

    @NotNull
    private Operation operation;

}
