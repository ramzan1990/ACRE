
package acre.ACR;

import java.util.ArrayList;
import java.util.Collections;

public class Complex {
    ArrayList<Species> speciesList = new ArrayList();

    public void addSpecies(Species species) {
        this.speciesList.add(species);
    }

    public String toString() {
        String complexText = "";
        int i = 0;
        while (i < this.speciesList.size() - 1) {
            complexText = String.valueOf(complexText) + this.speciesList.get(i) + "+";
            ++i;
        }
        complexText = String.valueOf(complexText) + this.speciesList.get(this.speciesList.size() - 1);
        return complexText;
    }

    public boolean isZer() {
        if (this.speciesList.size() == 1 && this.speciesList.get((int)0).symbol.equalsIgnoreCase("zer")) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.speciesList == null ? 0 : this.speciesList.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Complex other = (Complex)obj;
        if (this.speciesList == null) {
            if (other.speciesList != null) {
                return false;
            }
        } else {
            Collections.sort(this.speciesList);
            Collections.sort(other.speciesList);
            if (!this.speciesList.equals(other.speciesList)) {
                return false;
            }
        }
        return true;
    }
}

