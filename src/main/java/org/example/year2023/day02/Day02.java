package org.example.year2023.day02;

import org.example.common.AoC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Day02 extends AoC {

    static final int RED_MAX = 12;
    static final int BLUE_MAX = 14;
    static final int GREEN_MAX = 13;

    public Day02() {
        super(2023, 2);
    }

    private void part1() throws URISyntaxException, IOException {
        var lines = readLines();

        computeGames(lines);
    }

    private void computeGames(List<String> lines) throws URISyntaxException, IOException {
        int resultPart1 = 0;
        int resultPart2 = 0;
        for (String line : lines) {
            String parts[] = line.split(": ");
            String[] gameDetails = parts[1].split("; ");
            Integer number = Integer.parseInt(parts[0].split(" ")[1]);

            HighestGame highestGame = getHighestGame(gameDetails);
            if (isValidGame(highestGame)) {
                resultPart1 += number;
            }
            int power = calculatePower(highestGame);
            resultPart2 += power;
        }
        printResult(1, resultPart1);
        printResult(2, resultPart2);
    }

    private int calculatePower(HighestGame highestGame) {
        return highestGame.red * highestGame.blue * highestGame.green;
    }

    private HighestGame getHighestGame(String[] gameDetails) {
        int red = 0;
        int blue = 0;
        int green = 0;
        for (String gameDetail : gameDetails) {
            String[] gameParts = gameDetail.split(", ");
            for (String gamePart : gameParts) {
                String[] gamePartDetails = gamePart.split(" ");
                int count = Integer.parseInt(gamePartDetails[0]);
                Color color = Color.valueOf(gamePartDetails[1].toUpperCase());

                switch (color) {
                    case RED:
                        red = Math.max(red, count);
                        break;
                    case BLUE:
                        blue = Math.max(blue, count);
                        break;
                    case GREEN:
                        green = Math.max(green, count);
                        break;
                }
            }
        }
        return new HighestGame(red, blue, green);
    }

    private boolean isValidGame(HighestGame highestGame) {
        return highestGame.red <= RED_MAX &&
                highestGame.blue <= BLUE_MAX &&
                highestGame.green <= GREEN_MAX;
    }

    private boolean isValidGameDetail(String gameDetail) {
        String[] gameParts = gameDetail.split(", ");
        for (String gamePart : gameParts) {
            String[] gamePartDetails = gamePart.split(" ");
            int count = Integer.parseInt(gamePartDetails[0]);
            String color = gamePartDetails[1];

            if (!isValidCount(count, color)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidCount(int count, String color) {
        switch (color) {
            case "red":
                return count <= RED_MAX;
            case "blue":
                return count <= BLUE_MAX;
            case "green":
                return count <= GREEN_MAX;
            default:
                return false;
        }
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        Day02 day02 = new Day02();
        day02.part1();
    }

    record HighestGame(int red, int blue, int green) {
    }

    enum Color {
        RED, BLUE, GREEN
    }
}
