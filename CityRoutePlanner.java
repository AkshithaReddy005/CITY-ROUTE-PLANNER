import java.util.*;

// Edge class representing a road between two intersections
class Edge {
    String destination;
    int weight;

    public Edge(String destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }
}

// Graph class to manage intersections and roads
class Graph {
    private final Map<String, List<Edge>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    // Add an intersection
    public void addIntersection(String name) {
        adjList.putIfAbsent(name, new ArrayList<>());
    }

    // Add a road (edge) between intersections
    public void addRoad(String start, String end, int weight) {
        adjList.get(start).add(new Edge(end, weight));
        adjList.get(end).add(new Edge(start, weight)); // Bidirectional road
    }

    // Find shortest path using Dijkstra's algorithm
    public void findShortestPath(String start, String end) {
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();

        // Initialize distances
        for (String intersection : adjList.keySet()) {
            distances.put(intersection, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.add(new Edge(start, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            String currentNode = current.destination;

            if (visited.contains(currentNode)) continue;
            visited.add(currentNode);

            for (Edge neighbor : adjList.get(currentNode)) {
                if (!visited.contains(neighbor.destination)) {
                    int newDist = distances.get(currentNode) + neighbor.weight;
                    if (newDist < distances.get(neighbor.destination)) {
                        distances.put(neighbor.destination, newDist);
                        previous.put(neighbor.destination, currentNode);
                        pq.add(new Edge(neighbor.destination, newDist));
                    }
                }
            }
        }

        // Output the result
        if (distances.get(end) == Integer.MAX_VALUE) {
            System.out.println("No path found from " + start + " to " + end);
        } else {
            System.out.println("Shortest Path Distance: " + distances.get(end));
            List<String> path = new ArrayList<>();
            for (String at = end; at != null; at = previous.get(at)) {
                path.add(at);
            }
            Collections.reverse(path);
            System.out.println("Path: " + String.join(" -> ", path));
        }
    }

    // Detect cycles using DFS
    public boolean hasCycle() {
        Set<String> visited = new HashSet<>();
        for (String intersection : adjList.keySet()) {
            if (!visited.contains(intersection)) {
                if (dfsCycle(intersection, visited, null)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfsCycle(String node, Set<String> visited, String parent) {
        visited.add(node);
        for (Edge neighbor : adjList.get(node)) {
            if (!visited.contains(neighbor.destination)) {
                if (dfsCycle(neighbor.destination, visited, node)) {
                    return true;
                }
            } else if (!neighbor.destination.equals(parent)) {
                return true;
            }
        }
        return false;
    }
}

public class CityRoutePlanner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph graph = new Graph();

        while (true) {
            System.out.println("\nCity Route Planner:");
            System.out.println("1. Add Intersection");
            System.out.println("2. Add Road");
            System.out.println("3. Find Shortest Path");
            System.out.println("4. Detect Cycles");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Intersection Name: ");
                    String name = scanner.next();
                    graph.addIntersection(name);
                    System.out.println("Intersection added.");
                }
                case 2 -> {
                    System.out.print("Enter Starting Intersection: ");
                    String start = scanner.next();
                    System.out.print("Enter Ending Intersection: ");
                    String end = scanner.next();
                    System.out.print("Enter Distance: ");
                    int weight = scanner.nextInt();
                    graph.addRoad(start, end, weight);
                    System.out.println("Road added.");
                }
                case 3 -> {
                    System.out.print("Enter Starting Intersection: ");
                    String start = scanner.next();
                    System.out.print("Enter Ending Intersection: ");
                    String end = scanner.next();
                    graph.findShortestPath(start, end);
                }
                case 4 -> {
                    boolean hasCycle = graph.hasCycle();
                    System.out.println(hasCycle ? "The graph has cycles." : "The graph has no cycles.");
                }
                case 5 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
