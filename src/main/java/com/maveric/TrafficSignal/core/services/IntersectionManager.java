package com.maveric.TrafficSignal.core.services;

import com.maveric.TrafficSignal.core.model.Intersection;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class IntersectionManager {


    private final Map<String, Intersection> intersections;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public IntersectionManager() {
        this.intersections = new HashMap<>();
    }

    public Intersection getOrCreateIntersection(String intersectionId) {
        Objects.requireNonNull(intersectionId);

        lock.readLock().lock();
        try {
            if (intersections.containsKey(intersectionId)) {
                return intersections.get(intersectionId);
            }
        } finally {
            lock.readLock().unlock();
        }

        // Create new intersection if not found
        lock.writeLock().lock();
        try {
            return intersections.computeIfAbsent(intersectionId, Intersection::new);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<Intersection> getIntersection(String intersectionId) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(intersections.get(intersectionId));
        } finally {
            lock.readLock().unlock();
        }
    }

    public Collection<Intersection> getAllIntersections() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(intersections.values());
        } finally {
            lock.readLock().unlock();
        }
    }
    public int getIntersectionCount() {
        lock.readLock().lock();
        try {
            return intersections.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
