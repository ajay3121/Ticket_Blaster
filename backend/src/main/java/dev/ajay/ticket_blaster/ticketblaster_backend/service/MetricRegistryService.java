package dev.ajay.ticket_blaster.ticketblaster_backend.service;

import dev.ajay.ticket_blaster.ticketblaster_backend.models.SimulationMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MetricRegistryService {
    private final ConcurrentHashMap<String, SimulationMetrics> metricsConcurrentHashMap = new ConcurrentHashMap<>();

    public void registerSimulationMetrics(String userId, SimulationMetrics simulationMetrics){
        metricsConcurrentHashMap.put(userId, simulationMetrics);
    }

    public SimulationMetrics getSimulationMetrics(String userId){
        return metricsConcurrentHashMap.get(userId);
    }
}
