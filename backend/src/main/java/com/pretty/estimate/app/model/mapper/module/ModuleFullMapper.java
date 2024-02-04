package com.we.broke.app.model.mapper.module;


import com.we.broke.app.model.dto.module.ModuleFullDTO;
import com.we.broke.app.model.entity.Module;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModuleFullMapper {

    ModuleFullDTO sourceToDestinationAllFields(Module source,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Module destinationToSourceAllFields(ModuleFullDTO destination,
                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<ModuleFullDTO> sourceToDestinationAllFields(List<Module> source,
                                                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Module> destinationToSourceAllFields(List<ModuleFullDTO> destination,
                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default ModuleFullDTO sourceToDestinationAllFields(Module source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Module destinationToSourceAllFields(ModuleFullDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<ModuleFullDTO> sourceToDestinationAllFields(List<Module> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Module> destinationToSourceAllFields(List<ModuleFullDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
