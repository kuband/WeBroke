package com.we.broke.app.model.mapper.feature;


import com.we.broke.app.model.dto.feature.FeatureUpsertDTO;
import com.we.broke.app.model.entity.Feature;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FeatureUpsertMapper {

    FeatureUpsertDTO sourceToDestinationAllFields(Feature source,
                                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "tasks", ignore = true)
    void updateFeatureFromDto(Feature dto, @MappingTarget Feature entity);

    Feature destinationToSourceAllFields(FeatureUpsertDTO destination,
                                          @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<FeatureUpsertDTO> sourceToDestinationAllFields(List<Feature> source,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Feature> destinationToSourceAllFields(List<FeatureUpsertDTO> destination,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default FeatureUpsertDTO sourceToDestinationAllFields(Feature source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Feature destinationToSourceAllFields(FeatureUpsertDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<FeatureUpsertDTO> sourceToDestinationAllFields(List<Feature> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Feature> destinationToSourceAllFields(List<FeatureUpsertDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
