package org.aoc.year2024.day19;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


//25095430888469

// 616957151871345
public class Day19 {

    List<String> towels = new ArrayList<>();
    List<String> designs = new ArrayList<>();
    Map<String, Boolean> cache = new HashMap<>();
    Map<String, Long> cacheCount = new HashMap<>();



    public static void main(String[] args) throws URISyntaxException, IOException {
        Day19 day19 = new Day19();
        day19.runPart2();
    }

    void runPart1() throws URISyntaxException, IOException {
        long result = 0;
        Map<Character, List<String>> mapTowels = new HashMap<>();

        readInput("day19/input.txt");

        for (String towel : towels) {
            mapTowels.computeIfAbsent(towel.charAt(0), k -> new ArrayList<>()).add(towel);
        }

        for (String design : designs) {
            if (checkIfPossibleRecursion(design, mapTowels)) {
                System.out.println("Possible design: " + design);
                result++;
            }
        }
        System.out.println("Day 19, part1: " + result);
    }

    void runPart2() throws URISyntaxException, IOException {
        long result = 0;
        Map<Character, List<String>> mapTowels = new HashMap<>();

        readInput("day19/input.txt");

        for (String towel : towels) {
            mapTowels.computeIfAbsent(towel.charAt(0), k -> new ArrayList<>()).add(towel);
        }

        for (String design : designs) {
            System.out.println("Checking design: " + design);
            long count = countPossibleOptionsRecursive(design, mapTowels);
            System.out.println(count);
            result += count;
        }
        System.out.println("Day 19, part2: " + result);
    }

    long countPossiblesOptions(String design, Map<Character, List<String>> mapTowels) {
        Queue<String> queue = new LinkedList<>();
        Map<String, Long> visited = new HashMap<>();
        queue.add("");
        visited.put("", 1L);
        long count = 0;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(design)) {
                count += visited.get(current);
                continue;  // If we don't want to count just return true
            }
            if (current.length() > design.length()) {
                continue;
            }
            String remaining = design.substring(current.length());
            if (mapTowels.containsKey(remaining.charAt(0))) {
                for (String towel : mapTowels.get(remaining.charAt(0))) {
                    String next = current + towel;
                    if (remaining.startsWith(towel)) {
                        if (!visited.containsKey(next)) {
                            queue.add(next);
                        }
                        visited.put(next, visited.getOrDefault(next, 0L) + visited.get(current));
                    }
                }
            }
        }
        return count;
    }


    long countPossibleOptionsDP(String design, Map<Character, List<String>> mapTowels) {
        int n = design.length();
        long[] dp = new long[n + 1];
        dp[0] = 1L; // One way to construct an empty string

        for (int i = 0; i < n; i++) {
            if (dp[i] == 0) continue; // No ways to reach this position

            char currentChar = design.charAt(i);
            if (!mapTowels.containsKey(currentChar)) continue;

            for (String towel : mapTowels.get(currentChar)) {
                int end = i + towel.length();
                if (end > n) continue; // Towel pattern exceeds design length

                String substring = design.substring(i, end);
                if (substring.equals(towel)) {
                    dp[end] += dp[i];
                    // Optional: Handle potential overflow
                    // if (dp[end] < 0) throw new ArithmeticException("Count overflow");
                }
            }
        }

        return dp[n];
    }

    long countPossibleOptionsRecursive(String design, Map<Character, List<String>> mapTowels) {

        // check if it works with the visited Set
        System.out.println(design);

        if (design.equals("")) {
            System.out.println("possible!!");
            return 1;
        }
        if (cacheCount.containsKey(design)) {
            return cacheCount.get(design);
        }
        long count = 0;

        if (mapTowels.containsKey(design.charAt(0))) {
            for (String towel : mapTowels.get(design.charAt(0))) {
                if (design.startsWith(towel)) {
                    count += countPossibleOptionsRecursive(design.substring(towel.length()), mapTowels);
                }
            }
        }
        cacheCount.put(design, count);
        return count;
    }

    boolean checkIfPossible(String design, Map<Character, List<String>> mapTowels) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer("");
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(design)) {
                return true;
            }
            if (current.length() > design.length()) {
                continue;
            }
            String remaining = design.substring(current.length());
            if (mapTowels.containsKey(remaining.charAt(0))) {
                for (String towel : mapTowels.get(remaining.charAt(0))) {
                    String next = current + towel;
                    if (remaining.startsWith(towel) && !visited.contains(next)) {
                        queue.offer(next);
                        visited.add(next);
                    }
                }
            }
        }
        return false;
    }

    boolean checkIfPossibleStack(String design, Map<Character, List<String>> mapTowels) {
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        stack.push("");
        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (current.equals(design)) {
                return true;
            }
            if (current.length() > design.length()) {
                continue;
            }
            String remaining = design.substring(current.length());
            if (mapTowels.containsKey(remaining.charAt(0))) {
                for (String towel : mapTowels.get(remaining.charAt(0))) {
                    String next = current + towel;
                    if (remaining.startsWith(towel) && !visited.contains(next)) {
                        stack.push(next);
                        visited.add(next);
                    }
                }
            }
        }
        return false;
    }

    boolean checkIfPossibleRecursion(String design, int idx, Map<Character, List<String>> mapTowels) {

        // check if it works with the visited Set
        if (idx == design.length()) {
            return true;
        }
        if (idx > design.length()) {
            return false;
        }
        if (cache.containsKey(design.substring(idx))) {
            return cache.get(design.substring(idx));
        }
        if (mapTowels.containsKey(design.charAt(idx))) {
            for (String towel : mapTowels.get(design.charAt(idx))) {
                if (design.substring(idx).startsWith(towel)) {
                    if (checkIfPossibleRecursion(design, idx + towel.length(), mapTowels)) {
                        cache.put(design.substring(idx), true);
                        return true;
                    }
                }
            }
        }
        cache.put(design, false);
        return false;
    }

    boolean checkIfPossibleRecursion(String design, Map<Character, List<String>> mapTowels) {

        // check if it works with the visited Set
        System.out.println(design);

        if (design.equals("")) {
            System.out.println("possible!!");
            return true;
        }
        if (cache.containsKey(design)) {
            return cache.get(design);
        }
        if (mapTowels.containsKey(design.charAt(0))) {
            for (String towel : mapTowels.get(design.charAt(0))) {
                if (design.startsWith(towel)) {
                    if (checkIfPossibleRecursion(design.substring(towel.length()), mapTowels)) {
                        cache.put(design, true);
                        return true;
                    }
                }
            }
        }
        cache.put(design, false);
        return false;
    }

    void readInput(String file) throws URISyntaxException, IOException {
        var resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));

        towels = Arrays.stream(lines.get(0).split(", ")).toList();
        designs = new ArrayList<>();
        for (int i = 2; i < lines.size(); i++) {
            designs.add(lines.get(i));

        }
    }
}
