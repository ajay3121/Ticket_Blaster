package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
public class RedisLockService {
    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> releaseScript;

    public RedisLockService(StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = stringRedisTemplate;
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        this.releaseScript = new DefaultRedisScript<>(script, Long.class);
    }

    /**
     * Tries to acquire the lock ATOMICALLY.
     * @param key The seat (e.g., "lock:seat-A1")
     * @param userId The unique owner (e.g., "virtual-user-500")
     * @param ttlSeconds How long before it auto-expires (Safety net)
     * @return true if acquired, false if someone else has it
     */

    public boolean acquireLock(String key, String userId, int ttlSeconds){
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, userId, Duration.ofSeconds(ttlSeconds));

        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(String key, String userId) {
        redisTemplate.execute(releaseScript, Collections.singletonList(key), userId);
    }

}
