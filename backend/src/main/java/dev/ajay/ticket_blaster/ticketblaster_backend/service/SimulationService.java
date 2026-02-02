package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import dev.ajay.ticket_blaster.ticketblaster_backend.dto.StartSimulationRequestDto;
import dev.ajay.ticket_blaster.ticketblaster_backend.dto.StartSimulationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SimulationService {
    private final RateLimiterService rateLimiterService;
    private final RedisLockService redisLockService;
    private final BookingService bookingService;

    public void startUserJourney(int userNumber, UUID theatreId, UUID screenId, UUID seatId){
        String key = "ticket_blaster:lock:theatre:" + theatreId + "screen:" + screenId + "seat:" + seatId;
        log.info("journey started for {}", userNumber);
        long endTime = System.currentTimeMillis() + 2000;

        while(System.currentTimeMillis() <= endTime){
            log.info("User {} is trying to acquire redis lock", userNumber);
            if(rateLimiterService.acquireRedisLock()){
                if(redisLockService.acquireLock(key, "user-" + userNumber, 20)){
                    log.error("User {} acquired redis lock", userNumber);
                    try {
                        Thread.sleep(500);
                        bookingService.bookSeat(userNumber, seatId);
                    } catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                        break;
                    }finally {
                        redisLockService.releaseLock(key, "user-" + userNumber);
                    }
                    log.info("user {} can now book the seat", userNumber);
                    break;
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }else{
                log.info("User {} cannot acquire redis lock and retrying", userNumber);
                long jitter = 50 + (long) (50 * Math.random());
                try {
                    Thread.sleep(jitter);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public void stopSimulation() {
        // Logic to stop the simulation will be implemented here
    }

}
