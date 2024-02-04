package com.backend.app.model.mapper.module;


import com.backend.app.model.dto.module.ModuleSimpleDTO;
import com.backend.app.model.entity.Module;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModuleSimpleMapper {

    ModuleSimpleDTO sourceToDestinationAllFields(Module source,
                                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Module destinationToSourceAllFields(ModuleSimpleDTO destination,
                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<ModuleSimpleDTO> sourceToDestinationAllFields(List<Module> source,
                                                       @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Module> destinationToSourceAllFields(List<ModuleSimpleDTO> destination,
                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default ModuleSimpleDTO sourceToDestinationAllFields(Module source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Module destinationToSourceAllFields(ModuleSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<ModuleSimpleDTO> sourceToDestinationAllFields(List<Module> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Module> destinationToSourceAllFields(List<ModuleSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
