package com.realestate.backendrealestate.services.smptHandler;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    public String build(String message){
        Context context = new Context();
        context.setVariable("verificationlink",message);
        return templateEngine.process("mailTemplate",context);
    }
}
