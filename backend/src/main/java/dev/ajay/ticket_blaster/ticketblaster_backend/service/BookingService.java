package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import dev.ajay.ticket_blaster.ticketblaster_backend.dto.BookingRequestDto;
import dev.ajay.ticket_blaster.ticketblaster_backend.dto.BookingResponseDto;
import dev.ajay.ticket_blaster.ticketblaster_backend.enums.BookingStatus;
import dev.ajay.ticket_blaster.ticketblaster_backend.enums.SeatStatus;
import dev.ajay.ticket_blaster.ticketblaster_backend.models.Booking;
import dev.ajay.ticket_blaster.ticketblaster_backend.models.BookingSeat;
import dev.ajay.ticket_blaster.ticketblaster_backend.models.Seat;
import dev.ajay.ticket_blaster.ticketblaster_backend.repository.BookingRepository;
import dev.ajay.ticket_blaster.ticketblaster_backend.repository.BookingSeatRepository;
import dev.ajay.ticket_blaster.ticketblaster_backend.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatLockService seatLockService; // <--- 1. Inject this

    @Transactional
    public BookingResponseDto bookSeat(BookingRequestDto request) {

        // 2. The Redis Guard (Fast Fail)
        // We try to acquire a lock. If we can't, someone else is processing this seat.
        boolean acquired = seatLockService.acquireLock(request.getSeatId(), request.getUserId());

        if (!acquired) {
            // Redis says NO. We don't even bother the Database.
            throw new IllegalStateException("Seat is currently selected by another user.");
        }

        try {
            // ... Your Existing DB Logic ...
            Seat seat = seatRepository.findById(request.getSeatId())
                    .orElseThrow(() -> new NoSuchElementException("Seat not found"));

            if (seat.getSeatStatus() != SeatStatus.AVAILABLE) {
                throw new IllegalStateException("Seat is already booked!");
            }

            // 3. Lock the Seat (Update Status)
            seat.setSeatStatus(SeatStatus.BOOKED);
            seatRepository.save(seat);

            // 4. Create the "Booking" (The Receipt)
            Booking booking = Booking.builder()
                    .userId(3)
                    .bookingStatus(BookingStatus.CONFIRMED) // Assuming instant confirmation for now
                    .totalAmount(seat.getSeatType().getPrice())
                    .build();

            // Save Booking first so we have an ID
            bookingRepository.save(booking);

            // 5. Create the "BookingSeat" (The Link)
            BookingSeat bookingSeat = BookingSeat.builder()
                    .booking(booking)
                    .seat(seat)
                    .priceSnapshot(seat.getSeatType().getPrice()) // Lock the price at moment of purchase
                    .build();

            bookingSeatRepository.save(bookingSeat);

            return BookingResponseDto.builder()
                    .bookingId(booking.getId())
                    .status("CONFIRMED")
                    .amount(booking.getTotalAmount())
                    .build();

        } finally {
            // 3. Cleanup
            // Since this is "Instant Book", we release the lock immediately after
            // the DB transaction finishes (success or fail).
            seatLockService.releaseLock(request.getSeatId());
        }
    }
}
