import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Results: 2 Defects
 * t1 = 0,062s
 * t2 = 0,075s
 * t3 = 0,074s
 * t4 = 0,065s 
 * t5 = 0,061s
 * 
 * Efficiency = 2/0,0674 ~ 29,67
 * Effectiveness = 100%
 * 
 */

public class ShellSortLoopTesting {
	
    // --- Loop Testing ---
    // Loop testing checks the outer (gap), middle (i), and inner (j) loops.

    @Test
    void testLoop_OuterGapLoop_ZeroIterations() {
        // Test for n < 2, where the outer loop (gap) is skipped immediately.
        int[] input = {1};
        int[] expected = {1};
        int[] actual = ShellSort.sort(input);

        assertArrayEquals(expected, actual, 
            "Loop: Single-element array (0 iterations of outer loop).");
    }

    @Test
    void testLoop_OuterGapLoop_TwoGaps() {
        // Test for n=4, which yields two gap iterations (gap=2, then gap=1).
        int[] input = {4, 1, 3, 2};
        int[] expected = {1, 2, 3, 4}; 
        int[] actual = ShellSort.sort(input);

        // The assertion confirms the bug by checking if the actual result is NOT the expected sorted result.
        assertArrayEquals(expected, actual,
            "Loop: Two gap sizes (gap=2, gap=1) should result in descending sort. Actual: " + Arrays.toString(actual));
    }

    @Test
    void testLoop_MiddleILoop_MinimumIterations() {
        // Test for the minimum number of inner loop (i) iterations. 
        // With n=2, gap=1, the i loop runs once (i=1).
        int[] input = {2, 1};
        int[] expected = {1, 2}; 
        int[] actual = ShellSort.sort(input);
        
        // n=2. gap=1. i=1: temp=1. arr[0]=2. 2 < 1 is FALSE. No shift. arr[1]=1.
        // The assertion confirms the bug by checking if the actual result is NOT the expected sorted result.
        assertArrayEquals(expected, actual, 
            "Loop: Minimum middle loop iterations. Actual: " + Arrays.toString(actual));
    }
    
    @Test
    void testLoop_InnerJLoop_ExecuteOnce() {
        // Test to ensure the inner j loop executes its body exactly one time.
        // e.g., arr[j - gap] < temp is true once, then j condition fails.
        int[] input = {10, 15, 5};
        int[] expected = {5, 10, 15}; 
        int[] actual = ShellSort.sort(input);
        
        /* n=3. gap=1. 
         * i=1: temp=15. j=1. arr[0]=10. 10 < 15 is TRUE. 
         * arr[1]=10. j=0. 
         * j condition (j >= gap) fails. 
         * arr[0]=15. -> {15, 10, 5}
         * i=2: temp=5. j=2. arr[1]=10. 10 < 5 is FALSE. 
         * arr[2]=5. -> {15, 10, 5}
         * The assertion confirms the bug by checking if the actual result is NOT the expected sorted result.
         */
        assertArrayEquals(expected, actual, 
            "Loop: Inner loop body executes exactly once. Actual: " + Arrays.toString(actual)); 
    }
    
    @Test
    void testLoop_InnerJLoop_ExecuteMultipleTimes() {
        // Test to ensure the inner j loop executes its body multiple times (n=5, gap=1).
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5}; 
        int[] actual = ShellSort.sort(input);
        
        // For gap=1, the bugged code performs a descending insertion sort. 
        // The array should become {5, 4, 3, 2, 1}
        // The assertion confirms the bug by checking if the actual result is NOT the expected sorted result.
        assertArrayEquals(expected, actual,
            "Loop: Inner loop executes multiple times, confirming descending sort for gap=1. Actual: " + Arrays.toString(actual)); 
    }
}

