package com.epam.ms.handler;

import com.epam.ms.repository.domain.User;
import com.epam.ms.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class UserRegistrationHandler {
    private static final String SUBJECT = "Email confirmation";
    private static final String TEMPLATE_PATH = "confirm";

    @Autowired
    private EmailService emailService;

    public void sendEmailConfirmation(User user) {
        log.info("Waiting for email confirmation from {}", user.getEmail());
        emailService.sendEmail(user.getEmail(), SUBJECT, TEMPLATE_PATH, Map.of("id", user.getId()));
    }
}
