package org.aoc.year2023.day11;

import org.aoc.common.AoC;
import org.aoc.common.Position;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day11 extends AoC {

    Day11() {
        super(2023, 11);
    }

    private void  run() throws URISyntaxException, IOException {
        Set<Integer> rows = new HashSet<>();
        Set<Integer> columns = new HashSet<>();
        List<Position> initialPositions = new ArrayList<>();
        char[][] map = readMapOfChars();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '#') {
                    rows.add(i);
                    columns.add(j);
                    initialPositions.add(new Position(j, i));
                }
            }
        }

        List<Position> newPositions = getExpandedPositions(initialPositions, rows, columns, 1);
        long result = getSumOfDistances(newPositions);
        printResult(1, result);

        newPositions = getExpandedPositions(initialPositions, rows, columns, 999999);
        result = getSumOfDistances(newPositions);
        printResult(2, result);
    }

    private static long getSumOfDistances(List<Position> newPositions) {
        long result = 0;
        for (int i = 0; i < newPositions.size() - 1; i++) {
            for (int j = i+1; j < newPositions.size(); j++) {
                long distance = newPositions.get(i).calculateDistance(newPositions.get(j));
                result += distance;
            }
        }
        return result;
    }

    private static List<Position> getExpandedPositions(List<Position> initialPositions, Set<Integer> rows, Set<Integer> columns, int expansion) {
        List<Position> newPositions = new ArrayList<>();
        for (Position pos : initialPositions) {
            int dx = 0;
            int dy = 0;
            for (int i = 0; i < pos.y(); i++) {
                if (!rows.contains(i)) {
                    dy += expansion;
                }
            }
            for (int j = 0; j < pos.x(); j++) {
                if (!columns.contains(j)) {
                    dx += expansion;
                }
            }
            newPositions.add(new Position(pos.x() + dx, pos.y() + dy));
        }
        return newPositions;
    }

    /**
     * Expand the map by duplicating rows and columns without any occupied seats
     * This method is not needed for the final solution.
     * @param map
     * @param rows
     * @param columns
     * @return
     */
    char[][] expandMap(char[][] map, Set<Integer> rows, Set<Integer> columns) {
        int numRows = rows.size() + (map.length - rows.size()) * 2;
        int numColumns = columns.size() + (map[0].length - columns.size()) * 2;
        char[][] expandedMap = new char[numRows][numColumns];

        int newI = 0;
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, expandedMap[newI], 0, map[i].length);
            newI++;
            if (!rows.contains(i)) {
                System.arraycopy(map[i], 0, expandedMap[newI], 0, map[i].length);
                newI++;
            }
        }
        int newJ = 0;
        for (int j = 0; j < map[0].length; j++) {
            for (int i = 0; i < expandedMap.length; i++) {
                expandedMap[i][newJ] = map[i][j];
            }
            newJ++;
            if (!columns.contains(j)) {
                for (int i = 0; i < expandedMap.length; i++) {
                    expandedMap[i][newJ] = map[i][j];
                }
                newJ++;
            }
        }
        return expandedMap;
    }



    private int part2() {
        return 0;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        Day11 day11 = new Day11();
        day11.run();
    }
}
