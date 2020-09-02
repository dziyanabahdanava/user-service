package com.epam.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private EmailBuilder builder;

    public void sendEmail(String to, String subject, String templatePath, Map<String, Object> mapProperties) {
        emailSender.send(mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("microservices.mp@mail.ru");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            String content = builder.build(templatePath, mapProperties);
            messageHelper.setText(content, true);
        });
    }
}
