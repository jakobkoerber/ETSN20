import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class DijkstraGraphTest {

    // Short alias for inner class
    private DijkstraGraph.Node node(String name) {
        return new DijkstraGraph.Node(name);
    }

    // ---------------------------------------------------------------------
    // 1. PATH TESTS
    // ---------------------------------------------------------------------

    @Test
    void path_singleIsolatedNode_noAdjacencies_P2() {
        DijkstraGraph.Node a = node("A");
        DijkstraGraph graph = new DijkstraGraph();

        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        // Only the source node A should be processed; distance set to 0.
        assertEquals(Integer.valueOf(0), a.getDistance());
        // No path before A itself
        assertTrue(a.getShortestPath().isEmpty());
    }

  
    @Test
    void path_simpleEdge_AtoB_P5() {
        DijkstraGraph.Node a = node("A");
        DijkstraGraph.Node b = node("B");

        a.addDestination(b, 5);

        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        // These assertions define correct Dijkstra behavior.
        // If your current implementation is still buggy, they will fail (good for fault detection).
        assertEquals(Integer.valueOf(0), a.getDistance(), "Source distance should be 0");
        assertEquals(Integer.valueOf(5), b.getDistance(), "B should be at distance 5 from A");

        LinkedList<DijkstraGraph.Node> pathToB = b.getShortestPath();
        assertEquals(1, pathToB.size());
        assertEquals(a, pathToB.getFirst(), "Shortest path to B should go through A");
    }

   
    @Test
    void path_backEdge_BtoA_P3() {
        DijkstraGraph.Node a = node("A");
        DijkstraGraph.Node b = node("B");

        a.addDestination(b, 3);
        b.addDestination(a, 3); // back edge

        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        // For a correct Dijkstra:
        assertEquals(Integer.valueOf(0), a.getDistance());
        assertEquals(Integer.valueOf(3), b.getDistance());

        // We do not assert anything on second visit to A (back edge) except that
        // algorithm terminates without infinite loops or exceptions.
    }

   
    @Test
    void path_duplicateNodeInQueue_triggersContinue_P6() {
        DijkstraGraph.Node a = node("A");
        DijkstraGraph.Node b = node("B");
        DijkstraGraph.Node c = node("C");

        a.addDestination(b, 2);
        a.addDestination(c, 10);
        b.addDestination(c, 1); // C reachable via A->B->C as well

        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        // Correct Dijkstra expectation: dist(C) = 3 via A->B->C (2+1)
        assertEquals(Integer.valueOf(0), a.getDistance());
        assertEquals(Integer.valueOf(2), b.getDistance());
        assertEquals(Integer.valueOf(3), c.getDistance(),
                "C should be reached via A->B->C with total distance 3");
    }

    // ---------------------------------------------------------------------
    // 2. DATA-FLOW TESTS 
    // ---------------------------------------------------------------------

  
    @Test
    void dataFlow_DU1_DU2_simpleEdge_AtoB() {
        DijkstraGraph.Node a = node("A");
        DijkstraGraph.Node b = node("B");
        a.addDestination(b, 7);

        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        // d1 has definitely executed: the distance of A is 0.
        assertEquals(Integer.valueOf(0), a.getDistance());

        // If D5 were implemented correctly (<), B.distance should now be 7.
        assertEquals(Integer.valueOf(7), b.getDistance(),
                "Data-flow DU1/DU2: B should get distance 7 from A via a single edge");
    }

   
    @Test
    void dataFlow_calculateMinimumDistance_conditionTrueAndFalse() throws Exception {
        DijkstraGraph.Node src = node("S");
        DijkstraGraph.Node v = node("V");

        // Access the private static method via reflection
        Method m = DijkstraGraph.class.getDeclaredMethod(
                "calculateMinimumDistance",
                DijkstraGraph.Node.class, Integer.class, DijkstraGraph.Node.class
        );
        m.setAccessible(true);

        // CASE 1: D5 is false  (sourceDistance + edgeWeight <= evaluationNode.getDistance())
        src.setDistance(0);
        v.setDistance(100);
        m.invoke(null, v, 10, src); // distance = 0 + 10; 10 > 100? → false
        assertEquals(Integer.valueOf(100), v.getDistance(),
                "V's distance must remain 100 when the condition is false");

        // CASE 2: D5 is true (sourceDistance + edgeWeight > evaluationNode.getDistance())
        src.setDistance(10);
        v.setDistance(5);
        m.invoke(null, v, 10, src); // distance = 20; 20 > 5? → true in current (bugged) code
        assertEquals(Integer.valueOf(20), v.getDistance(),
                "V's distance must be updated to 20 when the condition is true");

        // For a correct Dijkstra, the comparison should be '<' instead of '>'.
        // This test will pass for the current (bugged) logic but reveals
        // that the implemented condition does not match the usual algorithm.
    }

    // ---------------------------------------------------------------------
    // 3. LOOP TESTS
    //    (same graphs as path tests, but interpreted in terms of loops)
    // ---------------------------------------------------------------------

  
    @Test
    void loop_outerOneIteration_innerZero_singleNode() {
        DijkstraGraph.Node a = node("A");
        DijkstraGraph graph = new DijkstraGraph();

        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        assertEquals(Integer.valueOf(0), a.getDistance());
    }

   
    @Test
    void loop_outerTwoIterations_innerOneAndZero_AtoB() {
        DijkstraGraph.Node a = node("A");
        DijkstraGraph.Node b = node("B");

        a.addDestination(b, 4);

        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        // Again, we check the expected final distances for a correct Dijkstra.
        assertEquals(Integer.valueOf(0), a.getDistance());
        assertEquals(Integer.valueOf(4), b.getDistance());
    }

  
    @Test
    void loop_innerMultipleIterations_AtoB_and_AtoC() {
        DijkstraGraph.Node a = node("A");
        DijkstraGraph.Node b = node("B");
        DijkstraGraph.Node c = node("C");

        a.addDestination(b, 2);
        a.addDestination(c, 5);

        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        // Correct Dijkstra expectations (again, used as oracles):
        assertEquals(Integer.valueOf(0), a.getDistance());
        assertEquals(Integer.valueOf(2), b.getDistance());
        assertEquals(Integer.valueOf(5), c.getDistance());
    }

   
    @Test
    void loop_outerWithContinue_duplicateCInQueue() {
        DijkstraGraph.Node a = node("A");
        DijkstraGraph.Node b = node("B");
        DijkstraGraph.Node c = node("C");

        a.addDestination(b, 2);
        a.addDestination(c, 10);
        b.addDestination(c, 1);

        DijkstraGraph graph = new DijkstraGraph();
        DijkstraGraph.calculateShortestPathFromSource(graph, a);

        // For a correct Dijkstra:
        assertEquals(Integer.valueOf(0), a.getDistance());
        assertEquals(Integer.valueOf(2), b.getDistance());
        assertEquals(Integer.valueOf(3), c.getDistance());
    }
}