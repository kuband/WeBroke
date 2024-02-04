package com.we.broke.app.model.mapper;


import com.we.broke.app.model.dto.ElementDTO;
import com.we.broke.app.model.entity.Element;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ElementMapper {
    ElementDTO sourceToDestinationAllFields(Element source,
                                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Element destinationToSourceAllFields(ElementDTO destination,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<ElementDTO> sourceToDestinationAllFields(List<Element> source,
                                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Element> destinationToSourceAllFields(List<ElementDTO> destination,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default ElementDTO sourceToDestinationAllFields(Element source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Element destinationToSourceAllFields(ElementDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<ElementDTO> sourceToDestinationAllFields(List<Element> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Element> destinationToSourceAllFields(List<ElementDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
