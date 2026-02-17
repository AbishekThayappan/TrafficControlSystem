package com.maverick.TrafficSignal.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrafficLightTest {
    private TrafficLight light;

    @BeforeEach
    public void setUp() {
        light = new TrafficLight(Direction.NORTH);
    }

    @Test
    public void shouldInitializeAsRed() {
        assertEquals(TrafficLightState.RED, light.getSnapshot().state());
        assertEquals(Direction.NORTH, light.getDirection());
    }

    @Test
    public void shouldTransitionThroughValidSequence() {
        assertEquals(TrafficLightState.RED, light.getSnapshot().state());

        light.transitionToNextState();
        assertEquals(TrafficLightState.GREEN, light.getSnapshot().state());

        light.transitionToNextState();
        assertEquals(TrafficLightState.YELLOW, light.getSnapshot().state());

        light.transitionToNextState();
        assertEquals(TrafficLightState.RED, light.getSnapshot().state());
    }

    @Test
    public void shouldSetStateManually() {
        light.setState(TrafficLightState.GREEN);
        assertEquals(TrafficLightState.GREEN, light.getSnapshot().state());

        light.setState(TrafficLightState.RED);
        assertEquals(TrafficLightState.RED, light.getSnapshot().state());
    }

    @Test
    public void shouldTrackHistory() {
        light.setState(TrafficLightState.GREEN);
        light.setState(TrafficLightState.YELLOW);
        light.setState(TrafficLightState.RED);

        var history = light.getHistory();
        assertEquals(4, history.size()); // initial RED + 3 transitions
    }

    @Test
    public void shouldReturnSnapshotWithTimestamp() {
        var snapshot = light.getSnapshot();
        assertNotNull(snapshot.timestamp());
        assertTrue(snapshot.timestamp().isBefore(Instant.now()) ||
                snapshot.timestamp().equals(Instant.now()));
    }

    @Test
    public void shouldHandleDefaultDurations() {
        assertEquals(30000L, light.getDurationForState(TrafficLightState.RED));
        assertEquals(25000L, light.getDurationForState(TrafficLightState.GREEN));
        assertEquals(5000L, light.getDurationForState(TrafficLightState.YELLOW));
    }

    @Test
    public void shouldHandleCustomDurations() {
        Map<TrafficLightState, Long> custom = new HashMap<>();
        custom.put(TrafficLightState.RED, 10000L);
        custom.put(TrafficLightState.GREEN, 20000L);
        custom.put(TrafficLightState.YELLOW, 5000L);

        TrafficLight customLight = new TrafficLight(Direction.EAST, TrafficLightState.RED, custom);
        assertEquals(10000L, customLight.getDurationForState(TrafficLightState.RED));
        assertEquals(20000L, customLight.getDurationForState(TrafficLightState.GREEN));
    }

    @Test
    public void shouldRejectNullDirection() {
        assertThrows(NullPointerException.class, () -> new TrafficLight(null));
    }

    @Test
    public void shouldRejectNullState() {
        assertThrows(NullPointerException.class, () -> light.setState(null));
    }

    @Test
    public void shouldClearHistory() {
        light.setState(TrafficLightState.GREEN);
        light.setState(TrafficLightState.YELLOW);
        assertEquals(3, light.getHistory().size());

        light.clearHistory();
        assertEquals(1, light.getHistory().size());
    }

    @Test
    public void shouldReturnImmutableHistory() {
        var history = light.getHistory();
        assertThrows(UnsupportedOperationException.class, () -> history.add(null));
    }

    @Test
    public void shouldTrackTimestampsInHistory() {
        light.setState(TrafficLightState.GREEN);
        light.setState(TrafficLightState.YELLOW);

        var history = light.getHistory();
        for (var snapshot : history) {
            assertNotNull(snapshot.timestamp());
        }
    }

    @Test
    public void shouldInitializeWithCustomState() {
        Map<TrafficLightState, Long> durations = new HashMap<>();
        durations.put(TrafficLightState.RED, 30000L);
        durations.put(TrafficLightState.GREEN, 25000L);
        durations.put(TrafficLightState.YELLOW, 5000L);

        TrafficLight greenLight = new TrafficLight(
                Direction.SOUTH,
                TrafficLightState.GREEN,
                durations
        );

        assertEquals(TrafficLightState.GREEN, greenLight.getSnapshot().state());
        assertEquals(Direction.SOUTH, greenLight.getDirection());
    }

    @Test
    public void shouldReturnCorrectSnapshot() {
        light.setState(TrafficLightState.GREEN);
        var snapshot = light.getSnapshot();

        assertEquals(Direction.NORTH, snapshot.direction());
        assertEquals(TrafficLightState.GREEN, snapshot.state());
        assertTrue(snapshot.isGreen());
    }

    @Test
    public void shouldHandleMultipleStateChanges() {
        for (int i = 0; i < 10; i++) {
            light.transitionToNextState();
        }
        var history = light.getHistory();
        assertTrue(history.size() >= 10);
    }
}
