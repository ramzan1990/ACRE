
package ACR;

public class Edge {
    protected int fromNodeID;
    protected int toNodeID;

    public Edge(int fromNodeID, int toNodeID) {
        this.fromNodeID = fromNodeID;
        this.toNodeID = toNodeID;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.fromNodeID;
        result = 31 * result + this.toNodeID;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Edge)) {
            return false;
        }
        Edge other = (Edge)obj;
        if (this.fromNodeID != other.fromNodeID) {
            return false;
        }
        if (this.toNodeID != other.toNodeID) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "(" + this.fromNodeID + " , " + this.toNodeID + ")";
    }
}

