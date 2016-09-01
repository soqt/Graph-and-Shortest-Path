import java.util.*;

/**CSE373 HW4
 * Yumeng Wang
 * Student ID: 1360735
 * 
 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 */
public class MyGraph implements Graph {
    // you will need some private fields to represent the graph
    // you are also likely to want some private helper methods

    // YOUR CODE HERE
	
	private Map<Vertex, Set<Edge>> graph;
	private Set<Edge> edges;
	
    /**
     * Creates a MyGraph object with the given collection of vertices
     * and the given collection of edges.
     * @param v a collection of the vertices in this graph
     * @param e a collection of the edges in this graph
     */
    public MyGraph(Collection<Vertex> v, Collection<Edge> e) {
    	if(v.isEmpty() || e.isEmpty()){
    		throw new IllegalArgumentException("null");
    	}
		graph = new HashMap<Vertex, Set<Edge>>();
		edges = new HashSet<Edge>();
    	
		for(Vertex vertex : v){
			if(!graph.containsKey(vertex))
				graph.put(vertex, new HashSet<Edge>());
		}
		
		for(Edge edge : e){
			if(!graph.containsKey(edge.getSource())){
				throw new IllegalArgumentException("No source found");
			}
			if(edge.getWeight() < 0){
				throw new IllegalArgumentException("Edge weights should not be negative");
			}
			
			for (Edge tempEdge : graph.get(edge.getSource())){
				if(tempEdge.getDestination() == edge.getDestination() &&
				   tempEdge.getSource() == edge.getSource() &&
				   tempEdge.getWeight() != edge.getWeight()){
					throw new IllegalArgumentException("There are two same edge with different weight");
				}
			}
			edges.add(edge);
			graph.get(edge.getSource()).add(edge);
		}
    }

    /** 
     * Return the collection of vertices of this graph
     * @return the vertices as a collection (which is anything iterable)
     */
    public Collection<Vertex> vertices() {
    	Collection<Vertex> outVertex = new HashSet<Vertex>();
    	for(Vertex vertex : this.graph.keySet()){
    		outVertex.add(new Vertex(vertex.getLabel()));
    	}
    	return outVertex;
    }

    /** 
     * Return the collection of edges of this graph
     * @return the edges as a collection (which is anything iterable)
     */
    public Collection<Edge> edges() {
    	Collection<Edge> outEdge = new HashSet<Edge>();
    	for(Edge edge : this.edges){
    		outEdge.add(new Edge(edge.getSource(), edge.getDestination(), edge.getWeight()));
    	}
    	return outEdge;
    }

    /**
     * Return a collection of vertices adjacent to a given vertex v.
     *   i.e., the set of all vertices w where edges v -> w exist in the graph.
     * Return an empty collection if there are no adjacent vertices.
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    public Collection<Vertex> adjacentVertices(Vertex v) {
    	if(!this.graph.containsKey(v)){
    		throw new IllegalArgumentException("input vertex does not exist");
    	}
    	List<Vertex> adjacentVertices = new ArrayList<Vertex>();
    	for(Edge edge : this.graph.get(v)){
    		adjacentVertices.add(new Vertex(edge.getDestination().getLabel()));
    	}
    	return adjacentVertices;
    }

    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
     * Assumes that we do not have negative cost edges in the graph.
     * @param a one vertex
     * @param b another vertex
     * @return cost of edge if there is a directed edge from a to b in the graph, 
     * return -1 otherwise.
     * @throws IllegalArgumentException if a or b do not exist.
     */
    public int edgeCost(Vertex a, Vertex b) {
    	if(a == null || b == null)
    		throw new IllegalArgumentException("null");
    	for(Edge edge : this.graph.get(a)){
    		if(edge.getDestination().equals(b))
    			return edge.getWeight();
    	}
    	return -1;
    }

    /**
     * Returns the shortest path from a to b in the graph, or null if there is
     * no such path.  Assumes all edge weights are nonnegative.
     * Uses Dijkstra's algorithm.
     * @param a the starting vertex
     * @param b the destination vertex
     * @return a Path where the vertices indicate the path from a to b in order
     *   and contains a (first) and b (last) and the cost is the cost of 
     *   the path. Returns null if b is not reachable from a.
     * @throws IllegalArgumentException if a or b does not exist.
     */
    public Path shortestPath(Vertex a, Vertex b) {
    	if(!this.graph.containsKey(a) || !this.graph.containsKey(b)){
    		throw new IllegalArgumentException("input vertex does not exist");
    	}
    	List<Vertex> vertices = new ArrayList<Vertex>();
    	if(a.equals(b)){
    		vertices.add(a);
    		return new Path(vertices, 0);
    	}
   
    	PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>();
    	a.cost = 0;
    	pq.add(a);
    	for(Vertex vertex : this.graph.keySet()){
    		if(!vertex.equals(a)){
    			vertex.cost = Integer.MAX_VALUE;
    			vertex.known = false;
    			pq.add(vertex);
    		}	
    	}
    	
    	// main loop of Djikstra's Algorithm
    	// find the shortest path between two given vertex
    	while(!pq.isEmpty()){
    		Vertex current = pq.poll();
    		current.known = true;
    		for(Vertex adjacentVertex :  adjacentVertices(current)){
    			// check whether or not the adjacent vertex has been updated
    			// break if it has been updated
    			Vertex next = null;
    			for(Vertex v : this.graph.keySet()){
    				if(v.equals(adjacentVertex)){
    					next = v;
    					break;
    				}
    			}
    			if(!next.known) {
        			int newCost = current.cost + edgeCost(current, adjacentVertex);
        			if(newCost < next.cost){
        				pq.remove(next);
        				next.cost = newCost;
        				next.parent = current;
        				pq.add(next);
        				if(next.equals(b)){
        					b.parent = next.parent;
        					b.cost = next.cost;
        				}
        			}
        		}
    		}
    	}
    		
    	if(b.parent == null){
    		return null;
    	}
    	Stack<Vertex> s = new Stack<Vertex>();
    	Vertex temp = b;
    	while(temp.parent != null){
    		s.push(temp.parent);
    		temp = temp.parent;
    	}
    	while(!s.isEmpty()){
    		vertices.add(s.pop());
    	}
    	vertices.add(b);
    	   	
    	return new Path(vertices, b.cost);
    }
}