package com.maveric.TrafficSignal.gateWay.response;

import com.maveric.TrafficSignal.core.model.TrafficLightSnapshot;

/**
 * DTO for API responses.
 * Represents the state of a single traffic light.
 */
public record TrafficLightResponse(
        String direction,
        String state,
        long durationMillis,
        String timestamp) {

    public static TrafficLightResponse from(TrafficLightSnapshot snapshot) {
        return new TrafficLightResponse(
                snapshot.direction().toString(),
                snapshot.state().getDisplayName(),
                snapshot.durationMillis(),
                snapshot.timestamp().toString());
    }
}
