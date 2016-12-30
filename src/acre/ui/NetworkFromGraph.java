
package acre.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SpeciesReference;
import acre.sbml.Reaction;
import acre.sbml.Species;

public class NetworkFromGraph {
    public ArrayList<Reaction> reactions = new ArrayList();
    public HashMap<String, ArrayList<Species>> species = new HashMap();
    public HashMap<String, String> SpeciesMapping = new HashMap();

    public NetworkFromGraph(WorkSpace s) {
        for (ModuleNode n : s.nodes) {
            String NodeID = n.modelNode.getIDWOM();
            for (Reaction r : n.model.getReactions()) {
                ArrayList<Species> temparray;
                String specTemp;
                Species tempSpecies;
                Reaction temp = new Reaction();
                for (Species spec2 : r.reactants) {
                    specTemp = String.valueOf(NodeID) + "." + spec2.getSymbol();
                    tempSpecies = new Species(specTemp, spec2.getStochemitry());
                    if (this.species.containsKey(specTemp)) {
                        this.species.get(specTemp).add(tempSpecies);
                    } else {
                        temparray = new ArrayList<Species>();
                        temparray.add(tempSpecies);
                        this.species.put(specTemp, temparray);
                    }
                    temp.addReactant(tempSpecies);
                }
                for (Species spec2 : r.products) {
                    specTemp = String.valueOf(NodeID) + "." + spec2.getSymbol();
                    tempSpecies = new Species(specTemp, spec2.getStochemitry());
                    if (this.species.containsKey(specTemp)) {
                        this.species.get(specTemp).add(tempSpecies);
                    } else {
                        temparray = new ArrayList();
                        temparray.add(tempSpecies);
                        this.species.put(specTemp, temparray);
                    }
                    temp.addProduct(tempSpecies);
                }
                this.reactions.add(temp);
            }
        }
    }

    public void removeRedundant() {
        HashMap<String, Boolean> candidredundant = new HashMap<String, Boolean>();
        for (String s3 : this.species.keySet()) {
            candidredundant.put(s3, true);
        }
        for (Reaction r : this.reactions) {
            for (Species s22 : r.reactants) {
                if (!((Boolean)candidredundant.get(s22.getSymbol())).booleanValue() || !this.speciesnotRedundant(s22, r)) continue;
                candidredundant.put(s22.getSymbol(), false);
            }
            for (Species s : r.products) {
                if (!((Boolean)candidredundant.get(s.getSymbol())).booleanValue() || !this.speciesnotRedundant(s, r)) continue;
                candidredundant.put(s.getSymbol(), false);
            }
        }
        for (Map.Entry s : candidredundant.entrySet()) {
            if (!((Boolean)s.getValue()).booleanValue()) continue;
            this.removeSpeciesFromAllReactions((String)s.getKey());
        }
    }

    private void removeSpeciesFromAllReactions(String s) {
        for (Reaction r : this.reactions) {
            r.removeSpecies(s);
        }
    }

    private boolean speciesnotRedundant(Species s, Reaction r) {
        int stockr = 0;
        int stockp = 0;
        for (Species ss2 : r.reactants) {
            if (ss2.getSymbol() != s.getSymbol()) continue;
            stockr = ss2.getStochemitry();
            break;
        }
        for (Species ss2 : r.products) {
            if (ss2.getSymbol() != s.getSymbol()) continue;
            stockp = ss2.getStochemitry();
            break;
        }
        if (stockr == stockp && r.reactants.size() > 1 && r.products.size() > 1) {
            return false;
        }
        return true;
    }

    public SBMLDocument toSBML() {
        SBMLDocument ret = new SBMLDocument(2, 4);
        Model model = ret.createModel();
        Compartment compartment = model.createCompartment();
        compartment.setId("default");
        compartment.setSize(this.species.keySet().size());
        int id = 0;
        for (Reaction r : this.reactions) {
            org.sbml.jsbml.Reaction react = model.createReaction("reaction" + ++id);
            react.setReversible(false);
            for (Species s2 : r.reactants) {
                if (!model.containsSpecies(s2.getSymbol().replaceAll("\\.", ""))) {
                    this.createspec(s2.getSymbol().replaceAll("\\.", ""), model, compartment);
                }
                SpeciesReference reactant = react.createReactant();
                reactant.setSpecies(model.getSpecies(s2.getSymbol().replaceAll("\\.", "")));
                reactant.setStoichiometry(s2.getStochemitry());
            }
            for (Species s2 : r.products) {
                if (!model.containsSpecies(s2.getSymbol().replaceAll("\\.", ""))) {
                    this.createspec(s2.getSymbol().replaceAll("\\.", ""), model, compartment);
                }
                SpeciesReference product = react.createProduct();
                product.setSpecies(model.getSpecies(s2.getSymbol().replaceAll("\\.", "")));
                product.setStoichiometry(s2.getStochemitry());
            }
        }
        return ret;
    }

    private void createspec(String name, Model model, Compartment compartment) {
        org.sbml.jsbml.Species spec = model.createSpecies();
        spec.setId(name.replaceAll("\\.", ""));
        spec.setName(name.replaceAll("\\.", ""));
        spec.setCompartment(compartment);
    }

    public void connect(String input, String output) {
        if (output.equals("NC")) {
            return;
        }
        this.SpeciesMapping.put(output, input);
        for (Species s : this.species.get(input)) {
            s.setSymbol(output);
        }
    }

    public void connectAll(String[] inputs, String[] outputs) {
        int i = 0;
        while (i < inputs.length) {
            this.connect(inputs[i], outputs[i]);
            ++i;
        }
    }

    public String toString() {
        String ret = "";
        for (Reaction r : this.reactions) {
            ret = String.valueOf(ret) + r.printReaction() + "\n";
        }
        return ret;
    }
}

