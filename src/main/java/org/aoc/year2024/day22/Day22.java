package org.aoc.year2024.day22;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day22 {
    public static void main(String[] args) throws URISyntaxException, IOException {


        Day22 day22 = new Day22();
        day22.runPart1("day22/input.txt");
    }

    void runPart1(String file) throws URISyntaxException, IOException {
        List<Long> secrets = readInput(file);
        List<List<Long>> sequences = new ArrayList<>();
        List<List<Integer>> changes = new ArrayList<>();

        for (Long secret : secrets) {
            List<Long> newSecrets = new ArrayList<>();
            List<Integer> ch = new ArrayList<>();
            newSecrets.add(secret);
            for (int i = 0; i < 2000; i++) {
                Long newSecret = computeNextSecret(secret);
                ch.add((int) (newSecret % 10 - newSecrets.get(newSecrets.size() - 1) % 10));
                newSecrets.add(newSecret);
                secret = newSecret;
            }
            sequences.add(newSecrets);
            changes.add(ch);
            secrets = newSecrets;
        }
        long result = 0;
        for (List<Long> sequence : sequences) {
            result += sequence.get(sequence.size() - 1);
        }
        Map<List<Integer>, Long> mapPatternsBananas = new HashMap<>();

        long maxResult = 0;
        List<Integer> maxPattern = null;
        for (int i = 0; i <= changes.get(0).size()-4; i++) {
            System.out.println("i: " + i);
            for (int j = 0; j < changes.size(); j++) {
                List<Integer> pattern = changes.get(j).subList(i, i + 4);

                if (mapPatternsBananas.containsKey(pattern)) {
                    continue;
                }
                long resultWithPattern = 0;
                for (int k = 0; k < changes.size(); k++) {
                    int idxOf = Collections.indexOfSubList(changes.get(k), pattern);
                    if (idxOf != -1) {
                        resultWithPattern += sequences.get(k).get(idxOf + 3) % 10;
                    }
                }
                mapPatternsBananas.put(pattern, resultWithPattern);
                if (resultWithPattern > maxResult) {
                    maxResult = resultWithPattern;
                    maxPattern = pattern;
                }
            }
        }

        System.out.println("Max result: " + maxResult + " pattern: " + maxPattern);

    }

    List<Integer> getChanges(List<Long> secrets) {
        return IntStream.range(1, secrets.size())
                .mapToObj(i -> (int) (secrets.get(i) % 10 - secrets.get(i - 1) % 10))
                .collect(Collectors.toList());
    }

    Long computeNextSecret(Long secret) {
        long step1 = secret * 64;
        long step2 = secret ^ step1;
        long step3 = step2 %  16777216;

        long step4 = step3 / 32;
        long step5 = step3 ^ step4;
        long step6 = step5 % 16777216;

        long step7 = step6 * 2048;
        long step8 = step6 ^ step7;
        long step9 = step8 % 16777216;

        return step9;
    }

    List<Long> readInput(String file) throws URISyntaxException, IOException {
        var resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));

        return lines.stream().map(Long::parseLong).toList();
    }


}

