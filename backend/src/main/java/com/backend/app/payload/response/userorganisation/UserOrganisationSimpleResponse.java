package com.backend.app.payload.response.userorganisation;

import com.backend.app.model.dto.userorganisation.UserOrganisationSimpleDTO;
import com.backend.common.model.dto.UserSecurityTokenDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserOrganisationSimpleResponse {

    private List<UserOrganisationSimpleDTO> userOrganisations;
    private List<UserSecurityTokenDTO> userSecurityTokens;
}
