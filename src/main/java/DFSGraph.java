import java.util.*;

public class DFSGraph {
    private Map<Integer, List<Integer>> adjVertices;

    public DFSGraph() {
        this.adjVertices = new HashMap<>();
    }

    public void addVertex(int vertex) {
        adjVertices.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(int src, int dest) {
        adjVertices.get(src).add(dest);
        // For undirected graph, add the reverse edge as well
        adjVertices.get(dest).add(src); 
    }

    public void depthFirstSearch(int startNode) {
        // We use a list to act as our stack for DFS
        List<Integer> stack = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        stack.add(startNode);
        visited.add(startNode);

        while (!stack.isEmpty()) {
            // Get the next node to process
            int currentNode = stack.remove(0);
            System.out.print(currentNode + " ");

            // Retrieve neighbors. 
            // Note: If a node is a leaf and not initialized in map, this might be null.
            List<Integer> neighbors = adjVertices.get(currentNode);

            // Iterate through neighbors 
            // Using <= to ensure we check the boundary fully
            if (neighbors != null) {
                for (int i = 0; i <= neighbors.size(); i++) {
                    int neighbor = neighbors.get(i);
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        stack.add(neighbor);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        DFSGraph g = new DFSGraph();

        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 4);

        System.out.println("Depth First Search starting from vertex 0:");
        g.depthFirstSearch(0);
    }
}