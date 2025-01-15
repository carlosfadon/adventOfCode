package org.aoc.common;

public class PrettyPrint {

    public static void print(char[][] map) {
        for (char[] chars : map) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void clearConsole() {
        // Clear the console
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void moveToTheStartOfLine() {
        System.out.print("\r"); // \r moves the cursor to the start of the line
    }

}
