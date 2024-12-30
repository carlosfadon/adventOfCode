package org.example.year2024.day17;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import static java.lang.Math.*;


public class Day17 {
    boolean debug = false;
    long registerA;
    long registerB;
    long registerC;
    List<Instruction> instructions = new ArrayList<>();

    List<Long> output = new ArrayList<>();
    List<Long> expectedOutput = new ArrayList<>();

    static final List<Integer> expected = Arrays.asList(2,4,1,2,7,5,4,5,0,3,1,7,5,5,3,0);

    long minRegisterA = Long.MAX_VALUE;

    public static void main (String[] args) throws URISyntaxException, IOException {
        Day17 day17 = new Day17();
        day17.runPart2(0, expected.size()-1);
        //day17.runPart1("day17/input.txt");
    }

    void runPart2(long a, int idx) throws URISyntaxException, IOException {
        if (idx == -1) {
            if (a < minRegisterA) {
                minRegisterA = a;
                System.out.println("Valid register A: " + a);
            }
            return;
        }

        List<Long> possibleA = new ArrayList<>();
        List<Integer> listBs = Arrays.asList(2,3,0,1,6,7,4,5);
        for (long i = 0; i < 8; i++) {
            possibleA.add(a*8 + i);
        }

        for (int i = 0; i < 8; i++ ) {
            long b = listBs.get(i);
            long result = b ^ (long) Math.floor(possibleA.get(i) / pow(2, b));
            long rest = result % 8;
            long nextDigit = rest ^ 7;
            if (nextDigit == expected.get(idx)) {
                idx--;
                runPart2(possibleA.get(i), idx);
                idx++;
            }
        }
    }

    void runPart1(String file) throws URISyntaxException, IOException {
        readInput(file);
        runProgram();
        System.out.println("part1:");
        printOutput();
    }

    void runProgram() {
        int idx = 0;
        while (idx >= 0 && idx < instructions.size()) {
            Instruction instruction = instructions.get(idx);
            int jump = runInstruction(instruction);
            if (jump >= 0) {
                idx = jump;
            }
            else {
                idx++;
            }
        }
    }

    boolean runProgram2(List<Long> expectedOutput) {
        int idx = 0;
        while ((idx >= 0 && idx < instructions.size()) &&
                expectedOutput.subList(0, output.size()).equals(output)) {

            if (output.size() == expectedOutput.size()) {
                System.out.println("MATCH!!!!!!!!");
            }
            Instruction instruction = instructions.get(idx);
            int jump = runInstruction(instruction);
            if (jump >= 0) {
                idx = jump;
            }
            else {
                idx++;
            }

        }
        if (expectedOutput.subList(0, output.size()).equals(output)) {
            System.out.println("output size: " + output.size());
            return true;
        }
        return false;



    }

    void printOutput() {
        System.out.println(formatOutput(output));
    }

    String formatOutput(List<Long> output) {
        return output.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    int runInstruction(Instruction instruction) {
        switch (instruction.opcode) {
            case 0:
                double denominator = pow(2, getComboOperand(instruction.operand));
                double result = (registerA / denominator);
                registerA = (long) Math.floor(result);
                break;
            case 1:
                registerB = registerB ^ instruction.operand;
                break;
            case 2:
                long result2 = getComboOperand(instruction.operand) % 8;
                registerB = result2;
                break;
            case 3:
                if (registerA != 0) {
                    return instruction.operand;
                }
                break;
            case 4:
                registerB = registerB ^ registerC;
                break;
            case 5:
                long result5 = getComboOperand(instruction.operand) % 8;
                output.add(result5);
                break;
            case 6:
                double denominator6 = pow(2, getComboOperand(instruction.operand));
                double result6 = (registerA / denominator6);
                registerB = (long) Math.floor(result6);
                break;
            case 7:
                double denominator7 = pow(2, getComboOperand(instruction.operand));
                double result7 = (registerA / denominator7);
                registerC = (long) Math.floor(result7);
                break;
        }
        return -1;

    }

    long getComboOperand(int operand) {
        long[] registers = {0, 1, 2, 3, registerA, registerB, registerC};
        return (operand >= 0 && operand < registers.length) ? registers[operand] : -1;
    }

    void readInput(String file) throws URISyntaxException, IOException {
        var resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
        String line0 = lines.get(0);
        String[] parts0 = line0.split(" ");
        registerA = Long.parseLong(parts0[2]);
        String line1 = lines.get(1);
        String[] parts1 = line1.split(" ");
        registerB = Long.parseLong(parts1[2]);
        String line2 = lines.get(2);
        String[] parts2 = line2.split(" ");
        registerC = Long.parseLong(parts2[2]);
        String lineInstructions = lines.get(4);
        String[] partsLineInstructions = lineInstructions.split(" ");
        String[] partsInstructions = partsLineInstructions[1].split(",");

        expectedOutput = Arrays.stream(partsInstructions)
                .map(Long::parseLong)
                .toList();
        for (int i = 0; i < partsInstructions.length/2; i++) {
            int opcode = Integer.parseInt(partsInstructions[i*2]);
            int operand = Integer.parseInt(partsInstructions[i*2+1]);
            Instruction instruction = new Instruction(opcode, operand);
            instructions.add(instruction);

        }

    }

}
