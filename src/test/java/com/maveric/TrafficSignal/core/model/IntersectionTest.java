package com.maveric.TrafficSignal.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntersectionTest {


    private Intersection intersection;

    @BeforeEach
    public void setUp() {
        intersection = new Intersection("Maveric");
    }

    @Test
    public void shouldInitializeAllLights() {
        var snapshots = intersection.getAllLightSnapshots();
        assertEquals(4, snapshots.size());
        assertTrue(snapshots.containsKey(Direction.NORTH));
        assertTrue(snapshots.containsKey(Direction.SOUTH));
        assertTrue(snapshots.containsKey(Direction.EAST));
        assertTrue(snapshots.containsKey(Direction.WEST));
    }

    @Test
    public void shouldStartWithAllLightsRed() {
        var snapshots = intersection.getAllLightSnapshots();
        for (var snapshot : snapshots.values()) {
            assertEquals(TrafficLightState.RED, snapshot.state());
        }
    }

    @Test
    public void shouldPreventConflictingGreenLights() {
        // Set NORTH to green
        intersection.setLightState(Direction.NORTH, TrafficLightState.GREEN);
        assertTrue(intersection.getGreenLights().contains(Direction.NORTH));

        // Should reject SOUTH -> GREEN because it conflicts with NORTH
        assertThrows(IllegalStateException.class,
                () -> intersection.setLightState(Direction.SOUTH, TrafficLightState.GREEN));
    }

    @Test
    public void shouldAllowPerpendicularGreenLights() {
        intersection.setLightState(Direction.NORTH, TrafficLightState.GREEN);

        // EAST and WEST don't conflict with NORTH, so should succeed
        intersection.setLightState(Direction.EAST, TrafficLightState.GREEN);

        var greenLights = intersection.getGreenLights();
        assertEquals(2, greenLights.size());
        assertTrue(greenLights.contains(Direction.NORTH));
        assertTrue(greenLights.contains(Direction.EAST));
    }

    @Test
    public void shouldPauseAllLights() {
        intersection.pause();
        assertTrue(intersection.isPaused());
        assertFalse(intersection.advanceAllLights());
    }

}
