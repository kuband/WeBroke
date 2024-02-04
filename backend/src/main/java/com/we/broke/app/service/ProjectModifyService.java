package com.we.broke.app.service;

import com.we.broke.app.exception.OperationException;
import com.we.broke.app.exception.UnsupportedOperationException;
import com.we.broke.app.model.ModificationStatus;
import com.we.broke.app.model.dto.project.ProjectSimpleDTO;
import com.we.broke.app.model.dto.project.ProjectUpsertDTO;
import com.we.broke.app.model.entity.Module;
import com.we.broke.app.model.entity.*;
import com.we.broke.app.model.mapper.estimate.EstimateUpsertMapper;
import com.we.broke.app.model.mapper.feature.FeatureUpsertMapper;
import com.we.broke.app.model.mapper.module.ModuleUpsertMapper;
import com.we.broke.app.model.mapper.project.ProjectSimpleMapper;
import com.we.broke.app.model.mapper.project.ProjectUpsertMapper;
import com.we.broke.app.model.mapper.section.SectionUpsertMapper;
import com.we.broke.app.model.mapper.task.TaskUpsertMapper;
import com.we.broke.app.payload.request.project.ProjectUpsertRequest;
import com.we.broke.app.payload.response.project.ProjectSimpleResponse;
import com.we.broke.app.repository.*;
import com.we.broke.auth.security.services.UserDetailsImpl;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ProjectModifyService {

    private ProjectRepository projectRepository;
    private ProjectSimpleMapper projectSimpleMapper;
    private ProjectUpsertMapper projectUpsertMapper;
    private EstimateRepository estimateRepository;
    private EstimateUpsertMapper estimateUpsertMapper;
    private SectionRepository sectionRepository;
    private SectionUpsertMapper sectionUpsertMapper;
    private ModuleRepository moduleRepository;
    private ModuleUpsertMapper moduleUpsertMapper;
    private FeatureRepository featureRepository;
    private FeatureUpsertMapper featureUpsertMapper;
    private TaskRepository taskRepository;
    private TaskUpsertMapper taskUpsertMapper;
    private ProjectReadService projectReadService;
    private WorkTypeValueService workTypeValueService;
    private EntityManager entityManager;

    public ProjectSimpleResponse crudProject(ProjectUpsertRequest projectRequest,
                                             UserDetailsImpl userDetailsImpl) {
        //when creating, should only send one org id
        if (userDetailsImpl.getOrganisationIds().size() != 1) {
            throw new UnsupportedOperationException("Can only add/delete in one org at a time.");
        }

        return switch (projectRequest.getOperation()) {
            case DELETE -> deleteProject(Collections.singletonList(projectRequest.getProject().getId()));
            case UPSERT -> upsertProjectWithRemoval(projectRequest.getProject(), userDetailsImpl);
        };
    }

    @Transactional
    public ProjectSimpleResponse upsertProjectWithRemoval(ProjectUpsertDTO projectFullDto,
                                                          UserDetailsImpl userDetailsImpl) {
        try {
            if (projectFullDto.getId() != null && projectReadService.getUserProjects(userDetailsImpl).stream().noneMatch(project -> Objects.equals(project.getId(), projectFullDto.getId()))) {
                throw new UnsupportedOperationException("Not editor of the project Id");
            }

            Project newProject = projectUpsertMapper.destinationToSourceAllFields(projectFullDto);
            newProject.setOrganisation(Organisation.builder().id(userDetailsImpl.getOrganisationIds().get(0)).build());
            newProject.setModificationStatus(ModificationStatus.ACTIVE);

            Project projectEntity;
            if (projectFullDto.getId() != null) {
                projectEntity = projectRepository.getReferenceById(projectFullDto.getId());

                //set all the parent links
                Optional.ofNullable(newProject.getEstimates()).ifPresent(estimates ->
                        estimates.forEach(estimate -> {
                            estimate.setProject(newProject);
                            estimate.setModificationStatus(ModificationStatus.ACTIVE);
                            Optional.ofNullable(estimate.getSections()).ifPresent(sections -> sections
                                    .forEach(section -> {
                                        section.setEstimate(estimate);
                                        section.setModificationStatus(ModificationStatus.ACTIVE);
                                        Optional.ofNullable(section.getModules()).ifPresent(modules -> modules
                                                .forEach(module -> {
                                                    module.setSection(section);
                                                    module.setModificationStatus(ModificationStatus.ACTIVE);
                                                    Optional.ofNullable(module.getFeatures()).ifPresent(features ->
                                                            features.forEach(feature -> {
                                                                feature.setModule(module);
                                                                feature.setModificationStatus(ModificationStatus.ACTIVE);
                                                                Optional.ofNullable(feature.getTasks()).ifPresent(tasks ->
                                                                        tasks.forEach(task -> {
                                                                            task.setFeature(feature);
                                                                            task.setModificationStatus(ModificationStatus.ACTIVE);
                                                                            Optional.ofNullable(task.getWorkTypeValues()).ifPresent(workTypeValues -> {
                                                                                workTypeValues.forEach(workTypeValue -> {
                                                                                    workTypeValue.setTask(task);
                                                                                    workTypeValue.setModificationStatus(ModificationStatus.ACTIVE);
                                                                                });
                                                                                workTypeValueService.setWorkTypes(projectEntity.getId(), null, userDetailsImpl, workTypeValues);
                                                                            });
                                                                        }));
                                                            }));
                                                }));
                                    }));
                        }));

                List<Estimate> savedEstimates =
                        Optional.ofNullable(newProject.getEstimates()).stream().flatMap(Collection::stream).map(estimate -> {
                            estimate.setProject(projectEntity);
                            Estimate savedEntity = null;
                            if (estimate.getId() == null) {
                                //new estimate
                                savedEntity = estimate;
                            } else {
                                Estimate estimateEntity =
                                        estimateRepository.getReferenceById(estimate.getId());
                                //update sections
                                List<Section> savedSections =
                                        Optional.ofNullable(estimate.getSections()).stream().flatMap(Collection::stream).map(section -> {
                                            section.setEstimate(estimateEntity);
                                            Section savedSec = null;
                                            if (section.getId() == null) {
                                                //new section
                                                savedSec = section;
                                            } else {
                                                Section sectionEntity =
                                                        sectionRepository.getReferenceById(section.getId());
                                                //update modules
                                                List<Module> savedModules =
                                                        Optional.ofNullable(section.getModules()).stream().flatMap(Collection::stream).map(module -> {
                                                            module.setSection(sectionEntity);
                                                            Module savedModule = null;
                                                            if (module.getId() == null) {
                                                                //new module
                                                                savedModule = module;
                                                            } else {
                                                                //merge and update without disjoint modules
                                                                Module moduleEntity =
                                                                        moduleRepository.getReferenceById(module.getId());
                                                                //update features
                                                                List<Feature> savedFeatures =
                                                                        Optional.ofNullable(module.getFeatures()).stream().flatMap(Collection::stream).map(feature -> {
                                                                            feature.setModule(moduleEntity);
                                                                            Feature savedFeature = null;
                                                                            if (feature.getId() == null) {
                                                                                //new feature
                                                                                savedFeature = feature;
                                                                            } else {
                                                                                //merge and update without disjoint feature
                                                                                Feature featureEntity =
                                                                                        featureRepository.getReferenceById(feature.getId());
                                                                                //update tasks
                                                                                List<Task> savedTasks =
                                                                                        Optional.ofNullable(feature.getTasks()).stream().flatMap(Collection::stream).map(task -> {
                                                                                            task.setFeature(featureEntity);
                                                                                            Task savedTask = null;
                                                                                            if (task.getId() == null) {
                                                                                                //new task
                                                                                                savedTask = task;
                                                                                            } else {
                                                                                                //merge and update without disjoint task
                                                                                                Task taskEntity =
                                                                                                        taskRepository.getReferenceById(task.getId());
                                                                                                //remove, insert, update wtvs
                                                                                                List<WorkTypeValue> savedWtvs =
                                                                                                        workTypeValueService.upsertWtvs(projectEntity.getId(), estimateEntity.getId(),
                                                                                                                task.getWorkTypeValues(), taskEntity, userDetailsImpl);

                                                                                                taskUpsertMapper.updateTaskFromDto(task,
                                                                                                        taskEntity);
                                                                                                //clear missing and add new wtv
                                                                                                taskEntity.getWorkTypeValues().removeIf(wtv -> Optional.ofNullable(task.getWorkTypeValues()).stream().flatMap(Collection::stream).filter(Objects::nonNull).filter(wtvDto -> wtvDto.getId() != null).noneMatch(wtvDto -> wtvDto.getId().equals(wtv.getId())));
                                                                                                if (!CollectionUtils.isEmpty(savedWtvs)) {
                                                                                                    taskEntity.getWorkTypeValues().addAll(savedWtvs.stream().filter(Objects::nonNull).toList());
                                                                                                }
                                                                                                taskRepository.save(taskEntity);
                                                                                            }
                                                                                            return savedTask;
                                                                                        }).toList();
                                                                                featureUpsertMapper.updateFeatureFromDto(feature,
                                                                                        featureEntity);
                                                                                //clear missing and add new tasks
                                                                                featureEntity.getTasks().removeIf(task -> Optional.ofNullable(feature.getTasks()).stream().flatMap(Collection::stream).filter(Objects::nonNull).filter(taskDto -> taskDto.getId() != null).noneMatch(taskDto -> taskDto.getId().equals(task.getId())));
                                                                                if (!CollectionUtils.isEmpty(savedTasks)) {
                                                                                    featureEntity.getTasks().addAll(savedTasks.stream().filter(Objects::nonNull).toList());
                                                                                }
                                                                                featureRepository.save(featureEntity);
                                                                            }
                                                                            return savedFeature;
                                                                        }).toList();
                                                                moduleUpsertMapper.updateModuleFromDto(module,
                                                                        moduleEntity);
                                                                //clear missing and add new features,
                                                                moduleEntity.getFeatures().removeIf(feature -> Optional.ofNullable(module.getFeatures()).stream().flatMap(Collection::stream).filter(Objects::nonNull).filter(featureDto -> featureDto.getId() != null).noneMatch(featureDto -> featureDto.getId().equals(feature.getId())));
                                                                if (!CollectionUtils.isEmpty(savedFeatures)) {
                                                                    moduleEntity.getFeatures().addAll(savedFeatures.stream().filter(Objects::nonNull).toList());
                                                                }
                                                                moduleRepository.save(moduleEntity);
                                                            }
                                                            return savedModule;
                                                        }).toList();
                                                sectionUpsertMapper.updateSectionFromDto(section,
                                                        sectionEntity);
                                                //clear missing and add new modules,
                                                sectionEntity.getModules().removeIf(module -> Optional.ofNullable(section.getModules()).stream().flatMap(Collection::stream).filter(Objects::nonNull).filter(moduleDto -> moduleDto.getId() != null).noneMatch(moduleDto -> moduleDto.getId().equals(module.getId())));
                                                if (!CollectionUtils.isEmpty(savedModules)) {
                                                    sectionEntity.getModules().addAll(savedModules.stream().filter(Objects::nonNull).toList());
                                                }
                                                sectionRepository.save(sectionEntity);
                                            }
                                            return savedSec;
                                        }).toList();
//                                }
                                estimateUpsertMapper.updateEstimateFromDto(estimate, estimateEntity);
                                //clear missing and add new sections,
                                estimateEntity.getSections().removeIf(section -> Optional.ofNullable(estimate.getSections()).stream().flatMap(Collection::stream).filter(Objects::nonNull).filter(sectionDto -> sectionDto.getId() != null).noneMatch(sectionDto -> sectionDto.getId().equals(section.getId())));
                                if (!CollectionUtils.isEmpty(savedSections)) {
                                    estimateEntity.getSections().addAll(savedSections.stream().filter(Objects::nonNull).toList());
                                }
                                estimateRepository.save(estimateEntity);
                            }
                            return savedEntity;
                        }).toList();
                projectUpsertMapper.updateProjectFromDto(projectFullDto, projectEntity);
                //clear missing and add new estimates,
                projectEntity.getEstimates().removeIf(estimate -> Optional.ofNullable(projectFullDto.getEstimates()).stream().flatMap(Collection::stream).filter(Objects::nonNull).filter(estimateDTO -> estimateDTO.getId() != null).noneMatch(estimateDTO -> estimateDTO.getId().equals(estimate.getId())));
                if (!CollectionUtils.isEmpty(savedEstimates)) {
                    projectEntity.getEstimates().addAll(savedEstimates.stream().filter(Objects::nonNull).toList());
                }
            } else {
                if (!CollectionUtils.isEmpty(newProject.getEstimates())) {
                    throw new OperationException("Please create a clean project first");
                }
                projectEntity = newProject;
            }
            Project save = persistProject(projectEntity);
            return getProjectResponse(save);
        } catch (
                Exception e) {
            log.error("Exception:", e);
            throw new OperationException("Upsert failed");
        }

    }

    @Transactional
    private ProjectSimpleResponse getProjectResponse(Project save) {
        entityManager.refresh(save);
        List<ProjectSimpleDTO> projectFullDTOS =
                projectSimpleMapper.sourceToDestinationAllFields(Collections.singletonList(save));
        return new ProjectSimpleResponse(projectFullDTOS);
    }

    @Transactional
    private Project persistProject(Project project) {
        return projectRepository.saveAndFlush(project);
    }

    public ProjectSimpleResponse deleteProject(List<Long> projectId) {
        try {
            projectRepository.deleteProjects(projectId);
        } catch (Exception e) {
            throw new OperationException("Delete failed");
        }
        return new ProjectSimpleResponse(Collections.emptyList());
    }
}
