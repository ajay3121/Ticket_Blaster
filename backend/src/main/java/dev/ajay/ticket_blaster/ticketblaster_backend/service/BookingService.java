package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import dev.ajay.ticket_blaster.ticketblaster_backend.enums.SeatStatus;
import dev.ajay.ticket_blaster.ticketblaster_backend.models.Seat;
import dev.ajay.ticket_blaster.ticketblaster_backend.repository.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final SeatRepository seatRepository;
    public void bookSeat(int userNumber, UUID seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found: " + seatId));

        if (seat.getSeatStatus() == SeatStatus.AVAILABLE) {
            seat.setSeatStatus(SeatStatus.BOOKED);

            seatRepository.save(seat);
            log.info("User {} successfully booked seat {}", userNumber, seatId);
        } else {
            log.warn("User {} failed to book: Seat {} is already {}", userNumber, seatId, seat.getSeatStatus());
        }
    }
}
