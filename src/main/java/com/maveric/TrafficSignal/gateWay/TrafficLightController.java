package com.maveric.TrafficSignal.gateWay;

import com.maveric.TrafficSignal.core.model.Intersection;
import com.maveric.TrafficSignal.core.services.IntersectionManager;
import com.maveric.TrafficSignal.gateWay.response.IntersectionStateResponse;
import com.maveric.TrafficSignal.gateWay.response.TrafficLightResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
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

}
