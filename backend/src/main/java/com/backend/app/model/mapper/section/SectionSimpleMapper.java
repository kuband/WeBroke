package com.backend.app.model.mapper.section;


import com.backend.app.model.dto.section.SectionSimpleDTO;
import com.backend.app.model.entity.Section;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SectionSimpleMapper {

    SectionSimpleDTO sourceToDestinationAllFields(Section source,
                                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Section destinationToSourceAllFields(SectionSimpleDTO destination,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<SectionSimpleDTO> sourceToDestinationAllFields(List<Section> source,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Section> destinationToSourceAllFields(List<SectionSimpleDTO> destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default SectionSimpleDTO sourceToDestinationAllFields(Section source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Section destinationToSourceAllFields(SectionSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<SectionSimpleDTO> sourceToDestinationAllFields(List<Section> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Section> destinationToSourceAllFields(List<SectionSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
