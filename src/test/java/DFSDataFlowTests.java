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
     * * Definition: int i = 0; i++
     * Use (Predicate): i <= neighbors.size()
     * Use (Computation): neighbors.get(i)
     * * WHY THIS IS A WHITE BOX TEST:
     * This explicitly targets the loop structure. 
     * EXPECTATION: A correct DFS should process neighbors [0...n-1] and stop.
     * REALITY (The Bug): The code uses '<=' causing an IndexOutOfBoundsException 
     * when i == size(). This test will fail (crash), successfully revealing the data flow bug.
     */
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
     * * Definition: stack.add(neighbor)
     * Use (Computation): stack.remove(0)
     * * WHY THIS IS A WHITE BOX TEST:
     * We are testing the flow of data into and out of the collection.
     * EXPECTATION: DFS uses a Stack (LIFO - Last In, First Out). 
     * If 0 connects to 1 and 2, and we visit 1 first, we should dive deeper into 1 before visiting 2.
     * REALITY (The Bug): The code does 'stack.remove(0)' which is FIFO (Queue/BFS behavior).
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
        // A BFS (FIFO - The bug in your code) produces: 0 1 2 3 (Level order)

        // We assert that the output represents a valid path finding depth (3 comes after 1 immediately)
        // rather than breadth.
        boolean isDFS = output.equals("0 1 3 2") || output.equals("0 2 1 3");

        assertTrue("Data Flow Error: 'stack' variable acted as a Queue (BFS) instead of a Stack (DFS). Actual: " + output, isDFS);
    }

    /**
     * TEST CASE 3: Variable 'neighbors' Null vs Empty
     * Focus: Data Flow of variable 'neighbors'
     * * Definition: neighbors = adjVertices.get(currentNode)
     * Use (Predicate): neighbors != null
     * * WHY THIS IS A WHITE BOX TEST:
     * This tests the specific branch in the flowchart where neighbors might be null.
     * Note: In the provided code, addVertex ensures the list is not null, 
     * but 'adjVertices.get(currentNode)' could be null if a node was added to the stack 
     * manually or via an edge definition without an addVertex call (if the code allowed it).
     * * However, strictly following the code: 'addVertex' initializes the ArrayList.
     * We test the standard "Leaf Node" path where the loop should not execute 
     * because neighbors.size() is 0.
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