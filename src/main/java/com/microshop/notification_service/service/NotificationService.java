package com.microshop.notification_service.service;

import com.microshop.notification_service.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final RestTemplate restTemplate;
    private final TemplateEngine templateEngine;

    @Value("${brevo.api.url}")
    private String apiUrl;

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.from.email}")
    private String fromEmail;

    @Value("${brevo.from.name}")
    private String fromName;

    public NotificationService(RestTemplate restTemplate, TemplateEngine templateEngine) {
        this.restTemplate = restTemplate;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(NotificationRequest request) {
        logger.info("Sending {} email to {} via Brevo API", request.getType(), request.getTo());

        // 1. Procesar Template HTML
        Context context = new Context();
        context.setVariable("subject", request.getSubject());
        context.setVariable("body", request.getBody());
        String htmlContent = templateEngine.process(getTemplateName(request.getType()), context);

        // 2. Construir JSON para Brevo
        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("email", fromEmail, "name", fromName));
        body.put("to", Collections.singletonList(Map.of("email", request.getTo())));
        body.put("subject", request.getSubject());
        body.put("htmlContent", htmlContent);

        // 3. Headers con la API Key de Brevo
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // 4. Enviar
        try {
            restTemplate.postForEntity(apiUrl, entity, String.class);
            logger.info("Email sent successfully via Brevo API");
        } catch (Exception e) {
            logger.error("Failed to send email via Brevo API: {}", e.getMessage());
            throw new RuntimeException("Error enviando email: " + e.getMessage());
        }
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
