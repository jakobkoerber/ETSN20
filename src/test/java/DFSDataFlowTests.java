import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DFSDataFlowTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * TEST CASE 1: Variable 'i' Boundary Coverage
     * Focus: Data Flow of variable 'i'
     **/
    @Test
    public void testNeighborLoopBoundary() {
        DFSGraph g = new DFSGraph();
        g.addVertex(0);
        g.addVertex(1);
        g.addEdge(0, 1);

        // Expected behavior: Traverses 0, then 1, then stops safely.
        try {
            g.depthFirstSearch(0);

            // If the code works correctly, we expect this string:
            String expectedOutput = "0 1 ";
            assertEquals(expectedOutput, outContent.toString());
        } catch (IndexOutOfBoundsException e) {
            fail("Data Flow Error Detected: Variable 'i' exceeded valid neighbor indices. " + e.getMessage());
        }
    }

    /**
     * TEST CASE 2: Variable 'stack' Processing Order (LIFO vs FIFO)
     * Focus: Data Flow of variable 'stack'
     */
    @Test
    public void testStackLIFODataFlow() {
        DFSGraph g = new DFSGraph();
        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3); // Child of 1

        // Structure:
        //      0
        //     / \
        //    1   2
        //    |
        //    3
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);

        g.depthFirstSearch(0);

        String output = outContent.toString().trim();

        // A correct DFS (LIFO) usually produces: 0 2 1 3 (or 0 1 3 2 depending on neighbor order)
        boolean isDFS = output.equals("0 1 3 2") || output.equals("0 2 1 3");

        assertTrue("Data Flow Error: 'stack' variable acted as a Queue (BFS) instead of a Stack (DFS). Actual: " + output, isDFS);
    }

    /**
     * TEST CASE 3: Variable 'neighbors' Null vs Empty
     * Focus: Data Flow of variable 'neighbors'
     */
    @Test
    public void testLeafNodeDataFlow() {
        DFSGraph g = new DFSGraph();
        g.addVertex(0);
        // 0 has no edges. neighbors is Empty (but not null).

        g.depthFirstSearch(0);

        assertEquals("0 ", outContent.toString());
    }
}