package com.we.broke.app.payload.response.organisation;

import com.we.broke.app.model.dto.organisation.OrganisationSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrganisationSimpleResponse {
    private List<OrganisationSimpleDTO> organisations;
}
