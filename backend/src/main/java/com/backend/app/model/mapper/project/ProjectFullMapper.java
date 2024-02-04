package com.backend.app.model.mapper.project;


import com.backend.app.model.dto.project.ProjectFullDTO;
import com.backend.app.model.entity.Project;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectFullMapper {

    ProjectFullDTO sourceToDestinationAllFields(Project source,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Project destinationToSourceAllFields(ProjectFullDTO destination,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<ProjectFullDTO> sourceToDestinationAllFields(List<Project> source,
                                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Project> destinationToSourceAllFields(List<ProjectFullDTO> destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default ProjectFullDTO sourceToDestinationAllFields(Project source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Project destinationToSourceAllFields(ProjectFullDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<ProjectFullDTO> sourceToDestinationAllFields(List<Project> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Project> destinationToSourceAllFields(List<ProjectFullDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
