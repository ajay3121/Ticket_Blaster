package dev.ajay.ticket_blaster.ticketblaster_backend.models;

import dev.ajay.ticket_blaster.ticketblaster_backend.enums.SeatName;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "seat_types")
public class SeatType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private SeatName type;

    private BigDecimal price;
}
