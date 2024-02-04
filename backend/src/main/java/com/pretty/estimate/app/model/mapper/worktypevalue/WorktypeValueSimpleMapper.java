package com.we.broke.app.model.mapper.worktypevalue;


import com.we.broke.app.model.dto.worktypevalue.WorkTypeValueSimpleDTO;
import com.we.broke.app.model.entity.WorkTypeValue;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorktypeValueSimpleMapper {
    WorkTypeValueSimpleDTO sourceToDestinationAllFields(WorkTypeValue source,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    WorkTypeValue destinationToSourceAllFields(WorkTypeValueSimpleDTO destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    void updateWorkTypeValueFromDto(WorkTypeValue dto, @MappingTarget WorkTypeValue entity);

    List<WorkTypeValueSimpleDTO> sourceToDestinationAllFields(List<WorkTypeValue> source,
                                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<WorkTypeValue> destinationToSourceAllFields(List<WorkTypeValueSimpleDTO> destination,
                                                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default WorkTypeValueSimpleDTO sourceToDestinationAllFields(WorkTypeValue source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default WorkTypeValue destinationToSourceAllFields(WorkTypeValueSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkTypeValueSimpleDTO> sourceToDestinationAllFields(List<WorkTypeValue> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkTypeValue> destinationToSourceAllFields(List<WorkTypeValueSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
