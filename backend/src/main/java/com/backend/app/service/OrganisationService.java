package com.backend.app.service;

import com.backend.app.exception.OperationException;
import com.backend.app.exception.UnsupportedOperationException;
import com.backend.app.model.ModificationStatus;
import com.backend.app.model.dto.organisation.OrganisationSimpleDTO;
import com.backend.app.model.dto.organisation.OrganisationUpsertDTO;
import com.backend.app.model.dto.userorganisation.UserOrganisationFullDTO;
import com.backend.app.model.entity.Organisation;
import com.backend.app.model.entity.UserOrganisation;
import com.backend.app.model.mapper.organisation.OrganisationSimpleMapper;
import com.backend.app.model.mapper.organisation.OrganisationUpsertMapper;
import com.backend.app.payload.request.userorganisation.UserOrganisationUpsertRequest;
import com.backend.app.payload.response.organisation.OrganisationSimpleResponse;
import com.backend.app.repository.OrganisationRepository;
import com.backend.auth.models.ERole;
import com.backend.auth.models.dto.RoleDTO;
import com.backend.auth.security.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class OrganisationService {

    private OrganisationRepository organisationRepository;
    private OrganisationSimpleMapper organisationSimpleMapper;
    private OrganisationUpsertMapper organisationUpsertMapper;
    private UserOrganisationService userOrganisationService;

    public OrganisationSimpleResponse crudOrganisation(UserOrganisationUpsertRequest UserOrganisationUpsertRequest,
                                                       UserDetailsImpl userDetailsImpl) {
        //when creating, should only send one org id
        if (userDetailsImpl.getOrganisationIds().size() != 1) {
            throw new UnsupportedOperationException("Can only add/delete in one org at a time.");
        }

        return switch (UserOrganisationUpsertRequest.getOperation()) {
            case DELETE -> deleteOrganisation(Collections.singletonList(UserOrganisationUpsertRequest.getOrganisation().getId()));
            case UPSERT -> upsertOrganisation(UserOrganisationUpsertRequest.getOrganisation(), userDetailsImpl);
        };
    }

    public List<OrganisationSimpleDTO> readOrganisation(UserDetailsImpl userDetailsImpl) {
        return organisationSimpleMapper.sourceToDestinationAllFields(
                userOrganisationService.getOnlyUserOrganisations(userDetailsImpl).stream().filter(UserOrganisation::isJoined).map(UserOrganisation::getOrganisation).collect(Collectors.toList()));
    }

    public OrganisationSimpleResponse upsertOrganisation(OrganisationUpsertDTO organisationUpsertDTO,
                                                         UserDetailsImpl userDetailsImpl) {
        try {
            Organisation organisation =
                    organisationUpsertMapper.destinationToSourceAllFields(organisationUpsertDTO);
            organisation.setModificationStatus(ModificationStatus.ACTIVE);
            if (organisation.getId() == null) {
                throw new OperationException("Can't insert without user organisation relationship");
                //update existing
            } else {
                UserOrganisationFullDTO userOrganisationFullDTOResult =
                        userOrganisationService.readAdminUserOrganisation(userDetailsImpl).stream().filter(userOrganisationDTO ->
                                userOrganisationDTO.getId().equals(organisationUpsertDTO.getId()) || userOrganisationDTO.getUser().getId().equals(userDetailsImpl.getId())).findAny().orElse(null);
                if (userOrganisationFullDTOResult != null && userOrganisationFullDTOResult.getOrganisation().getId().equals(organisationUpsertDTO.getId())
                        && userOrganisationFullDTOResult.getRoles().stream().map(RoleDTO::getName).anyMatch(eRole -> eRole == ERole.ROLE_ADMIN)) {
                    Organisation save = organisationRepository.save(organisation);
                    return new OrganisationSimpleResponse(organisationSimpleMapper.sourceToDestinationAllFields(Collections.singletonList(save)));
                } else {
                    throw new OperationException("Upsert failed, doesn't have ADMIN role in org.");
                }
            }
        } catch (Exception e) {
            throw new OperationException("Upsert failed");
        }
    }

    public OrganisationSimpleResponse deleteOrganisation(List<Long> organisationId) {
        try {
            organisationRepository.deleteOrgs(organisationId);
        } catch (Exception e) {
            throw new OperationException("Delete failed");
        }
        return new OrganisationSimpleResponse(Collections.emptyList());
    }
}
