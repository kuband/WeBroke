package com.we.broke.app.model.mapper.project;


import com.we.broke.app.model.dto.project.ProjectUpsertDTO;
import com.we.broke.app.model.entity.Project;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
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
