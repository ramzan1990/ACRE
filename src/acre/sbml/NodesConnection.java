
package acre.sbml;

public class NodesConnection {
    protected ModelNode fromNode;
    protected ModelNode toNode;
    protected String outputSpecies;
    protected String inputSpecies;

    public NodesConnection(ModelNode fromNode, String outputSpecies, ModelNode toNode, String inputSpecies) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.outputSpecies = outputSpecies;
        this.inputSpecies = inputSpecies;
    }

    public ModelNode getFromNode() {
        return this.fromNode;
    }

    public void setFromNode(ModelNode fromNode) {
        this.fromNode = fromNode;
    }

    public ModelNode getToNode() {
        return this.toNode;
    }

    public void setToNode(ModelNode toNode) {
        this.toNode = toNode;
    }

    public String getOutputSpecies() {
        return this.outputSpecies;
    }

    public void setOutputSpecies(String outputSpecies) {
        this.outputSpecies = outputSpecies;
    }

    public String getInputSpecies() {
        return this.inputSpecies;
    }

    public void setInputSpecies(String inputSpecies) {
        this.inputSpecies = inputSpecies;
    }
}

