import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DFSStatementTests {

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
     * Test case that covers all statements.
     */
    @Test
    public void testCoverAllStatements() {
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
}