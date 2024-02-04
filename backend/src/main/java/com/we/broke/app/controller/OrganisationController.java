package com.we.broke.app.controller;

import com.we.broke.app.model.dto.organisation.OrganisationSimpleDTO;
import com.we.broke.app.model.type.Operation;
import com.we.broke.app.payload.request.userorganisation.UserOrganisationUpsertRequest;
import com.we.broke.app.payload.response.organisation.OrganisationSimpleResponse;
import com.we.broke.app.service.OrganisationService;
import com.we.broke.auth.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/organisation/")
@AllArgsConstructor
@Slf4j
public class OrganisationController {

    private OrganisationService organisationService;

    @PostMapping("/upsert")
    public ResponseEntity<OrganisationSimpleResponse> upsertOrganisation(@Valid @RequestBody UserOrganisationUpsertRequest userOrganisationUpsertRequest,
                                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (Arrays.stream(Operation.values()).noneMatch(operation -> operation == userOrganisationUpsertRequest.getOperation())) {
            throw new UnsupportedOperationException();
        }
        return ResponseEntity.ok(organisationService.crudOrganisation(userOrganisationUpsertRequest,
                userDetails));
    }

    @GetMapping(value = {"/get/{id}", "/get"})
    public ResponseEntity<OrganisationSimpleResponse> getOrganisation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      @PathVariable(value = "id",
                                                                              required = false) final String id) {
        List<OrganisationSimpleDTO> organisation = organisationService.readOrganisation(userDetails);
        if (id != null) {
            organisation =
                    organisation.stream().filter(organisationDTO -> organisationDTO.getId().toString().equals(id)).collect(Collectors.toList());
        }

        return ResponseEntity.ok(new OrganisationSimpleResponse(organisation));
    }
}
