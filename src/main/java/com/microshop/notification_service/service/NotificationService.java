package com.microshop.notification_service.service;

import com.microshop.notification_service.dto.NotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public NotificationService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(NotificationRequest request) throws MessagingException {
        logger.info("Sending {} email to {}", request.getType(), request.getTo());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("subject", request.getSubject());
        context.setVariable("body", request.getBody());
        
        String templateName = getTemplateName(request.getType());
        String htmlContent = templateEngine.process(templateName, context);

        helper.setFrom(fromEmail);
        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());
        helper.setText(htmlContent, true);

        mailSender.send(message);
        logger.info("Email sent successfully");
    }

    private String getTemplateName(String type) {
        if (type == null) return "generic-email";
        return switch (type.toUpperCase()) {
            case "VERIFICATION" -> "verification-email";
            case "PASSWORD_RESET" -> "password-reset-email";
            case "ORDER_STATUS" -> "order-status-email";
            default -> "generic-email";
        };
    }
}
