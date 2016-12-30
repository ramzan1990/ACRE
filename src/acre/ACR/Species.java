
package acre.ACR;

import java.util.ArrayList;
import java.util.Collections;

public class Species
implements Comparable {
    protected int stochemitry;
    protected String symbol;

    public Species(String symbol, int stochemitry) {
        this.symbol = symbol;
        this.stochemitry = stochemitry;
    }

    public Species(String mixedStochemitryAndSymbol) {
        int index = this.findSymbolIndex(mixedStochemitryAndSymbol);
        this.stochemitry = index == 0 ? 1 : Integer.parseInt(mixedStochemitryAndSymbol.substring(0, index));
        this.symbol = mixedStochemitryAndSymbol.substring(index);
    }

    public int compareTo(Object other) {
        Species otherSpecies = (Species)other;
        if (this.symbol.equals(otherSpecies.symbol)) {
            return this.stochemitry - otherSpecies.stochemitry;
        }
        return this.symbol.compareTo(otherSpecies.symbol);
    }

    private int findSymbolIndex(String mixed) {
        int index = 0;
        int i = 0;
        while (i < mixed.length()) {
            if (!Character.isDigit(mixed.charAt(i))) break;
            ++index;
            ++i;
        }
        return index;
    }



    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.stochemitry;
        result = 31 * result + (this.symbol == null ? 0 : this.symbol.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Species)) {
            return false;
        }
        Species other = (Species)obj;
        if (this.stochemitry != other.stochemitry) {
            return false;
        }
        if (this.symbol == null ? other.symbol != null : !this.symbol.equals(other.symbol)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return this.stochemitry == 1 ? this.symbol : String.valueOf(this.stochemitry) + this.symbol;
    }
}

