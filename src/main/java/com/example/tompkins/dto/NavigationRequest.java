package com.example.tompkins.dto;

import com.example.tompkins.model.Coordinate;
import com.example.tompkins.model.Direction;

import java.util.Set;

public record NavigationRequest(
        int width,
        int height,
        Coordinate startPosition,
        Direction startDirection,
        Set<Coordinate> obstacles,
        String commands
) {
}
