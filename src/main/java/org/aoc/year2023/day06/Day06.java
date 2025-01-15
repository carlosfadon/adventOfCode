package org.aoc.year2023.day06;

import org.aoc.common.AoC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.*;

public class Day06 extends AoC {
    Day06() {
        super(2023, 6);
    }

    long compute(long time, long distance) {

        if (time % 2 == 0) {  // 1, 3, 5, 7, 9, etc.... p.e:    0,1,4,9,16,25
            long gap = (time / 2) * (time / 2) - distance;
            long fl = (long) floor(sqrt(gap));
            if (gap == fl * fl) {
                return fl * 2 - 1;
            } else {
                return fl * 2 + 1;
            }
        } else {      // 2, 4, 6, 8, etc...   p.e:         0,2,6,12,20,30
            long gap = (time / 2 + 1) * (time / 2) - distance;
            long fl = (long) floor(sqrt(gap));
            if (gap < fl * fl + fl) {
                return fl * 2;
            } else {
                return fl * 2 + 2;
            }
        }
    }

    public void runPart1() throws URISyntaxException, IOException {
        int result = 1;
        var lines = readLines();
        List<Integer> times = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();
        Arrays.asList(lines.get(0).split(":")[1].trim().split("\\s+")).forEach(s -> times.add(Integer.parseInt(s)));
        Arrays.asList(lines.get(1).split(":")[1].trim().split("\\s+")).forEach(s -> distances.add(Integer.parseInt(s)));

        for (int i = 0; i < times.size(); i++) {
            int time = times.get(i);
            int distance = distances.get(i);
            long num = compute(time, distance);
            result *= num;
        }
        printResult(1, result);
    }

    public void runPart2() throws URISyntaxException, IOException {
        var lines = readLines();
        StringBuilder sbTime = new StringBuilder();
        StringBuilder sbDistance = new StringBuilder();
        Arrays.asList(lines.get(0).split(":")[1].trim().split("\\s+")).forEach(sbTime::append);
        Arrays.asList(lines.get(1).split(":")[1].trim().split("\\s+")).forEach(sbDistance::append);

        long time = Long.parseLong(sbTime.toString());
        long distance = Long.parseLong(sbDistance.toString());
        long result = compute(time, distance);
        printResult(2, result);
    }

    public void run() throws URISyntaxException, IOException {
        runPart1();
        runPart2();
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day06().run();
    }
}
