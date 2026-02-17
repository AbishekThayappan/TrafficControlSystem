package com.maveric.TrafficSignal.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DirectionTest {

    @Test
    public void shouldDetectNorthSouthConflict() {
        assertTrue(Direction.NORTH.conflictsWith(Direction.SOUTH));
        assertTrue(Direction.SOUTH.conflictsWith(Direction.NORTH));
    }

    @Test
    public void shouldDetectEastWestConflict() {
        assertTrue(Direction.EAST.conflictsWith(Direction.WEST));
        assertTrue(Direction.WEST.conflictsWith(Direction.EAST));
    }

    @Test
    public void shouldNotConflictWithSameDirection() {
        assertFalse(Direction.NORTH.conflictsWith(Direction.NORTH));
        assertFalse(Direction.EAST.conflictsWith(Direction.EAST));
    }

    @Test
    public void shouldNotConflictWithPerpendicularDirection() {
        assertFalse(Direction.NORTH.conflictsWith(Direction.EAST));
        assertFalse(Direction.NORTH.conflictsWith(Direction.WEST));
        assertFalse(Direction.EAST.conflictsWith(Direction.SOUTH));
        assertFalse(Direction.SOUTH.conflictsWith(Direction.WEST));
    }

    @Test
    public void shouldHaveValidStringRepresentation() {
        assertEquals("north", Direction.NORTH.toString());
        assertEquals("south", Direction.SOUTH.toString());
        assertEquals("east", Direction.EAST.toString());
        assertEquals("west", Direction.WEST.toString());
    }

    @Test
    public void shouldCreateDirectionFromString() {
        assertEquals(Direction.NORTH, Direction.valueOf("NORTH"));
        assertEquals(Direction.SOUTH, Direction.valueOf("SOUTH"));
        assertEquals(Direction.EAST, Direction.valueOf("EAST"));
        assertEquals(Direction.WEST, Direction.valueOf("WEST"));
    }
}
