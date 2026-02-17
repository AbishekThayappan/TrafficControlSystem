package com.maveric.TrafficSignal.core.model;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class Intersection {
    private final String id;
    private final Map<Direction, TrafficLight> lights;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile boolean paused;

    public Intersection(String id) {
        this.id = Objects.requireNonNull(id);
        this.lights = new HashMap<>();
        this.paused = false;
        initializeLights();
    }

    private void initializeLights() {
        for (Direction direction : Direction.values()) {
            lights.put(direction, new TrafficLight(direction));
        }
    }

    public boolean advanceAllLights() {
        if (paused) {
            return false;
        }

        lock.writeLock().lock();
        try {
            // Create a copy of current states to validate before committing
            Map<Direction, TrafficLightState> proposedStates = new HashMap<>();
            for (Direction dir : Direction.values()) {
                TrafficLightState current = lights.get(dir).getSnapshot().state();
                proposedStates.put(dir, current.nextState());
            }

            if (!isValidState(proposedStates)) {
                return false;
            }

            for (Direction dir : Direction.values()) {
                lights.get(dir).transitionToNextState();
            }
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setLightState(Direction direction, TrafficLightState newState) {
        Objects.requireNonNull(direction);
        Objects.requireNonNull(newState);

        lock.writeLock().lock();
        try {
            // Only allow green transitions if they don't create conflicts
            if (newState.isGreen() && hasConflictingGreenLight(direction)) {
                throw new IllegalStateException(
                        String.format("Cannot set %s to GREEN: conflicting direction already green",
                                direction));
            }
            lights.get(direction).setState(newState);
        } finally {
            lock.writeLock().unlock();
        }
    }


    public void pause() {
        paused = true;
    }


    public void resume() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public Map<Direction, TrafficLightSnapshot> getAllLightSnapshots() {
        lock.readLock().lock();
        try {
            return lights.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().getSnapshot()));
        } finally {
            lock.readLock().unlock();
        }
    }


    public Set<Direction> getGreenLights() {
        lock.readLock().lock();
        try {
            return lights.entrySet().stream()
                    .filter(e -> e.getValue().getSnapshot().isGreen())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
        } finally {
            lock.readLock().unlock();
        }
    }


    public List<TrafficLightSnapshot> getLightHistory(Direction direction) {
        lock.readLock().lock();
        try {
            TrafficLight light = lights.get(direction);
            if (light == null) {
                return Collections.emptyList();
            }
            return light.getHistory();
        } finally {
            lock.readLock().unlock();
        }
    }

    public String getId() {
        return id;
    }

    public TrafficLight getLight(Direction direction) {
        Objects.requireNonNull(direction);
        lock.readLock().lock();
        try {
            return lights.get(direction);
        } finally {
            lock.readLock().unlock();
        }
    }


    private boolean hasConflictingGreenLight(Direction direction) {
        for (Direction other : Direction.values()) {
            if (direction.conflictsWith(other) &&
                lights.get(other).getSnapshot().isGreen()) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidState(Map<Direction, TrafficLightState> states) {
        for (Direction dir1 : Direction.values()) {
            if (!states.get(dir1).isGreen()) {
                continue;
            }
            for (Direction dir2 : Direction.values()) {
                if (dir1 != dir2 && dir1.conflictsWith(dir2) && states.get(dir2).isGreen()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "id='" + id + '\'' +
                ", paused=" + paused +
                '}';
    }
}
