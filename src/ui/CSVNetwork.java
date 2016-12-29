
package ui;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import sbml.Model;
import sbml.Reaction;
import sbml.Species;
import ui.NetworkFromGraph;

public class CSVNetwork {
    boolean[][] network;
    String[] nodes;

    public CSVNetwork(Model n) {
        HashMap networkmap = new HashMap();
        for (Reaction r : n.getReactions()) {
            String tempnodefrom = this.nodeFromSpecies(r.reactants);
            String tempnodeto = this.nodeFromSpecies(r.products);
            if (!networkmap.containsKey(tempnodefrom)) {
                networkmap.put(tempnodefrom, new HashSet());
            }
            if (!networkmap.containsKey(tempnodeto)) {
                networkmap.put(tempnodeto, new HashSet());
            }
            ((HashSet)networkmap.get(tempnodefrom)).add(tempnodeto);
        }
        this.nodes = new String[networkmap.size()];
        this.network = new boolean[networkmap.size()][networkmap.size()];
        int i = 0;
        Iterator iterator = networkmap.keySet().iterator();
        while (iterator.hasNext()) {
            String node;
            this.nodes[i] = node = (String)iterator.next();
            ++i;
        }
        i = 0;
        while (i < this.nodes.length) {
            int j = 0;
            while (j < this.nodes.length) {
                this.network[i][j] = ((HashSet)networkmap.get(this.nodes[i])).contains(this.nodes[j]);
                ++j;
            }
            ++i;
        }
    }

    public CSVNetwork(NetworkFromGraph n) {
        HashMap networkmap = new HashMap();
        for (Reaction r : n.reactions) {
            String tempnodefrom = this.nodeFromSpecies(r.reactants);
            String tempnodeto = this.nodeFromSpecies(r.products);
            if (!networkmap.containsKey(tempnodefrom)) {
                networkmap.put(tempnodefrom, new HashSet());
            }
            if (!networkmap.containsKey(tempnodeto)) {
                networkmap.put(tempnodeto, new HashSet());
            }
            ((HashSet)networkmap.get(tempnodefrom)).add(tempnodeto);
        }
        this.nodes = new String[networkmap.size()];
        this.network = new boolean[networkmap.size()][networkmap.size()];
        int i = 0;
        Iterator iterator = networkmap.keySet().iterator();
        while (iterator.hasNext()) {
            String node;
            this.nodes[i] = node = (String)iterator.next();
            ++i;
        }
        i = 0;
        while (i < this.nodes.length) {
            int j = 0;
            while (j < this.nodes.length) {
                this.network[i][j] = ((HashSet)networkmap.get(this.nodes[i])).contains(this.nodes[j]);
                ++j;
            }
            ++i;
        }
    }

    private String nodeFromSpecies(ArrayList<Species> species) {
        Collections.sort(species);
        String ret = "";
        for (Species s : species) {
            ret = String.valueOf(ret) + s.toString() + "+";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

    public String getCSV() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream outp = new PrintStream(out);
            String[] arrstring = this.nodes;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String s = arrstring[n2];
                outp.print("," + s);
                ++n2;
            }
            outp.println();
            int i = 0;
            while (i < this.nodes.length) {
                outp.print(this.nodes[i]);
                int j = 0;
                while (j < this.nodes.length) {
                    outp.print("," + (this.network[i][j] ? "1" : "0"));
                    ++j;
                }
                outp.println();
                ++i;
            }
            return out.toString();
        }
        catch (Exception e) {
            return null;
        }
    }
}

