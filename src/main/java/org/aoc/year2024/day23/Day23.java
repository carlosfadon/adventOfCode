package org.aoc.year2024.day23;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day23 {


    // 3249 wrong
    public static void main(String[] args) throws URISyntaxException, IOException {
        System.out.println("Hello, World!");

        new Day23().runPart2();

    }

    void runPart1() throws URISyntaxException, IOException {
        var mapConnections = new Day23().readInput("day23/input.txt");
        //System.out.println(mapConnections);

        //Set<Set<String>> sets = findSetsOfThreeInterconectedComputers(mapConnections);


//        int result = 0;
//        for (Set<String> set : sets) {
//            System.out.println(String.join(",", set));
//            if (set.stream().anyMatch(s -> s.startsWith("t"))) {
//                result++;
//            }
//        }
        //System.out.println("Result: " + result);
    }

    void runPart2() throws URISyntaxException, IOException {
        List<String> lines = new Day23().readInput("day23/input.txt");
        Map<String, Set<String>> mapConnections = new HashMap<>();
        Set<String> setComputers = new HashSet<>();


        for (String line : lines) {
            String[] parts = line.split("-");
            mapConnections.computeIfAbsent(parts[0], k -> new HashSet<>()).add(parts[1]);
            mapConnections.computeIfAbsent(parts[1], k -> new HashSet<>()).add(parts[0]);
            setComputers.add(parts[0]);
            setComputers.add(parts[1]);
        }
// Not needed set
        Set<String> largestParty = findLargestParty(setComputers, mapConnections);
        List<String> listResult = largestParty.stream().sorted().toList();
        String result = String.join(",", listResult);
        System.out.println("Result: " + result);
    }

    List<String> readInput(String file) throws URISyntaxException, IOException {

        var resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));


        return lines;
    }

//    Set<Set<String>> findSetsOfThreeInterconectedComputers(Map<String, Set<String>> mapConnections) {
//        Set<Set<String>> sets = new HashSet<>();
//        for (String computer : mapConnections.keySet()) {
//            Set<String> connections = mapConnections.get(computer);
//            if (connections.size() < 2) {
//                continue;
//            }
//            for (int i = 0; i < connections.size(); i++) {
//                for (int j = i + 1; j < connections.size(); j++) {
//                    if (mapConnections.get(connections.get(i)).contains(connections.get(j))) {
//                        Set<String> set = new HashSet<>();
//                        set.add(computer);
//                        set.add(connections.get(i));
//                        set.add(connections.get(j));
//                        sets.add(set);
//                    }
//                }
//            }
//        }
//        return sets;
//    }

    Set<String> findLargestParty(Set<String> setComputers, Map<String, Set<String>> mapConnections) {
        Set<Set<String>> parties = new HashSet<>();
        for (String computer : setComputers) {

            Set<String> partySoFar = new HashSet<>();
            partySoFar.add(computer);

            Set<String> connections = mapConnections.get(computer);
            for (String connection : connections) {
                if (mapConnections.get(connection).containsAll(partySoFar)) {
                    partySoFar.add(connection);
                }
            }
            parties.add(partySoFar);
        }

        Set<String> largestParty = new HashSet<>();
        for (Set<String> party : parties) {
            if (party.size() > largestParty.size()) {
                largestParty = party;
            }
        }

        return largestParty;
    }
}
