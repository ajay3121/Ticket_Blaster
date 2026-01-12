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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;

    @Transactional
    public BookingResponseDto bookSeat(BookingRequestDto request) {
        try{
            Seat seat = seatRepository.findById(request.getSeatId())
                    .orElseThrow(() -> new NoSuchElementException("Seat not found"));

            // 2. Strict Availability Check
            if (seat.getSeatStatus() != SeatStatus.AVAILABLE) {
                throw new RuntimeException("Seat is already booked!");
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

            bookingSeatRepository.save(bookingSeat); // You might need to inject this repo

            // 6. Return Success Response
            return BookingResponseDto.builder()
                    .bookingId(booking.getId())
                    .status("CONFIRMED")
                    .amount(booking.getTotalAmount())
                    .build();
        } catch (org.springframework.orm.ObjectOptimisticLockingFailureException e){
            throw new IllegalStateException("Too late! Someone just booked this seat.");
        }

    }
}
