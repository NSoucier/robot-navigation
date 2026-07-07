package com.example.tompkins.service;

import com.example.tompkins.dto.NavigationRequest;
import com.example.tompkins.dto.NavigationResponse;
import com.example.tompkins.model.Coordinate;
import com.example.tompkins.model.Direction;
import com.example.tompkins.model.Status;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NavigationServiceTest {

    private final NavigationService service = new NavigationService();

    @Test
    void successfulNavigationReturnsSuccess() {
        NavigationRequest request = new NavigationRequest(
                5,
                5,
                new Coordinate(1, 1),
                Direction.N,
                Set.of(),
                "FFRFF"
        );

        NavigationResponse response = service.navigate(request);

        assertEquals(new Coordinate(3, 3), response.finalPosition());
        assertEquals(Direction.E, response.finalDirection());
        assertEquals(Status.SUCCESS, response.status());
    }

    @Test
    void obstacleCollisionStopsProcessing() {
        NavigationRequest request = new NavigationRequest(
                4,
                4,
                new Coordinate(1, 1),
                Direction.E,
                Set.of(new Coordinate(2, 1)),
                "F"
        );

        NavigationResponse response = service.navigate(request);

        assertEquals(new Coordinate(1, 1), response.finalPosition());
        assertEquals(Direction.E, response.finalDirection());
        assertEquals(Status.STOPPED_BY_OBSTACLE, response.status());
    }

    @Test
    void boundaryCollisionStopsProcessing() {
        NavigationRequest request = new NavigationRequest(
                4,
                4,
                new Coordinate(0, 0),
                Direction.W,
                Set.of(),
                "F"
        );

        NavigationResponse response = service.navigate(request);

        assertEquals(new Coordinate(0, 0), response.finalPosition());
        assertEquals(Direction.W, response.finalDirection());
        assertEquals(Status.STOPPED_BY_BOUNDARY, response.status());
    }

    @Test
    void turningLeftChangesDirection() {
        NavigationRequest request = new NavigationRequest(
                5,
                5,
                new Coordinate(2, 2),
                Direction.N,
                Set.of(),
                "L"
        );

        NavigationResponse response = service.navigate(request);

        assertEquals(Direction.W, response.finalDirection());
        assertEquals(Status.SUCCESS, response.status());
    }

    @Test
    void turningRightChangesDirection() {
        NavigationRequest request = new NavigationRequest(
                5,
                5,
                new Coordinate(2, 2),
                Direction.N,
                Set.of(),
                "R"
        );

        NavigationResponse response = service.navigate(request);

        assertEquals(Direction.E, response.finalDirection());
        assertEquals(Status.SUCCESS, response.status());
    }

    @Test
    void multipleCommandsAreProcessedInOrder() {
        NavigationRequest request = new NavigationRequest(
                6,
                6,
                new Coordinate(2, 2),
                Direction.N,
                Set.of(),
                "FFLFF"
        );

        NavigationResponse response = service.navigate(request);

        assertEquals(new Coordinate(0, 4), response.finalPosition());
        assertEquals(Direction.W, response.finalDirection());
        assertEquals(Status.SUCCESS, response.status());
    }

    @Test
    void invalidRequestReturnsBadRequest() {
        NavigationRequest request = new NavigationRequest(
                0,
                5,
                new Coordinate(0, 0),
                Direction.N,
                Set.of(),
                "F"
        );

        assertThrows(IllegalArgumentException.class, () -> service.navigate(request));
    }
}
