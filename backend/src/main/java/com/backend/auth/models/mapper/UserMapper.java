package com.backend.auth.models.mapper;

import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import com.backend.auth.models.dto.UserDTO;
import com.backend.auth.models.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO sourceToDestinationAllFields(User source,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    User destinationToSourceAllFields(UserDTO destination,
                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<UserDTO> sourceToDestinationAllFields(List<User> source,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<User> destinationToSourceAllFields(List<UserDTO> destination,
                                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default UserDTO sourceToDestinationAllFields(User source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default User destinationToSourceAllFields(UserDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<UserDTO> sourceToDestinationAllFields(List<User> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<User> destinationToSourceAllFields(List<UserDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
