package com.we.broke.app.model.mapper.organisation;


import com.we.broke.app.model.dto.organisation.OrganisationUpsertDTO;
import com.we.broke.app.model.entity.Organisation;
import com.we.broke.app.util.CycleAvoidingMappingContext;
import com.we.broke.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrganisationUpsertMapper {

    OrganisationUpsertDTO sourceToDestinationAllFields(Organisation source,
                                                       @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Organisation destinationToSourceAllFields(OrganisationUpsertDTO destination,
                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<OrganisationUpsertDTO> sourceToDestinationAllFields(List<Organisation> source,
                                                             @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Organisation> destinationToSourceAllFields(List<OrganisationUpsertDTO> destination,
                                                    @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default OrganisationUpsertDTO sourceToDestinationAllFields(Organisation source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Organisation destinationToSourceAllFields(OrganisationUpsertDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<OrganisationUpsertDTO> sourceToDestinationAllFields(List<Organisation> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Organisation> destinationToSourceAllFields(List<OrganisationUpsertDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

}
