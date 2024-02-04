package com.we.broke.app.payload.response.userorganisation;

import com.we.broke.app.model.dto.userorganisation.UserOrganisationFullDTO;
import com.we.broke.common.model.dto.UserSecurityTokenDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserOrganisationFullResponse {

    private List<UserOrganisationFullDTO> userOrganisations;
    private List<UserSecurityTokenDTO> userSecurityTokens;
}
