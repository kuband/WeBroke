package com.backend.app.model.mapper.estimate;


import com.backend.app.model.dto.estimate.EstimateSimpleDTO;
import com.backend.app.model.entity.Estimate;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EstimateSimpleMapper {

    EstimateSimpleDTO sourceToDestinationAllFields(Estimate source,
                                                   @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Estimate destinationToSourceAllFields(EstimateSimpleDTO destination,
                                          @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<EstimateSimpleDTO> sourceToDestinationAllFields(List<Estimate> source,
                                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Estimate> destinationToSourceAllFields(List<EstimateSimpleDTO> destination,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default EstimateSimpleDTO sourceToDestinationAllFields(Estimate source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Estimate destinationToSourceAllFields(EstimateSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<EstimateSimpleDTO> sourceToDestinationAllFields(List<Estimate> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Estimate> destinationToSourceAllFields(List<EstimateSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
