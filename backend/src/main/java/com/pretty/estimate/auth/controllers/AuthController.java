package com.we.broke.auth.controllers;

import com.we.broke.app.model.entity.Organisation;
import com.we.broke.app.model.entity.UserOrganisation;
import com.we.broke.app.repository.OrganisationRepository;
import com.we.broke.auth.event.OnRegistrationCompleteEvent;
import com.we.broke.auth.exception.EmailException;
import com.we.broke.auth.exception.PasswordException;
import com.we.broke.auth.exception.TokenException;
import com.we.broke.auth.models.ERole;
import com.we.broke.auth.models.PasswordDto;
import com.we.broke.auth.models.entity.RefreshToken;
import com.we.broke.auth.models.entity.Role;
import com.we.broke.auth.models.entity.User;
import com.we.broke.auth.payload.request.LoginRequest;
import com.we.broke.auth.payload.request.SignupRequest;
import com.we.broke.auth.payload.response.MessageResponse;
import com.we.broke.auth.payload.response.UserInfoResponse;
import com.we.broke.auth.repository.RoleRepository;
import com.we.broke.auth.security.jwt.JwtUtils;
import com.we.broke.auth.security.services.*;
import com.we.broke.common.email.EmailService;
import com.we.broke.common.model.UserSecurityTokenType;
import com.we.broke.common.model.entity.UserSecurityToken;
import com.we.broke.common.services.UserSecurityTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import static com.we.broke.common.util.RequestUtil.getAppUrl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;
    private UserDetails userDetails;
    private RoleRepository roleRepository;
    private OrganisationRepository organisationRepository;
    private PasswordEncoder encoder;
    private JwtUtils jwtUtils;
    private RefreshTokenService refreshTokenService;
    private ApplicationEventPublisher eventPublisher;
    private VerificationTokenService validateVerificationToken;
    private PasswordResetService passwordResetService;
    private UserSecurityTokenService userSecurityTokenService;
    private EmailService emailService;
    private JavaMailSender javaMailSender;

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));
        return authUser(authentication, loginRequest.isRememberMe());
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                                        HttpServletRequest httpServletRequest) {
        if (userDetails.existsByEmail(signUpRequest.getEmail())) {
            Optional<User> optionalUser = userDetails.findUserByEmail(signUpRequest.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (!user.isEnabled()) {
                    validateVerificationToken.deleteUserVerificationToken(user);
                    eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                            getAppUrl(httpServletRequest)));
                    throw new EmailException("Error: Email " +
                            "is " +
                            "already registered but its not active!, resent the activation email");
                }
            }
            throw new EmailException("Error: Email is " +
                    "already taken!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getFullName(),
                false, false);

        Set<Role> roles = new HashSet<>();

        //Save user
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
        roles.add(userRole);
        user.setRoles(roles);
        User save = userDetails.save(user);

        Optional<Set<String>> organisations = Optional.ofNullable(signUpRequest.getOrganisations());
        organisations.flatMap(strings -> strings.stream().map(Organisation::new).findAny()).ifPresent(organisationToSave -> {
            //new org and set as admin
            organisationToSave.setCreatedBy(save.getId());
            organisationToSave.setLastModifyBy(save.getId());
            Organisation organisation = organisationRepository.save(organisationToSave);
            UserOrganisation userOrganisation =
                    UserOrganisation.builder().user(user).organisation(organisation).createdBy(save.getId()).lastModifyBy(save.getId()).build();
            userOrganisation.getRoles().add(roleRepository.findByName(ERole.ROLE_ADMIN).get());
            userOrganisation.getRoles().add(userRole);
            userOrganisation.setActive(true);
            userOrganisation.setJoined(true);
            user.setUserOrganisations(new HashSet<>(List.of(userOrganisation)));
            //save user and cascade orgs
            userDetails.save(user);
        });

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
                getAppUrl(httpServletRequest)));

        return ResponseEntity.ok(new MessageResponse("User registered successfully! Please " +
                "check " +
                "your email"));
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<UserInfoResponse> confirmRegistration(
            @RequestParam("token") final String token) throws UnsupportedEncodingException {
        final String result = validateVerificationToken.validateVerificationToken(token);
        if (result.equals("valid")) {
            final User user = validateVerificationToken.getUser(token);
            // if (user.isUsing2FA()) {
            // model.addAttribute("qr", userService.generateQRUrl(user));
            // return "redirect:/qrcode.html?lang=" + locale.getLanguage();
            // }
            ResponseEntity<UserInfoResponse> userInfoResponseResponseEntity =
                    authWithoutPassword(user);
            if (userInfoResponseResponseEntity.getStatusCode() == HttpStatus.OK) {
                //delete after use
                validateVerificationToken.deleteUserVerificationToken(user);
            }

            return userInfoResponseResponseEntity;
        }

        throw new TokenException("CONFIRM_EMAIL_TOKEN_NOT_VALID");
    }

    // reset password email
    @PostMapping("/user/resetPassword")
    public ResponseEntity<MessageResponse> resetPassword(HttpServletRequest request,
                                                         @RequestParam("email") String userEmail) {
        Optional<User> optionalUser = userDetails.findUserByEmail(userEmail);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.ok(new MessageResponse("If account exists the reset " +
                    "password " +
                    "request was sent, check your email!"));
        }
        User user = optionalUser.get();

        UserSecurityToken userSecurityTokenByUserId =
                userSecurityTokenService.getSecurityTokenByUserId(user.getId(),
                        UserSecurityTokenType.PASSWORD).stream().filter(userSecurityToken -> userSecurityToken.getUser().getId().equals(user.getId())).findFirst().orElse(null);
        if (userSecurityTokenByUserId != null) {
            String token = userSecurityTokenByUserId.getToken();
            String result = userSecurityTokenService.validateToken(token,
                    UserSecurityTokenType.PASSWORD);
            if (result == null) {
                log.info("Reset token: {}", token);
//            javaMailSender.send(emailService.constructPasswordResetTokenEmail(getAppUrl
//            (request), userSecurityTokenByUserId.getToken(), user));
                return ResponseEntity.ok(new MessageResponse("Reset password resent, check " +
                        "your " +
                        "email!"));
            } else if (result.equals("expired")) {
                userSecurityTokenService.delete(userSecurityTokenByUserId);
            }
        }

        String token = UUID.randomUUID().toString();
        userSecurityTokenService.createSecurityTokenForUser(user, token,
                UserSecurityTokenType.PASSWORD, null);
        log.info("Reset token: {}", token);
//        javaMailSender.send(emailService.constructPasswordResetTokenEmail(getAppUrl(request),
//        token, user));

        return ResponseEntity.ok(new MessageResponse("If account exists the reset password " +
                "request was sent, check your email!"));
    }

    // Save reset password
    @PostMapping("/user/savePassword")
    public ResponseEntity<MessageResponse> savePassword(@Valid @RequestBody PasswordDto
                                                                passwordDto) {
        final String result =
                userSecurityTokenService.validateToken(passwordDto.getToken(),
                        UserSecurityTokenType.PASSWORD);

        if (result != null) {
            throw new PasswordException("Issue resetting " +
                    "password: " + result);
        }

        Optional<User> user = userDetails.getUserByUserSecurityToken(passwordDto.getToken());
        if (user.isPresent()) {
            passwordResetService.changeUserPassword(user.get(), passwordDto.getNewPassword());
            userSecurityTokenService.deleteByToken(passwordDto.getToken());
            return ResponseEntity.ok(new MessageResponse("Password change successful"));
        } else {
            throw new PasswordException("Issue resetting " +
                    "password");
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<MessageResponse> refreshtoken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new MessageResponse("Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new TokenException("REFRESH_TOKEN_NOT_FOUND"));
        }

        throw new TokenException("REFRESH_TOKEN_NOT_SENT");
    }

    @PostMapping("/signout")
    public ResponseEntity<MessageResponse> logoutUser() {
        Object principle =
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle.toString() != "anonymousUser") {
            Long userId = ((UserDetailsImpl) principle).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

//    --------------------------------------------------------------------------------------------

    public ResponseEntity<UserInfoResponse> authWithoutPassword(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(UserDetailsImpl.build(user), null,
                        authorities);
        return authUser(authentication, false);
    }

    private ResponseEntity<UserInfoResponse> authUser(Authentication authentication,
                                                      boolean rememberMe) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(),
                rememberMe);

        ResponseCookie jwtRefreshCookie =
                jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getEmail(),
                        roles,
                        userDetails.getOrganisationIds()));
    }
}
