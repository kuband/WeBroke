package com.backend.app.model.mapper.project;


import com.backend.app.model.dto.project.ProjectUpsertDTO;
import com.backend.app.model.entity.Project;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectUpsertMapper {

    ProjectUpsertDTO sourceToDestinationAllFields(Project source,
                                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "estimates", ignore = true)
    void updateProjectFromDto(ProjectUpsertDTO dto, @MappingTarget Project entity);

    Project destinationToSourceAllFields(ProjectUpsertDTO destination,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<ProjectUpsertDTO> sourceToDestinationAllFields(List<Project> source,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Project> destinationToSourceAllFields(List<ProjectUpsertDTO> destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default ProjectUpsertDTO sourceToDestinationAllFields(Project source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Project destinationToSourceAllFields(ProjectUpsertDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<ProjectUpsertDTO> sourceToDestinationAllFields(List<Project> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Project> destinationToSourceAllFields(List<ProjectUpsertDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
