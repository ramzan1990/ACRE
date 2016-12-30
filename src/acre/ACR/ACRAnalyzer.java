
package acre.ACR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class ACRAnalyzer {
    private static void Reset() {
        Node.nodesID = 0;
    }

    public static Graph analyze(String string) throws IOException {
        ACRAnalyzer.Reset();
        BufferedReader bufferedReader = new BufferedReader(new StringReader(string));
        String string2 = bufferedReader.readLine();
        Graph graph = new Graph();
        Object var4_4 = null;
        Object var5_5 = null;
        Edge edge = null;
        String[] arrstring = string2.split(",");
        int n = 0;
        for (int i = 1; i < arrstring.length; ++i) {
            String[] arrstring2;
            String string3 = arrstring[i].trim();
            Complex complex = new Complex();
            for (String string4 : arrstring2 = string3.split("\\+")) {
                Species species = new Species(string4);
                complex.addSpecies(species);
                if (graph.speciesMatrixBySpecies.get(species.symbol) != null) continue;
                graph.speciesMatrixBySpecies.put(species.symbol, new Integer(n));
                graph.speciesMatrixByIndex.put(new Integer(n), species.symbol);
                ++n;
            }
            Node object2 = new Node(complex);
            graph.addNode(object2);
        }
        string2 = bufferedReader.readLine();
        int n2 = 1;
        while (string2 != null) {
            arrstring = string2.split(",");
            for (int j = 1; j < arrstring.length; ++j) {
                if (Integer.parseInt(arrstring[j]) != 1) continue;
                edge = new Edge(n2, graph.getNode((int)j).id);
                graph.createEdge(edge);
            }
            ++n2;
            string2 = bufferedReader.readLine();
        }
        graph.computeDeficiency();
        return graph;
    }

    public static void main(String[] arrstring) throws IOException {
        ACRAnalyzer.analyze("Hiro2.csv");
    }
}

