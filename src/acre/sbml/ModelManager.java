
package acre.sbml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ModelManager
implements Serializable,
Cloneable {
    ArrayList<ModelNode> nodes = new ArrayList();
    ArrayList<Reaction> reactions = new ArrayList();
    ArrayList<Model> modules = new ArrayList();
    HashMap<ModelNode, ArrayList<Reaction>> nodesToReactionsLink = new HashMap();
    HashMap<String, ModelNode> symbolToNodeLink = new HashMap();
    protected static int id = 0;
    protected static String species = "A";

    public Object clone() {
        ModelManager ret = new ModelManager();
        for (ModelNode n : this.nodes) {
            ret.nodes.add((ModelNode)n.clone());
        }
        for (Reaction r : this.reactions) {
            ret.reactions.add(r.clone());
        }
        for (Model m : this.modules) {
            ret.modules.add((Model)m.clone());
        }
        return ret;
    }

    public void print() {
        for (Reaction rec : this.reactions) {
            rec.printReaction();
        }
    }

    public void addModule(Model model) {
        this.modules.add(model);
    }

    public void replaceSpecies(ArrayList<Reaction> nodeReactions, String newSymbol, String oldSymbol) {
        for (Reaction reaction : nodeReactions) {
            reaction.replaceSpecies(newSymbol, oldSymbol);
        }
    }

    public void replaceSpeciesWithSummation(ArrayList<Reaction> nodeReactions, String newSymbol, String oldSymbol) {
        for (Reaction reaction : nodeReactions) {
            reaction.replaceSpeciesWithSummation(newSymbol, oldSymbol);
        }
    }

    public void deleteNode(ModelNode node) {
        int i = 0;
        while (i < this.nodes.size()) {
            if (node.getID().equals(this.nodes.get(i).getID())) {
                this.nodes.remove(i);
                break;
            }
            ++i;
        }
    }

    public ArrayList<ModelNode> deleteAllNodes(String ID) {
        ArrayList<ModelNode> ret = new ArrayList<ModelNode>();
        for (ModelNode n2 : this.nodes) {
            if (!ID.equals(n2.getModel().getAutoID())) continue;
            ret.add(n2);
        }
        for (ModelNode n2 : ret) {
            this.nodes.remove(n2);
        }
        return ret;
    }

    public ArrayList<ModelNode> deleteAllNodes() {
        ArrayList<ModelNode> ret = new ArrayList<ModelNode>();
        ret.addAll(this.nodes);
        this.nodes.clear();
        return ret;
    }

    public ArrayList<String> getOutputsExceptNode(ModelNode node) {
        ArrayList<String> outputs = new ArrayList<String>();
        for (ModelNode modelNode : this.nodes) {
            if (modelNode.getID().equals(node.getID())) continue;
            for (String output : modelNode.model.getOutputs()) {
                outputs.add(String.valueOf(modelNode.getIDWOM()) + "." + output);
            }
        }
        return outputs;
    }

    public void addNode(ModelNode n) {
        this.nodes.add(n);
    }

    public ModelNode createNode(Model model) {
        ModelNode node = new ModelNode(model);
        ArrayList nodeReactions = (ArrayList)model.reactions.clone();
        this.nodes.add(node);
        return node;
    }
}

