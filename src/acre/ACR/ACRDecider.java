package acre.ACR;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLWriter;
import acre.ui.CSVNetwork;
import acre.ui.NetworkFromGraph;
import acre.ui.externalFrames.EnumerationFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ACRDecider {

    private static int printedNetworks = 0;

    public static int decide(NetworkFromGraph localNetworkFromGraph, StringBuffer report, EnumerationFrame f, ArrayList<String> inputs, String[] Assignments, ArrayList<String> singletons, String[] singletonAssignments) {
        int ACRcount = 0;
        CSVNetwork localCSVNetwork = new CSVNetwork(localNetworkFromGraph);
        String str = localCSVNetwork.getCSV();
        boolean bool = false;
        Graph localGraph = null;
        try {
            localGraph = ACRAnalyzer.analyze(str);
        } catch (Exception localException1) {
            System.out.println(localException1.getMessage());
        }
        Object localObject1;
        Object localObject2;
        if (f.restrictACR.isSelected()) {
            bool = false;
            localObject1 = (String) f.speciesDropDown.getSelectedItem();
            for (localObject2 = localGraph.specieswithACR.iterator(); ((Iterator) localObject2).hasNext(); ) {
                Species localSpecies1 = (Species) ((Iterator) localObject2).next();
                if (localSpecies1.toString().equals(localObject1)) {
                    bool = true;
                }
                if ((localNetworkFromGraph.SpeciesMapping.containsKey(localSpecies1.toString())) && (((String) localNetworkFromGraph.SpeciesMapping.get(localSpecies1.toString())).equals(localObject1))) {
                    bool = true;
                }
            }
        } else {
            bool = localGraph.isACR();
        }
        if (bool) {
            ACRcount += 1;
            if (f.saveSBML.isSelected()) {
                localObject1 = localNetworkFromGraph.toSBML();
                localObject2 = new SBMLWriter();
                try {
                    ((SBMLWriter) localObject2).write((SBMLDocument) localObject1, String.valueOf(f.SBMLfolder.getText()) + "Network" + ACRcount + ".xml");
                } catch (Exception localException2) {
                    JOptionPane.showMessageDialog(null, localException2.getMessage(), "Error", 0);
                }
            }
        }
        if (((bool) && (f.EnumertaionswithACR.isSelected())) || ((!bool) && (f.EnumerationswithoutACR.isSelected()))) {
            printedNetworks += 1;
            report.append("Network " + printedNetworks + ":\n");
            int i = 0;
            while (i < singletonAssignments.length) {
                report.append(String.valueOf(singletons.get(i)) + ": " + singletonAssignments[i] + ", ");
                i++;
            }
            i = 0;
            while (i < Assignments.length) {
                report.append(String.valueOf(inputs.get(i)) + ": " + Assignments[i] + ", ");
                i++;
            }
            report.append("\n");
            if ((bool) && (f.EnumertaionswithACR.isSelected())) {
                if (f.ACRNetworks.isSelected()) {
                    report.append(localNetworkFromGraph.toString());
                }
                if (f.ACRspecies.isSelected()) {
                    report.append("Has ACR in the following species:\n");
                    i = 1;
                    for (localObject2 = localGraph.specieswithACR.iterator(); ((Iterator) localObject2).hasNext(); ) {
                        Species localSpecies2 = (Species) ((Iterator) localObject2).next();
                        report.append("" + i + ":  ");
                        if (localNetworkFromGraph.SpeciesMapping.containsKey(localSpecies2.toString())) {
                            report.append(String.valueOf(localNetworkFromGraph.SpeciesMapping.get(localSpecies2.toString())) + ", replaced by ");
                        }
                        report.append(localSpecies2 + "\n");
                        i++;
                    }
                } else {
                    report.append("Has ACR\n");
                }
            }
            if ((!bool) && (f.EnumerationswithoutACR.isSelected())) {
                if (localGraph.dontHaveACR()) {
                    report.append("Doesn't have ACR\n");
                } else {
                    report.append("Can't tell\n");
                }
            }
            report.append("------------------------\n");
        }
        return ACRcount;
    }
}
