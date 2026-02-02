package com.notification_service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String email;
    private String userName;
    private String eventName;
    private String bookingId;
    private String paymentAmount;
    private String numberOfTickets;
    private String paymentStatus;
    private String bookingTime;
    private LocalDate eventDate;
    private String eventLocation;
}

