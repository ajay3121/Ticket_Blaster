package dev.ajay.ticket_blaster.ticketblaster_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
    UUID seatId;
    UUID userId;
}
