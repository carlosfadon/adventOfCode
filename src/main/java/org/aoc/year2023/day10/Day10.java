package org.aoc.year2023.day10;

import org.aoc.common.AoC;
import org.aoc.common.Direction;
import org.aoc.common.Position;
import org.aoc.common.PrettyPrint;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.aoc.common.Direction.*;

public class Day10 extends AoC {

    private Tile[][] map;
    private Position start;

    public Day10() {
        super(2023, 10);
    }

    public void run() throws URISyntaxException, IOException {
        initialize();
        var path = part1();
        part2(path);
    }

    private void initialize() throws URISyntaxException, IOException {
        var lines = readLines("year2023/day10/input.txt");
        map = new Tile[lines.size()][lines.get(0).length()];
        start = null;
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length(); j++) {
                map[i][j] = Tile.fromChar(lines.get(i).charAt(j));
                if (map[i][j] == Tile.STARTING_TILE) {
                    start = new Position(j, i);
                }
            }
        }
    }

    public List<VisitedTileOut> part1() throws URISyntaxException, IOException {

        List<VisitedTileOut> path = new ArrayList<>();
        Set<Position> visited = new HashSet<>();

        List<Direction> dirs = getPossibleDirections(map, start);

        if (dirs.size() != 2) {
            System.out.println("Error");
            return Collections.emptyList();
        }

        int step = 0;

        path.add(new VisitedTileOut(start, dirs.get(0)));
        do {
            step++;
            VisitedTileOut lastTileVisited1 = path.get(path.size() - 1);
            Position nextPos = lastTileVisited1.position().move(lastTileVisited1.outDirection(), map[0].length, map.length);
            if (nextPos == null) {
                System.out.println("nextPos null!!");
            }
            if (visited.contains(nextPos)) {
                break;
            }
            visited.add(nextPos);
            Tile newTile = map[nextPos.y()][nextPos.x()];
            if (newTile != Tile.STARTING_TILE) {
                VisitedTileOut newVisitedTile1 = new VisitedTileOut(nextPos, getNextDirection(newTile, lastTileVisited1.outDirection()));
                path.add(newVisitedTile1);
            }
        } while (path.get(path.size() - 1).position != start);
        printResult(1, step/2);

        return path;
    }

    public void part2(List<VisitedTileOut> path) throws URISyntaxException, IOException {
        char[][] doubled = doubleMap(map, path);
        char[][] filledMap = floodFillOuter(doubled);
        PrettyPrint.print(filledMap);

        int count = getCount(filledMap);
        printResult(2, count);
    }

    private static int getCount(char[][] filledMap) {
        int count = 0;
        for (char[] chars : filledMap) {
            for (int j = 0; j < filledMap[0].length; j++) {
                if (chars[j] == '.') {
                    count++;
                }
            }
        }
        return count;
    }

    char[][] doubleMap(Tile[][] map, List<VisitedTileOut> path) {
        char[][] newMap = new char[map.length*2][map[0].length*2];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                newMap[i*2][j*2] = '.';
                newMap[i*2+1][j*2] = 'X';
                newMap[i*2][j*2+1] = 'X';
                newMap[i*2+1][j*2+1] = 'X';
            }
        }

        for (VisitedTileOut tile : path) {
            Position posInDoubledMap = new Position(tile.position().x() * 2, tile.position().y() * 2);
            newMap[posInDoubledMap.y()][posInDoubledMap.x()] = '*';
            Position nextPos = posInDoubledMap.move(tile.outDirection(), newMap[0].length, newMap.length);
            newMap[nextPos.y()][nextPos.x()] = '*';
        }

        PrettyPrint.print(newMap);
        return newMap;
    }

    private void fillEdges(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            fillEdge(map, i, 0, 1);
            fillEdge(map, i, map[0].length - 1, -1);
        }
        for (int j = 0; j < map[0].length; j++) {
            fillEdge(map, 0, j, 1);
            fillEdge(map, map.length - 1, j, -1);
        }
    }

    private void fillEdge(char[][] map, int i, int j, int step) {
        while (j >= 0 && j < map[0].length && map[i][j] != '*') {
            map[i][j] = 'F';
            j += step;
        }
    }

    char[][] floodFillOuter(char[][] map) {
        fillEdges(map);

        // Flood fill algorithm for remaining tiles
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 'F') {
                    // check neighbours
                    if (i > 0 && (map[i-1][j] == 'X' || map[i-1][j] == '.')) {
                        floodFill(map, new Position(j, i-1));
                    }
                    if (i < map.length - 1 && (map[i+1][j] == 'X' || map[i+1][j] == '.')) {
                        floodFill(map, new Position(j, i+1));
                    }
                    if (j > 0 && (map[i][j-1] == 'X' || map[i][j-1] == '.')) {
                        floodFill(map, new Position(j-1, i));
                    }
                    if (j < map[0].length - 1 && (map[i][j+1] == 'X' || map[i][j+1] == '.')) {
                        floodFill(map, new Position(j+1, i));
                    }
                    // in diagonal as well
                    if (i > 0 && j > 0 && (map[i-1][j-1] == 'X' || map[i-1][j-1] == '.')) {
                        floodFill(map, new Position(j-1, i-1));
                    }
                    if (i > 0 && j < map[0].length - 1 && (map[i-1][j+1] == 'X' || map[i-1][j+1] == '.')) {
                        floodFill(map, new Position(j+1, i-1));
                    }
                    if (i < map.length - 1 && j > 0 && (map[i+1][j-1] == 'X' || map[i+1][j-1] == '.')) {
                        floodFill(map, new Position(j-1, i+1));
                    }
                    if (i < map.length - 1 && j < map[0].length - 1 && (map[i+1][j+1] == 'X' || map[i+1][j+1] == '.')) {
                        floodFill(map, new Position(j + 1, i + 1));
                    }
                }
            }
        }
        return map;
    }

    void floodFill(char[][] map, Position start) {
        Queue<Position> queue = new LinkedList<>();
        queue.add(start);
        map[start.y()][start.x()] = 'F';
        while (!queue.isEmpty()) {
            Position current = queue.poll();
            for (Direction direction : Set.of(UP, DOWN, LEFT, RIGHT)) {
                if (current.canMove(direction, map.length, map[0].length)) {
                    Position next = current.move(direction, map.length, map[0].length);
                    if (map[next.y()][next.x()] == '.' || map[next.y()][next.x()] == 'X') {
                        map[next.y()][next.x()] = 'F';
                        queue.add(next);
                    }
                }
            }
        }
    }

    private List<Direction> getPossibleDirections(Tile[][] map, Position current) {
        List<Direction> directions = new ArrayList<>();
        for (Direction direction : Set.of(UP, DOWN, LEFT, RIGHT)) {
            if (checkIfPossibleDirection(map, current, direction)) {
                directions.add(direction);
            }
        }
        return directions;
    }

    /**
     * Checks if it is possible to move in a given direction.
     * @param map
     * @param current
     * @param direction
     * @return
     */
    boolean checkIfPossibleDirection(Tile[][] map, Position current, Direction direction) {
        if (current.canMove(direction, map[0].length, map.length)) {
            Position next = current.move(direction, map[0].length, map.length);
            return getPossibleTiles(direction).contains(map[next.y()][next.x()]);
        }
        return false;
    }

    /**
     * Returns the possible tiles that can be reached from a given direction.
     * @param dir
     * @return
     */
    private Set<Tile> getPossibleTiles(Direction dir) {
        switch (dir) {
            case DOWN: return Set.of(Tile.VERTICAL, Tile.NORTH_EAST, Tile.NORTH_WEST);
            case RIGHT: return Set.of(Tile.HORIZONTAL, Tile.NORTH_WEST, Tile.SOUTH_WEST);
            case UP: return Set.of(Tile.VERTICAL, Tile.SOUTH_EAST, Tile.SOUTH_WEST);
            case LEFT: return Set.of(Tile.HORIZONTAL, Tile.NORTH_EAST, Tile.SOUTH_EAST);
            default: throw new IllegalArgumentException("Invalid direction: " + dir);
        }
    }

    /**
     * Returns the next position given a current position and an entering direction
     * @param tile
     * @param direction
     * @return
     */
    Direction getNextDirection(Tile tile, Direction direction) {
        switch (tile) {
            case HORIZONTAL:
                return direction == LEFT ? LEFT : RIGHT;
            case VERTICAL:
                return direction == UP ? UP : DOWN;
            case NORTH_EAST:
                return direction == DOWN ? RIGHT : UP;
            case NORTH_WEST:
                return direction == DOWN ? LEFT : UP;
            case SOUTH_EAST:
                return direction == UP ? RIGHT : DOWN;
            case SOUTH_WEST:
                return direction == UP ? LEFT : DOWN;
            default: throw new IllegalArgumentException("Invalid tile: " + tile);
        }
    }

    /**
     * Enum to represent the different types of tiles in the map.
     */
    enum Tile {
        HORIZONTAL,
        VERTICAL,
        NORTH_EAST,
        NORTH_WEST,
        SOUTH_EAST,
        SOUTH_WEST,
        GROUND,
        STARTING_TILE,
        X;

        public static Tile fromChar(char c) {
            switch (c) {
                case '-': return HORIZONTAL;
                case '|': return VERTICAL;
                case 'L': return NORTH_EAST;
                case 'J': return NORTH_WEST;
                case 'F': return SOUTH_EAST;
                case '7': return SOUTH_WEST;
                case '.': return GROUND;
                case 'S': return STARTING_TILE;
                case 'X': return X;
                default: throw new IllegalArgumentException("Invalid tile character: " + c);
            }
        }
    }

    /**
     * Record to store the position of a visited tile and the direction to exit it.
     * @param position
     * @param outDirection
     */
    record VisitedTileOut (Position position, Direction outDirection) {
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day10().run();
    }
}
