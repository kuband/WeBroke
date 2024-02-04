package com.backend.auth.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.app.model.entity.Organisation;
import com.backend.app.model.entity.UserOrganisation;
import com.backend.auth.exception.OrganisationException;
import com.backend.auth.exception.TokenException;
import com.backend.auth.models.entity.RefreshToken;
import com.backend.auth.models.entity.User;
import com.backend.auth.repository.RefreshTokenRepository;
import com.backend.auth.security.services.UserDetailsImpl;
import com.backend.common.exception.DefaultExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

import static com.backend.auth.security.WebSecurityConfig.authedUrls;

@Slf4j
@AllArgsConstructor
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private RefreshTokenRepository refreshTokenRepository;
    private com.backend.auth.security.services.UserDetails userDetailsService;
    private DefaultExceptionHandler defaultExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (shouldSkipFiltering(request)) {
                filterChain.doFilter(request, response);
            } else {
                String jwt = parseJwt(request);
                if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                    String email = jwtUtils.getEmailFromJwtToken(jwt);
                    User user = userDetailsService.findByEmail(email)
                            .orElseThrow(() -> {
                                log.error("User Not Found with email: " + email);
                                return new UsernameNotFoundException("USER_NOT_FOUND");
                            });
                    UserDetailsImpl userDetails = UserDetailsImpl.build(user);

                    String orgHeader = request.getHeader("organisation-ids");
                    if (!ObjectUtils.isEmpty(orgHeader)) {
                        List<Long> orgIdsFromHeader =
                                Arrays.stream(orgHeader.split(",")).mapToLong(Long::valueOf).boxed().toList();
                        List<Long> matchedOrgIds =
                                user.getUserOrganisations().stream().filter(UserOrganisation::isJoined).map(UserOrganisation::getOrganisation).map(Organisation::getId)
                                        .filter(orgIdsFromHeader::contains).toList();

                        if (matchedOrgIds.isEmpty()) {
                            log.error("Orgs provided don't match for the email: " + email);
                            throw new OrganisationException("ORG_MISMATCH");
                        }

                        userDetails.setOrganisationIds(matchedOrgIds);
                    } else {
                        if (user.getUserOrganisations().stream().anyMatch
                                (UserOrganisation::isJoined)) {
                            //disable check for websocket as cant send header of org
                            if (!request.getRequestURI().contains("websocket")) {
                                log.error("Orgs assigned but non sent: " + email);
                                throw new OrganisationException("ORG_MISMATCH");
                            }
                        }
                        userDetails.setOrganisationIds(Collections.emptyList());
                    }

                    if (isRefreshTokenValid(userDetails)) {
                        if (userDetails.isEnabled()) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userDetails,
                                            null,
                                            userDetails.getAuthorities());

                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }

                filterChain.doFilter(request, response);
            }
        } catch (TokenException e) {
            ResponseEntity<Map<String, Object>> responseEntity =
                    defaultExceptionHandler.handleTokenException(e);
            response.setStatus(responseEntity.getStatusCode().value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
        } catch (OrganisationException e) {
            ResponseEntity<Map<String, Object>> responseEntity =
                    defaultExceptionHandler.handleAuthenticationException(e);
            response.setStatus(responseEntity.getStatusCode().value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
            filterChain.doFilter(request, response);
        }
    }

    protected boolean shouldSkipFiltering(HttpServletRequest request) {
        String path = request.getRequestURI();
        return authedUrls.stream().anyMatch(path::matches);
    }

    private String parseJwt(HttpServletRequest request) {
        return jwtUtils.getJwtFromCookies(request);
    }

    private boolean isRefreshTokenValid(UserDetailsImpl userDetails) {
        Optional<RefreshToken> optionalRefreshToken =
                refreshTokenRepository.findByUserId(userDetails.getId());
        return optionalRefreshToken.isPresent();
    }

}
