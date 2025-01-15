package org.aoc.year2023.day05;

import org.aoc.common.AoC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Day05 extends AoC {

    Day05() {
        super(2023, 5);
    }

    public void runPart1() throws URISyntaxException, IOException {
        var lines = readLines();

        List<String> mappings = null;
        List<Long> seeds = null;
        List<Long> soils = null;
        List<Long> fertilizers = null;
        List<Long> waters = null;
        List<Long> lights = null;
        List<Long> temperatures = null;
        List<Long> humidities = null;
        List<Long> locations = null;
        if (lines.get(0).startsWith("seeds: ")) {
            seeds = parseSeeds(lines.get(0).substring("seeds: ".length()));
        }
        int i = 2;
        while (i < lines.size()) {
            switch (lines.get(i)) {
                case "seed-to-soil map:" -> {
                    mappings = getMappings(lines, i);
                    soils = getDestinations(seeds, mappings);
                }
                case "soil-to-fertilizer map:" -> {
                    mappings = getMappings(lines, i);
                    fertilizers = getDestinations(soils, mappings);
                }
                case "fertilizer-to-water map:" -> {
                    mappings = getMappings(lines, i);
                    waters = getDestinations(fertilizers, mappings);
                }
                case "water-to-light map:" -> {
                    mappings = getMappings(lines, i);
                    lights = getDestinations(waters, mappings);
                }
                case "light-to-temperature map:" -> {
                    mappings = getMappings(lines, i);
                    temperatures = getDestinations(lights, mappings);
                }
                case "temperature-to-humidity map:" -> {
                    mappings = getMappings(lines, i);
                    humidities = getDestinations(temperatures, mappings);
                }
                case "humidity-to-location map:" -> {
                    mappings = getMappings(lines, i);
                    locations = getDestinations(humidities, mappings);
                }
            }
            i += mappings.size() + 2;
        }
        long result = locations.stream().mapToLong(Long::longValue).min().orElse(Integer.MAX_VALUE);
        printResult(1, result);
    }

    public void runPart2() throws URISyntaxException, IOException {
        var lines = readLines();

        List<String> mappings = null;

        Map<Long, Long> seeds = null;
        Map<Long, Long> soils = null;
        Map<Long, Long> fertilizers = null;
        Map<Long, Long> waters = null;
        Map<Long, Long> lights = null;
        Map<Long, Long> temperatures = null;
        Map<Long, Long> humidities = null;
        Map<Long, Long> locations = null;
        if (lines.get(0).startsWith("seeds: ")) {
            seeds = parseSeedsMap(lines.get(0).substring("seeds: ".length()));
        }
        int i = 2;
        while (i < lines.size()) {
            if (lines.get(i).equals("seed-to-soil map:")) {
                mappings = getMappings(lines, i);
                soils = getDestinationsMap(seeds, mappings);
            } else if (lines.get(i).equals("soil-to-fertilizer map:")) {
                mappings = getMappings(lines, i);
                fertilizers = getDestinationsMap(soils, mappings);
            } else if (lines.get(i).equals("fertilizer-to-water map:")) {
                mappings = getMappings(lines, i);
                waters = getDestinationsMap(fertilizers, mappings);
            } else if (lines.get(i).equals("water-to-light map:")) {
                mappings = getMappings(lines, i);
                lights = getDestinationsMap(waters, mappings);
            } else if (lines.get(i).equals("light-to-temperature map:")) {
                mappings = getMappings(lines, i);
                temperatures = getDestinationsMap(lights, mappings);
            } else if (lines.get(i).equals("temperature-to-humidity map:")) {
                mappings = getMappings(lines, i);
                humidities = getDestinationsMap(temperatures, mappings);
            } else if (lines.get(i).equals("humidity-to-location map:")) {
                mappings = getMappings(lines, i);
                locations = getDestinationsMap(humidities, mappings);
            }
            i += mappings.size() + 2;
        }
        long result = locations.keySet().stream().mapToLong(Long::longValue).min().orElse(Integer.MAX_VALUE);
        printResult(2, result);
    }

    List<Long> parseSeeds(String strSeeds) {
        return Arrays.asList(strSeeds.split("\\s+")).stream().map(Long::parseLong).toList();
    }

    Map<Long, Long> parseSeedsMap(String strSeeds) {
        var listSeeds = Arrays.asList(strSeeds.split("\\s+")).stream().map(Long::parseLong).toList();
        int i = 0;
        Map<Long, Long> seeds = new HashMap<>();
        while (i < listSeeds.size()) {
            seeds.put(listSeeds.get(i), listSeeds.get(i + 1));
            i += 2;
        }
        return seeds;
    }

    List<String> getMappings(List<String> lines, int i) {
        List<String> mappings = new ArrayList<>();
        i++;
        while ((i < lines.size()) && !lines.get(i).isEmpty()) {
            mappings.add(lines.get(i));
            i++;
        }
        return mappings;
    }

    List<Long> getDestinations(List<Long> sources, List<String> mappings) {
        List<Long> dest = new ArrayList<>();
        for (Long source : sources) {
            boolean found = false;
            for (String mapping : mappings) {
                String[] parts = mapping.split("\\s+");
                long range = source - Long.valueOf(parts[1]);
                if ((range >= 0) && (range < Integer.valueOf(parts[2]))) {
                    dest.add(Long.valueOf(parts[0]) + range);
                    found = true;
                }
            }
            if (found == false) {
                dest.add(Long.valueOf(source));
            }
        }
        return dest;
    }

    Map<Long, Long> getDestinationsMap(Map<Long, Long> sources, List<String> mappings) {
        Map<Long, Long> dest = new HashMap<>();
        sources.forEach((source, range) -> {
            for (String mapping : mappings) {
                String[] parts = mapping.split("\\s+");
                long rangeLow = source - Long.valueOf(parts[1]);
                long rangeHigh = source + range - Long.valueOf(parts[1]);
                if (rangeLow < 0) {
                    rangeLow = 0;
                }
                if (rangeHigh > Long.valueOf(parts[2])) {
                    rangeHigh = Long.valueOf(parts[2]);
                }
                if (rangeHigh >= rangeLow) {
                    dest.put(Long.valueOf(parts[0]) + rangeLow, rangeHigh - rangeLow + 1);
                }
            }
        });
        return dest;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day05().runPart1();
        new Day05().runPart2();
    }
}
