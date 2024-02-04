package com.backend.app.model.mapper.section;


import com.backend.app.model.dto.section.SectionFullDTO;
import com.backend.app.model.entity.Section;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SectionFullMapper {

    SectionFullDTO sourceToDestinationAllFields(Section source,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Section destinationToSourceAllFields(SectionFullDTO destination,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<SectionFullDTO> sourceToDestinationAllFields(List<Section> source,
                                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Section> destinationToSourceAllFields(List<SectionFullDTO> destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default SectionFullDTO sourceToDestinationAllFields(Section source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Section destinationToSourceAllFields(SectionFullDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<SectionFullDTO> sourceToDestinationAllFields(List<Section> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Section> destinationToSourceAllFields(List<SectionFullDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
