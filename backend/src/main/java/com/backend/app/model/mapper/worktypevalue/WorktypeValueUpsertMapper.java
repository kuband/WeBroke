package com.backend.app.model.mapper.worktypevalue;


import com.backend.app.model.dto.worktypevalue.WorkTypeValueUpsertDTO;
import com.backend.app.model.entity.WorkTypeValue;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorktypeValueUpsertMapper {
    WorkTypeValueUpsertDTO sourceToDestinationAllFields(WorkTypeValue source,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    WorkTypeValue destinationToSourceAllFields(WorkTypeValueUpsertDTO destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    void updateWorkTypeValueFromDto(WorkTypeValue dto, @MappingTarget WorkTypeValue entity);

    List<WorkTypeValueUpsertDTO> sourceToDestinationAllFields(List<WorkTypeValue> source,
                                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<WorkTypeValue> destinationToSourceAllFields(List<WorkTypeValueUpsertDTO> destination,
                                                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default WorkTypeValueUpsertDTO sourceToDestinationAllFields(WorkTypeValue source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default WorkTypeValue destinationToSourceAllFields(WorkTypeValueUpsertDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkTypeValueUpsertDTO> sourceToDestinationAllFields(List<WorkTypeValue> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkTypeValue> destinationToSourceAllFields(List<WorkTypeValueUpsertDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
