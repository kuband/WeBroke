package com.backend.app.payload.request.userorganisation;

import lombok.Data;

@Data
public class UserOrganisationInviteRequest {
    private String email;
    private Long orgId;
}
