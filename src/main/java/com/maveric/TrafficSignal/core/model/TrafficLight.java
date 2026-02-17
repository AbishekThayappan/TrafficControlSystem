package com.maveric.TrafficSignal.core.model;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TrafficLight {
    private final Direction direction;
    private final Map<TrafficLightState, Long> stateDurations;
    private TrafficLightState currentState;
    private Instant lastStateChangeTime;
    private final List<TrafficLightSnapshot> history;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public TrafficLight(Direction direction) {
        this(direction, TrafficLightState.RED, getDefaultDurations());
    }

    public TrafficLight(Direction direction, TrafficLightState initialState,
                        Map<TrafficLightState, Long> stateDurations) {
        this.direction = Objects.requireNonNull(direction);
        this.currentState = Objects.requireNonNull(initialState);
        this.stateDurations = new HashMap<>(Objects.requireNonNull(stateDurations));
        this.lastStateChangeTime = Instant.now();
        this.history = new ArrayList<>();
        recordSnapshot();
    }

    private static Map<TrafficLightState, Long> getDefaultDurations() {
        Map<TrafficLightState, Long> durations = new HashMap<>();
        durations.put(TrafficLightState.RED, 30000L);
        durations.put(TrafficLightState.GREEN, 25000L);
        durations.put(TrafficLightState.YELLOW, 5000L);
        return durations;
    }

    private void recordSnapshot() {
        TrafficLightSnapshot snapshot = new TrafficLightSnapshot(
                direction,
                currentState,
                0,
                lastStateChangeTime);
        history.add(snapshot);
    }

    public Direction getDirection() {
        return direction;
    }
    public TrafficLightSnapshot transitionToNextState() {
        lock.writeLock().lock();
        try {
            currentState = currentState.nextState();
            lastStateChangeTime = Instant.now();
            recordSnapshot();
            return getSnapshot();
        } finally {
            lock.writeLock().unlock();
        }
    }
    public TrafficLightSnapshot getSnapshot() {
        lock.readLock().lock();
        try {
            long elapsed = System.currentTimeMillis() - lastStateChangeTime.toEpochMilli();
            return new TrafficLightSnapshot(
                    direction,
                    currentState,
                    elapsed,
                    lastStateChangeTime);
        } finally {
            lock.readLock().unlock();
        }
    }

    public TrafficLightSnapshot setState(TrafficLightState newState) {
        Objects.requireNonNull(newState);
        lock.writeLock().lock();
        try {
            currentState = newState;
            lastStateChangeTime = Instant.now();
            recordSnapshot();
            return getSnapshot();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<TrafficLightSnapshot> getHistory() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(new ArrayList<>(history));
        } finally {
            lock.readLock().unlock();
        }
    }

    public long getDurationForState(TrafficLightState state) {
        lock.readLock().lock();
        try {
            return stateDurations.getOrDefault(state, 0L);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clearHistory() {
        lock.writeLock().lock();
        try {
            history.clear();
            recordSnapshot();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
