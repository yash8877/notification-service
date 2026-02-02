package com.notification_service.controller;

import com.notification_service.dto.BookingDTO;
import com.notification_service.dto.NotificationRequest;
import com.notification_service.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/payment-success")
    public ResponseEntity<String> sendPaymentNotification(@RequestBody NotificationRequest request) {
        try {
            emailService.sendEmailWithPdf(request);
            return ResponseEntity.ok("Notification with PDF sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send notification: " + e.getMessage());
        }
    }


    @PostMapping("/cancel-notification")
    public void sendCancelNotification(@RequestBody BookingDTO booking,@RequestParam String email){
        emailService.sendCancelNotification(booking,email);
    }

}

