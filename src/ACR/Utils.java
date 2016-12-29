
package ACR;

import ACR.Species;

public class Utils {
    public static Species getStiochemtricSpecies(String mixedStochemitryAndSymbol) {
        Species species = null;
        int index = Utils.findSymbolIndex(mixedStochemitryAndSymbol);
        int stochemitry = index == 0 ? 1 : Integer.parseInt(mixedStochemitryAndSymbol.substring(0, index));
        String symbol = mixedStochemitryAndSymbol.substring(index);
        species = new Species(symbol, stochemitry);
        return species;
    }

    private static int findSymbolIndex(String mixed) {
        int index = 0;
        int i = 0;
        while (i < mixed.length()) {
            if (!Character.isDigit(mixed.charAt(i))) break;
            ++index;
            ++i;
        }
        return index;
    }
}

