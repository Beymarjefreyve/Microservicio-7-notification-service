package com.microshop.notification_service.controller;

import com.microshop.notification_service.dto.NotificationRequest;
import com.microshop.notification_service.service.NotificationService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerification(@RequestBody NotificationRequest request) throws MessagingException {
        request.setType("VERIFICATION");
        notificationService.sendEmail(request);
        return ResponseEntity.ok("Verification email sent");
    }

    @PostMapping("/send-password-reset")
    public ResponseEntity<String> sendPasswordReset(@RequestBody NotificationRequest request) throws MessagingException {
        request.setType("PASSWORD_RESET");
        notificationService.sendEmail(request);
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/send-order-status")
    public ResponseEntity<String> sendOrderStatus(@RequestBody NotificationRequest request) throws MessagingException {
        request.setType("ORDER_STATUS");
        notificationService.sendEmail(request);
        return ResponseEntity.ok("Order status email sent");
    }
}
