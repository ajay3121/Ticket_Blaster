package dev.ajay.ticket_blaster.ticketblaster_backend.models;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class SimulationMetrics {
    private final AtomicInteger activeThreads = new AtomicInteger();
    private final AtomicInteger totalRequestsInitiated = new AtomicInteger();
    private final AtomicInteger rateLimiterRejections = new AtomicInteger();
    private final AtomicInteger successfulBookings = new AtomicInteger();
    private final AtomicInteger stoppedByShortCircuit = new AtomicInteger();
    private final AtomicInteger lockAcquisitionFailures = new AtomicInteger();
    private  Integer seatBookedUser;
}
