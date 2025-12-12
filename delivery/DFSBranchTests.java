import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Pink branch is impossible to be executed as it has a contradictory condition
 * (is it not possible to have an empty stack in the beginning of the program
 * as a node is pushed onto the stack right at the start).
 * */
public class DFSBranchTests {
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
     * TEST CASE Blue branch: only one node
     * Fails as the condition should be < instead of <=. Tries to fetch neighbours that do not exist.
     */
    @Test
    public void testBlueBranch() {
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
     * TEST CASE Green branch: Has three nodes, one that will have already been explored
     * Fails as the condition should be < instead of <=. Tries to fetch neighbours that do not exist.
     */
    @Test
    public void testGreenBranch() {
        // One node with no neighbours
        DFSGraph g = new DFSGraph();
        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addEdge(0, 1);
        g.addEdge(0,4);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        try {
            g.depthFirstSearch(0);
            String expectedOutput = "0 1 2 3 4 ";
            assertEquals(expectedOutput, outContent.toString());
        } catch (IndexOutOfBoundsException e) {
            fail("Statement Coverage: Variable 'i' exceeded valid neighbor indices. " + e.getMessage());
        }
    }
}
