package sbml;

import ACR.ACRAnalyzer;
import ACR.ACRDecider;
import ACR.Graph;
import ACR.Species;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLWriter;
import ui.CSVNetwork;
import ui.ModuleNode;
import ui.NetworkFromGraph;
import ui.WorkSpace;
import ui.externalFrames.EnumerationFrame;

public class NetEnum
{
    private WorkSpace workSpace;
    private EnumerationFrame f;
    private ArrayList<String> inputs;
    private ArrayList<String> singletons;
    HashMap<String, LinkedList<String>> Domains;
    private String[] Assignments;
    private String[] singletonAssignments;
    private int enums = 0;
    private int ACRcount = 0;
    public StringBuffer report = new StringBuffer();

    public NetEnum(WorkSpace paramWorkSpace, EnumerationFrame paramEnumerationFrame)
    {
        this.workSpace = paramWorkSpace;
        this.f = paramEnumerationFrame;
        constructArray();
        isolateSingletons();
        prioritizeNNC();
        prioritizeDomains();
        preAssign();
        String str = preCheck();
        if (!str.equals("pass"))
        {
            this.report.append(str);
        }
        else
        {
            long l = System.currentTimeMillis();
            System.out.println("l a pc = "+this.Assignments.length);
            EnumerateRecursive(0);
            l = System.currentTimeMillis() - l;
            double d = l * 1.0D / 1000.0D;
            if (paramEnumerationFrame.NumberOfEnumerations.isSelected()) {
                this.report.append("Total Number of Networks Enumerated: " + this.enums + "\n");
            }
            this.report.append("Number of Networks with ACR: " + this.ACRcount + "\n");
            if (paramEnumerationFrame.RunTime.isSelected()) {
                this.report.append("Total Time: " + d + " seconds\n");
            }
        }
    }

    private String preCheck()
    {
        int i = 0;
        int j = 0;
        int k = 0;
        int m = 0;
        int n = 0;
        while (n < this.singletons.size())
        {
            if (!this.singletonAssignments[n].equals("NC")) {
                k++;
            }
            n++;
        }
        for (String str : this.inputs) {
            if (!((LinkedList)this.Domains.get(str)).contains("NC")) {
                m++;
            }
        }
        i = k + m;
        j = k + this.inputs.size();
        if (i > this.workSpace.maxEdges) {
            return "The number of inputs that has to be assigned to and output exceed the maximum allowed number of connections";
        }
        if (j < this.workSpace.minEdges) {
            return "The minimum number of connections cannot be achieved with these constraints";
        }
        return "pass";
    }

    private void constructArray()
    {
        this.inputs = new ArrayList();
        this.Domains = new HashMap();
        for (Iterator localIterator1 = this.workSpace.nodes.iterator(); localIterator1.hasNext();)
        {
            ModuleNode localModuleNode = (ModuleNode)localIterator1.next();
            for (String str : localModuleNode.modelNode.model.inputs)
            {
                System.out.println(str);
                this.inputs.add(String.valueOf(localModuleNode.modelNode.getIDWOM()) + "." + str);
                this.Domains.put(String.valueOf(localModuleNode.modelNode.getIDWOM()) + "." + str, localModuleNode.modelNode.getConnections(str));
            }
        }
    }

    private boolean hasNC(LinkedList<String> paramLinkedList)
    {
        return paramLinkedList.contains("NC");
    }

    private void preAssign()
    {
        String[] arrayOfString = new String[this.inputs.size()];
        this.Assignments = new String[this.inputs.size()];
        int i = arrayOfString.length;
        int j = 0;
        while (j < i)
        {
            String str = arrayOfString[j];
            str = "NA";
            j++;
        }
        this.singletonAssignments = new String[this.singletons.size()];
        int k = 0;
        while (k < this.singletons.size())
        {
            this.singletonAssignments[k] = ((String)((LinkedList)this.Domains.get(this.singletons.get(k))).getFirst());
            k++;
        }
    }

    private void isolateSingletons()
    {
        this.singletons = new ArrayList();
        ArrayList localArrayList = new ArrayList();
        for (String str : this.inputs) {
            if (((LinkedList)this.Domains.get(str)).size() == 1) {
                this.singletons.add(str);
            } else {
                localArrayList.add(str);
            }
        }
        this.inputs = localArrayList;
    }

    private void prioritizeDomains()
    {
        for (String str : this.inputs)
        {
            LinkedList localLinkedList = (LinkedList)this.Domains.get(str);
            PrioritizeNC(localLinkedList);
            this.Domains.put(str, localLinkedList);
        }
    }

    private void prioritizeNNC()
    {
        int i = 0;
        int j = 0;
        while (j < this.inputs.size())
        {
            if (!hasNC((LinkedList)this.Domains.get(this.inputs.get(j))))
            {
                String str = (String)this.inputs.get(i);
                this.inputs.set(i, this.inputs.get(j));
                this.inputs.set(j, str);
            }
            j++;
        }
    }

    private void PrioritizeNC(LinkedList<String> paramLinkedList)
    {
        if (paramLinkedList.contains("NC"))
        {
            int i = paramLinkedList.indexOf("NC");
            paramLinkedList.set(i, paramLinkedList.getLast());
            paramLinkedList.set(paramLinkedList.size() - 1, "NC");
        }
    }

    public void EnumerateIterative()
    {
        int[] arrayOfInt = new int[this.Assignments.length];
        int i = 0;
    }

    public void EnumerateRecursive(int paramInt)
    {
        if (paramInt == this.Assignments.length)
        {
            if (this.workSpace.minEdges <= 0 ) {
                CheckACR();
            }
            else if(this.inputs.size() == 0){
                CheckACR();
            }
            return;
        }
        LinkedList<String> localLinkedList = (LinkedList)this.Domains.get(this.inputs.get(paramInt));
        for (String str : localLinkedList) {
            if (!str.equals("NC"))
            {
                if (this.workSpace.maxEdges >= 1)
                {
                    this.workSpace.maxEdges -= 1;
                    this.workSpace.minEdges -= 1;
                    this.Assignments[paramInt] = str;
                    EnumerateRecursive(paramInt + 1);
                    this.workSpace.maxEdges += 1;
                    this.workSpace.minEdges += 1;
                }
            }
            else
            {
                this.Assignments[paramInt] = str;
                EnumerateRecursive(paramInt + 1);
            }
        }
    }

    private void CheckACR()
    {
        this.enums += 1;
        NetworkFromGraph localNetworkFromGraph = new NetworkFromGraph(this.workSpace);
        localNetworkFromGraph.connectAll((String[])this.inputs.toArray(new String[0]), this.Assignments);
        localNetworkFromGraph.connectAll((String[])this.singletons.toArray(new String[0]), this.singletonAssignments);
        localNetworkFromGraph.removeRedundant();
        ACRcount += ACRDecider.decide(localNetworkFromGraph, report, f, this.inputs, this.Assignments, this.singletons, this.singletonAssignments);

    }
}

