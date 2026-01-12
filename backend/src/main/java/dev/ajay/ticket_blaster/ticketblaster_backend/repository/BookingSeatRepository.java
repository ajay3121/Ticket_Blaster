package dev.ajay.ticket_blaster.ticketblaster_backend.repository;

import dev.ajay.ticket_blaster.ticketblaster_backend.models.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, UUID> {
}
