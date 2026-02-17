package com.maveric.TrafficSignal.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void shouldIdentifyGreenState() {
        assertTrue(TrafficLightState.GREEN.isGreen());
        assertFalse(TrafficLightState.RED.isGreen());
        assertFalse(TrafficLightState.YELLOW.isGreen());
    }

    @Test
    public void shouldIdentifyRedState() {
        assertTrue(TrafficLightState.RED.isRed());
        assertFalse(TrafficLightState.GREEN.isRed());
        assertFalse(TrafficLightState.YELLOW.isRed());
    }

    @Test
    public void shouldIdentifyYellowState() {
        assertTrue(TrafficLightState.YELLOW.isYellow());
        assertFalse(TrafficLightState.RED.isYellow());
        assertFalse(TrafficLightState.GREEN.isYellow());
    }

    @Test
    public void shouldCreateStateFromString() {
        assertEquals(TrafficLightState.RED, TrafficLightState.valueOf("RED"));
        assertEquals(TrafficLightState.GREEN, TrafficLightState.valueOf("GREEN"));
        assertEquals(TrafficLightState.YELLOW, TrafficLightState.valueOf("YELLOW"));
    }
}
