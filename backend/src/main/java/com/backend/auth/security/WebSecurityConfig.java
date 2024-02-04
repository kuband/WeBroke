package com.backend.auth.security;

import com.backend.auth.security.jwt.AuthEntryPointJwt;
import com.backend.auth.security.jwt.AuthTokenFilter;
import com.backend.auth.security.jwt.JwtUtils;
import com.backend.auth.security.services.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig {

    private UserDetailsServiceImpl userDetailsService;
    private AuthEntryPointJwt unauthorizedHandler;
    private JwtUtils jwtUtils;
    public static List<String> authedUrls = Arrays.asList("/api/v1/auth/.*",
            "/actuator/.*",
            "/v3/api-docs/.*",
            "/swagger-ui/.*",
            "/csrf",
            "/api/v1/password-reset",
            "/api/v1/payments/webhook");

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthTokenFilter authTokenFilter) throws Exception {
        http.addFilterBefore(authTokenFilter,
                UsernamePasswordAuthenticationFilter.class);

        http.cors(Customizer.withDefaults());
        var configuration = new CorsConfiguration();
        var allowedOrigins = List.of("http://localhost:4200");
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of("authorization", "content-type", "x-auth-token",
                "tenant-id", "organisation-ids",
                jwtUtils.getJwtCookie(), jwtUtils.getJwtRefreshCookie()));
        configuration.setExposedHeaders(List.of("x-auth-token", "authorization",
                jwtUtils.getJwtCookie(), jwtUtils.getJwtRefreshCookie()));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(source));

        //Session csrf
//        http.csrf(Customizer.withDefaults());
//        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
//                httpSecuritySessionManagementConfigurer.sessionCreationPolicy
//                (SessionCreationPolicy.IF_REQUIRED)
//                        .invalidSessionUrl("/invalidSession.html")
//                        .maximumSessions(1)
//                        .expiredUrl("/sessionExpired.html"));

        //Stateless spa csrf
//        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()));
        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy
                        (SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);

        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedHandler)
        );
//        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
//            authedUrls.forEach(s -> authorizationManagerRequestMatcherRegistry.requestMatchers
//            (RegexRequestMatcher.regexMatcher(s)).permitAll());
//            authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
//        });
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/csrf").permitAll()
                        .requestMatchers("/api/v1/password-reset").permitAll()
                        .requestMatchers("/api/v1/payments/webhook").permitAll()
                        .anyRequest().authenticated());
        http.logout((logout) -> logout.logoutUrl("/singout").
                deleteCookies(jwtUtils.getJwtCookie(), jwtUtils.getJwtRefreshCookie()).
                logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))));

        http.authenticationProvider(authenticationProvider());


        return http.build();
    }
}

final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
    private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
    private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       Supplier<CsrfToken> csrfToken) {
        this.xor.handle(request, response, csrfToken);
        csrfToken.get(); // subscribe
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        String header = request.getHeader(csrfToken.getHeaderName());
        return ((header != null) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);
    }
}