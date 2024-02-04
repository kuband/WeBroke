package com.we.broke.common.email;

import com.we.broke.auth.models.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.ionos.co.uk");
        mailSender.setPort(465);

        mailSender.setUsername("contact@jakubkijak.co.uk");
        mailSender.setPassword("fuhgip-kuhnem-9pyjDy");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.enable", "true");

        return mailSender;
    }

    public SimpleMailMessage constructEmail(String subject, String body,
                                            User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom("webrokeltd@gmail.com");
        return email;
    }

    public SimpleMailMessage constructPasswordResetTokenEmail(
            String contextPath, String token, User user) {
        String url = contextPath + "/user/savePassword?token=" + token;
        return constructEmail("Reset Password", "Click here to reset your password!:" +
                " " + " \r\n" + url, user);
    }

    public SimpleMailMessage constructOrgInviteTokenEmail(
            String contextPath, String token, User user) {
        String url = contextPath + "/api/v1/organisation/acceptInvite/?token=" + token;
        return constructEmail("Reset Password", "Click here to reset your password!:" +
                " " + " \r\n" + url, user);
    }
}
