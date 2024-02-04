package com.we.broke.app.model.mapper.organisation;


import com.we.broke.app.model.dto.organisation.OrganisationFullDTO;
import com.we.broke.app.model.entity.Organisation;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrganisationFullMapper {

    OrganisationFullDTO sourceToDestinationAllFields(Organisation source,
                                                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Organisation destinationToSourceAllFields(OrganisationFullDTO destination,
                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<OrganisationFullDTO> sourceToDestinationAllFields(List<Organisation> source,
                                                           @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Organisation> destinationToSourceAllFields(List<OrganisationFullDTO> destination,
                                                    @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default OrganisationFullDTO sourceToDestinationAllFields(Organisation source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Organisation destinationToSourceAllFields(OrganisationFullDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<OrganisationFullDTO> sourceToDestinationAllFields(List<Organisation> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Organisation> destinationToSourceAllFields(List<OrganisationFullDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
