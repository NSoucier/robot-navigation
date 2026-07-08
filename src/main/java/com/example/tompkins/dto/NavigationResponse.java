package com.example.tompkins.dto;

import com.example.tompkins.model.Coordinate;
import com.example.tompkins.model.Direction;
import com.example.tompkins.model.Status;

import java.util.List;

public record NavigationResponse(
        Coordinate finalPosition,
        Direction finalDirection,
        List<Coordinate> path,
        Status status
) {
}
