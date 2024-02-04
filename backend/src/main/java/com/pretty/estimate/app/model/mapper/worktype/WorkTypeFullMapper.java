package com.we.broke.app.model.mapper.worktype;


import com.we.broke.app.model.dto.worktype.WorkTypeFullDTO;
import com.we.broke.app.model.entity.WorkType;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkTypeFullMapper {

    WorkTypeFullDTO sourceToDestinationAllFields(WorkType source,
                                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    WorkType destinationToSourceAllFields(WorkTypeFullDTO destination,
                                          @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<WorkTypeFullDTO> sourceToDestinationAllFields(List<WorkType> source,
                                                       @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<WorkType> destinationToSourceAllFields(List<WorkTypeFullDTO> destination,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default WorkTypeFullDTO sourceToDestinationAllFields(WorkType source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default WorkType destinationToSourceAllFields(WorkTypeFullDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkTypeFullDTO> sourceToDestinationAllFields(List<WorkType> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<WorkType> destinationToSourceAllFields(List<WorkTypeFullDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
