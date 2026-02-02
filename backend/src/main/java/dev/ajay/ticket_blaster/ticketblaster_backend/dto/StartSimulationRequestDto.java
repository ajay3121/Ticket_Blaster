package dev.ajay.ticket_blaster.ticketblaster_backend.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StartSimulationRequestDto {
    private int numberOfUsers;
    private UUID theatreId;
    private UUID seatId;
    private UUID screenId;
}
