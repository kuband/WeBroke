package com.backend.app.model.mapper.userorganisation;


import com.backend.app.model.dto.userorganisation.UserOrganisationSimpleDTO;
import com.backend.app.model.entity.UserOrganisation;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserOrganisationSimpleMapper {

    UserOrganisationSimpleDTO sourceToDestinationAllFields(UserOrganisation source,
                                                           @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    UserOrganisation destinationToSourceAllFields(UserOrganisationSimpleDTO destination,
                                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<UserOrganisationSimpleDTO> sourceToDestinationAllFields(List<UserOrganisation> source,
                                                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<UserOrganisation> destinationToSourceAllFields(List<UserOrganisationSimpleDTO> destination,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default UserOrganisationSimpleDTO sourceToDestinationAllFields(UserOrganisation source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default UserOrganisation destinationToSourceAllFields(UserOrganisationSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<UserOrganisationSimpleDTO> sourceToDestinationAllFields(List<UserOrganisation> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<UserOrganisation> destinationToSourceAllFields(List<UserOrganisationSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
