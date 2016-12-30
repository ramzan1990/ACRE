
package acre.ACR;

import java.util.ArrayList;
import java.util.Stack;

public class TarjanAlgorithm {
    protected int index;
    protected Stack<Node> stack = new Stack();
    protected ArrayList<ArrayList<Node>> scc = new ArrayList();

    public void findSCC(Graph graph) {
        this.index = 0;
        ArrayList<Node> nodes = graph.nodes;
        for (Node node : nodes) {
            if (node.index != -1) continue;
            this.findSCCFromRootNode(graph, node);
        }
    }

    public void findSCCFromRootNode(Graph graph, Node node) {
        node.index = this.index;
        node.lowLink = this.index++;
        this.stack.push(node);
        ArrayList<Node> adjacents = graph.getAdjacentNodes(node);
        for (Node toNode : adjacents) {
            if (toNode.index == -1) {
                this.findSCCFromRootNode(graph, toNode);
                node.lowLink = Math.min(node.lowLink, toNode.lowLink);
                continue;
            }
            if (!this.stack.contains(toNode)) continue;
            node.lowLink = Math.min(node.lowLink, toNode.index);
        }
        if (node.index == node.lowLink) {
            Node adjacentNode = null;
            ArrayList<Node> sccList = new ArrayList<Node>();
            do {
                adjacentNode = this.stack.pop();
                sccList.add(adjacentNode);
            } while (node != adjacentNode);
            this.scc.add(sccList);
        }
    }
}

