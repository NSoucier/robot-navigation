package com.example.tompkins.service;

import com.example.tompkins.dto.NavigationRequest;
import com.example.tompkins.dto.NavigationResponse;
import com.example.tompkins.model.Coordinate;
import com.example.tompkins.model.Direction;
import com.example.tompkins.model.RobotState;
import com.example.tompkins.model.Status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NavigationService {

    public NavigationResponse navigate(NavigationRequest request) {
        validateRequest(request);

        // Use a set so obstacle checks remain efficient even when the obstacle list is large.
        Set<Coordinate> obstacles = new HashSet<>(request.obstacles() == null ? Set.of() : request.obstacles());
        RobotState state = new RobotState(request.startPosition(), request.startDirection());
        Status status = Status.SUCCESS;
        List<Coordinate> path = new ArrayList<>();
        path.add(state.position()); // add start position to path history

        if (request.commands() == null || request.commands().isBlank()) {
            return new NavigationResponse(state.position(), state.direction(), path, status);
        }

        for (char command : request.commands().toUpperCase(Locale.ROOT).toCharArray()) {
            if (status != Status.SUCCESS) {
                break;
            }

            switch (command) {
                case 'L' -> state = new RobotState(state.position(), state.direction().turnLeft());
                case 'R' -> state = new RobotState(state.position(), state.direction().turnRight());
                case 'F' -> {
                    // Compute the next cell before mutating state so we can stop early on boundary or obstacle hits.
                    Coordinate nextPosition = moveForward(state.position(), state.direction());
                    if (nextPosition.x() >= request.width() || nextPosition.y() >= request.height()) { // if robot hits max boundary, reset next position to be 0 in that direction x or y
                        if (nextPosition.x() >= request.width()) {
                            // check if obstacle is in the way before moving 
                            Coordinate wrappedPosition = new Coordinate(0, nextPosition.y());
                            if (obstacles.contains(wrappedPosition)) {
                                status = Status.STOPPED_BY_OBSTACLE;
                            } else {
                                state = new RobotState(wrappedPosition, state.direction());
                                path.add(state.position());
                            }
                        } else {
                            Coordinate wrappedPosition = new Coordinate(nextPosition.x(), 0);
                            if (obstacles.contains(wrappedPosition)) {
                                status = Status.STOPPED_BY_OBSTACLE;
                            } else {
                                state = new RobotState(wrappedPosition, state.direction());
                                path.add(state.position());
                            }                      
                        }
                    } else if (nextPosition.x() < 0 || nextPosition.y() < 0) { // if robot goes into negative, set next position to be grid height or width
                        if (nextPosition.x() < 0) {
                            Coordinate wrappedPosition = new Coordinate(request.width(), nextPosition.y());
                            if (obstacles.contains(wrappedPosition)) {
                                status = Status.STOPPED_BY_OBSTACLE;
                            } else {
                                state = new RobotState(wrappedPosition, state.direction());   
                                path.add(state.position());
                            }                        
                        } else {
                            Coordinate wrappedPosition = new Coordinate(nextPosition.x(), request.height());
                            if (obstacles.contains(wrappedPosition)) {
                                status = Status.STOPPED_BY_OBSTACLE;
                            } else {
                            state = new RobotState(wrappedPosition, state.direction());
                            path.add(state.position());
                            }
                        }
                    } else if (obstacles.contains(nextPosition)) {
                        status = Status.STOPPED_BY_OBSTACLE;
                    } else {
                        state = new RobotState(nextPosition, state.direction());
                        path.add(state.position());
                    }
                }
                default -> throw new IllegalArgumentException("Unsupported command: " + command); // or System.out.println("Ignoring unsupported command: " + command);
            }
        }

        return new NavigationResponse(state.position(), state.direction(), path, status);
    }

    private void validateRequest(NavigationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }
        if (request.width() <= 0 || request.height() <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        if (request.startPosition() == null) {
            throw new IllegalArgumentException("Start position must not be null");
        }
        if (request.startDirection() == null) {
            throw new IllegalArgumentException("Start direction must not be null");
        }
        if (request.startPosition().x() < 0 || request.startPosition().x() >= request.width()
                || request.startPosition().y() < 0 || request.startPosition().y() >= request.height()) {
            throw new IllegalArgumentException("Start position must be inside the grid");
        }
    }

    private Coordinate moveForward(Coordinate current, Direction direction) {
        return switch (direction) {
            case N -> new Coordinate(current.x(), current.y() + 1);
            case E -> new Coordinate(current.x() + 1, current.y());
            case S -> new Coordinate(current.x(), current.y() - 1);
            case W -> new Coordinate(current.x() - 1, current.y());
        };
    }
}
