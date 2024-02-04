package com.backend.app.model.mapper.userorganisation;


import com.backend.app.model.dto.userorganisation.UserOrganisationUpsertDTO;
import com.backend.app.model.entity.UserOrganisation;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserOrganisationUpsertMapper {

    UserOrganisationUpsertDTO sourceToDestinationAllFields(UserOrganisation source,
                                                           @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    UserOrganisation destinationToSourceAllFields(UserOrganisationUpsertDTO destination,
                                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<UserOrganisationUpsertDTO> sourceToDestinationAllFields(List<UserOrganisation> source,
                                                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<UserOrganisation> destinationToSourceAllFields(List<UserOrganisationUpsertDTO> destination,
                                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default UserOrganisationUpsertDTO sourceToDestinationAllFields(UserOrganisation source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default UserOrganisation destinationToSourceAllFields(UserOrganisationUpsertDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<UserOrganisationUpsertDTO> sourceToDestinationAllFields(List<UserOrganisation> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<UserOrganisation> destinationToSourceAllFields(List<UserOrganisationUpsertDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
