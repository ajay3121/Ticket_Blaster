package dev.ajay.ticket_blaster.ticketblaster_backend.dto;

import lombok.Data;

@Data
public class StartSimulationRequestDto {
    private int numberOfUsers;
    private int theatreId;
    private int seatId;
    private int screenId;
}
