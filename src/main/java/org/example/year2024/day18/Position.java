package org.example.year2024.day18;

import org.example.year2024.day16.Direction;

import java.util.List;
import java.util.Objects;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Position move(Direction dir) {
        return switch (dir) {
            case UP -> new Position(x, y - 1);
            case DOWN -> new Position(x, y + 1);
            case LEFT -> new Position(x - 1, y);
            case RIGHT -> new Position(x + 1, y);
        };
    }

    List<Position> getAdjacentPositions() {
        return List.of(
            new Position(x, y - 1),
            new Position(x, y + 1),
            new Position(x - 1, y),
            new Position(x + 1, y)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
