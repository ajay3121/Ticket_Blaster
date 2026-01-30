package dev.ajay.ticket_blaster.ticketblaster_backend.dto;

import dev.ajay.ticket_blaster.ticketblaster_backend.models.Seat;
import dev.ajay.ticket_blaster.ticketblaster_backend.enums.SeatStatus;
import java.math.BigDecimal;
import java.util.UUID;

public record SeatResponseDto(
        UUID seatId,
        int row,
        int col,
        SeatStatus status
) {

    public static SeatResponseDto fromEntity(Seat seat) {
        return new SeatResponseDto(
                seat.getId(),
                seat.getRowNum(),
                seat.getColNum(),
                seat.getSeatStatus()

        );
    }
}