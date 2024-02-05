package edu.yu.da.ThereAndBackAgain;

import java.util.*;
import java.util.stream.Collectors;

public class ThereAndBackAgain extends ThereAndBackAgainBase {
  // Add edges into the SP subset the usual way making sure to use <= to allow edges of the same length.
  // Once Dijkstra is done, the distTo is known. Sort in VlogV. Now using priority queue push edges from largest distance 
  // need to find way back maybe in parallel choosing edges based on different criterion using hashin perhaps
  // if there is at least one different we are succesful otherwise 
  // keep track of edge from
  // any other return path in SP graph is by definition the same length - if longer would not be, if shorter the initial path would not be

    private static class Edge implements Comparable<Edge> {
      String dest;
      double weight;
      double dist;

      public Edge(String dest, double weight) {
          this.dest = dest;
          this.weight = weight;
      }

      public void dist(double dist) {
        this.dist = dist;
      }

      @Override
      public int compareTo(Edge otherEdge) {
        return Double.compare(this.dist, otherEdge.dist);
      }
      

    }

    boolean found;
    String start;
    String goal;
    List<String> lesser;
    List<String> greater;


    HashMap<String, List<Edge>> graph;
    HashMap<String, Double> distTo;
    HashMap<String, List<String>> edgeTo;
    
    /** Constructor which supplies the start vertex
   *
   * @param startVertex, length must be > 0.
   * @throws IllegalArgumentException if the pre-condiitions are
   * violated
   */
  public ThereAndBackAgain(String startVertex) {
    super(startVertex);
    this.found = false;

    validate(startVertex);
    this.start = startVertex;

    this.graph = new HashMap<>();
    this.graph.put(startVertex, new ArrayList<>());

    this.distTo = new HashMap<>();
    this.distTo.put(startVertex, 0.0);
  }

  /** Adds an weighted undirected edge between vertex v and vertex w.  The two
   * vertices must differ from one another, and an edge between the two
   * vertices cannot have been added previously.
   *
   * @param v specifies a vertex, length must be > 0.
   * @param w specifies a vertex, length must be > 0.
   * @param weight the edge's weight, must be > 0.
   * @throws IllegalStateException if doIt() has previously been invoked.
   * @throws IllegalArgumentException if the other pre-conditions are violated.
   */
  public void addEdge(String v, String w, double weight) {
    validate(v);
    validate(w);
    validate(weight);

    this.graph.computeIfAbsent(v, k -> new ArrayList<Edge>()).add(new Edge(w, weight));
    this.graph.computeIfAbsent(w, k -> new ArrayList<Edge>()).add(new Edge(v, weight));

    this.distTo.putIfAbsent(v, Double.MAX_VALUE);
    this.distTo.putIfAbsent(w, Double.MAX_VALUE);
  }
  
  /** Client informs implementation that the graph is fully constructed and
   * that the ThereAndBackAgainBase algorithm should be run on the graph.
   * After the method completes, the client is permitted to invoke the
   * solution's getters.
   *
   * Note: once invoked, the implementation must ignore subsequent calls to
   * this method.
   * @throws IllegalStateException if doIt() has previously been invoked.
   */
  public void doIt() {
    if (this.found == true) return;

    this.found = true;

    dijkstra();
  }

  /** If the graph contains a "goal vertex of the longest valid path" (as
   * defined by the requirements document), returns it.  Else returns null.
   *
   * @return goal vertex of the longest valid path if one exists, null
   * otherwise.
   */
  public String goalVertex() {
    return goal;
  }

  /** Returns the cost (sum of the edge weights) of the longest valid path if
   * one exists, 0.0 otherwise.
   *
   * @return the cost if the graph contains a longest valid path, 0.0
   * otherwise.
   */
  public double goalCost() {
    // in other words largest distTo that fulfills condition
    if (goal == null) {
      return 0.0;
    }
    return distTo.get(goal);
  }

  /** If a longest valid path exists, returns a ordered sequence of vertices
   * (beginning with the start vertex, and ending with the goal vertex)
   * representing that path.
   *
   * IMPORTANT: given the existence of (by definition) two longest valid paths,
   * this method returns the List with the LESSER of the two List.hashCode()
   * instances.
   *
   * @return one of the two longest paths, Collections.EMPTY_LIST if the graph
   * doesn't contain a longest valid path.
   */
  public List<String> getOneLongestPath() {
    if (goal == null) {
      return Collections.EMPTY_LIST;
    }
    return lesser;
  }

  /** If a longest valid path exists, returns the OTHER ordered sequence of
   * vertices (beginning with the start vertex, and ending with the goal
   * vertex) representing that path.
   *
   * IMPORTANT: given the existence of (by definition) two longest valid paths,
   * this method returns the List with the GREATER of the two List.hashCode()
   * instances.
   *
   * @return the other of the two longest paths, Collections.EMPTY_LIST if the
   * graph doesn't contain a longest valid path.
   */
  public List<String> getOtherLongestPath() {
    if (goal == null) {
      return Collections.EMPTY_LIST;
    }

    return greater;
  }

  private void validate(String s) {
    if (s.length() == 0) {
        throw new IllegalArgumentException();
    }
  }
  private void validate(double i) {
    if (i <= 0) {
        throw new IllegalArgumentException();
    }
  }

  private void dijkstra() {
    this.edgeTo = new HashMap<>();
    HashMap<String, Integer> pathCount = new HashMap<>();
    Set<String> visited = new HashSet<>();
    PriorityQueue<Edge> pq = new PriorityQueue<>();
    Edge source = new Edge(start, 0);
    source.dist(0);
    pq.add(source);
    pathCount.put(source.dest,1);

    while(!pq.isEmpty()) {
      Edge current = pq.poll();
      if (visited.contains(current.dest)) {
        continue;
      }
      System.out.printf("Current: %s, distance: %.2f, paths: %d\n", current.dest, this.distTo.get(current.dest), pathCount.get(current.dest));
      visited.add(current.dest);
      
      for (Edge edge: this.graph.get(current.dest)) {
        double tempDist = distTo.get(current.dest) + edge.weight;
        if (distTo.get(edge.dest) > tempDist) {
          if (edge.dest.equals("k")) {
            System.out.printf("Path to: %s through %s, k is %.2f away\n", edge.dest, current.dest, tempDist);
          }
          distTo.put(edge.dest, tempDist);
          //pq.remove(edge);
          edge.dist(tempDist);
          pq.add(edge);
          pathCount.put(edge.dest, pathCount.get(current.dest));
          this.edgeTo.put(edge.dest, new ArrayList<>());
          this.edgeTo.get(edge.dest).add(current.dest);
        } else if (distTo.get(edge.dest) == tempDist) {
          pathCount.put(edge.dest, pathCount.get(edge.dest)+pathCount.get(current.dest));
          this.edgeTo.get(edge.dest).add(current.dest);
          System.out.printf("Alternate path to: %s through %s\n", edge.dest, current.dest);
         // System.out.printf("Prior path to: %s through %s\n", edge.dest, this.edgeTo.get(edge.dest).get(0));
        }
      }
    }

    List<String> sortedNodes = distTo.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

    for (String node: sortedNodes) {
      if (pathCount.get(node) > 1) {
        goal = node;
        break;
      }
    }

    for (String node: sortedNodes) {
      System.out.printf("Node: %s, dist: %.2f, paths: %d\n", node, distTo.get(node), pathCount.get(node));
    }

    if (goal == null) {
      return;
    }
    pathTo(goal);
  }


  // pathTo inspired by Sedgewick: https://algs4.cs.princeton.edu/44sp/DijkstraSP.java.html
  private void pathTo(String goal) {
    List<String> path = new ArrayList<>();
    path.add(goal);
    System.out.printf("goal: %s\n", goal);

    List<String> current;
    for (current = edgeTo.get(goal); current.size() == 1; current = edgeTo.get(current.get(0))) {
      path.add(current.get(0));
    }

    List<String> path1 = processRest(current.get(0));
    List<String> path2 = processRest(current.get(1));

    path1.addAll(0, path);
    path2.addAll(0, path);

    Collections.reverse(path1);
    Collections.reverse(path2);

    printList(path1);
    printList(path2);
    
    if (path1.hashCode() < path2.hashCode()) {
      this.lesser = path1;
      this.greater = path2;
    } else {
      this.lesser = path2;
      this.greater = path1;
    }
  }

  private List<String> processRest(String curr) {
    List<String> path = new ArrayList<>();
    path.add(curr);
    List<String> current;
    for (current = edgeTo.get(curr); !current.get(0).equals(this.start); current = edgeTo.get(current.get(0))) {
      path.add(current.get(0));
    }
    path.add(current.get(0));

    return path;
  }

  private void printList(List<String> path) {
    for (String element : path) {
      System.out.printf("%s, ", element);
    }
    System.out.println();
  }
}
