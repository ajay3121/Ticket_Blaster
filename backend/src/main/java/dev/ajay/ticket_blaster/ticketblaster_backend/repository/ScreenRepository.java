package dev.ajay.ticket_blaster.ticketblaster_backend.repository;

import dev.ajay.ticket_blaster.ticketblaster_backend.models.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScreenRepository extends JpaRepository<Screen, UUID> {
}
