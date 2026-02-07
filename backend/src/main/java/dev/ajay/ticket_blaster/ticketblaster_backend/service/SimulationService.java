package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import dev.ajay.ticket_blaster.ticketblaster_backend.exception.SimulatedPaymentError;
import dev.ajay.ticket_blaster.ticketblaster_backend.models.SimulationMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class SimulationService {
    private final RateLimiterService rateLimiterService;
    private final RedisLockService redisLockService;
    private final BookingService bookingService;
    private final ConcurrentHashMap<String, Boolean> localCache = new ConcurrentHashMap<>();
    private final MetricRegistryService metricRegistryService;


    public void startUserJourney(int userNumber, UUID theatreId, UUID screenId, UUID seatId, SimulationMetrics simulationMetrics){
        String key = "ticket_blaster:lock:theatre:" + theatreId + "screen:" + screenId + "seat:" + seatId;

        log.info("journey started for {}", userNumber);
        simulationMetrics.getActiveThreads().incrementAndGet();

        long endTime = System.currentTimeMillis() + 4000;

        while(System.currentTimeMillis() <= endTime){
            simulationMetrics.getTotalRequestsInitiated().incrementAndGet();

            if(localCache.containsKey(seatId.toString())){
                simulationMetrics.getStoppedByShortCircuit().incrementAndGet();
                log.info("seat is booked by another user and hit is from local cache, User {} is stopping", userNumber);
                break;
            }
            //rate limiter
            if(!rateLimiterService.acquireBucket()){
               simulationMetrics.getRateLimiterRejections().incrementAndGet();
                log.info("User {} cannot acquire bucket in rate limiter, retrying", userNumber);
                retryWithJitter();
                continue;
            }

            //check to prevent thread from acquiring lock if seat is sold
            if(redisLockService.isSeatSold(seatId.toString())){
                simulationMetrics.getStoppedByShortCircuit().incrementAndGet();
                log.info("seat is booked by another user, User {} is stopping", userNumber);
                localCache.putIfAbsent(seatId.toString(), true);
                break;
            }

            if(redisLockService.acquireLock(key, "user-" + userNumber, 20)){
                log.info("User {} acquired redis lock", userNumber);
                try {
                    //simulate payment by the user
                    Thread.sleep(200);
                    simulateRandomFailure();
                    bookingService.bookSeat(userNumber, seatId);
                    redisLockService.markSeatAsSold(seatId.toString());
                    localCache.putIfAbsent(seatId.toString(), true);
                    simulationMetrics.getSuccessfulBookings().incrementAndGet();
                    simulationMetrics.setSeatBookedUser(userNumber);
                    return;
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                } catch (SimulatedPaymentError e){
                    log.error("simulated failure occurred for redis lock acquired user {}", userNumber,e);
                    retryWithJitter();
                }
                finally {
                    redisLockService.releaseLock(key, "user-" + userNumber);
                }
            }else {
                log.info("User {} failed to get redis lock. Waiting...", userNumber);
                simulationMetrics.getLockAcquisitionFailures().incrementAndGet();
                retryWithJitter();
            }
        }
    }

    public void stopSimulation() {
        // Logic to stop the simulation will be implemented here
    }

    private void retryWithJitter(){
        long jitter = 50 + (long) (50 * Math.random());
        try {
            Thread.sleep(jitter);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void simulateRandomFailure() throws SimulatedPaymentError {
        double chance = ThreadLocalRandom.current().nextDouble();

        if (chance <= 0.50) {
            log.warn("Chaos Monkey: Simulating a 50% failure rate.");
            throw new SimulatedPaymentError("Simulated Network/Rate-Limit Failure");
        }
    }
}
