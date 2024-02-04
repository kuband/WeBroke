package com.backend.app.service;

import com.backend.app.exception.OperationException;
import com.backend.app.exception.UnsupportedOperationException;
import com.backend.app.model.dto.userorganisation.UserOrganisationFullDTO;
import com.backend.app.model.dto.userorganisation.UserOrganisationSimpleDTO;
import com.backend.app.model.entity.Organisation;
import com.backend.app.model.entity.UserOrganisation;
import com.backend.app.model.mapper.userorganisation.UserOrganisationFullMapper;
import com.backend.app.model.mapper.userorganisation.UserOrganisationSimpleMapper;
import com.backend.app.model.mapper.userorganisation.UserOrganisationUpsertMapper;
import com.backend.app.payload.request.organisation.OrganisationAcceptRequest;
import com.backend.app.payload.request.userorganisation.UserOrganisationInviteRequest;
import com.backend.app.payload.request.userorganisation.UserOrganisationRequest;
import com.backend.app.payload.response.userorganisation.UserOrganisationSimpleResponse;
import com.backend.app.repository.OrganisationRepository;
import com.backend.app.repository.UserOrganisationRepository;
import com.backend.auth.exception.PasswordException;
import com.backend.auth.models.ERole;
import com.backend.auth.models.dto.RoleDTO;
import com.backend.auth.models.entity.User;
import com.backend.auth.payload.response.MessageResponse;
import com.backend.auth.repository.RoleRepository;
import com.backend.auth.security.services.UserDetails;
import com.backend.auth.security.services.UserDetailsImpl;
import com.backend.common.email.EmailService;
import com.backend.common.model.UserSecurityTokenType;
import com.backend.common.model.dto.UserSecurityTokenDTO;
import com.backend.common.model.entity.UserSecurityToken;
import com.backend.common.model.mapper.UserSecurityTokenMapper;
import com.backend.common.repository.UserSecurityTokenRepository;
import com.backend.common.services.UserSecurityTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserOrganisationService {

    private UserOrganisationRepository userOrganisationRepository;
    private OrganisationRepository organisationRepository;
    private UserOrganisationFullMapper userOrganisationFullMapper;
    private UserOrganisationSimpleMapper userOrganisationSimpleMapper;
    private UserOrganisationUpsertMapper userOrganisationUpsertMapper;
    private UserSecurityTokenMapper userSecurityTokenMapper;
    private RoleRepository roleRepository;
    private UserSecurityTokenService userSecurityTokenService;
    private UserDetails userDetailsService;
    private UserSecurityTokenRepository userSecurityTokenRepository;
    private EmailService emailService;
    private JavaMailSender javaMailSender;

    public UserOrganisationSimpleResponse crudOrganisation(UserOrganisationRequest organisationRequest,
                                                           UserDetailsImpl userDetailsImpl) {
        //when creating, should only send one org id
        if (userDetailsImpl.getOrganisationIds().size() != 1) {
            throw new UnsupportedOperationException("Can only add/delete in one org at a time.");
        }

        return switch (organisationRequest.getOperation()) {
            case DELETE -> deleteUserOrganisationId(organisationRequest.getUserOrganisation().getOrganisation().getId()
                    , userDetailsImpl);
            case UPSERT -> upsertOrganisation(organisationRequest, userDetailsImpl);
        };
    }

    public List<UserOrganisationSimpleDTO> readUserOrganisation(UserDetailsImpl userDetailsImpl) {
        return userOrganisationSimpleMapper.sourceToDestinationAllFields(getOnlyUserOrganisations(userDetailsImpl));
    }

    public List<UserOrganisationFullDTO> readAdminUserOrganisation(UserDetailsImpl userDetailsImpl) {
        return userOrganisationFullMapper.sourceToDestinationAllFields(getAdminUserOrganisation(userDetailsImpl));
    }

    public List<UserOrganisationFullDTO> readUserOrganisationDetails(UserDetailsImpl userDetailsImpl) {
        return userOrganisationFullMapper.sourceToDestinationAllFields(getAllUserOrganisationDetails(userDetailsImpl));
    }

    public List<UserOrganisation> getOnlyUserOrganisations(UserDetailsImpl userDetailsImpl) {
        Optional<List<UserOrganisation>> byOrganisationIds =
                userOrganisationRepository.findAllByUserIdIn(Collections.singleton(userDetailsImpl.getId()));
        return byOrganisationIds.map(orgs -> orgs.stream().toList()).orElse(Collections.emptyList()).stream().filter(UserOrganisation::isJoined).toList();
    }

    public List<UserOrganisation> getAllUserOrganisationDetails(UserDetailsImpl userDetailsImpl) {
        Optional<List<UserOrganisation>> byOrganisationIds =
                userOrganisationRepository.findAllByOrganisationIdIn(userDetailsImpl.getOrganisationIds().stream().toList());
        return byOrganisationIds.map(orgs -> orgs.stream().toList()).orElse(Collections.emptyList());
    }

    public List<UserOrganisation> getAdminUserOrganisation(UserDetailsImpl userDetailsImpl) {
        List<UserOrganisation> byOrganisationIds = getAllUserOrganisationDetails(userDetailsImpl);
        List<Long> adminOrgs =
                byOrganisationIds.stream().filter(userOrganisation -> userOrganisation.getUser().getId().equals(userDetailsImpl.getId())).filter(userOrganisation ->
                                userOrganisation.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN))
                        .map(userOrganisation -> userOrganisation.getOrganisation().getId()).toList();
        return byOrganisationIds.stream().filter(userOrganisation ->
                adminOrgs.contains(userOrganisation.getOrganisation().getId())).collect(Collectors.toList());
    }

    public UserOrganisationSimpleResponse activateUserOrganisation(UserDetailsImpl userDetailsImpl) {
        List<UserOrganisation> byOrganisationIds = getOnlyUserOrganisations(userDetailsImpl);
        byOrganisationIds.forEach(organisations -> {
            organisations.setActive(userDetailsImpl.getOrganisationIds().contains(organisations.getOrganisation().getId()));
        });
        userOrganisationRepository.saveAll(byOrganisationIds);
        return new UserOrganisationSimpleResponse(userOrganisationSimpleMapper.sourceToDestinationAllFields(byOrganisationIds), Collections.emptyList());
    }


    public UserOrganisationSimpleResponse upsertOrganisation(UserOrganisationRequest organisationRequest, UserDetailsImpl userDetailsImpl) {
        try {
            //new org
            UserOrganisation userOrganisation =
                    userOrganisationUpsertMapper.destinationToSourceAllFields(organisationRequest.getUserOrganisation());
            if (userOrganisation.getId() == null) {
                Organisation savedOrg =
                        organisationRepository.save(new Organisation(organisationRequest.getUserOrganisation().getOrganisation().getName()));
                userOrganisation.setOrganisation(savedOrg);
                userOrganisation.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName(ERole.ROLE_ADMIN).get(), roleRepository.findByName(ERole.ROLE_USER).get())));
                userOrganisation.setUser(User.builder().id(userDetailsImpl.getId()).build());
                userOrganisation.setJoined(true);
                UserOrganisation save = userOrganisationRepository.save(userOrganisation);
                return new UserOrganisationSimpleResponse(userOrganisationSimpleMapper.sourceToDestinationAllFields(Collections.singletonList(save)), Collections.emptyList());
                //update existing
            } else {
                UserOrganisationSimpleDTO userOrganisationFullDTOResult =
                        readUserOrganisation(userDetailsImpl).stream().filter(userOrganisationDTO ->
                                userOrganisationDTO.getId().equals(organisationRequest.getUserOrganisation().getId())).findAny().orElse(null);
                if (userOrganisationFullDTOResult != null && userOrganisationFullDTOResult.getOrganisation().getId().equals(userOrganisation.getId())
                        && userOrganisationFullDTOResult.getRoles().stream().map(RoleDTO::getName).anyMatch(eRole -> eRole == ERole.ROLE_ADMIN)) {
                    UserOrganisation save = userOrganisationRepository.save(userOrganisation);
                    return new UserOrganisationSimpleResponse(userOrganisationSimpleMapper.sourceToDestinationAllFields(Collections.singletonList(save)), Collections.emptyList());
                } else {
                    throw new OperationException("Upsert failed, doesn't have ADMIN role in org.");
                }
            }
        } catch (Exception e) {
            throw new OperationException("Upsert failed");
        }
    }

    public UserOrganisationSimpleResponse deleteUserOrganisationId(Long userOrganisationId,
                                                                   UserDetailsImpl userDetailsImpl) {
        try {
            Optional<UserOrganisation> byId =
                    userOrganisationRepository.findById(userOrganisationId);

            //leave org as user
            if (byId.isPresent() && userDetailsImpl.getOrganisationIds().contains(byId.get().getOrganisation().getId()) && byId.get().getUser().getId().equals(userDetailsImpl.getId())) {
                byId.get().setJoined(false);
                byId.get().setActive(false);
                userOrganisationRepository.save(byId.get());
                //delete as admin
            } else if (byId.isPresent() && readAdminUserOrganisation(userDetailsImpl).stream().anyMatch(userOrganisationDTO ->
                    userOrganisationDTO.getId().equals(userOrganisationId))) {
                byId.get().setJoined(false);
                byId.get().setActive(false);
                userOrganisationRepository.save(byId.get());
            } else {
                throw new OperationException("Delete failed, doesn't have ADMIN or USER role in " +
                        "org.");
            }
        } catch (Exception e) {
            throw new OperationException("Delete user org failed");
        }
        return new UserOrganisationSimpleResponse(Collections.emptyList(), Collections.emptyList());
    }

    public MessageResponse inviteUserOrganisation(UserOrganisationInviteRequest userOrganisationInviteRequest, UserDetailsImpl userDetails, HttpServletRequest request) {
        UserOrganisationSimpleDTO userOrganisationFullDTOResult =
                readUserOrganisation(userDetails).stream().filter(userOrganisationDTO ->
                        userOrganisationDTO.getOrganisation().getId().equals(userOrganisationInviteRequest.getOrgId())).findAny().orElse(null);
        if (userOrganisationFullDTOResult != null && userOrganisationFullDTOResult.getOrganisation().getId().equals(userOrganisationInviteRequest.getOrgId())
                && userOrganisationFullDTOResult.getRoles().stream().map(RoleDTO::getName).anyMatch(eRole -> eRole == ERole.ROLE_ADMIN)) {
            Optional<User> optionalUser =
                    userDetailsService.findUserByEmail(userOrganisationInviteRequest.getEmail());
            if (optionalUser.isEmpty()) {
                return new MessageResponse("Account doesn't exist");
            }
            User user = optionalUser.get();

            if (user.getUserOrganisations().stream().filter(UserOrganisation::isJoined).anyMatch(userOrganisation -> userOrganisation.getOrganisation().getId().equals(userOrganisationInviteRequest.getOrgId()))) {
                return new MessageResponse("User already in org");
            }

            UserSecurityToken userSecurityTokenByUserId =
                    userSecurityTokenService.getSecurityTokenByUserId(user.getId(),
                            UserSecurityTokenType.ORG).stream().filter(userSecurityToken -> userSecurityToken.getSecurityId().equals(userOrganisationInviteRequest.getOrgId())).findFirst().orElse(null);
            if (userSecurityTokenByUserId != null) {
                String token = userSecurityTokenByUserId.getToken();
                String result = userSecurityTokenService.validateToken(token,
                        UserSecurityTokenType.ORG);
                if (result == null) {
                    log.info("Security token: {}", token);
//                javaMailSender.send(emailService.constructOrgInviteTokenEmail(getAppUrl
//                        (request), userSecurityTokenByUserId.getToken(), user));
                    return new MessageResponse("Org invite resent");
                } else if (result.equals("expired")) {
                    userSecurityTokenService.delete(userSecurityTokenByUserId);
                }
            }

            String token = UUID.randomUUID().toString();
            userSecurityTokenService.createSecurityTokenForUser(user, token,
                    UserSecurityTokenType.ORG, userOrganisationInviteRequest.getOrgId());
            UserOrganisation userOrganisation =
                    UserOrganisation.builder().user(user).organisation(new Organisation(userOrganisationInviteRequest.getOrgId())).build();
            userOrganisation.getRoles().add(roleRepository.findByName(ERole.ROLE_USER).get());
            //save not joined user orgs
            userOrganisationRepository.save(userOrganisation);
            log.info("Security token: {}", token);
//        javaMailSender.send(emailService.constructOrgInviteTokenEmail(getAppUrl(request),
//                token, user));

            return new MessageResponse("The Invite has been sent");
        } else {
            throw new OperationException("Invite failed, doesn't have ADMIN role in org.");
        }
    }

    public List<UserSecurityTokenDTO> getInviteUserOrganisation(UserDetailsImpl userDetails) {
        List<UserSecurityToken> securityTokens =
                userSecurityTokenService.getSecurityTokenByUserId(userDetails.getId(),
                        UserSecurityTokenType.ORG);
        if (!securityTokens.isEmpty()) {
            return userSecurityTokenMapper.sourceToDestinationAllFields(securityTokens);
        }
        return Collections.emptyList();
    }

    public MessageResponse acceptInviteUserOrganisation(OrganisationAcceptRequest organisationAcceptRequest, UserDetailsImpl userDetails) {
        final String result =
                userSecurityTokenService.validateToken(organisationAcceptRequest.getToken(),
                        UserSecurityTokenType.ORG);

        if (result != null) {
            throw new PasswordException("Issue resetting " +
                    "password: " + result);
        }

        UserSecurityToken securityToken =
                userSecurityTokenService.getSecurityToken(organisationAcceptRequest.getToken());
        if (securityToken != null) {
            User user = securityToken.getUser();
            if (!Objects.equals(user.getId(), userDetails.getId())) {
                throw new OperationException("User mismatch");
            }
            if (organisationAcceptRequest.isAccept()) {
                Optional<UserOrganisation> allByOrganisationIdIn =
                        userOrganisationRepository.findAllByOrganisationIdAndUserId(securityToken.getSecurityId(), user.getId());
                if (allByOrganisationIdIn.isPresent()) {
                    UserOrganisation userOrganisation = allByOrganisationIdIn.get();
                    userOrganisation.setJoined(true);
                    userOrganisation.setActive(true);
                    //save user orgs
                    userOrganisationRepository.save(userOrganisation);
                    userSecurityTokenRepository.deleteByToken(organisationAcceptRequest.getToken());
                    return new MessageResponse("Organisation invite accept successful");
                }
            } else {
                userSecurityTokenRepository.deleteByToken(organisationAcceptRequest.getToken());
                return new MessageResponse("Organisation invite reject successful");
            }
        }
        throw new PasswordException("Issue joining " +
                "organisation");
    }
}
