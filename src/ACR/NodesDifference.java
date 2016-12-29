
package ACR;

import ACR.Node;
import ACR.Species;

public class NodesDifference {
    protected Node node1;
    protected Node node2;
    public Species species;

    public NodesDifference(Node node1, Node node2, Species species) {
        this.node1 = node1;
        this.node2 = node2;
        this.species = species;
    }
}

