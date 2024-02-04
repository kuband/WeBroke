package com.we.broke.app.model.mapper;


import com.we.broke.app.model.dto.TagDTO;
import com.we.broke.app.model.entity.Tag;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
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
