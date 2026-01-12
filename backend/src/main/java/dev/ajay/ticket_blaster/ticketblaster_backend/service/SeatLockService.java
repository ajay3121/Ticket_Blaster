package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatLockService {

    private final StringRedisTemplate redisTemplate;

    private static final String LOCK_PREFIX = "seat-lock:";

    /**
     * Tries to acquire a lock on a seat.
     * @return true if lock acquired, false if seat is already locked by someone else
     */
    public boolean acquireLock(UUID seatId, UUID userId) {
        String key = LOCK_PREFIX + seatId;

        // "setIfAbsent" is Atomic. It corresponds to Redis "SETNX" command.
        // It only sets the key if it DOES NOT exist.
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, userId.toString(), Duration.ofMinutes(10));

        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(UUID seatId) {
        redisTemplate.delete(LOCK_PREFIX + seatId);
    }

    public boolean isLocked(UUID seatId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(LOCK_PREFIX + seatId));
    }
}