package dev.ajay.ticket_blaster.ticketblaster_backend.bootstrap;

import dev.ajay.ticket_blaster.ticketblaster_backend.enums.SeatName;
import dev.ajay.ticket_blaster.ticketblaster_backend.enums.SeatStatus;
import dev.ajay.ticket_blaster.ticketblaster_backend.models.Screen;
import dev.ajay.ticket_blaster.ticketblaster_backend.models.Seat;
import dev.ajay.ticket_blaster.ticketblaster_backend.models.SeatType;
import dev.ajay.ticket_blaster.ticketblaster_backend.models.Theater;
import dev.ajay.ticket_blaster.ticketblaster_backend.repository.ScreenRepository;
import dev.ajay.ticket_blaster.ticketblaster_backend.repository.SeatRepository;
import dev.ajay.ticket_blaster.ticketblaster_backend.repository.SeatTypeRepository;
import dev.ajay.ticket_blaster.ticketblaster_backend.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class TicketBlasterSeeder implements ApplicationRunner {

    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final SeatTypeRepository seatTypeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (theaterRepository.existsByName("PVR ICON")) {
            log.info("application has been seeded already");
            return;
        }

        log.info("Beginning the seeding process...");

        SeatType vip = SeatType.builder()
                .type(SeatName.VIP)
                .price(BigDecimal.valueOf(500.00))
                .build();
        seatTypeRepository.save(vip);

        SeatType economy = SeatType.builder()
                .type(SeatName.ECONOMY)
                .price(BigDecimal.valueOf(100.00))
                .build();
        seatTypeRepository.save(economy);

        Theater pvr = Theater.builder()
                .name("PVR ICON")
                .location("Hyderabad")
                .build();
        theaterRepository.save(pvr);

        Screen screen = Screen.builder()
                .theater(pvr)
                .name("Screen 1")
                .build();
        screenRepository.save(screen);

        List<Seat> allSeats = new ArrayList<>();

        // Rows 1-7 (Economy)
        for (int row = 1; row <= 7; row++) {
            for (int col = 1; col <= 10; col++) {
                allSeats.add(Seat.builder()
                        .seatType(economy)
                        .screen(screen)
                        .rowNum(row)
                        .colNum(col)
                        .seatStatus(SeatStatus.AVAILABLE)
                        .build());
            }
        }

        // Rows 8-10 (VIP)
        for (int row = 8; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                allSeats.add(Seat.builder()
                        .seatType(vip)
                        .screen(screen)
                        .rowNum(row)
                        .colNum(col)
                        .seatStatus(SeatStatus.AVAILABLE)
                        .build());
            }
        }

        seatRepository.saveAll(allSeats);

        log.info("Seeding Completed: Created 1 Theater, 1 Screen, and 100 Seats.");
    }
}