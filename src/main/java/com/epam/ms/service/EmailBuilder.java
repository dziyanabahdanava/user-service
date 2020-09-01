package com.epam.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class EmailBuilder {
    @Autowired
    private TemplateEngine templateEngine;

    public String build(String templatePath, Map<String, Object> properties) {
        Context context = new Context();
        context.setVariables(properties);
        return templateEngine.process(templatePath, context);
    }
}
