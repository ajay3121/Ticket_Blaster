package dev.ajay.ticket_blaster.ticketblaster_backend.controller;

import dev.ajay.ticket_blaster.ticketblaster_backend.dto.SeatResponseDto;
import dev.ajay.ticket_blaster.ticketblaster_backend.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/screens")
public class SeatsController {

    private final SeatService seatService;

    @GetMapping("/{screenId}/seats")
    public ResponseEntity<List<SeatResponseDto>> getSeats(@PathVariable UUID screenId){
        return ResponseEntity.ok().body(seatService.getSeatsByScreenId(screenId));
    }
}
