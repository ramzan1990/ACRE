
package sbml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import sbml.ModelNode;
import sbml.NodesConnection;

public class ConstraintsManager {
    static int counter = 0;
    HashMap<String, HashSet<String>> nodeToPoolsLink = new HashMap();
    HashMap<String, ArrayList<NodesConnection>> counterToConnectionGroup = new HashMap();

    public void addConnection(ModelNode fromNode, String fromOutput, ModelNode toNode, String toInput, String num) {
        HashSet poolLink = this.nodeToPoolsLink.get(toNode.getID());
        if (poolLink == null) {
            poolLink = new HashSet();
            this.nodeToPoolsLink.put(toNode.getID(), poolLink);
        }
        poolLink.add(num);
        ArrayList conns = this.counterToConnectionGroup.get(num);
        if (conns == null) {
            conns = new ArrayList();
            this.counterToConnectionGroup.put(num, conns);
        }
        NodesConnection connection = new NodesConnection(fromNode, fromOutput, toNode, toInput);
        conns.add(connection);
    }

    public void deleteNode(String nodeID) {
    }

    public String getNewNumber() {
        return String.valueOf(++counter);
    }
}

