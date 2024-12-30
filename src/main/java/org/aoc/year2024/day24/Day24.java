package org.aoc.year2024.day24;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day24 {

    public static void main(String[] args) throws URISyntaxException, IOException {
        new Day24().runPart1();


    }

    void runPart1() throws URISyntaxException, IOException {
        Map<String, Integer> wires = new HashMap<>();
        List<Operation> operations = new ArrayList<>();
        //Map<String, List<Operation>> operations = new HashMap<>();
        Set<String> zWires = new HashSet<>();
        List<String> lines = readInput("day24/input.txt");
        for (String line : lines) {
            String[] parts = line.split(": ");
            if (parts.length == 2) {
                wires.put(parts[0], Integer.parseInt(parts[1]));
            }

            parts = line.split(" ");
            if (parts.length == 5) {
                Operation operation = new Operation(parts[0], parts[2], Operator.valueOf(parts[1]), parts[4]);
                operations.add(operation);
                if (parts[4].startsWith("z")) {
                    zWires.add(parts[4]);
                }
            }

        }


        //computePart1(wires, zWires, operations);
        computePart2(wires, zWires, operations);


    }

    private void computePart2(Map<String, Integer> wires,
                              Set<String> zWires, List<Operation> operations) {

        var dependants = getDependants(wires, zWires, operations);

//        for (int i = 0; i < 46; i++) {
//            System.out.println();
//            String wire = String.format("z%02d", i);
//            System.out.println(wire + ": ");
//            var listDependants = dependants.get(wire);
//            for (String dependant : listDependants) {
//                System.out.print(", " + dependant);
//            }
//        }

        int iz06 = findOperationByOutput(operations, "z06");
//        int ihwk = findOperationByOutput(operations, "hwk");
//        List<Operation> copyOperations = swapOutputs(operations, iz06, ihwk);

        //  falso positivo
//        int z32 = findOperationByOutput(operations, "z32");
//        int mjr = findOperationByOutput(operations, "mjr");
//        copyOperations = swapOutputs(copyOperations, z32, mjr);
//

        // possibles:
        //spw qmd
        // qmd ptr
        //qmd nbs
        // qmd tnt
        // qmd z25
        //z26 z25
        // khj z25
//        int qmd = findOperationByOutput(operations, "qmd");
//        int nbs = findOperationByOutput(operations, "nbs");
//        copyOperations = swapOutputs(copyOperations, qmd, nbs);
//
//        int cgr = findOperationByOutput(operations, "cgr");
//        int z37 = findOperationByOutput(operations, "z37");
//        copyOperations = swapOutputs(copyOperations, cgr, z37);


        long wrong = 0;


        //       do {
//            System.out.println("Iteration for operation " + i);
//            if (iz06 == i) {
//                continue;
//            }
//            List<Operation> copyOperations = swapOutputs(operations, iz06, i);
        //wrong = 0;

        for (int i = 0; i < operations.size()-1; i ++) {

            //for (int j = i+1; j < operations.size(); j++) {
            System.out.println("check i: " + i);
            int j = iz06;
               if (j == i) {
                   continue;
               }
                System.out.println("swap " + i + " " + j + " " +
                                operations.get(i).output() + " " +
                        operations.get(j).output());
                List<Operation> operationsWithSwap = swapOutputs(operations, i, j);
//        for (long x = 1; x < Math.pow(2, 45); x = x * 2) {
//            for (long y = 1; y < Math.pow(2, 45); y = y * 2) {

                        for (long x = 0; x < 100; x++) {
                            for (long y = 0; y < 100; y++) {

//        long x = 68719476736L;
//        long y = 33554432L;

//        for (int i = 0; i < operations.size()-1; i ++) {
//            for (int j = i+1; j < operations.size(); j++) {
//                System.out.println("swap " + i + " " + j + " " +
//                        operations.get(i).output() + " " +
//                        operations.get(j).output());
//                List<Operation> operationsWithSwap = swapOutputs(operations, i, j);
                boolean correctSwap = computePart2Helper(wires, zWires, operationsWithSwap, x, y);

            }}
        }

    }


    boolean computePart2Helper(Map<String, Integer> wires, Set<String> zWires, List<Operation> operations, long x, long y) {
        Map<String, Integer> wiresCopy = new HashMap<>(wires);
        setLongX(wiresCopy, x);
        setLongY(wiresCopy, y);

        long expectedResult = x + y;
        var binaryExpected = toBitBinaryString(expectedResult, 46);
        computeLogics(wiresCopy, zWires, operations);
        String binary = binaryStringBuilder(wiresCopy, zWires);
        if (binary == null) {
     //       System.out.println("binary null, probably cicle");
            return false;
        }
        long decimalValue = Long.parseLong(binary, 2);
        if (expectedResult == decimalValue) {
           //System.out.println("correct swap");
           return true;
        } else {
            return false;
//            System.out.println(x + " + " + y + " = ");
//            System.out.println(binary);
//            System.out.println(binaryExpected);
        }
    }

    int findOperationByOutput(List<Operation> operations, String wire) {
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).output().equals(wire)) {
                return i;
            }
        }
        return -1;
    }

    public static String toBitBinaryString(long value, int length) {
        String binaryString = Long.toBinaryString(value);
        if (binaryString.length() > length) {
            throw new IllegalArgumentException("The binary representation of the value exceeds length " + length);
        }
        var format = "%" + length + "s";
        return String.format(format, binaryString).replace(' ', '0');
    }

    Map<String, List<String>> getDependants(Map<String, Integer> wires, Set<String> zWires, List<Operation> operations) {
        // get all dependants for each zXX wire
        Map<String, List<String>> mapDependants = new HashMap<>();
        for (String zWire : zWires) {
            var dependants = getDependantsHelper(zWire, operations);
            mapDependants.put(zWire, dependants);
        }
        return mapDependants;
    }

    List<String> getDependantsHelper(String wire, List<Operation> operations) {
        List<String> result = new ArrayList<>();

        if (wire.startsWith("x") || wire.startsWith("y")) {
            return Collections.emptyList();
        }
        for (Operation operation : operations) {
            if (operation.output().equals(wire)) {
                var dep1 = getDependantsHelper(operation.input1(), operations);
                var dep2 = getDependantsHelper(operation.input2(), operations);
                result.addAll(dep1);
                result.addAll(dep2);
                result.add(operation.input1());
                result.add(operation.input2());
            }
        }
        return result;
    }

    List<Operation> swapOutputs(List<Operation> operations, int i, int j) {

        Operation op1 = operations.get(i);
        Operation op2 = operations.get(j);

        Operation newOp1 = new Operation(op1.input1(), op1.input2(), op1.operator(), op2.output());
        Operation newOp2 = new Operation(op2.input1(), op2.input2(), op2.operator(), op1.output());

        List<Operation> result = new ArrayList<>(operations);
        result.set(i, newOp1);
        result.set(j, newOp2);
        return result;
    }

    private void computeLogics(Map<String, Integer> wires, Set<String> zWires, List<Operation> operations) {
        int i = 0;
        while ((!allZwires(wires, zWires) && (i < 100))) {
            for (Operation operation : operations) {
                var value1 = wires.get(operation.input1());
                var value2 = wires.get(operation.input2());
                if ((value1 != null) && (value2 != null)) {
                    switch (operation.operator()) {
                        case AND -> wires.put(operation.output(), value1 & value2);
                        case OR -> wires.put(operation.output(), value1 | value2);
                        case XOR -> wires.put(operation.output(), value1 ^ value2);
                    }
                }
            }
            i++;
        }
        if (i == 100) {
            System.out.println("Too many iterations");
        }
    }

    private void setLongX(Map<String, Integer> wires, long x) {
        // convert x to binary String
        String binaryString = Long.toBinaryString(x);
        String binary = String.format("%45s", binaryString).replace(' ', '0');


        for (int i = 44; i >= 0; i--) {
            char c = binary.charAt(i);
            // set x00, x01, x02, etc to 0, 1
            String wire = String.format("x%02d", 44 - i);
            wires.put(wire, c == '1' ? 1 : 0);
        }
    }

    private void setLongY(Map<String, Integer> wires, long x) {
        // convert x to binary String
        String binaryString = Long.toBinaryString(x);
        String binary = String.format("%45s", binaryString).replace(' ', '0');


        for (int i = binary.length() - 1; i >= 0; i--) {
            char c = binary.charAt(i);
            // set x00, x01, x02, etc to 0, 1
            String wire = String.format("y%02d", 44 - i);
            wires.put(wire, c == '1' ? 1 : 0);
        }
    }

    private void computePart1(Map<String, Integer> wires, Set<String> zWires, List<Operation> operations) {
        computeLogics(wires, zWires, operations);
        for (String zWire : zWires) {
            System.out.println(zWire + " " + wires.get(zWire));
        }
        String binary = binaryStringBuilder(wires, zWires);
        long decimalValue = Long.parseLong(binary, 2);
        System.out.println("binary reuslt: " + binary);
        System.out.println("Result: " + decimalValue);
    }

    String binaryStringBuilder(Map<String, Integer> wires, Set<String> zWires) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zWires.size(); i++) {
            sb.append('0');
        }
        for (String zWire : zWires) {
            int bitPosition = Integer.parseInt(zWire.substring(1));
            Integer bitValue = wires.get(zWire);
            if (bitValue == null) {
                return null;
            }
            if (bitValue == 1) {
                sb.setCharAt(sb.length() - bitPosition - 1, '1');
            }

        }
        return sb.toString();
    }

    boolean allZwires(Map<String, Integer> wires, Set<String> zWires) {
        for (String zWire : zWires) {
            if (!wires.containsKey(zWire)) {
                return false;
            }
        }
        return true;
    }

    List<String> readInput(String file) throws URISyntaxException, IOException {
        var resource = getClass().getClassLoader().getResource(file);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
        return lines;
    }
}
