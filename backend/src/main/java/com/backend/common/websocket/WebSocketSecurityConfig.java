package com.backend.common.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

import static org.springframework.messaging.simp.SimpMessageType.*;

@Configuration
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private ApplicationEventPublisher context;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        AuthorizationManager<Message<?>> manager =
                new MessageMatcherDelegatingAuthorizationManager.Builder()
                        .simpTypeMatchers(CONNECT, DISCONNECT, UNSUBSCRIBE).permitAll()
                        .simpDestMatchers("/websocket/**").hasRole("USER")
                        .anyMessage().authenticated().build();
        AuthorizationChannelInterceptor interceptor = new AuthorizationChannelInterceptor(manager);
        AuthorizationEventPublisher publisher = new SpringAuthorizationEventPublisher(this.context);
        interceptor.setSecurityContextHolderStrategy(SecurityContextHolder
                .getContextHolderStrategy());
        interceptor.setAuthorizationEventPublisher(publisher);
        registration.interceptors(new SecurityContextChannelInterceptor(), interceptor);
    }
}
