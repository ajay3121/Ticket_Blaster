package dev.ajay.ticket_blaster.ticketblaster_backend.models;

import dev.ajay.ticket_blaster.ticketblaster_backend.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer userId;

   @OneToMany(orphanRemoval = true, mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingSeat> bookingSeatList;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @CreationTimestamp
    private LocalDateTime bookingTime;

    private BigDecimal totalAmount;
}
