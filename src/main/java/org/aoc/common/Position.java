package org.aoc.common;

public record Position(int x, int y) {
    public Position move(Direction direction, int maxX, int maxY) {
        Position next =  new Position(x + direction.dx, y + direction.dy);
        if (next.isInside(maxX, maxY)) {
            return next;
        }
        return null;
    }

    public boolean canMove(Direction direction, int maxX, int maxY) {
        Position next =  new Position(x + direction.dx, y + direction.dy);
        return next.isInside(maxX, maxY);
    }

    public boolean isInside(int maxX, int maxY) {
        return x >= 0 && x < maxX && y >= 0 && y < maxY;
    }

    public int calculateDistance(Position other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}
