
package acre.sbml;

import java.io.File;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SpeciesReference;

public class SBMLLoader {
    public static Model loadModelFromSBML(String sbmlFileName) throws Exception {
        Model model = new Model();
        SBMLDocument document = new SBMLReader().readSBML(sbmlFileName);
        org.sbml.jsbml.Model sbmlModel = document.getModel();
        model.setId(sbmlModel.getId());
        ListOf<org.sbml.jsbml.Reaction> reactionsList = sbmlModel.getListOfReactions();
        for (org.sbml.jsbml.Reaction sbmlReaction : reactionsList) {
            Reaction reaction = SBMLLoader.mapFromSBMLReaction(sbmlReaction);
            model.addReaction(reaction);
        }
        return model;
    }

    public static Model loadModelFromSBML(File sbmlFile) throws Exception {
        Model model = new Model();
        SBMLDocument document = new SBMLReader().readSBML(sbmlFile);
        org.sbml.jsbml.Model sbmlModel = document.getModel();
        model.setId(sbmlModel.getId());
        ListOf<org.sbml.jsbml.Reaction> reactionsList = sbmlModel.getListOfReactions();
        for (org.sbml.jsbml.Reaction sbmlReaction : reactionsList) {
            Reaction reaction = SBMLLoader.mapFromSBMLReaction(sbmlReaction);
            model.addReaction(reaction);
            if (!sbmlReaction.isReversible()) continue;
            org.sbml.jsbml.Reaction sbmlReactionRev = sbmlReaction.clone();
            sbmlReactionRev.getListOfReactants().clear();
            sbmlReactionRev.getListOfReactants().addAll(sbmlReaction.getListOfProducts());
            sbmlReactionRev.getListOfProducts().clear();
            sbmlReactionRev.getListOfProducts().addAll(sbmlReaction.getListOfReactants());
            reaction = SBMLLoader.mapFromSBMLReaction(sbmlReactionRev);
            model.addReaction(reaction);
        }
        return model;
    }

    public static Reaction mapFromSBMLReaction(org.sbml.jsbml.Reaction sbmlReaction) {
        Species species;
        Reaction reaction = new Reaction();
        reaction.setId(sbmlReaction.getId());
        ListOf<SpeciesReference> reactants = sbmlReaction.getListOfReactants();
        ListOf<SpeciesReference> products = sbmlReaction.getListOfProducts();
        for (SpeciesReference ref2 : reactants) {
            species = new Species(ref2.getSpecies(), (int)ref2.getStoichiometry());
            reaction.addReactant(species);
        }
        for (SpeciesReference ref2 : products) {
            species = new Species(ref2.getSpecies(), (int)ref2.getStoichiometry());
            reaction.addProduct(species);
        }
        return reaction;
    }


}

