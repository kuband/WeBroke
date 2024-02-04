package com.we.broke.app.model.mapper.estimate;


import com.we.broke.app.model.dto.estimate.EstimateSimpleDTO;
import com.we.broke.app.model.entity.Estimate;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
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
