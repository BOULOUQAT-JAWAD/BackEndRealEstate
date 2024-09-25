package com.realestate.backendrealestate.services.smptHandler;


import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;


    public String fetchReceiptHtml(String receiptUrl) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(receiptUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch the receipt content.");
        }
        return content.toString();
    }

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
