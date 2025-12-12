import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Cyclomatic complexity:
 * #decisions (4) + 1 = 5
 * or
 * #edges (18) - #nodes (15) + 2 = 5
 * Paths:
 * Edges: 1-2-3-4-5-6-7-8-9-10-11-12-13-14-15-16-17-18
 * Path1: 1-1-1-1-1-0-0-0-0-0-0-0-0-0-0-0-0-0 (Impossible)
 * Path2: 1-1-1-1-0-1-1-1-1-0-0-0-0-0-0-0-0-0
 * Path3: 1-1-1-1-1-1-1-1-1-1-1-1-0-0-0-0-0-0 (Impossible)
 * Path4: 1-1-1-1-0-1-1-1-1-1-1-1-1-1-1-0-1-1
 * Path5: 1-1-1-1-0-1-1-1-1-1-1-1-1-1-0-1-0-1
 * */
public class DFSPathTests {
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
     * TEST Path 2: only one node
     * Fails as the condition should be < instead of <=. Tries to fetch neighbours that do not exist.
     */
    @Test
    public void testPath2() {
        // One node with no neighbours
        DFSGraph g = new DFSGraph();
        g.addVertex(0);
        try {
            g.depthFirstSearch(0);
            String expectedOutput = "0 ";
            assertEquals(expectedOutput, outContent.toString());
        } catch (IndexOutOfBoundsException e) {
            fail("Statement Coverage: Variable 'i' exceeded valid neighbor indices. " + e.getMessage());
        }
    }

    /**
     * TEST Path 3: Has two nodes
     * Fails as the condition should be < instead of <=. Tries to fetch neighbours that do not exist.
     */
    @Test
    public void testPath4() {
        // One node with no neighbours
        DFSGraph g = new DFSGraph();
        g.addVertex(0);
        g.addVertex(1);
        g.addEdge(0, 1);
        try {
            g.depthFirstSearch(0);
            String expectedOutput = "0 1 ";
            assertEquals(expectedOutput, outContent.toString());
        } catch (IndexOutOfBoundsException e) {
            fail("Statement Coverage: Variable 'i' exceeded valid neighbor indices. " + e.getMessage());
        }
    }

    /**
     * TEST Path 3: Has three nodes with one already visited
     * Fails as the condition should be < instead of <=. Tries to fetch neighbours that do not exist.
     */
    @Test
    public void testPath5() {
        // One node with no neighbours
        DFSGraph g = new DFSGraph();
        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        try {
            g.depthFirstSearch(0);
            String expectedOutput = "0 1 2 ";
            assertEquals(expectedOutput, outContent.toString());
        } catch (IndexOutOfBoundsException e) {
            fail("Statement Coverage: Variable 'i' exceeded valid neighbor indices. " + e.getMessage());
        }
    }
}
