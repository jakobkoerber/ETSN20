import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

public class DFSTest {

    private String runAndCapture(DFSGraph g, int start) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(baos));
            g.depthFirstSearch(start);
            return baos.toString().trim();
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void dfs_runsOnce_whenSingleIsolatedVertex() {
        DFSGraph g = new DFSGraph();
        g.addVertex(0);

        String output = runAndCapture(g, 0);

        assertEquals("0", output);
    }

    @Test
    void dfs_runsMultipleIterations_whenStartHasNeighbor() {
        DFSGraph g = new DFSGraph();
        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addVertex(6);
        g.addVertex(7);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(0, 5);
        g.addEdge(5, 6);
        g.addEdge(0, 7);

        String output = runAndCapture(g, 0);

        assertEquals("0 1 2 3 5 6 7", output);
    }
}