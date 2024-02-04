package com.backend.app.model.mapper;


import com.backend.app.model.dto.TemplateDTO;
import com.backend.app.model.entity.Template;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TemplateMapper {
    TemplateDTO sourceToDestinationAllFields(Template source,
                                             @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Template destinationToSourceAllFields(TemplateDTO destination,
                                          @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<TemplateDTO> sourceToDestinationAllFields(List<Template> source,
                                                   @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Template> destinationToSourceAllFields(List<TemplateDTO> destination,
                                                @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default TemplateDTO sourceToDestinationAllFields(Template source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Template destinationToSourceAllFields(TemplateDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<TemplateDTO> sourceToDestinationAllFields(List<Template> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Template> destinationToSourceAllFields(List<TemplateDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
