import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DijkstraGraphTest {

    /**
     * Source node with no outgoing edges.
     */
    @Test
    void calculateShortestPath_isolatedSourceNode() {
        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.Node source = new DijkstraGraph.Node("A");

        DijkstraGraph.calculateShortestPathFromSource(graph, source);

        assertEquals(0, source.getDistance());
        assertTrue(source.getShortestPath().isEmpty());
    }

    /**
     * Single edge A -> B with weight 10.
     */
    @Test
    void calculateShortestPath_allBranchesTrue_singleEdge() {
        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.Node source = new DijkstraGraph.Node("A");
        DijkstraGraph.Node dest = new DijkstraGraph.Node("B");

        source.addDestination(dest, 10);

        DijkstraGraph.calculateShortestPathFromSource(graph, source);

        assertEquals(0, source.getDistance());
        assertEquals(10, dest.getDistance());
        assertEquals(1, dest.getShortestPath().size());
    }

    /**
     * Graph with a simple cycle: A <-> B.
     */
    @Test
    void calculateShortestPath_settledNodeTrue_skipDuplicateInQueue() {
        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.Node a = new DijkstraGraph.Node("A");
        DijkstraGraph.Node b = new DijkstraGraph.Node("B");

        a.addDestination(b, 10);
        b.addDestination(a, 5);

        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        assertEquals(0, a.getDistance());
        assertEquals(10, b.getDistance());
    }

    /**
     * Relaxation FALSE: existing distance is already shorter than the new candidate.
     * We manually preset dest distance to 5 and add a worse path with cost 10.
     */
    @Test
    void calculateShortestPath_relaxationFalse_alreadyShorterPath() {
        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.Node source = new DijkstraGraph.Node("A");
        DijkstraGraph.Node dest = new DijkstraGraph.Node("B");

        dest.setDistance(5);
        source.addDestination(dest, 10);

        DijkstraGraph.calculateShortestPathFromSource(graph, source);

        assertEquals(5, dest.getDistance());
    }

    /**
     * Relaxation TRUE via a strictly shorter alternate path.
     */
    @Test
    void calculateShortestPath_relaxationTrue_findsShorterAlternatePath() {
        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.Node a = new DijkstraGraph.Node("A");
        DijkstraGraph.Node b = new DijkstraGraph.Node("B");
        DijkstraGraph.Node c = new DijkstraGraph.Node("C");

        a.addDestination(b, 10);
        a.addDestination(c, 1);  // better first step
        c.addDestination(b, 1);  // completes the shorter path A -> C -> B

        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        assertEquals(0, a.getDistance());
        assertEquals(2, b.getDistance());
        assertEquals(2, b.getShortestPath().size());
    }
}


