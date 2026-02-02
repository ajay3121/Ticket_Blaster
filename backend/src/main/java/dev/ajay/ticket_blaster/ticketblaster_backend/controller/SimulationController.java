package dev.ajay.ticket_blaster.ticketblaster_backend.controller;

import dev.ajay.ticket_blaster.ticketblaster_backend.dto.StartSimulationRequestDto;
import dev.ajay.ticket_blaster.ticketblaster_backend.dto.StartSimulationResponseDto;
import dev.ajay.ticket_blaster.ticketblaster_backend.service.VirtualUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
@Slf4j
public class SimulationController {

    private final VirtualUserService virtualUserService;

    @PostMapping("/start")
    private ResponseEntity<StartSimulationResponseDto> startSimulation(@RequestBody StartSimulationRequestDto request){
        virtualUserService.initializeUser(request);
        return ResponseEntity.ok().body(new StartSimulationResponseDto());
    }
}
