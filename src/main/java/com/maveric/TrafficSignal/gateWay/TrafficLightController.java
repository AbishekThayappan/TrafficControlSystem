package com.maveric.TrafficSignal.gateWay;

import com.maveric.TrafficSignal.core.model.Direction;
import com.maveric.TrafficSignal.core.model.Intersection;
import com.maveric.TrafficSignal.core.model.TrafficLightState;
import com.maveric.TrafficSignal.core.services.IntersectionManager;
import com.maveric.TrafficSignal.gateWay.response.IntersectionStateResponse;
import com.maveric.TrafficSignal.gateWay.response.TrafficLightResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/maveric/intersections")
public class TrafficLightController {

    private final IntersectionManager intersectionManager;

    @Autowired
    public TrafficLightController(IntersectionManager intersectionManager) {
        this.intersectionManager = intersectionManager;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IntersectionStateResponse> getIntersectionState(
            @PathVariable String id) {
        Intersection intersection = intersectionManager.getOrCreateIntersection(id);

        var snapshots = intersection.getAllLightSnapshots();
        var lights = snapshots.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        e -> TrafficLightResponse.from(e.getValue())));

        IntersectionStateResponse response = new IntersectionStateResponse(id,
                intersection.isPaused(), lights);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/light/{direction}")
    public ResponseEntity<TrafficLightResponse> getLightState(
            @PathVariable String id,
            @PathVariable String direction) {
        try {
            Intersection intersection = intersectionManager.getOrCreateIntersection(id);
            Direction dir = Direction.valueOf(direction.toUpperCase());
            var snapshot = intersection.getLight(dir).getSnapshot();
            return ResponseEntity.ok(TrafficLightResponse.from(snapshot));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/light/{direction}/set")
    public ResponseEntity<?> setLightState(
            @PathVariable String id,
            @PathVariable String direction,
            @RequestBody Map<String, String> request) {
        try {
            Intersection intersection = intersectionManager.getOrCreateIntersection(id);
            Direction dir = Direction.valueOf(direction.toUpperCase());
            String state = request.get("state");

            if (state == null) {
                return ResponseEntity.badRequest().body("Missing 'state' in request body");
            }

            TrafficLightState newState = TrafficLightState.valueOf(state.toUpperCase());
            intersection.setLightState(dir, newState);

            var snapshot = intersection.getLight(dir).getSnapshot();
            return ResponseEntity.ok(TrafficLightResponse.from(snapshot));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid direction or state: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
