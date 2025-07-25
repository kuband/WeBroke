package com.backend.app.model.mapper.module;


import com.backend.app.model.dto.module.ModuleUpsertDTO;
import com.backend.app.model.entity.Module;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModuleUpsertMapper {

    ModuleUpsertDTO sourceToDestinationAllFields(Module source,
                                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "features", ignore = true)
    void updateModuleFromDto(Module dto, @MappingTarget Module entity);

    Module destinationToSourceAllFields(ModuleUpsertDTO destination,
                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<ModuleUpsertDTO> sourceToDestinationAllFields(List<Module> source,
                                                       @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Module> destinationToSourceAllFields(List<ModuleUpsertDTO> destination,
                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default ModuleUpsertDTO sourceToDestinationAllFields(Module source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Module destinationToSourceAllFields(ModuleUpsertDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<ModuleUpsertDTO> sourceToDestinationAllFields(List<Module> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Module> destinationToSourceAllFields(List<ModuleUpsertDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
