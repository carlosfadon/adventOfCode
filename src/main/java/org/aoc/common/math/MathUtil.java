package org.aoc.common.math;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MathUtil {

    public static long lcm(Set<Long> numbers) {
        return numbers.stream().reduce(1L, (a, b) -> a * b / gcd(a, b));
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public List<Long> resolveEquation(int a1, int a2, long result1, int b1, int b2, long result2) {
        // Calculate the determinant of the coefficient matrix
        long determinant = (long) a1 * b2 - (long) a2 * b1;

        // Check if the determinant is zero (no unique solution)
        if (determinant == 0) {
            System.out.println("The system of equations has no unique solution.");
            return Collections.emptyList();
        }

        // Calculate determinants for x and y
        long determinantX = result1 * b2 - result2 * a2;
        long determinantY = a1 * result2 - b1 * result1;

        // Solve for x and y (integer division, since determinant is non-zero)
        long x = determinantX / determinant;
        long y = determinantY / determinant;

        // Verify that the solution is exact (no remainder)
        if (determinantX % determinant != 0 || determinantY % determinant != 0) {
            System.out.println("The system of equations does not have integer solutions.");
            return Collections.emptyList();
        }

        return List.of(x, y);
    }
}
