
package acre.sbml;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class ModelNode
implements Cloneable,
Serializable {
    static int idCounter;
    protected String nodeID;
    protected Model model;
    private HashMap<String, LinkedList<String>> connections = new HashMap();

    public ModelNode(Model model) {
        this.model = model;
        this.nodeID = "n" + ++idCounter + ": " + model.getAutoID();
        for (String s : model.getInputs()) {
            this.connections.put(s, new LinkedList());
            this.connections.get(s).add("NC");
        }
    }

    public static int GetIDCounter() {
        return idCounter;
    }

    public void removeConnections(ModelNode n) {
        String nodename = n.getIDWOM();
        HashSet<String> outs = new HashSet<String>();
        for (String s : n.model.outputs) {
            outs.add(String.valueOf(nodename) + "." + s);
        }
        for (String input : this.connections.keySet()) {
            for (String out : outs) {
                this.connections.get(input).remove(out);
            }
        }
    }

    public LinkedList<String> getConnections(String input) {
        if (this.connections.containsKey(input) && this.connections.get(input).size() > 0) {
            return this.connections.get(input);
        }
        LinkedList<String> ret = new LinkedList<String>();
        ret.add("NC");
        return ret;
    }

    public void addConnection(String inputName, String output) {
        LinkedList<String> linkedto;
        if (!this.connections.containsKey(inputName)) {
            this.connections.put(inputName, new LinkedList());
        }
        if (!(linkedto = this.connections.get(inputName)).contains(output)) {
            linkedto.add(output);
        }
    }

    public void updateConnections(String inputName, TreePath[] paths) {
        if (this.connections.containsKey(inputName)) {
            this.connections.get(inputName).clear();
        } else {
            this.connections.put(inputName, new LinkedList());
        }
        int i = 0;
        while (i < paths.length) {
            if (paths[i].getPath().length < 3) {
                if (paths[i].getPath().length == 2 && ((DefaultMutableTreeNode)paths[i].getPath()[1]).toString().equals("Allow No Connection")) {
                    this.addConnection(inputName, "NC");
                }
            } else {
                String temp = this.pathToString(paths[i]);
                this.addConnection(inputName, temp);
            }
            ++i;
        }
    }

    private String pathToString(TreePath path) {
        String ret = "";
        Object[] nodes = path.getPath();
        String node = ((DefaultMutableTreeNode)nodes[1]).toString();
        String output = ((DefaultMutableTreeNode)nodes[2]).toString();
        return String.valueOf(node) + "." + output;
    }

    public void addConnection(String inputName, String toNode, String output) {
        this.addConnection(inputName, String.valueOf(toNode) + "." + output);
    }

    public static void SetIDCounter(int n) {
        idCounter = n;
    }

    public Object clone() {
        ModelNode ret = new ModelNode(this.model);
        --idCounter;
        ret.nodeID = this.nodeID;
        for (String s : this.connections.keySet()) {
            LinkedList<String> temp = new LinkedList<String>();
            for (String connection : this.connections.get(s)) {
                temp.add(connection);
            }
            ret.connections.put(s, temp);
        }
        return ret;
    }

    public Model getModel() {
        return this.model;
    }

    public void setModel(Model m) {
        this.model = m;
    }

    public String getID() {
        return this.nodeID;
    }

    public String getIDWOM() {
        return this.nodeID.substring(0, this.nodeID.indexOf(":"));
    }
}

