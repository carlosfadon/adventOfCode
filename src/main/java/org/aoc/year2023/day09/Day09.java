package org.aoc.year2023.day09;

import org.aoc.common.AoC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day09 extends AoC {

    public Day09() {
        super(2023, 9);
    }

    public void part1() throws URISyntaxException, IOException {
        int result = 0;
        var lines = readLines();
        for (String line : lines) {
            List<List<Integer>> pyramid = new ArrayList<>();
            List<Integer> currentLevel = new ArrayList<>(Arrays.stream(line.split("\\s+")).map(Integer::parseInt).toList());
            pyramid.add(currentLevel);
            while (!allZeros(currentLevel)) {
                currentLevel = getNextLevel(currentLevel);
                pyramid.add(currentLevel);
            }
            int nextValue = extrapolateNextValue(pyramid);
            result += nextValue;
        }
        printResult(1, result);
    }

    public void part2() throws URISyntaxException, IOException {
        int result = 0;
        var lines = readLines();
        for (String line : lines) {
            List<List<Integer>> pyramid = new ArrayList<>();
            List<Integer> currentLevel = new LinkedList<>(Arrays.stream(line.split("\\s+")).map(Integer::parseInt).toList());
            pyramid.add(currentLevel);
            while (!allZeros(currentLevel)) {
                currentLevel = getNextLevel(currentLevel);
                pyramid.add(currentLevel);
            }
            int nextValue = extrapolatePrevValue(pyramid);
            result += nextValue;
        }
        printResult(2, result);
    }

    private boolean allZeros(List<Integer> currentLevel) {
        return currentLevel.stream().allMatch(i -> i == 0);
    }

    private List<Integer> getNextLevel(List<Integer> currentLevel) {
        List<Integer> nextLevel = new ArrayList<>();
        for (int i = 0; i < currentLevel.size() - 1; i++) {
            nextLevel.add(currentLevel.get(i+1) - currentLevel.get(i));
        }
        return nextLevel;
    }

    private int extrapolateNextValue(List<List<Integer>> pyramid) {
        pyramid.get(pyramid.size() - 1).add(0);

        for (int i = pyramid.size() - 2; i >= 0; i--) {
            int last = pyramid.get(i).get(pyramid.get(i).size() - 1);
            int last2 = pyramid.get(i + 1).get(pyramid.get(i).size() - 1);
            pyramid.get(i).add(last + last2);
        }
        return pyramid.get(0).get(pyramid.get(0).size()-1);
    }

    private int extrapolatePrevValue(List<List<Integer>> pyramid) {
        pyramid.get(pyramid.size() - 1).add(0, 0);

        for (int i = pyramid.size() - 2; i >= 0; i--) {
            int first = pyramid.get(i).get(0);
            int first2 = pyramid.get(i + 1).get(0);
            pyramid.get(i).add(0, first - first2);
        }
        return pyramid.get(0).get(0);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        Day09 day = new Day09();
        day.part1();
        day.part2();
    }
}
