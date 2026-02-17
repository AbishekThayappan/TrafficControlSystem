package com.maverick.TrafficSignal.core.model;

import java.time.Instant;


public record TrafficLightSnapshot(
        Direction direction,
        TrafficLightState state,
        long durationMillis,
        Instant timestamp) {

    public TrafficLightSnapshot {
        if (direction == null)
            throw new IllegalArgumentException("Direction cannot be null");
        if (state == null)
            throw new IllegalArgumentException("State cannot be null");
        if (durationMillis < 0)
            throw new IllegalArgumentException("Duration cannot be negative");
        if (timestamp == null)
            throw new IllegalArgumentException("Timestamp cannot be null");
    }

    public boolean isGreen() {
        return state.isGreen();
    }
}
