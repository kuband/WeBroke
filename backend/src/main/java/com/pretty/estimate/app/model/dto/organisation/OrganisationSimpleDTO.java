package com.we.broke.app.model.dto.organisation;

import com.we.broke.app.model.dto.TagDTO;
import com.we.broke.app.model.dto.worktype.WorkTypeSimpleDTO;
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
public class OrganisationSimpleDTO {
    private Long id;
    private String name;
    private Set<TagDTO> tags;
    private boolean organisationWorktypesAllowed;
    private boolean projectWorktypesAllowed;
    private boolean estimateWorktypesAllowed;
    private Set<WorkTypeSimpleDTO> organisationWorkTypes;
    private SubscriptionStatus subscriptionStatus;

    private Long createdBy;
    private Long lastModifyBy;
    private long createdDate;
    private long lastModifiedDate;
}