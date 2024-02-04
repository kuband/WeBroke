package com.backend.app.model.mapper.feature;

import com.backend.app.model.dto.feature.FeatureFullDTO;
import com.backend.app.model.entity.Feature;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FeatureFullMapper {

    FeatureFullDTO sourceToDestinationAllFields(Feature source,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Feature destinationToSourceAllFields(FeatureFullDTO destination,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<FeatureFullDTO> sourceToDestinationAllFields(List<Feature> source,
                                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Feature> destinationToSourceAllFields(List<FeatureFullDTO> destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default FeatureFullDTO sourceToDestinationAllFields(Feature source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Feature destinationToSourceAllFields(FeatureFullDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<FeatureFullDTO> sourceToDestinationAllFields(List<Feature> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Feature> destinationToSourceAllFields(List<FeatureFullDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
