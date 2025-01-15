package org.aoc.year2023.day08;

import org.aoc.common.AoC;
import org.aoc.common.math.MathUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Day08 extends AoC {
    Day08() {
        super(2023, 8);
    }

    void part1() throws URISyntaxException, IOException {
        List<String> lines = readLines();

        String instructions = lines.get(0);
        Map<String, Link> nodes = parseNodes(lines);

        String current = "AAA";
        long steps = computeSteps(current, nodes, instructions, "ZZZ");

        printResult(1, steps);
    }

    void part2() throws URISyntaxException, IOException {
        Set<String> currentNodes = new HashSet<>();
        Set<String> endingNodes = new HashSet<>();
        Set<Long> stepsSet = new HashSet<>();

        List<String> lines = readLines();

        String instructions = lines.get(0);
        Map<String, Link> nodes = new HashMap<>();
        for (int i = 2; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("=");
            String node = parts[0].trim();
            String[] next = parts[1].trim().substring(1, parts[1].length() - 2).split(", ");
            nodes.put(node, new Link(next[0], next[1]));
            if (node.endsWith("A")) {
                currentNodes.add(node);
            }
            if (node.endsWith("Z")) {
                endingNodes.add(node);
            }
        }
        for (String currentNode : currentNodes) {
            long steps = computeSteps(currentNode, nodes, instructions, "^.{2}Z$");
            stepsSet.add(steps);
        }
        // calculate LCM of all steps set
        long lcm = MathUtil.lcm(stepsSet);

        printResult(2, lcm);
    }

    long computeSteps(String currentNode, Map<String, Link> nodes, String instructions, String regexEnd) {
        int instructionIdx = 0;
        long steps = 0;
        while (!currentNode.matches(regexEnd)) {
            char instruction = instructions.charAt(instructionIdx);
            Link link = nodes.get(currentNode);
            currentNode = (instruction == 'R') ? link.right : link.left;
            instructionIdx = ++instructionIdx % instructions.length();
            steps++;
        }
        return steps;
    }

    private static Map<String, Link> parseNodes(List<String> lines) {

        Map<String, Link> nodes = new HashMap<>();
        for (int i = 2; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("=");
            String node = parts[0].trim();
            String[] next = parts[1].trim().substring(1, parts[1].length() - 2).split(", ");
            nodes.put(node, new Link(next[0], next[1]));
        }
        return nodes;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        Day08 day08 = new Day08();
        day08.part1();
        day08.part2();
    }

    record Link(String left, String right) {
    }
}
