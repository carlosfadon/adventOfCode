package org.example.year2023.day01;

import org.example.common.AoC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Day01 extends AoC {

    private static final List<String> DIGITS = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

    Day01() {
        super(2023, 1);
    }

    void compute() throws URISyntaxException, IOException {
        var calibrationDocument = readLines();
        int resultPart1 = 0;
        int resultPart2 = 0;
        for (String line : calibrationDocument) {
            resultPart1 += getCalibrationValue(line, 1);
            resultPart2 += getCalibrationValue(line, 2);
        }
        printResult(1, resultPart1);
        printResult(2, resultPart2);
    }

    int getCalibrationValue(String line, int part) {
        int i = 0;
        int j = line.length() - 1;
        while ((isDigit(line, i, part) == null) || (isDigit(line, j, part) == null)) {
            if (isDigit(line, i, part) == null) {
                i++;
            }
            if (isDigit(line, j, part) == null) {
                j--;
            }
        }
        int digit1 = isDigit(line, i, part);
        int digit2 = isDigit(line, j, part);
        return Integer.valueOf("%d%d".formatted(digit1, digit2));
    }

    Integer isDigit(String line, int idx, int part) {
        char c = line.charAt(idx);
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (part == 2) {
            for (String digit : DIGITS) {
                if (line.substring(idx).startsWith(digit)) {
                    return DIGITS.indexOf(digit) + 1;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        Day01 day01 = new Day01();
        day01.compute();
    }
}
