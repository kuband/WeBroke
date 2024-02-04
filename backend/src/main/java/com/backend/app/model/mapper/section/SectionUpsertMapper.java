package com.backend.app.model.mapper.section;


import com.backend.app.model.dto.section.SectionUpsertDTO;
import com.backend.app.model.entity.Section;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SectionUpsertMapper {

    SectionUpsertDTO sourceToDestinationAllFields(Section source,
                                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "modules", ignore = true)
    void updateSectionFromDto(Section dto, @MappingTarget Section entity);

    Section destinationToSourceAllFields(SectionUpsertDTO destination,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<SectionUpsertDTO> sourceToDestinationAllFields(List<Section> source,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Section> destinationToSourceAllFields(List<SectionUpsertDTO> destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default SectionUpsertDTO sourceToDestinationAllFields(Section source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Section destinationToSourceAllFields(SectionUpsertDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<SectionUpsertDTO> sourceToDestinationAllFields(List<Section> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Section> destinationToSourceAllFields(List<SectionUpsertDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
