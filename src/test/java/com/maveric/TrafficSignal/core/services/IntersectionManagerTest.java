package com.maveric.TrafficSignal.core.services;

import com.maveric.TrafficSignal.core.model.Intersection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class IntersectionManagerTest {


    private IntersectionManager manager;

    @BeforeEach
    public void setUp() {
        manager = new IntersectionManager();
    }

    @Test
    public void shouldCreateIntersectionOnDemand() {
        Intersection intersection = manager.getOrCreateIntersection("Maveric");
        assertNotNull(intersection);
        assertEquals("Maveric", intersection.getId());
    }

    @Test
    public void shouldReturnSameIntersectionForSameId() {
        Intersection first = manager.getOrCreateIntersection("Maveric");
        Intersection second = manager.getOrCreateIntersection("Maveric");
        assertSame(first, second);
    }

    @Test
    public void shouldCreateMultipleIntersections() {
        Intersection first = manager.getOrCreateIntersection("Maveric");
        Intersection second = manager.getOrCreateIntersection("Broadway & 42nd");

        assertNotEquals(first.getId(), second.getId());
        assertEquals(2, manager.getIntersectionCount());
    }
    @Test
    public void shouldReturnExistingIntersectionViaOptional() {
        manager.getOrCreateIntersection("Maveric");
        var intersection = manager.getIntersection("Maveric");

        assertTrue(intersection.isPresent());
        assertEquals("Maveric", intersection.get().getId());
    }

    @Test
    public void shouldReturnEmptyOptionalForNonExistent() {
        var intersection = manager.getIntersection("NonExistent");
        assertTrue(intersection.isEmpty());
    }

    @Test
    public void shouldListAllIntersections() {
        manager.getOrCreateIntersection("Maveric");
        manager.getOrCreateIntersection("Broadway & 42nd");
        manager.getOrCreateIntersection("5th & Park");

        var all = manager.getAllIntersections();
        assertEquals(3, all.size());
    }



}
