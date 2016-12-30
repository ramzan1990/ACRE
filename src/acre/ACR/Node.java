
package acre.ACR;

import java.util.Hashtable;

public class Node {
    protected static int nodesID;
    protected int id;
    protected int index;
    protected int lowLink;
    protected boolean visited;
    protected Complex complex;

    public Node(Complex complex) {
        this.id = ++nodesID;
        this.index = -1;
        this.lowLink = -1;
        this.visited = false;
        this.complex = complex;
    }

    public Node() {
        this.id = ++nodesID;
        this.index = -1;
        this.lowLink = -1;
        this.visited = false;
    }

    public Node(int forcedID) {
        this.id = forcedID;
        this.index = -1;
        this.lowLink = -1;
        this.visited = false;
    }

    public String toString() {
        return this.complex.toString();
    }

    public boolean hasSingleSpeciesDifference(Node anotherNode, Hashtable<String, Integer> speciesMatrix) {
        int id;
        double[] differenceVector = new double[speciesMatrix.size()];
        for (Species toSpecies : this.complex.speciesList) {
            id = speciesMatrix.get(toSpecies.symbol);
            differenceVector[id] = toSpecies.stochemitry;
        }
        for (Species fromSpecies : anotherNode.complex.speciesList) {
            id = speciesMatrix.get(fromSpecies.symbol);
            double val = differenceVector[id];
            differenceVector[id] = val - (double)fromSpecies.stochemitry;
        }
        int countDifferences = 0;
        int i = 0;
        while (i < differenceVector.length) {
            if (differenceVector[i] != 0.0) {
                ++countDifferences;
            }
            ++i;
        }
        if (countDifferences == 1) {
            return true;
        }
        return false;
    }

    public void set() {
        this.index = -1;
        this.lowLink = -1;
        this.visited = false;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.complex == null ? 0 : this.complex.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Node)) {
            return false;
        }
        Node other = (Node)obj;
        if (this.complex == null ? other.complex != null : !this.complex.equals(other.complex)) {
            return false;
        }
        return true;
    }
}

