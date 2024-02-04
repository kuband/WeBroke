package com.backend.app.model.mapper.estimate;


import com.backend.app.model.dto.estimate.EstimateUpsertDTO;
import com.backend.app.model.entity.Estimate;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EstimateUpsertMapper {

    EstimateUpsertDTO sourceToDestinationAllFields(Estimate source,
                                                   @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "sections", ignore = true)
    void updateEstimateFromDto(Estimate dto, @MappingTarget Estimate entity);

    Estimate destinationToSourceAllFields(EstimateUpsertDTO destination,
                                          @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<EstimateUpsertDTO> sourceToDestinationAllFields(List<Estimate> source,
                                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Estimate> destinationToSourceAllFields(List<EstimateUpsertDTO> destination,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default EstimateUpsertDTO sourceToDestinationAllFields(Estimate source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Estimate destinationToSourceAllFields(EstimateUpsertDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<EstimateUpsertDTO> sourceToDestinationAllFields(List<Estimate> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Estimate> destinationToSourceAllFields(List<EstimateUpsertDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
