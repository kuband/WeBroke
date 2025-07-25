package com.backend.app.model.mapper.project;


import com.backend.app.model.dto.project.ProjectSimpleDTO;
import com.backend.app.model.entity.Project;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectSimpleMapper {

    ProjectSimpleDTO sourceToDestinationAllFields(Project source,
                                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Project destinationToSourceAllFields(ProjectSimpleDTO destination,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<ProjectSimpleDTO> sourceToDestinationAllFields(List<Project> source,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Project> destinationToSourceAllFields(List<ProjectSimpleDTO> destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default ProjectSimpleDTO sourceToDestinationAllFields(Project source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Project destinationToSourceAllFields(ProjectSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<ProjectSimpleDTO> sourceToDestinationAllFields(List<Project> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Project> destinationToSourceAllFields(List<ProjectSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
