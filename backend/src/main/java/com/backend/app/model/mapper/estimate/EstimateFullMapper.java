package com.backend.app.model.mapper.estimate;


import com.backend.app.model.dto.estimate.EstimateFullDTO;
import com.backend.app.model.entity.Estimate;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EstimateFullMapper {

    EstimateFullDTO sourceToDestinationAllFields(Estimate source,
                                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Estimate destinationToSourceAllFields(EstimateFullDTO destination,
                                          @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<EstimateFullDTO> sourceToDestinationAllFields(List<Estimate> source,
                                                       @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Estimate> destinationToSourceAllFields(List<EstimateFullDTO> destination,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default EstimateFullDTO sourceToDestinationAllFields(Estimate source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Estimate destinationToSourceAllFields(EstimateFullDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<EstimateFullDTO> sourceToDestinationAllFields(List<Estimate> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Estimate> destinationToSourceAllFields(List<EstimateFullDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
