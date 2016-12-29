 package sbml;

 import java.io.PrintStream;
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.HashSet;






 public class ReactionAfter
   implements Cloneable, Serializable
 {
       protected String id;
       public ArrayList<Species> reactants = new ArrayList();
       public ArrayList<Species> products = new ArrayList();


   protected HashSet<String> speciesSet = new HashSet();

   public boolean replaceSpecies(String newSpecies, String oldSpecies)
   {
     boolean hasReplaced = false;
     if (this.speciesSet.contains(oldSpecies)) {
       for (Species species : this.reactants) {
         if (species.symbol.equals(oldSpecies)) {
           species.symbol = newSpecies;
         }
       }

       for (Species species : this.products) {
         if (species.symbol.equals(oldSpecies)) {
           species.symbol = newSpecies;
         }
       }

       this.speciesSet.remove(oldSpecies);
       this.speciesSet.add(newSpecies);

       hasReplaced = true;
     }

     return hasReplaced;
   }

   public void replaceSpeciesWithSummation(String newInput, String oldInput)
   {
     if (replaceSpecies(newInput, oldInput)) {
       ArrayList<Species> existingReactantSpecies = new ArrayList();
       ArrayList<Species> existingProductsSpecies = new ArrayList();


       for (Species species : this.reactants) {
         if (species.symbol.equals(newInput)) {
           existingReactantSpecies.add(species);
         }
       }
       int stoch;
       if (existingReactantSpecies.size() > 1) {
         Species species = new Species(newInput);
         stoch = 0;

         for (int i = 0; i < existingReactantSpecies.size(); i++) {
           stoch += ((Species)existingReactantSpecies.get(i)).stochemitry;
         }
         species.stochemitry = stoch;

         this.reactants.removeAll(existingReactantSpecies);
         this.reactants.add(species);
       }

       for (Species species : this.products) {
         if (species.symbol.equals(newInput)) {
           existingProductsSpecies.add(species);
         }
       }

       if (existingProductsSpecies.size() > 1) {
         Species species = new Species(newInput);
         stoch = 0;

         for (int i = 0; i < existingProductsSpecies.size(); i++) {
           stoch += ((Species)existingProductsSpecies.get(i)).stochemitry;
         }
         species.stochemitry = stoch;

         this.products.removeAll(existingProductsSpecies);
         this.products.add(species);
       }

       System.err.println(this.reactants);
       System.err.println(this.products);
     }
   }

   public void addReactant(Species species) {
     this.reactants.add(species);
     this.speciesSet.add(species.symbol);
   }

   public void addProduct(Species species) {
     this.products.add(species);
     this.speciesSet.add(species.symbol);
   }

   public ReactionAfter clone() {
     ReactionAfter reaction = new ReactionAfter();
     reaction.id = this.id;

     for (Species species : this.reactants) {
       reaction.addReactant(species.clone());
     }

     for (Species species : this.products) {
       reaction.addProduct(species.clone());
     }

     reaction.speciesSet = new HashSet();
     reaction.speciesSet.addAll(this.speciesSet);
     return reaction;
   }

   public String printReaction() {
     String reactantsText = "";

     for (int i = 0; i < this.reactants.size(); i++) {
       reactantsText = reactantsText + this.reactants.get(i) + " + ";
     }

     if (reactantsText.contains("+")) {
       reactantsText = reactantsText.substring(0, reactantsText.lastIndexOf("+"));
     }
     String productsText = "";

     for (int i = 0; i < this.products.size(); i++) {
       productsText = productsText + this.products.get(i) + " + ";
     }

     if (productsText.contains("+")) {
       productsText = productsText.substring(0, productsText.lastIndexOf("+"));
     }


     return reactantsText + " ---> " + productsText;
   }


   public void setId(String id2)
   {
     this.id = id2;
   }
 }


