package dev.ajay.ticket_blaster.ticketblaster_backend.controller;

import dev.ajay.ticket_blaster.ticketblaster_backend.models.SimulationMetrics;
import dev.ajay.ticket_blaster.ticketblaster_backend.service.MetricRegistryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MetricsController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MetricRegistryService metricRegistryService;

    // Runs every 100 milliseconds
    @Scheduled(fixedRate = 200)
    public void sendSimulationMetrics() {
        log.info("metrics are sent to client");
        SimulationMetrics simulationMetrics = metricRegistryService.getSimulationMetrics("user-1");

        if (simulationMetrics != null) {
            messagingTemplate.convertAndSend("/metrics", simulationMetrics);
        }
//        messagingTemplate.convertAndSend("/metrics", "hello-world");

    }
}