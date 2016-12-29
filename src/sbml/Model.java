
package sbml;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import sbml.Reaction;
import sbml.Species;

public class Model
implements Cloneable,
Serializable {
    protected String modelId;
    protected String moduleAutoID = "M" + ++autoIDCounter;
    private static int autoIDCounter;
    protected ArrayList<Reaction> reactions = new ArrayList();
    protected HashSet<String> speciesSet = new HashSet();
    protected HashSet<String> inputs = new HashSet();
    protected HashSet<String> outputs = new HashSet();

    public ArrayList<Reaction> getReactions() {
        return this.reactions;
    }

    public static int GetIDCounter() {
        return autoIDCounter;
    }

    public static void SetIDCounter(int n) {
        autoIDCounter = n;
    }

    public Object clone() {
        try {
            Model ret = new Model();
            --autoIDCounter;
            ret.modelId = this.modelId;
            ret.moduleAutoID = this.moduleAutoID;
            for (Reaction r : this.reactions) {
                ret.reactions.add(r.clone());
            }
            ret.speciesSet.addAll(this.speciesSet);
            ret.inputs.addAll(this.inputs);
            ret.outputs.addAll(this.outputs);
            return ret;
        }
        catch (Exception e) {
            return null;
        }
    }

    public void setId(String id) {
        this.modelId = id;
    }

    public void setAutoID(String id) {
        this.moduleAutoID = id;
    }

    public String getAutoID() {
        return this.moduleAutoID;
    }

    public HashSet<String> getSpeciesSet() {
        return this.speciesSet;
    }

    public HashSet<String> getInputs() {
        return this.inputs;
    }

    public HashSet<String> getOutputs() {
        return this.outputs;
    }

    public void setInputs(String[] inputsArray) {
        String[] arrstring = inputsArray;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String i = arrstring[n2];
            this.inputs.add(i);
            ++n2;
        }
    }

    public void setOutputs(String[] outputsArray) {
        String[] arrstring = outputsArray;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String i = arrstring[n2];
            this.outputs.add(i);
            ++n2;
        }
    }

    public String getModelID() {
        return this.modelId;
    }

    protected void addReaction(Reaction reaction) {
        this.reactions.add(reaction);
        this.speciesSet.addAll(reaction.speciesSet);
    }

    protected void addInput(String speciesSymbol) {
        this.inputs.add(speciesSymbol);
    }

    protected void addOutput(String speciesSymbol) {
        this.outputs.add(speciesSymbol);
    }

    public void printModelReactions() {
        for (Reaction rec : this.reactions) {
            rec.printReaction();
        }
    }

    public String getPrintedReactions() {
        String reactionText = "";
        for (Reaction rec : this.reactions) {
            reactionText = String.valueOf(reactionText) + rec.printReaction() + ";\n";
        }
        return reactionText;
    }

    public void printModel() {
        System.out.println("---------------------------------------------------");
        System.out.println("Model ID : " + this.modelId);
        System.out.println("Reactions : ");
        System.out.println("*******************************");
        for (Reaction reaction : this.reactions) {
            System.out.print("Reaction ID: " + reaction.id);
            System.out.print(" {Reactants: ");
            System.out.print(reaction.reactants);
            System.out.print(", Products: ");
            System.out.println(reaction.products + "}");
        }
        System.out.println("*******************************");
        System.out.println("---------------------------------------------------");
    }
}

