package com.backend.auth.models.mapper;

import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import com.backend.auth.models.dto.RoleDTO;
import com.backend.auth.models.entity.Role;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleDTO sourceToDestinationAllFields(Role source,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Role destinationToSourceAllFields(RoleDTO destination,
                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<RoleDTO> sourceToDestinationAllFields(List<Role> source,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Role> destinationToSourceAllFields(List<RoleDTO> destination,
                                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default RoleDTO sourceToDestinationAllFields(Role source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Role destinationToSourceAllFields(RoleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<RoleDTO> sourceToDestinationAllFields(List<Role> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Role> destinationToSourceAllFields(List<RoleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
