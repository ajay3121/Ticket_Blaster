package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import dev.ajay.ticket_blaster.ticketblaster_backend.dto.StartSimulationRequestDto;
import dev.ajay.ticket_blaster.ticketblaster_backend.dto.StartSimulationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SimulationService {
    private final RateLimiterService rateLimiterService;

    public void startUserJourney(int userNumber, int theatreId, int screenId, int seatId){
        log.info("journey started for {}", userNumber);
        if(rateLimiterService.allowRequest()){
            log.info("User {} can now acquire redis lock", userNumber);
        }else{
            log.info("User {} cannot acquire redis lock", userNumber);
        }
    }

    public void stopSimulation() {
        // Logic to stop the simulation will be implemented here
    }

}
