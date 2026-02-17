package com.maveric.TrafficSignal.core.model;

public enum Direction {
    NORTH,SOUTH,EAST,WEST;

    public boolean conflictsWith(Direction other) {
        if (this == other) {
            return false;
        }
        return (this == NORTH && other == SOUTH) ||
                (this == SOUTH && other == NORTH) ||
                (this == EAST && other == WEST) ||
                (this == WEST && other == EAST);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
