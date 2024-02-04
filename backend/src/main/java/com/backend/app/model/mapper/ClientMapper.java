package com.backend.app.model.mapper;


import com.backend.app.model.dto.ClientDTO;
import com.backend.app.model.entity.Client;
import com.backend.app.util.CycleAvoidingMappingContext;
import com.backend.app.util.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientMapper {
    ClientDTO sourceToDestinationAllFields(Client source,
                                           @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    Client destinationToSourceAllFields(ClientDTO destination,
                                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<ClientDTO> sourceToDestinationAllFields(List<Client> source,
                                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    List<Client> destinationToSourceAllFields(List<ClientDTO> destination,
                                              @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default ClientDTO sourceToDestinationAllFields(Client source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Client destinationToSourceAllFields(ClientDTO source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<ClientDTO> sourceToDestinationAllFields(List<Client> source) {
        return sourceToDestinationAllFields(source, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default List<Client> destinationToSourceAllFields(List<ClientDTO> source) {
        return destinationToSourceAllFields(source, new CycleAvoidingMappingContext());
    }
}
