import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class DijkstraStatementTests {

    private DijkstraGraph graph;

    @BeforeEach
    void setUp() {
        graph = new DijkstraGraph();
    }

    /**
     * Test 1: Basic Pathfinding & Node Initialization
     * * Target Coverage:
     * - Node constructor, addDestination, getters/setters.
     * - Main loop entry in calculateShortestPathFromSource.
     * - Processing of adjacent nodes.
     */
    @Test
    void testBasicShortestPath() {
        DijkstraGraph.Node nodeA = new DijkstraGraph.Node("A");
        DijkstraGraph.Node nodeB = new DijkstraGraph.Node("B");

        // Edge A -> B with weight 10
        nodeA.addDestination(nodeB, 10);

        DijkstraGraph.calculateShortestPathFromSource(graph, nodeA);

        // Expectation: Distance to B should be 10
        assertEquals(10, nodeB.getDistance(), "Distance to B should be 10");

        // Expectation: Path to B should contain A
        assertTrue(nodeB.getShortestPath().contains(nodeA), "Path to B should go through A");
    }

    /**
     * Test 2: The "Relaxation" Logic (Updating a shorter path)
     * * Target Coverage:
     * - calculateMinimumDistance() method.
     * - The 'if' block inside calculateMinimumDistance (where distance is updated).
     * - Node.setShortestPath() and Node.setDistance().
     */
    @Test
    void testRelaxationLogic() {
        DijkstraGraph.Node nodeA = new DijkstraGraph.Node("A");
        DijkstraGraph.Node nodeB = new DijkstraGraph.Node("B");
        DijkstraGraph.Node nodeC = new DijkstraGraph.Node("C");

        // A -> B (10)
        // A -> C (2)
        // C -> B (2) -> Effective path A->C->B is cost 4
        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 2);
        nodeC.addDestination(nodeB, 2);

        DijkstraGraph.calculateShortestPathFromSource(graph, nodeA);

        // If logic is correct, distance should be 4 (A->C->B), not 10 (A->B)
        assertEquals(4, nodeB.getDistance(), "Shortest path to B should be via C (cost 4)");
        assertEquals(2, nodeB.getShortestPath().size(), "Path should have 2 steps (A, C)");
    }

    /**
     * Test 3: Skipping Settled Nodes
     * * Target Coverage:
     * - The 'if (settledNodes.contains(currentNode))' check inside the while loop.
     * - The 'continue' statement.
     * * Explanation: We need a scenario where a node is added to the PriorityQueue
     * twice with different weights, so it is pulled out twice. The second time,
     * it should be in 'settledNodes' and skipped.
     */
    @Test
    void testSkipSettledNodes() {
        DijkstraGraph.Node nodeA = new DijkstraGraph.Node("A");
        DijkstraGraph.Node nodeB = new DijkstraGraph.Node("B");
        DijkstraGraph.Node nodeC = new DijkstraGraph.Node("C");

        // Path 1: A -> B (Cost 10) -> Added to PQ
        nodeA.addDestination(nodeB, 10);

        // Path 2: A -> C (Cost 1) -> C -> B (Cost 1) -> Total 2
        // When C is processed, B is added to PQ again with Cost 2.
        nodeA.addDestination(nodeC, 1);
        nodeC.addDestination(nodeB, 1);

        DijkstraGraph.calculateShortestPathFromSource(graph, nodeA);

        assertEquals(2, nodeB.getDistance());
        // Coverage is implicit: if the 'continue' statement wasn't there or failed,
        // the logic might re-process neighbors redundantly (though functional output might still look okay).
    }

    /**
     * Test 4: Disconnected Graph
     * * Target Coverage:
     * - Ensures the loop terminates correctly when unsettledNodes is empty.
     * - Handles cases where 'adjacentNodes' is empty or map iteration finishes.
     */
    @Test
    void testDisconnectedGraph() {
        DijkstraGraph.Node nodeA = new DijkstraGraph.Node("A");
        DijkstraGraph.Node nodeZ = new DijkstraGraph.Node("Z");

        // No connection between A and Z
        DijkstraGraph.calculateShortestPathFromSource(graph, nodeA);

        assertEquals(0, nodeA.getDistance());
        assertEquals(Integer.MAX_VALUE, nodeZ.getDistance(), "Unreachable node should remain at MAX_VALUE");
    }

    /**
     * Test 5: Complex Graph (From your Main Method)
     * * Target Coverage:
     * - Full integration test to ensure all statements work together in a larger context.
     */
    @Test
    void testComplexScenario() {
        DijkstraGraph.Node nodeA = new DijkstraGraph.Node("A");
        DijkstraGraph.Node nodeB = new DijkstraGraph.Node("B");
        DijkstraGraph.Node nodeC = new DijkstraGraph.Node("C");
        DijkstraGraph.Node nodeD = new DijkstraGraph.Node("D");
        DijkstraGraph.Node nodeE = new DijkstraGraph.Node("E");
        DijkstraGraph.Node nodeF = new DijkstraGraph.Node("F");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);
        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);
        nodeC.addDestination(nodeE, 10);
        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);
        nodeF.addDestination(nodeE, 5);

        DijkstraGraph.calculateShortestPathFromSource(graph, nodeA);

        // Expected distance for E: A(0) -> B(10) -> D(22) -> E(24)
        // Note: The logic in your main method output implies a specific path.
        // A->B(10), B->D(12)=22, D->E(2)=24.
        assertEquals(24, nodeE.getDistance());
    }
}