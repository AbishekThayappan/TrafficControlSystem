package com.maverick.TrafficSignal.core.model;

public enum TrafficLightState {
    RED("red"),
    GREEN("green"),
    YELLOW("yellow");

    private final String displayName;

    TrafficLightState(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

    public TrafficLightState nextState() {
        return switch (this) {
            case RED -> GREEN;
            case GREEN -> YELLOW;
            case YELLOW -> RED;
        };
    }

    public boolean isGreen() {

        return this == GREEN;
    }

    public boolean isRed() {

        return this == RED;
    }

    public boolean isYellow() {

        return this == YELLOW;
    }

}
