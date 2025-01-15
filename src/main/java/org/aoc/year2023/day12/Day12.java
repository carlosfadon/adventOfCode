package org.aoc.year2023.day12;

import org.aoc.common.AoC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

public class Day12 extends AoC {
    Day12() {
        super(2023, 12);
    }

    void run() throws URISyntaxException, IOException {
        part1();
        part2();
    }

    void part1() throws URISyntaxException, IOException {
        List<String> conditionRecords = new ArrayList<>();
        List<List<Integer>> listGroups = new ArrayList<>();
        var lines = readLines();
        for (String line : lines) {
            String[] parts = line.split(" ");
            conditionRecords.add(parts[0]);
            String[] groups = parts[1].split(",");
            listGroups.add(new ArrayList<>(Arrays.asList(groups).stream().map(Integer::parseInt).toList()));
        }

        int result = 0;
        for (int i = 0; i < conditionRecords.size(); i++) {
            var permutations = computeConditionRecord(conditionRecords.get(i), listGroups.get(i));
            result += permutations;
        }

        printResult(1, result);
    }

    void part2() throws URISyntaxException, IOException {
        List<String> conditionRecords = new ArrayList<>();
        List<List<Integer>> listGroups = new ArrayList<>();
        var lines = readLines();
        for (String line : lines) {
            String[] parts = line.split(" ");
            conditionRecords.add(getConditionGroupX5(parts[0]));
            String[] groups = parts[1].split(",");
            listGroups.add(getListOfGroupsX5(groups));
        }

        int result = 0;
        for (int i = 0; i < conditionRecords.size(); i++) {
            var permutations = computeConditionRecord2(conditionRecords.get(i), listGroups.get(i));
            System.out.println("Permutations: " + permutations);
            result += permutations;
        }
        printResult(2, result);
    }

    List<Integer> getListOfGroupsX5(String[] groups) {
        List<Integer> groupList = new ArrayList<>(Arrays.asList(groups).stream().map(Integer::parseInt).toList());
        List<Integer> groupListX5 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            groupListX5.addAll(groupList);
        }
        return groupListX5;
    }

    String getConditionGroupX5(String condition) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(condition);
        }
        return sb.toString();
    }

    int computeConditionRecord(String conditionRecord, List<Integer> groups) {
        int minLen = getMinimumLength(groups);
        if (conditionRecord.length() < minLen) {
            return 0;
        }
        if (groups.isEmpty() && !conditionRecord.isEmpty()) {
            return 0;
        }
        if (groups.size() == 1) {
            int result = 0;
            if (possibleGroup(conditionRecord, groups.get(0)) &&
                    noMoreGroups(conditionRecord.substring(groups.get(0)))) {
                result++;
            }
            if (conditionRecord.charAt(0) != '#') {
                result += computeConditionRecord(conditionRecord.substring(1), groups);
            }
            return result;
        }

        if (conditionRecord.charAt(0) == '?') {
            int pos1 = computeConditionRecord(conditionRecord.substring(1), new ArrayList(groups));
            int pos2 = 0;
            if (possibleGroup(conditionRecord, groups.get(0))) {
                Integer group0 = groups.remove(0);
                pos2 = computeConditionRecord(conditionRecord.substring(group0 + 1), groups);
            }
            return pos1 + pos2;
        } else if (conditionRecord.charAt(0) == '.') {
            return computeConditionRecord(conditionRecord.substring(1), groups);
        } else if (conditionRecord.charAt(0) == '#') {
            if (possibleGroup(conditionRecord, groups.get(0))) {
                Integer group0 = groups.remove(0);
                return computeConditionRecord(conditionRecord.substring(group0 + 1), groups);
            }
            return 0;
        }
        return 0;
    }

    int computeConditionRecord2(String conditionRecord, List<Integer> groups) {
        // number of ways to match the first i characters of the conditionRecord with the first j groups
        int dp[][] = new int[conditionRecord.length() + 1][groups.size() + 1];
        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(dp[i], -1);
        }
        dp[0][0] = 1;

        for (int i = 0; i < conditionRecord.length(); i++) {
            for (int j = 0; j < groups.size(); j++) {
                if (dp[i][j] != -1) {
                    continue;
                }
                if (conditionRecord.charAt(i) == '?') {
                    dp[i + 1][j + 1] += dp[i][j];
                    if (possibleGroup(conditionRecord.substring(i), groups.get(j))) {
                        dp[i + groups.get(j) + 1][j] += dp[i][j];
                    }
                } else if (conditionRecord.charAt(i) == '.') {
                    dp[i + 1][j] += dp[i][j];
                } else if (conditionRecord.charAt(i) == '#') {
                    if (possibleGroup(conditionRecord.substring(i), groups.get(j))) {
                        dp[i + groups.get(j) + 1][j] += dp[i][j];
                    }
                }
            }
        }
        return dp[conditionRecord.length()][groups.size()];
    }

    int getMinimumLength(List<Integer> groups) {
        return groups.stream().mapToInt(Integer::intValue).sum() + groups.size() - 1;
    }

    private boolean possibleGroup(String conditionRecord, int group) {
        if (conditionRecord.length() < group) {
            return false;
        }
        for (int i = 0; i < group; i++) {
            if (conditionRecord.charAt(i) == '.') {
                return false;
            }
        }
        if (conditionRecord.length() > group && conditionRecord.charAt(group) == '#') {
            return false;
        }
        return true;
    }

    private boolean noMoreGroups(String conditionRecord) {
        return conditionRecord.chars().noneMatch(c -> c == '#');
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day12().run();
    }

}
