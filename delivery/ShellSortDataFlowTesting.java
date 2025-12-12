import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Results: 1 Defects
 * t1 = 0,042s
 * t2 = 0,050s
 * t3 = 0,046s
 * t4 = 0,047s 
 * t5 = 0,046s
 * 
 * Efficiency = 2/0,0462 ~ 21,645
 * Effectiveness = 50%
 * 
 */

public class ShellSortDataFlowTesting {
    // --- Data Flow Testing ---
    // Data flow testing focuses on the definition (D), use (U), and kill (K) of variables.
    // We'll focus on the 'temp' variable (defined at line 20, used at lines 28, 32)
    // and the array elements 'arr[j]' (used/defined in the j loop).

    @Test
    void testDataFlow_TempVariable_DefinitionAndUse() {
        // Test the definition of 'temp' and its use in the inner 'j' loop's condition 
        // and final assignment (Def-Use pairs).
        int[] input = {20, 10, 5};
        int[] expected = {5, 10, 20}; 
        int[] actual = ShellSort.sort(input);
        
        /* The process (n=3):
         * gap=1 (n/2). i=1: temp=10. j loop: j=1. arr[0]=20. 20 < 10 is FALSE. 
         * Loop body skipped. arr[1]=10. -> {20, 10, 5} -> bug
         * i=2: temp=5. j loop: j=2. arr[1]=10. 10 < 5 is FALSE. 
         * Loop body skipped. arr[2]=5. -> {20, 10, 5} -> bug
         * The assertion confirms the bug by checking if the actual result is NOT the expected sorted result.
         */
        assertArrayEquals(expected, actual,
            "Data Flow: 'temp' comparison bug prevents shifting. Actual: " + Arrays.toString(actual));
    }

    @Test
    void testDataFlow_ArrayElement_KillAndUse() {
        // Test the kill/definition (arr[j] = arr[j - gap]) and subsequent use (arr[j] = temp) of array elements.
        // This test ensures the j loop executes at least once for a kill-use sequence.
        int[] input = {1, 5, 2};
        int[] expected = {1, 2, 5};
        int[] actual = ShellSort.sort(input);
        
        /* The process (n=3):
         * gap=1. i=1: temp=5. j=1. arr[0]=1. 1 < 5 is TRUE.
         * j loop: arr[1] = arr[0] (Kill arr[1], arr becomes {1, 1, 2}). j becomes 0.
         * j condition fails (j >= gap is false).
         * arr[0] = temp (arr becomes {5, 1, 2}).
         * i=2: temp=2. j=2. arr[1]=1. 1 < 2 is TRUE.
         * j loop: arr[2] = arr[1] (Kill arr[2], arr becomes {5, 1, 1}). j becomes 1.
         * j condition fails (arr[0]=5. 5 < 2 is FALSE).
         * arr[1] = temp (arr becomes {5, 2, 1}).
         * The assertion confirms the bug by checking if the actual result is NOT the expected sorted result.
         */
        assertArrayEquals(expected, actual, 
            "Data Flow: Confirming Kill-Use sequence within the bugged logic. Actual: " + Arrays.toString(actual)); 
    }

}
