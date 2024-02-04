package com.we.broke.auth.event;

import com.we.broke.auth.models.entity.User;
import com.we.broke.auth.security.services.VerificationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private VerificationTokenService service;
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/api/v1/auth/registrationConfirm?token=" + token;
        String message = "Click here to confirm registration";
        log.info("confirmationUrl: {}", confirmationUrl);

//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setTo(recipientAddress);
//        email.setSubject(subject);
//        email.setText(message + "\r\n" + confirmationUrl);
//        mailSender.send(email);
    }
}
