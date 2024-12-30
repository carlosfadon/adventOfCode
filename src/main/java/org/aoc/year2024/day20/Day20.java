package org.aoc.year2024.day20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day20 {
    static int MAX_STEPS_CHEAT = 20;

    public static void main(String[] args) throws URISyntaxException, IOException {
        Day20 day20 = new Day20();
        day20.runPart1(100);
    }

    void runPart1(int minimumSave) throws URISyntaxException, IOException {
        char[][] board = readInput("day20/input.txt");
        Position start = findStartPoint(board);
        Position end = findEndPoint(board);
        Map<Position, Integer> mapPositionToSteps = new HashMap<>();
        long result = 0;

        List<List<Node>> shortestPaths = findShortestPaths(board, start, end);
        System.out.println("num paths: " + shortestPaths.size());
        for (List<Node> path : shortestPaths) {
            for (int i = 0; i < path.size(); i++) {
                // add position to the map if value is less than the current value
                if (mapPositionToSteps.containsKey(path.get(i).pos) && mapPositionToSteps.get(path.get(i).pos) < i) {
                    continue;
                }
                mapPositionToSteps.put(path.get(i).pos, i);
            }

            for (int i = 0; i < path.size(); i++) {
                List<Integer> listCheats = getCheats(board, path.get(i).pos, mapPositionToSteps);
                for (Integer cheat : listCheats) {
                    if (cheat >= minimumSave) {
                        result++;
                    }
                }
            }
            System.out.println("Day 20, part1: " + result);
            result = 0;
            for (int i = 0; i < path.size(); i++) {
                int numCheats = getNumberOfSofisticatedCheats(board, path, i, minimumSave);
                result += numCheats;
            }
            System.out.println("Day 20, part2: " + result);

        }
    }

    private void printPath(List<Node> path) {
        System.out.println("Path:");
        for (Node node : path) {
            System.out.println(node.pos.x + "," + node.pos.y);
        }
    }

    private List<List<Node>> findShortestPaths(char[][] board, Position start, Position end) {

        int[][] distances = new int[board.length][board[0].length];
        for (int i = 0; i < distances.length; i++) {
            Arrays.fill(distances[i], Integer.MAX_VALUE);
        }
        List<List<Node>> result = new ArrayList<>();
        int minScore = Integer.MAX_VALUE;

        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(start, null));
        distances[start.y][start.x] = 0;
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.pos.equals(end)) {
                List<Node> path = constructPath(current);
                System.out.println("Eureka!");
                int pathSteps = path.size() - 1;
                if (pathSteps < minScore) {
                    minScore = pathSteps;
                    result.clear();
                }
                if (pathSteps == minScore) {
                    result.add(path);
                }
                continue;
            }
            for (Position neighbor : current.pos.getAdjacentPositions()) {
                if (neighbor.x < 0 || neighbor.x >= board[0].length || neighbor.y < 0 || neighbor.y >= board.length) {
                    continue;
                }
                if (board[neighbor.y][neighbor.x] == '#') {
                    continue;
                }
                if (distances[neighbor.y][neighbor.x] >= distances[current.pos.y][current.pos.x] + 1) {
                    distances[neighbor.y][neighbor.x] = distances[current.pos.y][current.pos.x] + 1;
                    queue.offer(new Node(neighbor, current));
                }
            }
        }

        return result;
    }

    private List<Integer> getCheats(char[][] board, Position pos, Map<Position, Integer> mapPositions) {
        List<Position> possibleCheats = getPossibleCheatsFromPos(board, pos);
        List<Integer> result = new ArrayList<>();
        for (Position cheat : possibleCheats) {
            if (mapPositions.containsKey(cheat)) {
                int save =  mapPositions.get(cheat) - mapPositions.get(pos) - 2;
                result.add(save);
            }
        }
        return result;
    }

    private int getNumberOfSofisticatedCheats(char[][] board, List<Node> path, int i, int minimumSave) {
        int result = 0;

        for (int j = i; j < path.size(); j++) {
            int cheatLength = calculateCheatLength(board, path, i, j);
            if (cheatLength <= MAX_STEPS_CHEAT) {
                int save = j - i - cheatLength;
                if (save >= minimumSave) {
                    result++;
                }
            }
        }
        return result;
    }

    private int calculateCheatLength(char[][] board, List<Node> path, int src, int dest) {
        Position ini = path.get(src).pos;
        Position end = path.get(dest).pos;

        // calculate minimum distance from src to dest
        return Math.abs(ini.x - end.x) + Math.abs(ini.y - end.y);
    }

    private List<Position> getPossibleCheatsFromPos(char[][] board, Position pos) {
        List<Position> cheats = new ArrayList<>();

        if (isInside(board, new Position(pos.x, pos.y-2))) {
            if ((board[pos.y - 1][pos.x] == '#') && (board[pos.y - 2][pos.x] != '#')) {
                cheats.add(new Position(pos.x, pos.y - 2));
            }
        }
        if (isInside(board, new Position(pos.x, pos.y+2))) {
            if ((board[pos.y + 1][pos.x] == '#') && (board[pos.y + 2][pos.x] != '#')) {
                cheats.add(new Position(pos.x, pos.y + 2));
            }
        }
        if (isInside(board, new Position(pos.x-2, pos.y))) {
            if ((board[pos.y][pos.x - 1] == '#') && (board[pos.y][pos.x - 2] != '#')) {
                cheats.add(new Position(pos.x - 2, pos.y));
            }
        }
        if (isInside(board, new Position(pos.x+2, pos.y))) {
            if ((board[pos.y][pos.x + 1] == '#') && (board[pos.y][pos.x + 2] != '#')) {
                cheats.add(new Position(pos.x + 2, pos.y));
            }
        }
        return cheats;
    }

    private Map<Position, Integer>  getPossibleCheatsFromPos2(char[][] board, Position pos) {
        Map<Position, Integer> cheats = new HashMap<>();

        Position wall = new Position(pos.x, pos.y-1);
        Map<Position, Integer> perimeter = new HashMap<>();
        Map<Position, Integer> visited = new HashMap<>();
        if ((isInside(board, wall)) && (board[wall.y][wall.x] == '#')) {
            visited.put(wall, 1);
            getPerimeterBFS(board, wall, perimeter, visited, 20);
            perimeter.forEach((key, value) -> cheats.merge(key, value, Math::min));
        }
        wall = new Position(pos.x, pos.y+1);
        perimeter = new HashMap<>();
        visited = new HashMap<>();
        if ((isInside(board, wall)) && (board[wall.y][wall.x] == '#')) {
            visited.put(wall, 1);
            getPerimeterBFS(board, wall, perimeter, visited, 20);
            perimeter.forEach((key, value) -> cheats.merge(key, value, Math::min));
        }
        wall = new Position(pos.x-1, pos.y);
        perimeter = new HashMap<>();
        visited = new HashMap<>();
        if ((isInside(board, wall)) && (board[wall.y][wall.x] == '#')) {
            visited.put(wall, 1);
            getPerimeterBFS(board, wall, perimeter, visited, 20);
            perimeter.forEach((key, value) -> cheats.merge(key, value, Math::min));
        }
        wall = new Position(pos.x+1, pos.y);
        perimeter = new HashMap<>();
        visited = new HashMap<>();
        if ((isInside(board, wall)) && (board[wall.y][wall.x] == '#')) {
            visited.put(wall, 1);
            getPerimeterBFS(board, wall, perimeter, visited, 20);
            perimeter.forEach((key, value) -> cheats.merge(key, value, Math::min));
        }
        return cheats;
    }

    void getPerimeterBFS(char[][] board, Position pos, Map<Position, Integer> perimeter, Map<Position, Integer> visited, int limit) {

        int result = 0;
        Queue<Position> queue = new LinkedList<>();
        queue.offer(pos);

        while (!queue.isEmpty()) {
            int size = queue.size();
            result++;
            if (result > limit) {
                return;
            }
            for (int i = 0; i < size; i++) {
                Position current = queue.poll();
                if (board[current.y][current.x] != '#') {
                    perimeter.put(current, result);
                    continue;
                }
                for (Position neighbor : current.getAdjacentPositions()) {
                    if (isInside(board, neighbor)) {
                        if (visited.containsKey(neighbor) && visited.get(neighbor) <= visited.get(current) + 1) {
                            continue;
                        }
                        visited.computeIfAbsent(neighbor, k -> visited.get(current) + 1);
                        if (visited.get(neighbor) >= visited.get(current) + 1) {
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }
    }

    boolean isInside(char[][] board, Position pos) {
        return pos.x >= 0 && pos.x < board[0].length && pos.y >= 0 && pos.y < board.length;
    }

    private List<Node> constructPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node current = endNode;
        while (current != null) {
            path.add(current);
            current = current.prev;
        }
        Collections.reverse(path);
        return path;
    }

    char[][] readInput(String file) throws URISyntaxException, IOException {
        var resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));

        char[][] board = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                board[i][j] = lines.get(i).charAt(j);
            }
        }
        return board;
    }

    private Position findStartPoint(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'S') {
                    return new Position(j, i);
                }
            }
        }
        return null;
    }

    private Position findEndPoint(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'E') {
                    return new Position(j, i);
                }
            }
        }
        return null;
    }
}
