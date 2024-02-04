package com.backend.app.model.mapper.userorganisation;


import com.backend.app.model.dto.userorganisation.UserOrganisationFullDTO;
import com.backend.app.model.entity.UserOrganisation;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserOrganisationFullMapper {

    UserOrganisationFullDTO sourceToDestinationAllFields(UserOrganisation source,
                                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    UserOrganisation destinationToSourceAllFields(UserOrganisationFullDTO destination,
                                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<UserOrganisationFullDTO> sourceToDestinationAllFields(List<UserOrganisation> source,
                                                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<UserOrganisation> destinationToSourceAllFields(List<UserOrganisationFullDTO> destination,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default UserOrganisationFullDTO sourceToDestinationAllFields(UserOrganisation source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default UserOrganisation destinationToSourceAllFields(UserOrganisationFullDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<UserOrganisationFullDTO> sourceToDestinationAllFields(List<UserOrganisation> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<UserOrganisation> destinationToSourceAllFields(List<UserOrganisationFullDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
