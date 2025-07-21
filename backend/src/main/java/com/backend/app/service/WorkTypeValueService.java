package com.backend.app.service;

import com.backend.app.exception.OperationException;
import com.backend.app.model.entity.Task;
import com.backend.app.model.entity.WorkType;
import com.backend.app.model.entity.WorkTypeValue;
import com.backend.app.model.mapper.worktypevalue.WorktypeValueFullMapper;
import com.backend.app.payload.response.WtvResponse;
import com.backend.app.repository.WorkTypeValueRepository;
import com.backend.auth.security.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class WorkTypeValueService {

    private WorkTypeService workTypeService;
    private WorkTypeValueRepository workTypeValueRepository;
    private WorktypeValueFullMapper worktypeValueFullMapper;

    //TODO
//    public ProjectResponse crudWtv(ProjectRequest projectRequest,
//                                   UserDetailsImpl userDetailsImpl) {
//        //when creating, should only send one org id
//        if (userDetailsImpl.getOrganisationIds().size() != 1) {
//            throw new UnsupportedOperationException("Can only add/delete in one org at a time.");
//        }
//        projectRequest.getProject().setOrganisation(OrganisationDTO.builder().id(userDetailsImpl.getOrganisationIds().get(0)).build());
//
//        return switch (projectRequest.getOperation()) {
//            case DELETE -> deleteProject(projectRequest.getProject().getId());
//            case UPSERT -> upsertProject(projectRequest.getProject(), userDetailsImpl);
//        };
//    }

    @Transactional
    public List<WorkTypeValue> upsertWtvs(Long projectId, Long estimateId, List<WorkTypeValue> wtvs, Task taskEntity,
                                          UserDetailsImpl userDetailsImpl) {

        return Optional.ofNullable(wtvs).stream().flatMap(Collection::stream).map(wtv -> {
            wtv.setTask(taskEntity);
            WorkTypeValue savedWtv = null;
            if (wtv.getId() == null) {
                //new wtv
                setWorkTypes(projectId, estimateId, userDetailsImpl, Collections.singletonList(wtv));
                savedWtv = wtv;
            } else {
                //Update existing wtv
                WorkTypeValue wtvEntity =
                        workTypeValueRepository.getReferenceById(wtv.getId());
                worktypeValueFullMapper.updateWorkTypeValueFromDto(wtv,
                        wtvEntity);
                setWorkTypes(projectId, estimateId, userDetailsImpl, Collections.singletonList(wtv));
                savedWtv = persistWtv(wtvEntity);
            }
            return savedWtv;
        }).toList();
    }

    public void setWorkTypes(Long projectId, Long estimateId, UserDetailsImpl userDetailsImpl, List<WorkTypeValue> workTypeValues) {
        //check for any empty worktypes
        List<WorkType> workTypes = new ArrayList<>(workTypeValues
                .stream().map(WorkTypeValue::getWorkType)
                .filter(Objects::nonNull).collect(Collectors.toMap
                        (WorkType::getId, Function.identity(), (workType,
                                                                workType2) -> {
                            throw new OperationException("Can't have duplicate worktypes for one task");
                        })).values());
        if (workTypeValues.stream().anyMatch(workTypeValue ->
                workTypeValue.getWorkType() == null)) {
            throw new OperationException("Not worktype selected");
        }

        //update worktypesvalue with the given worktype
        workTypes.forEach(workType -> {
            WorkType workTypeEntity =
                    workTypeService.getWorktypesById(workType.getId
                            ().toString()).get(0);
            workTypeService.validateJoining(projectId, estimateId, workTypeEntity, userDetailsImpl);
            List<WorkTypeValue> matchedWorktypes = workTypeValues
                    .stream().filter(workTypeValue -> Objects.equals
                            (workTypeValue.getWorkType().getId(), workTypeEntity.getId())
                    ).toList();
            matchedWorktypes.forEach(mtvs -> mtvs.setWorkType
                    (workTypeEntity));
        });
    }

    @Transactional
    private WorkTypeValue persistWtv(WorkTypeValue wtvEntity) {
        return workTypeValueRepository.saveAndFlush(wtvEntity);
    }

    @Transactional
    private List<WorkTypeValue> persistWtvs(List<WorkTypeValue> wtvEntities) {
        return workTypeValueRepository.saveAllAndFlush(wtvEntities);
    }

    @Transactional
    public WtvResponse deleteWtv(List<Long> wtvId) {
        try {
            workTypeValueRepository.deleteWtvs(wtvId);
        } catch (Exception e) {
            throw new OperationException("Delete failed");
        }
        return new WtvResponse(Collections.emptyList());
    }
}
