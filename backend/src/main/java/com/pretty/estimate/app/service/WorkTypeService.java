package com.we.broke.app.service;

import com.we.broke.app.exception.OperationException;
import com.we.broke.app.exception.UnsupportedOperationException;
import com.we.broke.app.model.ModificationStatus;
import com.we.broke.app.model.dto.organisation.OrganisationUpsertDTO;
import com.we.broke.app.model.dto.worktype.WorkTypeSimpleDTO;
import com.we.broke.app.model.dto.worktype.WorkTypeUpsertDTO;
import com.we.broke.app.model.entity.WorkType;
import com.we.broke.app.model.mapper.worktype.WorkTypeFullMapper;
import com.we.broke.app.model.mapper.worktype.WorkTypeSimpleMapper;
import com.we.broke.app.model.mapper.worktype.WorkTypeUpsertMapper;
import com.we.broke.app.model.type.WorkTypeEnum;
import com.we.broke.app.payload.request.WorkTypeUpsertRequest;
import com.we.broke.app.payload.response.WorktypeResponse;
import com.we.broke.app.repository.WorkTypeRepository;
import com.we.broke.auth.models.ERole;
import com.we.broke.auth.security.services.UserDetailsImpl;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class WorkTypeService {

    private WorkTypeRepository workTypeRepository;
    private WorkTypeFullMapper workTypeFullMapper;
    private WorkTypeSimpleMapper workTypeSimpleMapper;
    private WorkTypeUpsertMapper workTypeUpsertMapper;
    private UserOrganisationService userOrganisationService;
    private ProjectReadService projectService;
    private EntityManager entityManager;

    public WorktypeResponse crudWorktype(WorkTypeUpsertRequest workTypeUpsertRequest,
                                         UserDetailsImpl userDetailsImpl) {
        //when creating, should only send one org id
        if (userDetailsImpl.getOrganisationIds().size() != 1) {
            throw new UnsupportedOperationException("Can only add/delete in one org at a time.");
        }

        //if org, get users org
        if (workTypeUpsertRequest.getWorkType() == WorkTypeEnum.ORG) {
            workTypeUpsertRequest.getWorkTypeDTO().setOrganisation(OrganisationUpsertDTO.builder().id(userDetailsImpl.getOrganisationIds().get(0)).build());
        }

        return switch (workTypeUpsertRequest.getOperation()) {
            case DELETE -> deleteWorktype(Collections.singletonList(workTypeUpsertRequest.getWorkTypeDTO().getId()));
            case UPSERT -> upsertWorktypeDto(workTypeUpsertRequest.getWorkTypeDTO(), userDetailsImpl);
        };
    }

    public List<WorkType> getWorktypesById(final String id) {
        try {
            if (!StringUtils.hasText(id)) {
                throw new UnsupportedOperationException("Need worktype Id");
            }
            List<Long> orgs = Collections.singletonList(Long.valueOf(id));
            //filter deleted worktype
            Session session = entityManager.unwrap(Session.class);
            Filter filter = session.enableFilter("deletedFilter");
            filter.setParameter("isDeleted", "DELETED");
            Optional<List<WorkType>> byOrganisationIds =
                    Optional.of(workTypeRepository.findAllById(orgs));
            return byOrganisationIds.map(worktypes -> worktypes.stream().toList()).orElse(Collections.emptyList());
        } catch (NumberFormatException e) {
            throw new UnsupportedOperationException("Wrong worktype number");
        }
    }

    public List<WorkType> getWorktypesByOrg(UserDetailsImpl userDetailsImpl, final String id) {
        try {
            List<Long> orgs;
            if (!StringUtils.hasText(id)) {
                orgs = userDetailsImpl.getOrganisationIds().stream().toList();
            } else {
                orgs = Collections.singletonList(Long.valueOf(id));
            }
            //filter deleted worktype
            Session session = entityManager.unwrap(Session.class);
            Filter filter = session.enableFilter("deletedFilter");
            filter.setParameter("isDeleted", "DELETED");
            Optional<List<WorkType>> byOrganisationIds =
                    workTypeRepository.findAllByOrganisationIdIn(orgs);
            return byOrganisationIds.map(worktypes -> worktypes.stream().toList()).orElse(Collections.emptyList());
        } catch (NumberFormatException e) {
            throw new UnsupportedOperationException("Wrong org number");
        }
    }

    public List<WorkType> getWorktypesByProject(final String id) {
        try {
            if (!StringUtils.hasText(id)) {
                throw new UnsupportedOperationException("Need project Id");
            }
            List<Long> orgs = Collections.singletonList(Long.valueOf(id));
            //filter deleted worktype
            Session session = entityManager.unwrap(Session.class);
            Filter filter = session.enableFilter("deletedFilter");
            filter.setParameter("isDeleted", "DELETED");
            Optional<List<WorkType>> byOrganisationIds =
                    workTypeRepository.findAllByProjectIdIn(orgs);
            return byOrganisationIds.map(worktypes -> worktypes.stream().toList()).orElse(Collections.emptyList());
        } catch (NumberFormatException e) {
            throw new UnsupportedOperationException("Wrong project number");
        }
    }

    public List<WorkType> getWorktypesByEstimate(final String id) {
        try {
            if (!StringUtils.hasText(id)) {
                throw new UnsupportedOperationException("Need estimate Id");
            }
            List<Long> orgs = Collections.singletonList(Long.valueOf(id));
            //filter deleted worktype
            Session session = entityManager.unwrap(Session.class);
            Filter filter = session.enableFilter("deletedFilter");
            filter.setParameter("isDeleted", "DELETED");
            Optional<List<WorkType>> byOrganisationIds =
                    workTypeRepository.findAllByEstimateIdIn(orgs);
            return byOrganisationIds.map(worktypes -> worktypes.stream().toList()).orElse(Collections.emptyList());
        } catch (NumberFormatException e) {
            throw new UnsupportedOperationException("Wrong estimate number");
        }
    }

    public List<WorkTypeSimpleDTO> readWorktypesByOrg(UserDetailsImpl userDetailsImpl, final String id) {
        return workTypeSimpleMapper.sourceToDestinationAllFields(getWorktypesByOrg(userDetailsImpl, id));
    }

    public List<WorkTypeSimpleDTO> readWorktypesByProject(final String id) {
        return workTypeSimpleMapper.sourceToDestinationAllFields(getWorktypesByProject(id));
    }

    public List<WorkTypeSimpleDTO> readWorktypesByEstimate(final String id) {
        return workTypeSimpleMapper.sourceToDestinationAllFields(getWorktypesByEstimate(id));
    }


    @Transactional
    public WorktypeResponse upsertWorktypeDto(WorkTypeUpsertDTO workTypeFullDTO,
                                              UserDetailsImpl userDetailsImpl) {
        WorkType save = upsertWorktype(Collections.singletonList(workTypeUpsertMapper.destinationToSourceAllFields(workTypeFullDTO)),
                userDetailsImpl).get(0);
        return new WorktypeResponse(workTypeSimpleMapper.sourceToDestinationAllFields(Collections.singletonList(save)));
    }

    public List<WorkType> upsertWorktype(List<WorkType> workTypes,
                                         UserDetailsImpl userDetailsImpl) {
        try {
            workTypes.forEach(workType -> {
                validateCreation(workType, userDetailsImpl);
                workType.setModificationStatus(ModificationStatus.ACTIVE);
            });

            return upsertWorktypeNoValidation(workTypes);
        } catch (Exception e) {
            throw new OperationException("Upsert failed");
        }
    }

    public List<WorkType> upsertWorktypeNoValidation(List<WorkType> workTypes) {
        try {
            return workTypeRepository.saveAll(workTypes);
        } catch (Exception e) {
            throw new OperationException("Upsert failed");
        }
    }

    public boolean validateCreation(WorkType workType, UserDetailsImpl userDetailsImpl) {
        if (workType.getOrganisation() == null && workType.getProject() == null && workType.getEstimate() == null) {
            throw new OperationException("Need to provide Project/Estimate/Org");
        }
        //check if admin of org
        if (workType.getOrganisation() != null && !userDetailsImpl.getOrganisationIds().contains(workType.getOrganisation().getId())
                && userOrganisationService.getOnlyUserOrganisations(userDetailsImpl).stream().noneMatch(userOrganisation ->
                userOrganisation.getOrganisation().getId().equals(workType.getOrganisation().getId()) &&
                        userOrganisation.getRoles().stream().noneMatch(role -> role.getName() == ERole.ROLE_ADMIN))) {
            throw new OperationException("Not admin of the org");
        }
        //check if user belongs to given project
        if (workType.getProject() != null && workType.getProject().getId() != null &&
                projectService.getUserProjects(userDetailsImpl).stream().noneMatch(project -> project.getId().equals(workType.getProject().getId()))) {
            throw new OperationException("Not editor of the project");
        }
        if (workType.getId() != null && workType.getProject().getId() != null && getWorktypesById(workType.getId().toString()).stream().noneMatch(workTypeInner ->
                Objects.equals(workTypeInner.getProject().getId(), workType.getProject().getId()))) {
            throw new UnsupportedOperationException("Worktype is not part of any project");
        }
        //check if user belongs to given estimate
        if (workType.getEstimate() != null && workType.getEstimate().getId() != null &&
                projectService.getUserProjects(userDetailsImpl).stream().noneMatch(project -> project.getEstimates().stream().anyMatch(estimate ->
                        estimate.getId().equals(workType.getEstimate().getId())))) {
            throw new OperationException("Not editor of the estimate");
        }
        return true;
    }

    public boolean validateJoining(Long projectId, Long estimateId, WorkType workType, UserDetailsImpl userDetailsImpl) {
        //check if org
        if (workType.getOrganisation() != null && !userDetailsImpl.getOrganisationIds().contains(workType.getOrganisation().getId())) {
            throw new OperationException("Not part of the org to use the worktype");
        }
        //check if project
        if (workType.getProject() != null && workType.getProject().getId() != null && !workType.getProject().getId().equals(projectId)) {
            throw new OperationException("Not belong to the project");
        }
        //check if estimate
        if (workType.getEstimate() != null && workType.getEstimate().getId() != null && !workType.getEstimate().getId().equals(estimateId)) {
            throw new OperationException("Not belong to the estimate");
        }
        return true;
    }

    public WorktypeResponse deleteWorktype(List<Long> workTypeId) {
        try {
            workTypeRepository.deleteWorkTypes(workTypeId);
        } catch (Exception e) {
            throw new OperationException("Delete failed");
        }
        return new WorktypeResponse(Collections.emptyList());
    }
}
