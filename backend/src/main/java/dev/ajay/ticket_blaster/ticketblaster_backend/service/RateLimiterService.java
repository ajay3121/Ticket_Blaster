package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RateLimiterService {
    private final Bucket bucket;

    public boolean acquireBucket(){
        return bucket.tryConsume(1);
    }
}
