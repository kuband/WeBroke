package com.backend.app.model.mapper.worktype;


import com.backend.app.model.dto.worktype.WorkTypeUpsertDTO;
import com.backend.app.model.entity.WorkType;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkTypeUpsertMapper {

    WorkTypeUpsertDTO sourceToDestinationAllFields(WorkType source,
                                                   @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    WorkType destinationToSourceAllFields(WorkTypeUpsertDTO destination,
                                          @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<WorkTypeUpsertDTO> sourceToDestinationAllFields(List<WorkType> source,
                                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<WorkType> destinationToSourceAllFields(List<WorkTypeUpsertDTO> destination,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default WorkTypeUpsertDTO sourceToDestinationAllFields(WorkType source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default WorkType destinationToSourceAllFields(WorkTypeUpsertDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkTypeUpsertDTO> sourceToDestinationAllFields(List<WorkType> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkType> destinationToSourceAllFields(List<WorkTypeUpsertDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
