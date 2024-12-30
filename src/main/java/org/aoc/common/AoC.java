package org.aoc.common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AoC {
    int year;
    int day;

    public AoC(int year, int day) {
        this.year = year;
        this.day = day;
    }

    protected List<String> readLines() throws URISyntaxException, IOException {
        return readLines(getFileName());
    }

    protected List<String> readLines(String file) throws URISyntaxException, IOException {

        var resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        return Files.readAllLines(Paths.get(resource.toURI()));
    }

    protected char[][] readMapOfChars() throws URISyntaxException, IOException {
        return readMapOfChars(getFileName());
    }

    protected char[][] readMapOfChars(String file) throws URISyntaxException, IOException {

        List<String> lines = readLines(file);
        char[][] map = new char[lines.size()][lines.get(0).length()];
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                map[y][x] = line.charAt(x);
            }
        }
        return map;
    }

    private String getFileName() {
        return "year%d/day%02d/input.txt".formatted(year, day);
    }

    protected void printResult(int part, Object result) {
        System.out.println("Year " + year + ". Day " + day + ". Part " + part + ": " + result);
    }
}
