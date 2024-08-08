package com.realestate.backendrealestate.services.smptHandler;


import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    @Async
    public void send(String to, String subject, String text) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(new InternetAddress("realEstate@sfrg.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject(subject);
            String htmlContent = text;
            message.setContent(htmlContent, "text/html; charset=utf-8");
            javaMailSender.send(message);
            log.info("Mail sent to the user!");
        }
        catch (Exception exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

}
