package org.aoc.year2023;

import org.aoc.common.AoC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Day13 extends AoC {
    Day13() {
        super(2023, 13);
    }

    public void run() throws URISyntaxException, IOException {
        List<char[][]> maps = getMaps();
        part1(maps);
        part2();
    }

    private List<char[][]> getMaps() throws URISyntaxException, IOException {
        List<char[][]> maps = new ArrayList<>();
        var lines = readLines();
        int i = 0;
        while (i < lines.size()) {
            List<char[]> linesList = new ArrayList<>();
            while (i < lines.size() && !lines.get(i).isEmpty()) {
                char[] charArray = lines.get(i).toCharArray();
                linesList.add(charArray);
                i++;
            }
            var g = linesList.toArray(char[][]::new);
            maps.add(g);
            i++;
        }
        return maps;
    }

    public void part1(List<char[][]> maps) {
        int result = 0;
        for (char[][] map : maps) {
            int mirror = findMirror(map);
            result += mirror;
        }
        printResult(1, result);
    }

    public void part2() {
        printResult(2, 1);
    }

    int findMirror(char[][] map) {
        for (int i = 1; i < map[0].length; i++) {
            if (checkMirrorColumn(map, i)) {
                return i;
            }
        }
        for (int i = 1; i < map.length; i++) {
            if (checkMirrorRow(map, i)) {
                return 100*i;
            }
        }
        return -1;
    }

    boolean checkMirrorColumn(char[][] map, int x) {
        for (char[] chars : map) {
            for (int j = 0; x - j - 1 >= 0 && x + j < map[0].length; j++) {
                if (chars[x - j - 1] != chars[x + j]) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean checkMirrorRow(char[][] map, int x) {
        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; x - j - 1 >= 0 && x + j < map.length; j++) {
                if (map[x - j - 1][i] != map[x + j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day13().run();
    }
}
