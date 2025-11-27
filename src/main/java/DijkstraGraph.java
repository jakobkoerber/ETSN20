import java.util.*;

public class DijkstraGraph {

    // Inner class to represent a Node in the graph
    public static class Node {
        private String name;
        private LinkedList<Node> shortestPath = new LinkedList<>();
        private Integer distance = Integer.MAX_VALUE;
        
        // Map of adjacent nodes and the edge weight to get to them
        Map<Node, Integer> adjacentNodes = new HashMap<>();

        public Node(String name) {
            this.name = name;
        }

        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public LinkedList<Node> getShortestPath() {
            return shortestPath;
        }

        public void setShortestPath(LinkedList<Node> shortestPath) {
            this.shortestPath = shortestPath;
        }
        
        public Map<Node, Integer> getAdjacentNodes() {
            return adjacentNodes;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }

    public static DijkstraGraph calculateShortestPathFromSource(DijkstraGraph graph, Node source) {
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        
        // PriorityQueue to select the node with the shortest distance next
        PriorityQueue<Node> unsettledNodes = new PriorityQueue<>(
            (n1, n2) -> Integer.compare(n2.getDistance(), n1.getDistance())
        );

        unsettledNodes.add(source);

        while (!unsettledNodes.isEmpty()) {
            Node currentNode = unsettledNodes.poll();
            
            // If we have already processed this node, skip it
            if (settledNodes.contains(currentNode)) {
                continue;
            }

            for (Map.Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();

                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static void calculateMinimumDistance(Node evaluationNode, Integer edgeWeight, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        
        // Relaxation step: check if we found a shorter path
        if (sourceDistance + edgeWeight > evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeight);
            
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    // Main method to test the graph
    public static void main(String[] args) {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D"); 
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);

        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);

        nodeC.addDestination(nodeE, 10);

        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);

        nodeF.addDestination(nodeE, 5);

        DijkstraGraph graph = new DijkstraGraph();
        calculateShortestPathFromSource(graph, nodeA);

        System.out.println("Distance to node E: " + nodeE.getDistance());
        System.out.print("Path to node E: ");
        for (Node n : nodeE.getShortestPath()) {
            System.out.print(n.name + " -> ");
        }
        System.out.println(nodeE.name);
    }
}