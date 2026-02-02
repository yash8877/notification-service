package com.notification_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long id;

    private Long userId;
    private Long eventId;
    private int numberOfTickets;
    private LocalDateTime bookingTime;
    private String bookingStatus;
    private int numberOfCancelTickets;
    private LocalDateTime cancellationTime;
}
