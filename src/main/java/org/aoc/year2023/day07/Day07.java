package org.aoc.year2023.day07;

import org.aoc.common.AoC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;


public class Day07 extends AoC {

        public Day07() {
            super(2023, 7);
        }

        public long computeResult(Map<Hand, Integer> map) {
            long result = 0;
            int multiplier = 1;
            for (Map.Entry<Hand, Integer> entry : map.entrySet()) {
                result += (long) entry.getValue() * multiplier;
                multiplier++;
            }
            return result;
        }

        public void run() throws URISyntaxException, IOException {
            List<String> lines = readLines();
            TreeMap<Hand, Integer> hands1 = new TreeMap<>();
            TreeMap<Hand, Integer> hands2 = new TreeMap<>();

            for (String line : lines) {
                String[] parts = line.split("\\s+");
                hands1.put(new Hand(parts[0], 1), Integer.valueOf(parts[1]));
                hands2.put(new Hand(parts[0], 2), Integer.valueOf(parts[1]));
            }
            printResult(1, computeResult(hands1));
            printResult(2, computeResult(hands2));
        }

        public static void main(String[] args) throws URISyntaxException, IOException {
            new Day07().run();
        }

        public enum HandType {
            FIVE_OF_A_KIND(6),
            FOUR_OF_A_KIND(5),
            FULL_HOUSE(4),
            THREE_OF_A_KIND(3),
            TWO_PAIRS(2),
            ONE_PAIR(1),
            HIGH_CARD(0);

            private final int value;

            HandType(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        public enum Card {
            ACE('A'),
            KING('K'),
            QUEEN('Q'),
            JACK('J'),
            TEN('T'),
            NINE('9'),
            EIGHT('8'),
            SEVEN('7'),
            SIX('6'),
            FIVE('5'),
            FOUR('4'),
            THREE('3'),
            TWO('2');

            private final char value;

            Card(char value) {
                this.value = value;
            }

            public char getValue() {
                return value;
            }

            public int getStrength(int part) {
                return switch (this) {
                    case ACE -> 14;
                    case KING -> 13;
                    case QUEEN -> 12;
                    case JACK -> (part == 1) ? 11 : 1;
                    case TEN -> 10;
                    case NINE -> 9;
                    case EIGHT -> 8;
                    case SEVEN -> 7;
                    case SIX -> 6;
                    case FIVE -> 5;
                    case FOUR -> 4;
                    case THREE -> 3;
                    case TWO -> 2;
                };
            }

            public static Card fromChar(char c) {
                for (Card card : Card.values()) {
                    if (card.getValue() == c) {
                        return card;
                    }
                }
                throw new IllegalArgumentException("Invalid card character: " + c);
            }
        }

        public static class Hand implements Comparable<Hand> {
            private final Card[] hand;

            public Hand(String cards, int part) {
                hand = new Card[5];
                for (int i = 0; i < 5; i++) {
                    hand[i] = Card.fromChar(cards.charAt(i));
                }
            }

            public HandType getTypeFromTreeMap(Map<Card, Integer> map) {
                int[] counts = new int[5];
                for (int count : map.values()) {
                    if (count > 0 && count <= 5) {
                        counts[count - 1]++;
                    }
                }

                if (counts[4] == 1) {
                    return HandType.FIVE_OF_A_KIND;
                }

                if (counts[3] == 1) {
                    return HandType.FOUR_OF_A_KIND;
                }

                if (counts[2] == 1) {
                    if (counts[1] == 1) {
                        return HandType.FULL_HOUSE;
                    }
                    return HandType.THREE_OF_A_KIND;
                }

                return switch (counts[1]) {
                    case 2 -> HandType.TWO_PAIRS;
                    case 1 -> HandType.ONE_PAIR;
                    default -> HandType.HIGH_CARD;
                };

            }

            public HandType getType() {
                Map<Card, Integer> map = Arrays.stream(hand)
                        .collect(Collectors.toMap(card -> card, card -> 1, Integer::sum));
                return getTypeFromTreeMap(map);
            }

            public Card[] getHand() {
                return hand;
            }

            public int compareCards(Hand o) {
                for (int i = 0; i < hand.length; i++) {
                    if (hand[i] != o.getHand()[i]) {
                        return Integer.compare(hand[i].getStrength(1), o.getHand()[i].getStrength(1));
                    }
                }
                return 0;
            }

            @Override
            public int compareTo(Hand o) {
                int typeComparison;
                typeComparison = Integer.compare(this.getType().getValue(), o.getType().getValue());
                if (typeComparison != 0) {
                    return typeComparison;
                }
                return compareCards(o);
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                for (Card card : hand) {
                    sb.append(card.getValue());
                }
                return sb.toString();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (obj == null || obj.getClass() != this.getClass()) {
                    return false;
                }
                Hand o = (Hand) obj;
                return Arrays.equals(this.hand, o.hand);
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(hand);
            }
        }

    public static class Hand2 implements Comparable<Hand> {
        private final Card[] hand;

        public Hand2(String cards, int part) {
            hand = new Card[5];
            for (int i = 0; i < 5; i++) {
                hand[i] = Card.fromChar(cards.charAt(i));
            }
        }

        public HandType getTypeFromTreeMap(Map<Card, Integer> map) {
            int[] counts = new int[5];
            for (int count : map.values()) {
                if (count > 0 && count <= 5) {
                    counts[count - 1]++;
                }
            }

            if (counts[4] == 1) {
                return HandType.FIVE_OF_A_KIND;
            }

            if (counts[3] == 1) {
                return HandType.FOUR_OF_A_KIND;
            }

            if (counts[2] == 1) {
                if (counts[1] == 1) {
                    return HandType.FULL_HOUSE;
                }
                return HandType.THREE_OF_A_KIND;
            }

            return switch (counts[1]) {
                case 2 -> HandType.TWO_PAIRS;
                case 1 -> HandType.ONE_PAIR;
                default -> HandType.HIGH_CARD;
            };

        }

        public HandType getType() {
            Map<Card, Integer> map = new HashMap<>();
            int numJ = 0;
            for (Card card : hand) {
                if (card == Card.JACK) {
                    numJ++;
                }
                else {
                    map.put(card, map.getOrDefault(card, 0) + 1);
                }
            }

            Map<Card, Integer> sortedMap = sortByValue(map);
            if (numJ > 0) {
                if (sortedMap.isEmpty()) {
                    return HandType.FIVE_OF_A_KIND;
                }
                Map.Entry<Card, Integer> entry = sortedMap.entrySet().iterator().next();
                entry.setValue(entry.getValue() + numJ);
            }
            return getTypeFromTreeMap(sortedMap);
        }

        public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
            List<Map.Entry<K, V>> entryList = new ArrayList<>(map.entrySet());
            entryList.sort(Map.Entry.<K, V>comparingByValue().reversed());

            Map<K, V> sortedMap = new LinkedHashMap<>();
            for (Map.Entry<K, V> entry : entryList) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            return sortedMap;
        }

        public Card[] getHand() {
            return hand;
        }

        public int compareCards(Hand o) {
            for (int i = 0; i < hand.length; i++) {
                if (hand[i] != o.getHand()[i]) {
                    return Integer.compare(hand[i].getStrength(2), o.getHand()[i].getStrength(2));
                }
            }
            return 0;
        }

        @Override
        public int compareTo(Hand o) {
            int typeComparison;
            typeComparison = Integer.compare(this.getType().getValue(), o.getType().getValue());
            if (typeComparison != 0) {
                return typeComparison;
            }
            return compareCards(o);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Card card : hand) {
                sb.append(card.getValue());
            }
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            Hand o = (Hand) obj;
            return Arrays.equals(this.hand, o.hand);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(hand);
        }
    }
}
