package com.backend.app.model.mapper.worktypevalue;


import com.backend.app.model.dto.worktypevalue.WorkTypeValueFullDTO;
import com.backend.app.model.entity.WorkTypeValue;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorktypeValueFullMapper {
    WorkTypeValueFullDTO sourceToDestinationAllFields(WorkTypeValue source,
                                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    WorkTypeValue destinationToSourceAllFields(WorkTypeValueFullDTO destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    void updateWorkTypeValueFromDto(WorkTypeValue dto, @MappingTarget WorkTypeValue entity);

    List<WorkTypeValueFullDTO> sourceToDestinationAllFields(List<WorkTypeValue> source,
                                                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<WorkTypeValue> destinationToSourceAllFields(List<WorkTypeValueFullDTO> destination,
                                                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default WorkTypeValueFullDTO sourceToDestinationAllFields(WorkTypeValue source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default WorkTypeValue destinationToSourceAllFields(WorkTypeValueFullDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkTypeValueFullDTO> sourceToDestinationAllFields(List<WorkTypeValue> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkTypeValue> destinationToSourceAllFields(List<WorkTypeValueFullDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
