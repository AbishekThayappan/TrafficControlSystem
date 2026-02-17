package com.maverick.TrafficSignal.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
