package com.we.broke.app.model.mapper.userorganisation;


import com.we.broke.app.model.dto.userorganisation.UserOrganisationFullDTO;
import com.we.broke.app.model.entity.UserOrganisation;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
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
