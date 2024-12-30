package org.example.year2024.day17;

import java.util.Arrays;
import java.util.List;

public class ReverseState {
    public static final List<Long> EXPECTED_OUTPUT = Arrays.asList(2L, 4L, 1L, 2L, 7L, 5L, 4L, 5L, 0L, 3L, 1L, 7L, 5L, 5L, 3L, 0L);

    MemoryState memoryState;
    int idxReverse;

    public ReverseState(MemoryState memoryState, int idxReverse) {
        this.memoryState = memoryState;
        this.idxReverse = idxReverse;
    }

    public boolean tryAdd() {
        long result = memoryState.registerB % 8;
        if (result == EXPECTED_OUTPUT.get(idxReverse)) {
            idxReverse--;
            return true;
        }
        return false;
    }

    public boolean check() {
        return idxReverse == 0;
    }

}
