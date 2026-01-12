package dev.ajay.ticket_blaster.ticketblaster_backend.dto;

import lombok.Builder;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record BookingResponseDto(
        UUID bookingId,
        String status,
        BigDecimal amount
) {}