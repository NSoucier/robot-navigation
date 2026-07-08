package com.example.tompkins.dto;

import com.example.tompkins.model.Coordinate;
import com.example.tompkins.model.Direction;
import com.example.tompkins.model.Status;

public record NavigationResponse(
        Coordinate finalPosition,
        Direction finalDirection,
        //path
        Status status
) {
}
