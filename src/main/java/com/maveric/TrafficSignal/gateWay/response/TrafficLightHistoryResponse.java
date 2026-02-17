package com.maveric.TrafficSignal.gateWay.response;

import com.maveric.TrafficSignal.core.model.TrafficLightSnapshot;

import java.util.List;
import java.util.stream.Collectors;

public record TrafficLightHistoryResponse(
        String direction,
        List<TrafficLightResponse> history,
        int totalEntries) {

    public static TrafficLightHistoryResponse from(String direction,
                                                   List<TrafficLightSnapshot> snapshots) {
        return new TrafficLightHistoryResponse(
                direction,
                snapshots.stream()
                        .map(TrafficLightResponse::from)
                        .collect(Collectors.toList()),
                snapshots.size());
    }
}
