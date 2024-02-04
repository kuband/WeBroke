package com.backend.app.controller;

import com.backend.app.model.dto.userorganisation.UserOrganisationFullDTO;
import com.backend.app.model.dto.userorganisation.UserOrganisationSimpleDTO;
import com.backend.app.model.type.Operation;
import com.backend.app.payload.request.organisation.OrganisationAcceptRequest;
import com.backend.app.payload.request.userorganisation.UserOrganisationInviteRequest;
import com.backend.app.payload.request.userorganisation.UserOrganisationRequest;
import com.backend.app.payload.response.userorganisation.UserOrganisationFullResponse;
import com.backend.app.payload.response.userorganisation.UserOrganisationSimpleResponse;
import com.backend.app.service.UserOrganisationService;
import com.backend.auth.payload.response.MessageResponse;
import com.backend.auth.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user/organisation/")
@AllArgsConstructor
@Slf4j
public class UserOrganisationController {

    private UserOrganisationService userOrganisationService;

    @PostMapping("/upsert")
    public ResponseEntity<UserOrganisationSimpleResponse> upsertUserOrganisation(@Valid @RequestBody UserOrganisationRequest organisationRequest,
                                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (Arrays.stream(Operation.values()).noneMatch(operation -> operation == organisationRequest.getOperation())) {
            throw new UnsupportedOperationException();
        }
        return ResponseEntity.ok(userOrganisationService.crudOrganisation(organisationRequest,
                userDetails));
    }

    @GetMapping(value = {"/get/{id}", "/get"})
    public ResponseEntity<UserOrganisationSimpleResponse> getUserOrganisationForUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                     @PathVariable(value = "id", required = false) final String id) {
        List<UserOrganisationSimpleDTO> userOrganisationSimpleDTOS =
                userOrganisationService.readUserOrganisation(userDetails);
        if (id != null) {
            userOrganisationSimpleDTOS =
                    userOrganisationSimpleDTOS.stream().filter(userOrganisationDTO -> userOrganisationDTO.getId().toString().equals(id)).collect(Collectors.toList());
        }

        return ResponseEntity.ok(new UserOrganisationSimpleResponse(userOrganisationSimpleDTOS,
                userOrganisationService.getInviteUserOrganisation(userDetails)));
    }

    @GetMapping(value = {"/getAdmin/{id}", "/getAdmin"})
    public ResponseEntity<UserOrganisationFullResponse> getAdminUserOrganisation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                 @PathVariable(value
                                                                                         = "id",
                                                                                         required =
                                                                                                 false) final String id) {
        List<UserOrganisationFullDTO> userOrganisationFullDTOS =
                userOrganisationService.readAdminUserOrganisation(userDetails);
        if (id != null) {
            userOrganisationFullDTOS =
                    userOrganisationFullDTOS.stream().filter(userOrganisationDTO -> userOrganisationDTO.getId().toString().equals(id)).collect(Collectors.toList());
        }
        return ResponseEntity.ok(new UserOrganisationFullResponse(userOrganisationFullDTOS,
                Collections.emptyList()));
    }

    @GetMapping(value = {"/getDetails/{id}", "/getDetails"})
    public ResponseEntity<UserOrganisationFullResponse> getUserOrganisationDetails(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                   @PathVariable(value = "id", required = false) final String id) {
        List<UserOrganisationFullDTO> userOrganisationFullDTOS =
                userOrganisationService.readUserOrganisationDetails(userDetails);
        if (id != null) {
            userOrganisationFullDTOS =
                    userOrganisationFullDTOS.stream().filter(userOrganisationDTO -> userOrganisationDTO.getId().toString().equals(id)).collect(Collectors.toList());
        }
        return ResponseEntity.ok(new UserOrganisationFullResponse(userOrganisationFullDTOS,
                Collections.emptyList()));
    }

    @PostMapping("/activate")
    public ResponseEntity<UserOrganisationSimpleResponse> activateUserOrganisation(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userOrganisationService.activateUserOrganisation(userDetails));
    }

    @PostMapping("/invite")
    public ResponseEntity<MessageResponse> inviteUserOrganisation(@Valid @RequestBody UserOrganisationInviteRequest userOrganisationInviteRequest,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userOrganisationService.inviteUserOrganisation(userOrganisationInviteRequest, userDetails, httpServletRequest));
    }

    @PostMapping("/acceptInvite")
    public ResponseEntity<MessageResponse> acceptInviteUserOrganisation(@Valid @RequestBody OrganisationAcceptRequest organisationAcceptRequest,
                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userOrganisationService.acceptInviteUserOrganisation(organisationAcceptRequest, userDetails));
    }

    //TODO: request to make a user admin
}
