
package ACR;

import ACR.Graph;
import ACR.Node;
import java.util.ArrayList;
import java.util.LinkedList;

public class BFS {
    protected ArrayList<ArrayList<Node>> linkageClasses = new ArrayList();

    public void findLinckageClasses(Graph graph) {
        ArrayList<Node> nodes = graph.nodes;
        for (Node node : nodes) {
            if (node.visited) continue;
            this.findLinkageClassFromRootNode(graph, node);
        }
        graph.resetSearching();
    }

    public void findLinkageClassFromRootNode(Graph graph, Node node) {
        ArrayList<Node> adjacents = null;
        ArrayList<Node> linkageClass = new ArrayList<Node>();
        LinkedList<Node> queue = new LinkedList<Node>();
        queue.add(node);
        node.visited = true;
        while (!queue.isEmpty()) {
            Node currentNode = (Node)queue.remove();
            linkageClass.add(currentNode);
            adjacents = graph.getAdjacentNodes(currentNode);
            for (Node thisNode : adjacents) {
                if (thisNode.visited) continue;
                thisNode.visited = true;
                queue.add(thisNode);
            }
        }
        this.linkageClasses.add(linkageClass);
    }
}

