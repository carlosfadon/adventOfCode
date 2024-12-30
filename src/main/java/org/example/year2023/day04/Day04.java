package org.example.year2023.day04;

import org.example.common.AoC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

public class Day04 extends AoC {
    public Day04() {
        super(2023, 4);
    }

    private List<Card> parseCards(List<String> lines) {
        List<Card> cards = new ArrayList<>();
        for (String line : lines) {
            parseCard(line);
        }
        return cards;
    }

    private Card parseCard(String line) {
        String[] cardParts = line.split(":\\s+");
        String[] numbersParts = cardParts[1].split("\\|");
        String[] winningNumbersParts = numbersParts[0].trim().split("\\s+");
        String[] myNumbersParts = numbersParts[1].trim().split("\\s+");

        Set<Integer> winningNumbers = Arrays.stream(winningNumbersParts).map(Integer::parseInt).collect(Collectors.toSet());
        Set<Integer> myNumbers = Arrays.stream(myNumbersParts).map(Integer::parseInt).collect(Collectors.toSet());
        return new Card(winningNumbers, myNumbers);
    }

    public void part1(List<Card> cards) throws URISyntaxException, IOException {
        int result = 0;
        for (Card card : cards) {
            int worth = card.getWorth();
            result += worth;
        }
        printResult(1, result);
    }

    public void part2(List<Card> cards) {
        Map<Integer, Integer> copys = new HashMap<>();
        for (int i = 0; i < cards.size(); i++) {
            copys.computeIfAbsent(i, k -> 0);
            copys.put(i, copys.get(i) + 1);
            int count = cards.get(i).getCount();
            for (int j = i+1; j <= i+count; j++) {
                copys.computeIfAbsent(j, k -> 0);
                copys.put(j, copys.get(j) + copys.get(i));
            }
        }
        int result = copys.values().stream().mapToInt(Integer::intValue).sum();
        printResult(2, result);
    }

    public void run() throws URISyntaxException, IOException {
        var lines = readLines();
        var cards = lines.stream().map(this::parseCard).toList();

        part1(cards);
        part2(cards);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day04().run();
    }

    record Card(Set<Integer> winningNumbers, Set<Integer> myNumbers) {

        int getCount() {
            int count = 0;
            for (Integer myNumber : myNumbers) {
                if (winningNumbers.contains(myNumber)) {
                    count++;
                }
            }
            return count;
        }

        int getWorth() {
            int exp = 0;
            for (Integer myNumber : myNumbers) {
                if (winningNumbers.contains(myNumber)) {
                   exp++;
                }
            }
            if (exp > 0) {
                return (int) pow(2, (double) exp - 1);
            }
            return 0;
        }

    }

}
