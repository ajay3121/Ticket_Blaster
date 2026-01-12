package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import dev.ajay.ticket_blaster.ticketblaster_backend.dto.SeatResponseDto;
import dev.ajay.ticket_blaster.ticketblaster_backend.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadFactory;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    SeatService(SeatRepository seatRepository){
        this.seatRepository = seatRepository;
    }

    public List<SeatResponseDto> getSeatsByScreenId(UUID id){
        return seatRepository.findByScreenId(id)
                .stream()
                .map(SeatResponseDto::fromEntity)
                .toList();

    }
}
