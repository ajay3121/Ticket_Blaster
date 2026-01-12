package dev.ajay.ticket_blaster.ticketblaster_backend.repository;

import dev.ajay.ticket_blaster.ticketblaster_backend.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {
    public List<Seat> findByScreenId(UUID id);
}
