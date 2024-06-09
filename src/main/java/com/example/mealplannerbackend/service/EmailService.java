package com.example.mealplannerbackend.service;

import com.example.mealplannerbackend.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendWelcomeEmail(User user) {
        try {
            // Create a MimeMessage
            MimeMessage message = mailSender.createMimeMessage();

            // Use the helper to set the properties of the message
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            // Create a Thymeleaf context and add variables
            Context context = new Context();
            context.setVariable("recipientName", user.getName()); // You can pass actual user details here
            context.setVariable("welcomeMessage", "Welcome to Munchie!");

            // Process the Thymeleaf template to generate the email content
            String html = templateEngine.process("welcome-email-template", context);

            // Set the email attributes
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to Munchie");
            helper.setText(html, true);
            helper.setFrom("munchie.application@gmail.com"); // Replace with your email address

            // Send the email
            mailSender.send(message);
        } catch (MessagingException e) {
            // Handle the exception (log it, etc.)
            e.printStackTrace();
        }
    }

}
