package com.we.broke.app.model.mapper.project;


import com.we.broke.app.model.dto.project.ProjectFullDTO;
import com.we.broke.app.model.entity.Project;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
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
