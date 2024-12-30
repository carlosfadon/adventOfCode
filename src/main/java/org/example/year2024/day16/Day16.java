package org.example.year2024.day16;

import org.example.year2024.day15.Day15;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//  wrong 144520
// 10048
public class Day16 {

    private static final int MOVE_COST = 1;
    private static final int TURN_COST = 1000;
    private Map<String, Integer> memo = new HashMap<>();

    public static void main (String[] args) throws URISyntaxException, IOException {
        Day16 day16 = new Day16();
        //day16.runPart1("day16/min.txt");
      //  day16.runPart1("day16/mini.txt");
       // day16.runPart1("day16/mini2.txt");
     //   day16.runPart1("day16/input.txt");
        day16.runPart2("day16/input.txt");
    }

    private void runPart1(String filename) throws URISyntaxException, IOException {
        char[][] board = readInput(filename);

        Position pos = findStartPoint(board);

//        Position start = findStartPoint(board);
//        Position end = findEndPoint(board);
//        int score = findLowestScorePath(board, start, end);

        Reindeer reindeer = new Reindeer(board, pos);

        boolean[][] visited = new boolean[board.length][board[0].length];
        int score = findPathDFS(reindeer, board, visited);

        System.out.println("Score: " + score);
    }

    private void runPart2(String filename) throws URISyntaxException, IOException {
        char[][] board = readInput(filename);

        Position pos = findStartPoint(board);

        Position start = findStartPoint(board);
        Position end = findEndPoint(board);

        List<List<Node>> paths = findAllLowestScorePathsPart2(board, start, end);

        System.out.println("num paths: %d".formatted(paths.size()));

        Set<String> result = new HashSet<>();
        for (List<Node> path : paths) {
            for (Node node : path) {
                result.add(node.pos.x + "," + node.pos.y);
            }
        }
        System.out.println("Unique positions: " + result.size());
    }

    private int findPathDFS(Reindeer reindeer, char[][] board,  boolean[][] visited) {

        if (board[reindeer.pos.y][reindeer.pos.x] == 'E') {
            printBoard(board);

            return reindeer.score;
        }

//        String stateKey = reindeer.pos.x + "," + reindeer.pos.y + "," + reindeer.dir;
//        if (memo.containsKey(stateKey)) {
//            return memo.get(stateKey);
//        }

        visited[reindeer.pos.y][reindeer.pos.x] = true;
        board[reindeer.pos.y][reindeer.pos.x] = 'X';


        int minScore = Integer.MAX_VALUE;
        for (Direction dir : Direction.values()) {
            Reindeer newReindeer = new Reindeer(reindeer);
            if (newReindeer.tryMove(dir, board, visited)) {
                int score = findPathDFS(newReindeer, board, visited);
                if (score < minScore) {
                    minScore = score;
                }
            }
        }


        visited[reindeer.pos.y][reindeer.pos.x] = false;
        board[reindeer.pos.y][reindeer.pos.x] = '.';
        //memo.put(stateKey, minScore);
        return minScore;
    }

    Position findStartPoint(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'S') {
                    return new Position(j, i);
                }
            }
        }
        return null;
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

    void printBoard(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printPath(char[][] board, Node endNode) {
        Node current = endNode;
        while (current != null) {
            if (board[current.pos.y][current.pos.x] != 'S' && board[current.pos.y][current.pos.x] != 'E') {
                board[current.pos.y][current.pos.x] = getDirectionChar(current.dir);
            }
            current = current.prev;
        }
        printBoard(board);
    }

    private char getDirectionChar(Direction dir) {
        return switch (dir) {
            case UP -> '^';
            case DOWN -> 'v';
            case LEFT -> '<';
            case RIGHT -> '>';
        };
    }

    private int findLowestScorePath(char[][] board, Position start, Position end) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.score));
        pq.add(new Node(start, 0, Direction.RIGHT, null));
        Map<Position, Integer> visited = new HashMap<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.pos.equals(end)) {
                printPath(board, current);
                return current.score;
            }

            if (visited.containsKey(current.pos) && visited.get(current.pos) < current.score) {
                continue;
            }
            visited.put(current.pos, current.score);

            for (Direction dir : current.dir.getPossibleTurns()) {

                Position newPos = current.pos.move(dir);
                if (isValidMove(board, newPos)) {
                    int newScore = current.score + MOVE_COST;
                    if (current.dir != null && current.dir != dir) {
                        newScore += TURN_COST;
                    }
                    pq.add(new Node(newPos, newScore, dir, current));
                }
            }
        }
        return -1; // No path found
    }

    private List<List<Node>> findAllLowestScorePathsPart2(char[][] board, Position start, Position end) {

        int minScore = Integer.MAX_VALUE;
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.score));
        pq.offer(new Node(start, 0, Direction.RIGHT, null));
        Map<State, Integer> visited = new HashMap<>();
        List<List<Node>> result = new ArrayList<>();

        while (!pq.isEmpty()) {

            //printQueue(pq);
            Node current = pq.poll();

            //System.out.println("poll node: " + current.pos.x + "," + current.pos.y + " score: " + current.score);

            if (current.score > minScore) {
                continue;
            }

            if (current.pos.equals(end)) {
                List<Node> path = constructPath(current);
                System.out.println("Eureka!");
                printPath(board, current);
                int pathScore = path.get(path.size() - 1).score;
                if (pathScore < minScore) {
                    minScore = pathScore;
                    result.clear();
                }
                if (pathScore == minScore) {
                    result.add(path);
                }
                continue;
            }
            State state = new State(current.pos, current.dir);

            if (visited.containsKey(state) && visited.get(state) < current.score) {
                continue;
            }
            visited.put(state, current.score);

            for (Direction dir : current.dir.getPossibleTurns()) {
                Position newPos = current.pos.move(dir);
                if (isValidMove(board, newPos)) {
                    int newScore = current.score + MOVE_COST;
                    if (current.dir != dir) {
                        newScore += TURN_COST;
                    }
                    if (newScore <= minScore) {
                        // !!!!!!!! shoulr be offer!!!!!!!!!!
                        pq.add(new Node(newPos, newScore, dir, current));
                    }
                }
            }
        }
        return result;
    }

    private List<List<Node>> myFindAllLowestScorePathsPart2recursive(char[][] board, Position start, Position end, List<Node> currentPath, List<List<Node>> allPaths) {

        int minScore = Integer.MAX_VALUE;
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.score));
        pq.offer(new Node(start, 0, Direction.RIGHT, null));
        Map<Position, Integer> visited = new HashMap<>();
        List<List<Node>> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.score > minScore) {
                continue;
            }

            if (current.pos.equals(end)) {
                List<Node> path = constructPath(current);
                result.add(path);
            }

            if (visited.containsKey(current.pos) && visited.get(current.pos) <= current.score) {
                continue;
            }
            visited.put(current.pos, current.score);

            for (Direction dir : Direction.values()) {
                Position newPos = current.pos.move(dir);
                if (isValidMove(board, newPos)) {
                    int newScore = current.score + MOVE_COST;
                    if (current.dir != dir) {
                        newScore += TURN_COST;
                    }
                    if (newScore <= minScore) {
                        pq.add(new Node(newPos, newScore, dir, current));
                    }
                }
            }
        }
        return result;
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

    private boolean isValidMove(char[][] board, Position pos) {
        return pos.y >= 0 && pos.y < board.length && pos.x >= 0 && pos.x < board[0].length && board[pos.y][pos.x] != '#';
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

    private void printQueue(PriorityQueue<Node> pq) {
        System.out.println("queue:");
        for (Node node : pq) {
            System.out.println("Node pos: " + node.pos.x + "," + node.pos.y + " score: " + node.score);
        }
    }


}
