package org.example.year2024.day16;

import java.util.List;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    List<Direction> getPossibleTurns() {
        switch (this) {
            case UP:
                return List.of(LEFT, RIGHT, UP);
            case DOWN:
                return List.of(LEFT, RIGHT, DOWN);
            case LEFT:
                return List.of(UP, DOWN, LEFT);
            case RIGHT:
                return List.of(UP, DOWN, RIGHT);
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

}
