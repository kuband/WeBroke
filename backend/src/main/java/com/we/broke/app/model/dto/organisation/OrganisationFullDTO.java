package com.we.broke.app.model.dto.organisation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.we.broke.app.model.dto.ClientDTO;
import com.we.broke.app.model.dto.TagDTO;
import com.we.broke.app.model.dto.project.ProjectFullDTO;
import com.we.broke.app.model.dto.userorganisation.UserOrganisationFullDTO;
import com.we.broke.app.model.dto.worktype.WorkTypeFullDTO;
import com.we.broke.app.swagger.KeepSwaggerJson;
import com.we.broke.payment.model.enumeration.SubscriptionStatus;
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
public class OrganisationFullDTO {
    private Long id;
    private String name;
    private Set<TagDTO> tags;
    private boolean organisationWorktypesAllowed;
    private boolean projectWorktypesAllowed;
    private boolean estimateWorktypesAllowed;
    private SubscriptionStatus subscriptionStatus;

    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"organisation", "estimate", "project"})
    private Set<WorkTypeFullDTO> organisationWorkTypes;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"organisation"})
    private Set<ProjectFullDTO> organisationProjects;
    private Set<UserOrganisationFullDTO> userOrganisations;
    @KeepSwaggerJson
    @JsonIgnoreProperties(value = {"organisation"})
    private Set<ClientDTO> clients;
}