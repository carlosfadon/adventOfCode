package org.aoc.year2023.day03;

import org.aoc.common.AoC;
import org.aoc.common.Position;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class Day03 extends AoC {
    public Day03() {
        super(2023, 3);
    }

    public void part1() throws URISyntaxException, IOException {
        char[][] map = readMapOfChars();
        int result = 0;

        for (int i = 0; i < map.length; i++) {
            int j = 0;
            while (j < map[0].length) {
                String number = checkNumber(map, i, j);
                if (!number.isEmpty()) {
                    var perimeter = getPerimeter(map, i, j, number);
                    for (Position position : perimeter) {
                        if (checkSymbol(map, position)) {
                            result += Integer.parseInt(number);
                            break;
                        }
                    }
                    j += number.length();
                } else {
                    j++;
                }
            }
        }
        printResult(1, result);
    }

    void part2() throws URISyntaxException, IOException {
        char[][] map = readMapOfChars();
        int result = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == '*') {
                    int gearRatio = checkGear(map, i, j);
                    if (gearRatio != -1) {
                        result += gearRatio;
                    }
                }
            }
        }
        printResult(2, result);
    }

    int checkGear(char[][] map, int i, int j) {
        Set<Integer> partNumbers = new HashSet<>();
        int gearRatio = 1;
        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                if (k == 0 && l == 0) {
                    continue;
                }
                int partNumber = getPartNumber(map, i + k, j + l);
                if (partNumber != -1) {
                    partNumbers.add(partNumber);
                }
            }
        }
        if (partNumbers.size() != 2) {
            return -1;
        }
        for (int partNumber : partNumbers) {
            gearRatio *= partNumber;
        }
        return gearRatio;
    }

    private int getPartNumber(char[][] map, int i, int j) {
        if (!isInside(map, i, j)) {
            return -1;
        }
        if (!isDigit(map[i][j])) {
            return -1;
        }

        char c = map[i][j];
        int k = j;
        int l = j;
        if (isDigit(c)) {
            while (k >= 0 && isDigit(map[i][k])) {
                k--;
            }
            while (l < map[0].length && isDigit(map[i][l])) {
                l++;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int a = k+1; a < l; a++) {
            sb.append(map[i][a]);
        }
        return Integer.parseInt(sb.toString());
    }

    private List<Position> getPerimeter(char[][] map, int i, int j, String number) {
        List<Position> perimeter = new ArrayList<>();

        for (int k = -1; k <= number.length(); k++) {
            if (isInside(map, i - 1, j + k)) {
                perimeter.add(new Position(j + k, i - 1));
            }
            if (isInside(map, i + 1, j + k)) {
                perimeter.add(new Position(j + k, i + 1));
            }
        }
        if (isInside(map, i, j - 1)) {
            perimeter.add(new Position(j - 1, i));
        }
        if (isInside(map, i, j + number.length())) {
            perimeter.add(new Position(j + number.length(), i));
        }
        return perimeter;
    }

    private boolean checkSymbol(char[][] map, Position pos) {
        char c = map[pos.y()][pos.x()];
        return c != '.' && (!isDigit(c));
    }

    private String checkNumber(char[][] map, int i, int j) {
        StringBuilder sb = new StringBuilder();
        while ((j < map[0].length) && isDigit(map[i][j])) {
            sb.append(map[i][j]);
            j++;
        }
        return sb.toString();
    }

    private boolean isInside(char[][] map, int i, int j) {
        return i >= 0 && i < map.length && j >= 0 && j < map[0].length;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day03().part1();
        new Day03().part2();
    }
}
