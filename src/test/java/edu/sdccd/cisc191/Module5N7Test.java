package edu.sdccd.cisc191;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Module5N7Test {
    // Recursive JavaFX sequence, reduced to testable methods
    public List<Integer> computeCountdownSequence(int n) {
        List<Integer> result = new ArrayList<>();
        computeCountdownRecursive(n, result);
        return result;
    }
    // Helper method
    private void computeCountdownRecursive(int n, List<Integer> result) {
        if (n <= 0) {
            return;
        }
        result.add(n);
        computeCountdownRecursive(n - 1, result);
    }

    @Test
    void checkRecursiveCountdown(){
        List<Integer> result = new ArrayList<>();
        result.add(3);
        result.add(2);
        result.add(1);
        assertEquals(computeCountdownSequence(3), result);
    }

    @Test
    void checkCountdownFrom0(){
        assert(computeCountdownSequence(0)).isEmpty();
    }
}
