package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import dev.ajay.ticket_blaster.ticketblaster_backend.dto.StartSimulationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Service
@RequiredArgsConstructor
@Slf4j
public class VirtualUserService {
    private final SimulationService simulationService;


    @Async
    public void initializeUser(StartSimulationRequestDto requestDto){
        ThreadFactory threadFactory = Thread.ofVirtual()
                        .name("virtual_user_#", 1)
                        .factory();
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch readyLatch = new CountDownLatch(requestDto.getNumberOfUsers());

        try (var executor = Executors.newThreadPerTaskExecutor(threadFactory)) {

            for (int i = 0; i < requestDto.getNumberOfUsers(); i++) {
                final int userNumber = i+1;
                executor.submit(
                        () -> {
                            try {
                                readyLatch.countDown();
                                startLatch.await();
                                simulationService.startUserJourney(userNumber, requestDto.getTheatreId(), requestDto.getScreenId(),requestDto.getSeatId());
                            } catch (InterruptedException e) {
                            }
                            }
                        );
            }

            readyLatch.await();
            log.info("all user threads are initialized and waiting");
            startLatch.countDown();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
