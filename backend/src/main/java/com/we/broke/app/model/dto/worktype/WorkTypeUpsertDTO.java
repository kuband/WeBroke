package com.we.broke.app.model.dto.worktype;

import com.we.broke.app.model.dto.estimate.EstimateUpsertDTO;
import com.we.broke.app.model.dto.organisation.OrganisationUpsertDTO;
import com.we.broke.app.model.dto.project.ProjectUpsertDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkTypeUpsertDTO {
    private Long id;
    private String name;
    private Double price;
    private OrganisationUpsertDTO organisation;
    private ProjectUpsertDTO project;
    private EstimateUpsertDTO estimate;
}
