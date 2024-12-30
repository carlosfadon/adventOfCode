package org.example.year2024.day18;

import org.example.year2024.day17.Day17;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day18 {

    public static void main (String[] args) throws URISyntaxException, IOException {
        Day18 day18 = new Day18();
        day18.runPart1(71, 2872);
    }

    void runPart1(int size, int iterations) throws URISyntaxException, IOException {
        List<Position> positions = readInput("day18/input.txt", iterations);
        char board[][] = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = '.';
            }
        }
        for (Position position : positions) {
            board[position.x][position.y] = '#';
        }
        Position initialPos = new Position(0, 0);

        boolean[][] visited = new boolean[size][size];

        Queue<Position> que = new LinkedList();
        que.add(initialPos);
        int minPathSize = 0;
        while (!que.isEmpty()) {
            int numElements = que.size();
            minPathSize++;

            for (int i = 0; i < numElements; i++) {

                Position pos = que.poll();
                if (pos.equals(new Position(size - 1, size - 1))) {
                    System.out.println("Found path, iterations: %d".formatted(minPathSize-1));
                    return;
                }
                if (pos.x < 0 || pos.y < 0 || pos.x >= size || pos.y >= size) {
                    continue;
                }
                if (visited[pos.x][pos.y]) {
                    continue;
                }
                visited[pos.x][pos.y] = true;
                if (board[pos.x][pos.y] == '#') {
                    continue;
                }

                que.offer(new Position(pos.x, pos.y - 1));
                que.offer(new Position(pos.x, pos.y + 1));
                que.offer(new Position(pos.x - 1, pos.y));
                que.offer(new Position(pos.x + 1, pos.y));
            }

        }
        System.out.println("No path!");
    }

    List<Position> readInput(String file, int iterations) throws URISyntaxException, IOException {
        List<Position> positions = new ArrayList<>();

        var resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));

        for (int i = 0; i < iterations; i++) {
            String[] parts = lines.get(i).split(",");
            positions.add(new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            if (i == iterations-1) {
                System.out.println("Last position: %d, %d".formatted(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }
        }
        return positions;

    }

    void printBoard(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }



}
