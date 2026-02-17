package com.maverick.TrafficSignal.core.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    @Test
    public void shouldRejectNullDirection() {
        assertThrows(IllegalArgumentException.class, () ->
                new TrafficLightSnapshot(
                        null,
                        TrafficLightState.GREEN,
                        0,
                        Instant.now()
                )
        );
    }

    @Test
    public void shouldRejectNullState() {
        assertThrows(IllegalArgumentException.class, () ->
                new TrafficLightSnapshot(
                        Direction.NORTH,
                        null,
                        0,
                        Instant.now()
                )
        );
    }

    @Test
    public void shouldRejectNullTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                new TrafficLightSnapshot(
                        Direction.NORTH,
                        TrafficLightState.GREEN,
                        0,
                        null
                )
        );
    }

    @Test
    public void shouldRejectNegativeDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                new TrafficLightSnapshot(
                        Direction.NORTH,
                        TrafficLightState.GREEN,
                        -1,
                        Instant.now()
                )
        );
    }

    @Test
    public void shouldAllowZeroDuration() {
        TrafficLightSnapshot snapshot = new TrafficLightSnapshot(
                Direction.NORTH,
                TrafficLightState.GREEN,
                0,
                Instant.now()
        );
        assertEquals(0, snapshot.durationMillis());
    }

    @Test
    public void shouldIdentifyGreenSnapshot() {
        TrafficLightSnapshot greenSnapshot = new TrafficLightSnapshot(
                Direction.NORTH,
                TrafficLightState.GREEN,
                0,
                Instant.now()
        );
        assertTrue(greenSnapshot.isGreen());

        TrafficLightSnapshot redSnapshot = new TrafficLightSnapshot(
                Direction.NORTH,
                TrafficLightState.RED,
                0,
                Instant.now()
        );
        assertFalse(redSnapshot.isGreen());
    }


}
