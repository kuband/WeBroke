package com.backend.app.model.mapper.worktype;


import com.backend.app.model.dto.worktype.WorkTypeSimpleDTO;
import com.backend.app.model.entity.WorkType;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkTypeSimpleMapper {

    WorkTypeSimpleDTO sourceToDestinationAllFields(WorkType source,
                                                   @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    WorkType destinationToSourceAllFields(WorkTypeSimpleDTO destination,
                                          @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<WorkTypeSimpleDTO> sourceToDestinationAllFields(List<WorkType> source,
                                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<WorkType> destinationToSourceAllFields(List<WorkTypeSimpleDTO> destination,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default WorkTypeSimpleDTO sourceToDestinationAllFields(WorkType source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default WorkType destinationToSourceAllFields(WorkTypeSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkTypeSimpleDTO> sourceToDestinationAllFields(List<WorkType> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkType> destinationToSourceAllFields(List<WorkTypeSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
