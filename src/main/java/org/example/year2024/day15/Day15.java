package org.example.year2024.day15;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day15 {
    private RobotState robotState;
    private String movements;

    public static void main (String[] args) throws URISyntaxException, IOException {
        Day15 day15 = new Day15();
        day15.runPart1("day15/boxes.txt");
        day15.runPart2("day15/boxes.txt");
    }

    private void runPart1(String filename) throws URISyntaxException, IOException {
        readInput(filename);

        for (char c : movements.toCharArray()) {
            robotState.tryMove(c);
        }

        long result = calculateGPScoords(robotState, 'O');
        System.out.println("Day 15, part1: %d".formatted(result));
    }

    private void runPart2(String filename) throws URISyntaxException, IOException {
        readInput(filename);

        char[][] resizedMap = resize(robotState.board);
        robotState.board = resizedMap;
        robotState.posX = robotState.posX * 2;

        int second = 0;
        try (FileWriter writer = new FileWriter("/Users/carlosfadonperlines/boxes.txt")) {

            printMap(writer, robotState.board);
            for (char c : movements.toCharArray()) {
                second++;
                writer.write("Second " + second + "Trying to move: " + c + "\n");
                if (robotState.tryMove2(c)) {
                    robotState.move2(c);
                }
                printMap(writer, robotState.board);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long result = calculateGPScoords(robotState, '[');
        System.out.println("Day 15, part2: %d".formatted(result));
    }

    private long calculateGPScoords(RobotState robotState, char box) {
        long result = 0;
        for (int i = 0; i < robotState.board.length; i++) {
            for (int j = 0; j < robotState.board[0].length; j++) {
                if (robotState.board[i][j] == box) {
                    result += gpsCoordinates(j, i);
                }
            }
        }
        return result;
    }


    private void  readInput(String file) throws URISyntaxException, IOException {
        var resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
        boolean firstPart = true;
        List<String> mapLines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            if (firstPart == true) {
                if (lines.get(i).equals("")) {
                    firstPart = false;
                } else {
                    mapLines.add(lines.get(i));
                }
            } else if (firstPart == false) {
                String line = lines.get(i);
                sb.append(line);
            }
        }
        int robotPosX = 0;
        int robotPosY = 0;
        char[][] map = new char[mapLines.size()][mapLines.get(0).length()];
        for (int i = 0; i < mapLines.size(); i++) {
            for (int j = 0; j < mapLines.get(i).length(); j++) {
                map[i][j] = mapLines.get(i).charAt(j);
                if (map[i][j] == '@') {
                    robotPosY = i;
                    robotPosX = j;
                }
            }
        }
        robotState = new RobotState(map, robotPosX, robotPosY);
        movements = sb.toString();
    }

    int gpsCoordinates(int x, int y) {
        return y * 100 + x;
    }

    char[][] resize(char[][] origBoard) {
        char[][] newBoard = new char[origBoard.length][origBoard[0].length*2];
        for (int i = 0; i < origBoard.length; i++) {
            for (int j = 0; j < origBoard[0].length; j++) {
                if (origBoard[i][j] == '@') {
                    newBoard[i][j*2] = '@';
                    newBoard[i][j*2+1] = '.';
                }
                if (origBoard[i][j] == 'O') {
                    newBoard[i][j*2] = '[';
                    newBoard[i][j*2+1] = ']';
                }
                if (origBoard[i][j] == '#') {
                    newBoard[i][j*2] = '#';
                    newBoard[i][j*2+1] = '#';
                }
                if (origBoard[i][j] == '.') {
                    newBoard[i][j*2] = '.';
                    newBoard[i][j*2+1] = '.';
                }
            }
        }
        return newBoard;
    }

    void printMap(FileWriter writer, char[][] board) {
        try {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    writer.write(board[i][j]);
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
