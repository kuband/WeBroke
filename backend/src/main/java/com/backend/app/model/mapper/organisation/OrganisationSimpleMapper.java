package com.backend.app.model.mapper.organisation;


import com.backend.app.model.dto.organisation.OrganisationSimpleDTO;
import com.backend.app.model.entity.Organisation;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrganisationSimpleMapper {

    OrganisationSimpleDTO sourceToDestinationAllFields(Organisation source,
                                                       @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Organisation destinationToSourceAllFields(OrganisationSimpleDTO destination,
                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<OrganisationSimpleDTO> sourceToDestinationAllFields(List<Organisation> source,
                                                             @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Organisation> destinationToSourceAllFields(List<OrganisationSimpleDTO> destination,
                                                    @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default OrganisationSimpleDTO sourceToDestinationAllFields(Organisation source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Organisation destinationToSourceAllFields(OrganisationSimpleDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<OrganisationSimpleDTO> sourceToDestinationAllFields(List<Organisation> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Organisation> destinationToSourceAllFields(List<OrganisationSimpleDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

}
