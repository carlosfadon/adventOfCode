package org.aoc.year2024.day21;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

// 1854640 to high
// 434832 to high
// 378013 to high
// 414302 to high
// 157108
// 156822
// 154494

//+---+---+---+

//        | 7 | 8 | 9 |
//        +---+---+---+
//        | 4 | 5 | 6 |
//        +---+---+---+
//        | 1 | 2 | 3 |
//        +---+---+---+
//            | 0 | A |
//            +---+---+
public class Day21 {

    Map<Character, Position> keypadMap = Map.ofEntries(
            new AbstractMap.SimpleEntry<>('7', new Position(0, 0)),
            new AbstractMap.SimpleEntry<>('8', new Position(1, 0)),
            new AbstractMap.SimpleEntry<>('9', new Position(2, 0)),
            new AbstractMap.SimpleEntry<>('4', new Position(0, 1)),
            new AbstractMap.SimpleEntry<>('5', new Position(1, 1)),
            new AbstractMap.SimpleEntry<>('6', new Position(2, 1)),
            new AbstractMap.SimpleEntry<>('1', new Position(0, 2)),
            new AbstractMap.SimpleEntry<>('2', new Position(1, 2)),
            new AbstractMap.SimpleEntry<>('3', new Position(2, 2)),
            new AbstractMap.SimpleEntry<>('0', new Position(1, 3)),
            new AbstractMap.SimpleEntry<>('A', new Position(2, 3))
    );

//                +---+---+
//                | ^ | A |
//            +---+---+---+
//            | < | v | > |
//            +---+---+---+

    Map<Character, Position> directKeypadMap = Map.ofEntries(
            new AbstractMap.SimpleEntry<>('^', new Position(1, 0)),
            new AbstractMap.SimpleEntry<>('v', new Position(1, 1)),
            new AbstractMap.SimpleEntry<>('<', new Position(0, 1)),
            new AbstractMap.SimpleEntry<>('>', new Position(2, 1)),
            new AbstractMap.SimpleEntry<>('A', new Position(2, 0))
    );

    private static final Map<Character, Integer> DIRECT_PRIORITIES = Map.ofEntries(
            new AbstractMap.SimpleEntry<>('^', 3),
            new AbstractMap.SimpleEntry<>('v', 2),
            new AbstractMap.SimpleEntry<>('<', 1),
            new AbstractMap.SimpleEntry<>('>', 3)
    );

    private static final List<Character[]> SAMPLE = List.of(
            new Character[]{'0', '2', '9', 'A'},
            new Character[]{'9', '8', '0', 'A'},
            new Character[]{'1', '7', '9', 'A'},
            new Character[]{'4', '5', '6', 'A'},
            new Character[]{'3', '7', '9', 'A'}
    );

    private static final List<Character[]> CODES = List.of(
            new Character[]{'1', '4', '0', 'A'},
            new Character[]{'1', '4', '3', 'A'},
            new Character[]{'3', '4', '9', 'A'},
            new Character[]{'5', '8', '2', 'A'},
            new Character[]{'9', '6', '4', 'A'}
    );

    private static final List<Character[]> MY_SAMPLE = List.of(
            new Character[]{'3', '7', '9', 'A'},
            new Character[]{'3', '7', '9', 'A'}
    );

    public static void main(String[] args) throws URISyntaxException, IOException {


        Day21 day21 = new Day21();
        day21.runPart2(CODES);
    }

    void runPart1(List<Character[]> codes) {
        Position currentPosition;
        List<List<Character>> totalSequence1 = new ArrayList<>();
        currentPosition = keypadMap.get('A');
        for (Character[] code : codes) {
            List<Character> totalCode = new ArrayList<>();
            for (Character c : code) {
                Position destination = keypadMap.get(c);
                List<Character> sequence = getSequence(currentPosition, destination);
                totalCode.addAll(sequence);
                totalCode.add('A');
                currentPosition = destination;
            }
            totalSequence1.add(totalCode);
        }

        List<List<Character>> totalSequence = new ArrayList<>();
        for (int i =0; i < 25; i++) {
            System.out.println("Iteration: %d".formatted(i));
            totalSequence = getNextLevel(totalSequence1);
            totalSequence1 = totalSequence;
        }

        System.out.println("final size: %d".formatted(totalSequence.size()));
        int result = 0;
        for (int i = 0; i < totalSequence.size(); i++) {
            System.out.println("Total size: %d".formatted(totalSequence.get(i).size()));
            int num = getNumericPart(codes.get(i));
            System.out.println("Num: %d".formatted(num));
            result += totalSequence.get(i).size() * getNumericPart(codes.get(i));
        }
        System.out.println("Result: %d".formatted(result));
    }

    List<List<Character>> getNextLevel(List<List<Character>> input) {
        List<List<Character>> totalSequence = new ArrayList<>();
        Position currentPosition;
        currentPosition = directKeypadMap.get('A');
        for (List<Character> sequence : input) {
            List<Character> totalCode = new ArrayList<>();
            for (Character c : sequence) {
                Position destination = directKeypadMap.get(c);
                List<Character> sequence2 = getSequenceDirect(currentPosition, destination);
                totalCode.addAll(sequence2);
                totalCode.add('A');
                currentPosition = destination;
            }
            totalSequence.add(totalCode);
        }
        return totalSequence;
    }

    int getNumericPart(Character[] sequence) {
        return (sequence[0] - '0') * 100 + (sequence[1] - '0') * 10 + (sequence[2] - '0');
    }

    boolean isInsideTrackPad(Position position) {
        return position.x >= 0 &&
                position.x <= 2 &&
                position.y >= 0 &&
                position.y <= 3 &&
                (position.x != 0 || position.y != 3);
    }

    boolean isInsideDirectTrackPad(Position position) {
        return position.x >= 0 &&
                position.x <= 2 &&
                position.y >= 0 &&
                position.y <= 1 &&
                (position.x != 0 || position.y != 0);
    }

    /**
     * Get the sequence of moves to go from currentPosition to destination
     * Priorities are '<', 'v' and '>', '<'
     * Important thing is to have together all the moves in the same direction without going into the hole
     * so the next keyboard can minimize its movements.
     *
     * @param currentPosition
     * @param destination
     * @return
     */
    List<Character> getSequence(Position currentPosition, Position destination) {
        List<Character> result = new ArrayList<>();

        int dx = destination.x - currentPosition.x;
        int dy = destination.y - currentPosition.y;

        Position newPos = currentPosition;

        if ((currentPosition.y == 3) && (currentPosition.x + dx == 0)) {
            // start vertically
            while (dy != 0) {
                if ((dy > 0) && (isInsideTrackPad(new Position(newPos.x, newPos.y + 1)))) {
                    newPos = new Position(newPos.x, newPos.y + 1);
                    result.add('v');
                    dy--;

                } else if ((dy < 0) && (isInsideTrackPad(new Position(newPos.x, newPos.y - 1)))) {
                    newPos = new Position(newPos.x, newPos.y - 1);
                    result.add('^');
                    dy++;
                }
            }
        }
        if ((currentPosition.x == 0) && (currentPosition.y + dy == 3)) {
            // start horizontally
            while (dx != 0) {
                if ((dx > 0) && (isInsideTrackPad(new Position(newPos.x + 1, newPos.y)))) {
                    newPos = new Position(newPos.x + 1, newPos.y);
                    result.add('>');
                    dx--;
                } else if ((dx < 0) && (isInsideTrackPad(new Position(newPos.x - 1, newPos.y)))) {
                    newPos = new Position(newPos.x - 1, newPos.y);
                    result.add('<');
                    dx++;
                }
            }
        }

        while (dx != 0 || dy != 0) {
            if ((dx < 0) && (isInsideTrackPad(new Position(newPos.x - 1, newPos.y)))) {
                newPos = new Position(newPos.x - 1, newPos.y);
                result.add('<');
                dx++;
            } else if ((dy > 0) && (isInsideTrackPad(new Position(newPos.x, newPos.y + 1)))) {
                newPos = new Position(newPos.x, newPos.y + 1);
                result.add('v');
                dy--;

            } else if ((dy < 0) && (isInsideTrackPad(new Position(newPos.x, newPos.y - 1)))) {
                newPos = new Position(newPos.x, newPos.y - 1);
                result.add('^');
                dy++;
            } else if ((dx > 0) && (isInsideTrackPad(new Position(newPos.x + 1, newPos.y)))) {
                newPos = new Position(newPos.x + 1, newPos.y);
                result.add('>');
                dx--;
            }
        }
        return result;

    }

    List<Character> getSequenceDirect(Position currentPosition, Position destination) {
        List<Character> result = new ArrayList<>();

        int dx = destination.x - currentPosition.x;
        int dy = destination.y - currentPosition.y;

        Position newPos = currentPosition;

        if ((currentPosition.y == 0) && (currentPosition.x + dx == 0)) {
            // start vertically
            while (dy != 0) {
                if ((dy > 0) && (isInsideDirectTrackPad(new Position(newPos.x, newPos.y + 1)))) {
                    newPos = new Position(newPos.x, newPos.y + 1);
                    result.add('v');
                    dy--;

                } else if ((dy < 0) && (isInsideDirectTrackPad(new Position(newPos.x, newPos.y - 1)))) {
                    newPos = new Position(newPos.x, newPos.y - 1);
                    result.add('^');
                    dy++;
                }
            }
        }
        if ((currentPosition.x == 0) && (currentPosition.y + dy == 0)) {
            // start horizontally
            while (dx != 0) {
                if ((dx > 0) && (isInsideDirectTrackPad(new Position(newPos.x + 1, newPos.y)))) {
                    newPos = new Position(newPos.x + 1, newPos.y);
                    result.add('>');
                    dx--;
                } else if ((dx < 0) && (isInsideDirectTrackPad(new Position(newPos.x - 1, newPos.y)))) {
                    newPos = new Position(newPos.x - 1, newPos.y);
                    result.add('<');
                    dx++;
                }
            }
        }
        /**
         * down and right should be prioritized to avoid going into the hole
         */
        while (dx != 0 || dy != 0) {
            if ((dx < 0) && (isInsideDirectTrackPad(new Position(newPos.x - 1, newPos.y)))) {
                newPos = new Position(newPos.x - 1, newPos.y);
                result.add('<');
                dx++;
            } else if ((dy > 0) && (isInsideDirectTrackPad(new Position(newPos.x, newPos.y + 1)))) {
                newPos = new Position(newPos.x, newPos.y + 1);
                result.add('v');
                dy--;
            } else if ((dx > 0) && (isInsideDirectTrackPad(new Position(newPos.x + 1, newPos.y)))) {
                newPos = new Position(newPos.x + 1, newPos.y);
                result.add('>');
                dx--;
            } else if ((dy < 0) && (isInsideDirectTrackPad(new Position(newPos.x, newPos.y - 1)))) {
                newPos = new Position(newPos.x, newPos.y - 1);
                result.add('^');
                dy++;
            }
        }
        return result;
    }

    public Map<String, List<Character>> generateNumericKeypadMap() {
        Map<String, List<Character>> trips = new HashMap<>();
        for (char origin : keypadMap.keySet()) {
            for (char destination : keypadMap.keySet()) {
                if (origin != destination) {
                    Position start = keypadMap.get(origin);
                    Position end = keypadMap.get(destination);
                    List<Character> sequence = getSequence(start, end);
                    trips.put(origin + "->" + destination, sequence);
                }
            }
        }
        return trips;
    }

    /**
     * cache is a list of maps, each map is a level of the cache
     * > -> A in level 5 will be stored in cache.get(5).get(new Trip('>', 'A'))
     * cache.get(0).get(new Trip('>', 'A')) will be the size of the sequence to go from > to A
     * @param codes
     */
    void runPart2(List<Character[]> codes) {
        List<Map<Trip, Long>> cache = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            cache.add(new HashMap<>());
        }


        Position currentPosition;
        List<List<Character>> totalSequence1 = new ArrayList<>();
        currentPosition = keypadMap.get('A');
        for (Character[] code : codes) {
            List<Character> totalCode = new ArrayList<>();
            for (Character c : code) {
                Position destination = keypadMap.get(c);
                List<Character> sequence = getSequence(currentPosition, destination);
                totalCode.addAll(sequence);
                totalCode.add('A');
                currentPosition = destination;
            }
            totalSequence1.add(totalCode);
        }

        // print totalSequence1
        for (List<Character> sequence : totalSequence1) {
            long totalSize = 0;
            Character orig = 'A';
            for (Character c : sequence) {
                long size = getSizeLevel(new Trip(orig, c), 25, cache);
                totalSize += size;
                orig = c;
            }
            System.out.println("totalSize: " + totalSize);
        }


    }

    long getSizeLevel(Trip t, int level, List<Map<Trip, Long>> cache) {

//        if (cache.get(level).get(t) != null) {
//            return cache.get(level).get(t);
//        }
//        if (level)
//
//        getSizeLevel(c, level-1);
        return 0;
    }

    public Map<Trip, List<Character>> generateDirectionalKeypadMap() {
        Map<Trip, List<Character>> trips = new HashMap<>();
        for (char origin : directKeypadMap.keySet()) {
            for (char destination : directKeypadMap.keySet()) {
                if (origin != destination) {
                    Position start = directKeypadMap.get(origin);
                    Position end = directKeypadMap.get(destination);
                    List<Character> sequence = getSequenceDirect(start, end);
                    trips.put(new Trip(origin, destination), sequence);
                }
            }
        }
        return trips;
    }
}

