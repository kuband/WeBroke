package com.backend.app.model.mapper.feature;


import com.backend.app.model.dto.feature.FeatureSimpleDTO;
import com.backend.app.model.entity.Feature;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FeatureSimpleMapper {

    FeatureSimpleDTO sourceToDestinationAllFields(Feature source,
                                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Feature destinationToSourceAllFields(FeatureSimpleDTO destination,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<FeatureSimpleDTO> sourceToDestinationAllFields(List<Feature> source,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Feature> destinationToSourceAllFields(List<FeatureSimpleDTO> destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default FeatureSimpleDTO sourceToDestinationAllFields(Feature source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Feature destinationToSourceAllFields(FeatureSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<FeatureSimpleDTO> sourceToDestinationAllFields(List<Feature> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Feature> destinationToSourceAllFields(List<FeatureSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
