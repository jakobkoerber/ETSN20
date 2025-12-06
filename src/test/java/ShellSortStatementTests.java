import org.junit.Test;
import static org.junit.Assert.*;

public class ShellSortStatementTests {

    ShellSort sorter = new ShellSort();

    /**
     * Test Case 1: Standard Unsorted Array
     * GOAL: Trigger all loops and the swap logic (Statement Coverage).
     * * This input ensures:
     * 1. gap > 0 is True (Outer loop entered)
     * 2. i < n is True (Middle loop entered)
     * 3. The logic inside the inner loop is exercised. 
     * (Note: Since the buggy code sorts descending, providing an Ascending
     * input like {2, 3...} usually forces the swap body to execute).
     */
    @Test
    public void testStandardSort() {
        int[] input = {12, 34, 54, 2, 3};
        int[] expected = {2, 3, 12, 34, 54}; // We expect Ascending

        sorter.sort(input);

        assertArrayEquals("Should sort array in ascending order", expected, input);
    }

    /**
     * Test Case 2: Already Sorted Array
     * GOAL: Cover the path where the inner swap condition is FALSE.
     * * If the array is already sorted, correct Shell Sort logic should check 
     * the condition 'arr[j - gap] > temp', find it false, and skip the 
     * body of the inner while loop.
     */
    @Test
    public void testAlreadySorted() {
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        sorter.sort(input);

        assertArrayEquals("Sorted array should remain sorted", expected, input);
    }

    /**
     * Test Case 3: Reverse Sorted Array
     * GOAL: Maximize execution of the inner while/for loop body.
     * * This forces the algorithm to do the maximum amount of work/swapping
     * ensuring that the 'j = j - 1' (or j -= gap) update statement is hit repeatedly.
     */
    @Test
    public void testReverseSorted() {
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        sorter.sort(input);

        assertArrayEquals("Reverse array should be sorted ascending", expected, input);
    }

    /**
     * Test Case 4: Edge Case - Empty or Single Element
     * GOAL: Cover the 'False' branch of the initial 'gap > 0' check.
     * * If n=0 or n=1, gap becomes 0 immediately. 
     * This tests the path: Start -> gap=n/2 -> gap>0 (False) -> Return.
     */
    @Test
    public void testEmptyAndSingleElement() {
        // Empty
        int[] inputEmpty = {};
        sorter.sort(inputEmpty);
        assertArrayEquals(new int[]{}, inputEmpty);

        // Single
        int[] inputSingle = {42};
        sorter.sort(inputSingle);
        assertArrayEquals(new int[]{42}, inputSingle);
    }
}