package com.backend.auth.models.mapper;

import com.backend.auth.models.dto.UserSimpleDTO;
import com.backend.auth.models.entity.User;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserSimpleMapper {

    UserSimpleDTO sourceToDestinationAllFields(User source,
                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    User destinationToSourceAllFields(UserSimpleDTO destination,
                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<UserSimpleDTO> sourceToDestinationAllFields(List<User> source,
                                                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<User> destinationToSourceAllFields(List<UserSimpleDTO> destination,
                                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default UserSimpleDTO sourceToDestinationAllFields(User source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default User destinationToSourceAllFields(UserSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<UserSimpleDTO> sourceToDestinationAllFields(List<User> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<User> destinationToSourceAllFields(List<UserSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
