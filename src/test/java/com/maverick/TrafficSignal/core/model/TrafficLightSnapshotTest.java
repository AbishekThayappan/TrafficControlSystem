package com.maverick.TrafficSignal.core.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrafficLightSnapshotTest {

    @Test
    public void shouldCreateValidSnapshot() {
        Instant now = Instant.now();
        TrafficLightSnapshot snapshot = new TrafficLightSnapshot(
                Direction.NORTH,
                TrafficLightState.GREEN,
                1000,
                now
        );

        assertEquals(Direction.NORTH, snapshot.direction());
        assertEquals(TrafficLightState.GREEN, snapshot.state());
        assertEquals(1000, snapshot.durationMillis());
        assertEquals(now, snapshot.timestamp());
    }

}
