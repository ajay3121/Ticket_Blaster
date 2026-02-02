package dev.ajay.ticket_blaster.ticketblaster_backend.config;

import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiter {

    @Bean
    public Bucket bucket() {
      return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(100)
                        .refillGreedy(50, Duration.ofSeconds(1)))
                .build();
    }
}
