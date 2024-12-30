package org.aoc.year2024.day25;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day25  {

    static final int HEIGHT = 7;

    public static void main(String[] args) throws URISyntaxException, IOException {
        Day25 day25 = new Day25();
        day25.runPart1();
    }


    void runPart1() throws URISyntaxException, IOException {
        List<List<Integer>> doors = new ArrayList<>();
        List<List<Integer>> keys = new ArrayList<>();

        var lines = readInput("day25/input.txt");

        initializeDoorsAndKeys(doors, keys, lines);

        var result = tryAllKeys(doors, keys);

        System.out.println("Result: " + result);
    }

    int tryAllKeys(List<List<Integer>> doors, List<List<Integer>> keys) {
        int result = 0;
        for (int i = 0; i < keys.size(); i++) {
            List<Integer> key = keys.get(i);
            for (int j = 0; j < doors.size(); j++) {
                List<Integer> door = doors.get(j);
                if (canOpen(key, door)) {
                    System.out.println("Key " + i + " can open door " + j);
                    result++;
                }
            }
        }
        return result;
    }

    boolean canOpen(List<Integer> key, List<Integer> door) {
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) + door.get(i) > 5) {
                return false;
            }
        }
        return true;
    }

    void initializeDoorsAndKeys(List<List<Integer>> doors, List<List<Integer>> keys, List<String> lines) {
        int currentLine = 0;
        while (currentLine < lines.size()) {
            List<Integer> seq;
            boolean isLock;
            int i = 0;
            String line = lines.get(currentLine + i);
            if (line.startsWith("#")) {
                seq = new ArrayList<>(List.of(0, 0, 0, 0, 0));
                isLock = true;
            } else {
                seq = new ArrayList<>(List.of(-1, -1, -1, -1, -1));
                isLock = false;
            }
            i++;
            while (i < HEIGHT) {
                line = lines.get(currentLine + i);
                if (isLock) {
                    for (int j = 0; j < 5; j++) {
                        if (line.charAt(j) == '#') {
                            seq.set(j, seq.get(j) + 1);
                        }
                    }
                } else {
                    for (int j = 0; j < 5; j++) {
                        if (line.charAt(j) == '#') {
                            if (seq.get(j) == -1) {
                                seq.set(j, HEIGHT - i - 1);
                            }
                        }
                    }
                }
                i++;
            }
            if (isLock) {
                doors.add(seq);
            } else {
                keys.add(seq);
            }
            currentLine = currentLine + HEIGHT + 1;
        }
    }

    List<String> readInput(String file) throws URISyntaxException, IOException {
        var resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
        return lines;
    }
}
