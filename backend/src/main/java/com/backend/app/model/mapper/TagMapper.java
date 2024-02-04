package com.backend.app.model.mapper;


import com.backend.app.model.dto.TagDTO;
import com.backend.app.model.entity.Tag;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TagMapper {
    TagDTO sourceToDestinationAllFields(Tag source,
                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Tag destinationToSourceAllFields(TagDTO destination,
                                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<TagDTO> sourceToDestinationAllFields(List<Tag> source,
                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Tag> destinationToSourceAllFields(List<TagDTO> destination,
                                           @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default TagDTO sourceToDestinationAllFields(Tag source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Tag destinationToSourceAllFields(TagDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<TagDTO> sourceToDestinationAllFields(List<Tag> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Tag> destinationToSourceAllFields(List<TagDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
