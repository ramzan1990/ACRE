
package ACR;

import ACR.BFS;
import ACR.Complex;
import ACR.Edge;
import ACR.Node;
import ACR.NodesDifference;
import ACR.Species;
import ACR.TarjanAlgorithm;
import Jama.CholeskyDecomposition;
import Jama.Matrix;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.PrintStream;

public class Graph {
    protected ArrayList<Node> nodes = new ArrayList();
    public ArrayList<NodesDifference> differentNodesPair = new ArrayList();
    protected Hashtable<Integer, Node> nodesTable = new Hashtable();
    protected ArrayList<Edge> edges = new ArrayList();
    protected Hashtable<Integer, ArrayList<Node>> adjacencyList = new Hashtable();
    protected ArrayList<Node> terminalNodes = new ArrayList();
    protected ArrayList<Node> nonTerminalNodes = new ArrayList();
    protected ArrayList<ArrayList<Node>> linkageClasses = new ArrayList();
    protected ArrayList<ArrayList<Node>> strongLinkageClasses = new ArrayList();
    protected ArrayList<ArrayList<Node>> nonTerminalSCC = new ArrayList();
    protected ArrayList<ArrayList<Node>> terminalSCC = new ArrayList();
    public ArrayList<Species> specieswithACR;
    protected Hashtable<String, Integer> speciesMatrixBySpecies = new Hashtable();
    protected Hashtable<Integer, String> speciesMatrixByIndex = new Hashtable();
    private int deficiency;

    private static final boolean DEBUG = false;

    public void computeDeficiency() {
        int n = this.computeRank();
        this.findLinkageClasses();
        this.findStrongLinkageClassesRelatedInfo();
        this.findNodesDifferInSingleSpecies();
        this.deficiency = this.nodes.size() - this.linkageClasses.size() - n;
    }

    public boolean isACR() {
        try {
            if( DEBUG ) {
                System.out.println("aaa");
                printInfo( System.out );
            }
            if (this.deficiency == 1 && this.differentNodesPair.size() > 0) {
                return true;
            }
            return false;
        }
        catch (Exception var1_1) {
            return false;
        }
    }

    private void printInfo( PrintStream out ) {

        int rank = this.computeRank();
        out.println();
        out.println("############################");
        out.println("#### Summary of Results ####");
        out.println("############################");

        out.println();
        out.println("The linkage classes of the network are:");
        out.println();

        int i = 1;
        for (ArrayList<Node> linkageClass : this.linkageClasses) {
            out.print("" + i + ":   ");

            for (Node lClass : linkageClass) {
                out.print(lClass + "   ");
            }

            out.println();
            i++;
        }

        out.println();
        out.println("The number of nodes is " + this.nodes.size());
        out.println();

        out.println("The number of linkage classes is " + this.linkageClasses.size());
        out.println();

        out.println("The rank of the network is " + rank);
        out.println();

        out.println("The deficiency of the network is " + (this.nodes.size() - this.linkageClasses.size() - rank));
        out.println();

        out.println("The *terminal* strong-linkage classes are:");
        out.println();

        i = 1;
        for (ArrayList<Node> linkageClass : this.terminalSCC) {
            out.print("" + i + ":   ");

            for (Node lClass : linkageClass) {
                out.print(lClass + "   ");
            }

            out.println();
            i++;
        }

        out.println();
        out.println("The *non-terminal* strong-linkage classes are:");
        out.println();

        i = 1;
        for (ArrayList<Node> linkageClass : this.nonTerminalSCC) {
            out.print("" + i + ":   ");

            for (Node lClass : linkageClass) {
                out.print(lClass + "   ");
            }

            out.println();
            i++;
        }

        out.println();
        out.println("The pairs of non-terminal nodes that differ only in a species are:");
        out.println();

        i = 1;

        for (NodesDifference nodePair : this.differentNodesPair) {
            out.print("" + i + ":   ");
            out.print(nodePair.node1 + "   " + nodePair.node2);

            out.println();
            i++;
        }

        i = 1;
        out.println();
        out.println("The species that are different are:");
        out.println();
        if (this.differentNodesPair.size() > 0) {
            for (NodesDifference nodePair : this.differentNodesPair) {
                out.print("" + i + ":   ");
                out.print(nodePair.species);

                out.println();
                i++;
            }
        }
    }

    public boolean dontHaveACR() {
        return this.deficiency == 0;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
        this.nodesTable.put(node.id, node);
        this.adjacencyList.put(node.id, new ArrayList());
    }

    public boolean containsNode(Node node) {
        return this.nodes.contains(node);
    }

    public boolean containsEdge(Edge edge) {
        return this.edges.contains(edge);
    }

    public Node getNode(int n) {
        return this.nodesTable.get(n);
    }

    public ArrayList<Node> getAdjacentNodes(Node node) {
        return this.adjacencyList.get(node.id);
    }

    public void createEdge(Edge edge) {
        this.edges.add(edge);
        Node node = this.getNode(edge.toNodeID);
        ArrayList<Node> arrayList = this.adjacencyList.get(edge.fromNodeID);
        arrayList.add(node);
    }
    public void findLinkageClasses() {

        this.linkageClasses = LinkageClassFinder.find(this);
        this.resetSearching();
    }

    public void findStrongLinkageClassesRelatedInfo() {
        this.findStronglyLinkageClasses();
        this.findTerminalAndTerminalStronglyLinkageClasses();
    }

    private void findStronglyLinkageClasses() {
        TarjanAlgorithm tarjanAlgorithm = new TarjanAlgorithm();
        tarjanAlgorithm.findSCC(this);
        this.resetSearching();
        this.strongLinkageClasses = tarjanAlgorithm.scc;
    }

    public void findTerminalAndTerminalStronglyLinkageClasses() {
        this.terminalSCC.clear();
        this.nonTerminalSCC.clear();
        this.terminalNodes.clear();
        this.nonTerminalNodes.clear();
        for (ArrayList<Node> arrayList : this.strongLinkageClasses) {
            if (this.isTerminalScc(arrayList)) {
                this.terminalSCC.add(arrayList);
                this.terminalNodes.addAll(arrayList);
                continue;
            }
            this.nonTerminalSCC.add(arrayList);
            this.nonTerminalNodes.addAll(arrayList);
        }
    }

    public boolean isTerminalScc(ArrayList<Node> arrayList) {
        boolean bl = true;
        block0 : for (Node node : arrayList) {
            ArrayList<Node> arrayList2 = this.adjacencyList.get(node.id);
            for (Node node2 : arrayList2) {
                if (arrayList.contains(node2)) continue;
                bl = false;
                continue block0;
            }
        }
        return bl;
    }

    public void findNodesDifferInSingleSpecies() {
        ArrayList<NodesDifference> arrayList = new ArrayList<NodesDifference>();
        ArrayList<Species> arrayList2 = null;
        for (int i = 0; i < this.nonTerminalNodes.size() - 1; ++i) {
            for (int j = i + 1; j < this.nonTerminalNodes.size(); ++j) {
                arrayList2 = this.findDifferentSpeciesBetweenNodes(this.nonTerminalNodes.get(i), this.nonTerminalNodes.get(j));
                if (arrayList2.size() != 1) continue;
                NodesDifference nodesDifference = new NodesDifference(this.nonTerminalNodes.get(i), this.nonTerminalNodes.get(j), arrayList2.get(0));
                arrayList.add(nodesDifference);
            }
        }
        this.differentNodesPair = arrayList;
        this.addnodes(arrayList);
    }

    private void addnodes(ArrayList<NodesDifference> arrayList) {
        HashSet<String> hashSet = new HashSet<String>();
        this.specieswithACR = new ArrayList();
        for (NodesDifference nodesDifference : arrayList) {
            if (hashSet.contains(nodesDifference.species.toString())) continue;
            hashSet.add(nodesDifference.species.toString());
            this.specieswithACR.add(nodesDifference.species);
        }
    }

    private ArrayList<Species> findDifferentSpeciesBetweenNodes(Node node, Node node2) {
        int n;
        ArrayList<Species> arrayList = new ArrayList<Species>();
        double[] arrd = new double[this.speciesMatrixBySpecies.size()];
        for (Species species2 : node.complex.speciesList) {
            n = this.speciesMatrixBySpecies.get(species2.symbol);
            arrd[n] = species2.stochemitry;
        }
        for (Species species2 : node2.complex.speciesList) {
            n = this.speciesMatrixBySpecies.get(species2.symbol);
            double d = arrd[n];
            arrd[n] = d - (double)species2.stochemitry;
        }
        for (int i = 0; i < arrd.length; ++i) {
            if (arrd[i] == 0.0) continue;
            arrayList.add(new Species(this.speciesMatrixByIndex.get(i)));
        }
        return arrayList;
    }

    public int computeRank() {
        double[][] arrd = this.constructStiochemetricMatrix();
        Matrix matrix = new Matrix(arrd);
        return matrix.rank();
    }

    public boolean isSPD() {
        double[][] arrd = this.constructStiochemetricMatrix();
        Matrix matrix = new Matrix(arrd);
        return matrix.chol().isSPD();
    }

    public double[][] constructStiochemetricMatrix() {
        double[][] arrd = new double[this.speciesMatrixBySpecies.size()][this.edges.size()];
        for (int i = 0; i < this.edges.size(); ++i) {
            int n;
            Edge edge = this.edges.get(i);
            Complex complex = this.getNode((int)edge.fromNodeID).complex;
            Complex complex2 = this.getNode((int)edge.toNodeID).complex;
            boolean bl = complex.isZer() || complex2.isZer();
            boolean bl2 = bl;
            if (bl) continue;
            for (Species species2 : complex2.speciesList) {
                n = this.speciesMatrixBySpecies.get(species2.symbol);
                arrd[n][i] = species2.stochemitry;
            }
            for (Species species2 : complex.speciesList) {
                n = this.speciesMatrixBySpecies.get(species2.symbol);
                double d = arrd[n][i];
                arrd[n][i] = d - (double)species2.stochemitry;
            }
        }
        return arrd;
    }

    public void resetSearching() {
        for (Node node : this.nodes) {
            node.visited = false;
            node.index = -1;
            node.lowLink = -1;
        }
    }
}

