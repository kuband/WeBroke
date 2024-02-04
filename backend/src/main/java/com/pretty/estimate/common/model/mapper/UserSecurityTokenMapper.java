package com.we.broke.common.model.mapper;

import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
import com.we.broke.common.model.dto.UserSecurityTokenDTO;
import com.we.broke.common.model.entity.UserSecurityToken;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserSecurityTokenMapper {

    UserSecurityTokenDTO sourceToDestinationAllFields(UserSecurityToken source,
                                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    UserSecurityToken destinationToSourceAllFields(UserSecurityTokenDTO destination,
                                                   @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<UserSecurityTokenDTO> sourceToDestinationAllFields(List<UserSecurityToken> source,
                                                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<UserSecurityToken> destinationToSourceAllFields(List<UserSecurityTokenDTO> destination,
                                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default UserSecurityTokenDTO sourceToDestinationAllFields(UserSecurityToken source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default UserSecurityToken destinationToSourceAllFields(UserSecurityTokenDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<UserSecurityTokenDTO> sourceToDestinationAllFields(List<UserSecurityToken> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<UserSecurityToken> destinationToSourceAllFields(List<UserSecurityTokenDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
