import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ShellSortTest {
    
    @Test
    void branchCoverage1() {
		int arr[] = {12, 34, 8, 9};

        ShellSort ob = new ShellSort();
        ob.sort(arr);

        int sorted[] = {8, 9, 12, 34}; 
        assertEquals(sorted, arr);
    }

}
