import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ShellSortTest {
    
    @Test
    void branchCoverage1() {
		int arr[] = {1, 32, 34, 7, 12, 8, 9};

        ShellSort ob = new ShellSort();
        ob.sort(arr);

        int sorted[] = {1, 7, 8, 9, 12, 12, 32, 34, 34}; 
        assertEquals(sorted, arr);
    }

}
