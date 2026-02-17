package com.maverick.TrafficSignal.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrafficLightStateTest {

    @Test
    public void shouldTransitionFromRedToGreen() {
        TrafficLightState next = TrafficLightState.RED.nextState();
        assertEquals(TrafficLightState.GREEN, next);
    }

    @Test
    public void shouldTransitionFromGreenToYellow() {
        TrafficLightState next = TrafficLightState.GREEN.nextState();
        assertEquals(TrafficLightState.YELLOW, next);
    }

    @Test
    public void shouldTransitionFromYellowToRed() {
        TrafficLightState next = TrafficLightState.YELLOW.nextState();
        assertEquals(TrafficLightState.RED, next);
    }

    @Test
    public void shouldCompleteFullCycle() {
        TrafficLightState state = TrafficLightState.RED;
        state = state.nextState();
        assertEquals(TrafficLightState.GREEN, state);
        state = state.nextState();
        assertEquals(TrafficLightState.YELLOW, state);
        state = state.nextState();
        assertEquals(TrafficLightState.RED, state);
    }

    @Test
    public void shouldReturnCorrectDisplayName() {
        assertEquals("red", TrafficLightState.RED.getDisplayName());
        assertEquals("green", TrafficLightState.GREEN.getDisplayName());
        assertEquals("yellow", TrafficLightState.YELLOW.getDisplayName());
    }
}
