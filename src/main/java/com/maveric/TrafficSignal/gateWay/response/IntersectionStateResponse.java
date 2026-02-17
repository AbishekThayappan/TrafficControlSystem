package com.maveric.TrafficSignal.gateWay.response;

import java.util.Map;

public record IntersectionStateResponse(
        String intersectionId,
        boolean isPaused,
        Map<String, TrafficLightResponse> lights) {
}