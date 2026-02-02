package dev.ajay.ticket_blaster.ticketblaster_backend.dto;

import lombok.Data;

@Data
public class StartSimulationResponseDto {
    private String message = "Simulation started successfully";
    private boolean status = true;

}
