/**CSE373 HW4
 * Yumeng Wang
 * Student ID: 1360735
 * 
 * Representation of a graph vertex
 */
public class Vertex implements Comparable<Vertex>{
	private String label;   // label attached to this vertex
	public int cost;
	public boolean known;
	public Vertex parent;
	/**
	 * Construct a new vertex
	 * @param label the label attached to this vertex
	 */
	public Vertex(String label) {
		this(label, Integer.MAX_VALUE, false, null);
	}
	
	/**
	 * Construct a new vertex
	 * @param label the label attached to this vertex
	 * @param cost the cost attached to this vertex
	 * @param known weather or not this vertex has been visited
	 * @param path the vertex to this vertex
	 */
	public Vertex(String label, int cost, boolean known, Vertex path){
		if(label == null)
			throw new IllegalArgumentException("null");
		this.label = label;
		this.known = known;
		this.cost = cost;
		this.parent = path;
	}

	/**
	 * Get a vertex label
	 * @return the label attached to this vertex
	 */
	public String getLabel() {
		return label;
	}
		
	/**
	 * A string representation of this object
	 * @return the label attached to this vertex
	 */
	public String toString() {
		return label;
	}

	//auto-generated: hashes on label
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	//auto-generated: compares labels
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Vertex other = (Vertex) obj;
		if (label == null) {
                    return other.label == null;
		} else {
		    return label.equals(other.label);
		}
	}
	
	public int compareTo(Vertex v){
		return Integer.compare(this.cost, v.cost);
	}

}