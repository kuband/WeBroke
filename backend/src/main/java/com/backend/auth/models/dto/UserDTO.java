package com.backend.auth.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.backend.app.model.dto.userorganisation.UserOrganisationSimpleDTO;
import com.backend.app.swagger.KeepSwaggerJson;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    private Long id;
    private String email;
    //        @JsonIgnore
//        private String password;
    private Set<RoleDTO> roles;
    private String fullName;
    private boolean enabled;
    private boolean using2FA;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"user"})
    private Set<UserOrganisationSimpleDTO> userOrganisations;
}
