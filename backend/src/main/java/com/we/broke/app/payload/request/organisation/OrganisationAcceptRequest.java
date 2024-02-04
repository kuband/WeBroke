package com.we.broke.app.payload.request.organisation;

import lombok.Data;

@Data
public class OrganisationAcceptRequest {

    private String token;

    private boolean accept;

}
