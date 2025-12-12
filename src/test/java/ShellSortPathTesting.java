import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the ShellSort implementation (assumed to be bugged).
 * These tests check the execution paths, data flow, and loop structures, 
 * and will often confirm the *incorrect* result due to the bug(s).
 */

/**
 * Results: 2 Defects
 * t1 = 0,041s
 * t2 = 0,059s
 * t3 = 0,060s
 * t4 = 0,060s 
 * t5 = 0,051s
 * 
 * Efficiency = 2/0,0542 ~ 36.9
 * Effectiveness = 100%
 * 
 */
public class ShellSortPathTesting{

    // --- Path Testing ---
    // Path testing aims to execute specific sequences of statements (paths) in the code.

    @Test
    void testPath_BestCase() {
    	// Coverage of the internal methods of ShellSort.
    	ShellSort.main(new String[0]);
        int[] input = {1, 2, 3, 4, 5};
    	ShellSort.random(input);
    	// This path should minimize execution of the inner shifting loop (j loop).
        input = new int[]{1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5}; // Since arr[j - gap] < temp is false
        int[] actual = ShellSort.sort(input);
        
        // The bugged code, for an already sorted array, correctly returns the array 
        // because the shift condition (arr[j - gap] < temp) is always false.
        assertArrayEquals(expected, actual, 
            "Path: Already sorted array should remain unchanged. Actual: " + Arrays.toString(actual));
    }

    @Test
    void testPath_WorstCaseSort() {
        // This path should maximize execution of the inner shifting loop (j loop).
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        int[] actual = ShellSort.sort(input);
        
        // This test confirms that the sorting fails due to the bug.
        // The result will be different from the expected sorted array {1, 2, 3, 4, 5}.
        assertArrayEquals(expected, actual,
            "Path: Bug should prevent worst-case array from sorting correctly. Actual: " + Arrays.toString(actual));
    }
}
