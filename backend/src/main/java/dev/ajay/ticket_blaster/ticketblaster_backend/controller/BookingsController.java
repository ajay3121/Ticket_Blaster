package dev.ajay.ticket_blaster.ticketblaster_backend.controller;

import dev.ajay.ticket_blaster.ticketblaster_backend.dto.BookingRequestDto;
import dev.ajay.ticket_blaster.ticketblaster_backend.models.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/bookings")
public class BookingsController {

    @PostMapping()
    public ResponseEntity<Booking> bookSeat(@RequestBody BookingRequestDto bookingRequestDTO){
        return ResponseEntity.ok().body(Booking.builder().build());
    }
}
