package acre.ui.externalFrames;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import acre.sbml.Reaction;
import acre.sbml.Species;
import acre.ui.ModuleNode;
import acre.ui.WorkSpace;
import java.awt.*;
import javax.swing.*;

public class EnumerationFrame
        extends JFrame {
    WorkSpace s;
    public JCheckBox NumberOfEnumerations = new JCheckBox("Number of Enumerations");
    public JCheckBox NumberofNetworkswithACR = new JCheckBox("Number of Networks with ACR");
    public JCheckBox EnumertaionswithACR = new JCheckBox("Enumertaions with ACR");
    public JCheckBox ACRNetworks = new JCheckBox("For each ACR Enumeration: Networks with ACR");
    public JCheckBox ACRspecies = new JCheckBox("For each ACR Enumeration: Species with ACR");
    public JCheckBox EnumerationswithoutACR = new JCheckBox("Enumerations without ACR");
    public JCheckBox RunTime = new JCheckBox("Run Time");
    public JCheckBox showtoScreen = new JCheckBox("Show to Screen");
    public JCheckBox restrictACR = new JCheckBox("Search for ACR in specific species:");
    public JCheckBox savetofile = new JCheckBox("Save Report to File");
    public JCheckBox saveSBML = new JCheckBox("Save ACR networks to SBML");
    public JTextField fileURI = new JTextField(15);
    public JComboBox<String> speciesDropDown;
    JButton browseFile = new JButton("Browse..");
    public JTextField SBMLfolder = new JTextField(15);
    JButton browseSBMLfolder = new JButton("Browse Folder..");

    public EnumerationFrame(WorkSpace s) {
        this.s = s;
        this.createSpeciesDropDown();
        this.setTitle("Enumeration Options");
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(Box.createRigidArea(new Dimension(0,5)));
        JPanel includesPane = new JPanel(new GridLayout(7, 1));
        includesPane.add(this.RunTime);
        includesPane.add(this.NumberOfEnumerations);
        includesPane.add(this.NumberofNetworkswithACR);
        includesPane.add(this.EnumertaionswithACR);
        includesPane.add(this.ACRNetworks);
        includesPane.add(this.ACRspecies);
        includesPane.add(this.EnumerationswithoutACR);
        includesPane.setBorder(BorderFactory.createTitledBorder("Include in the Report"));
        main.add(includesPane);
        main.add(Box.createRigidArea(new Dimension(0,5)));
        JPanel outputsPane = new JPanel(new GridLayout(5, 1));
        outputsPane.add(this.showtoScreen);
        outputsPane.add(this.restrictACR);
        JPanel speciesDropDownPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        speciesDropDownPane.add(this.speciesDropDown);
        outputsPane.add(speciesDropDownPane);
        outputsPane.add(this.savetofile);
        JPanel filebrowsePane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filebrowsePane.add(this.fileURI);
        filebrowsePane.add(this.browseFile);
        outputsPane.add(filebrowsePane);
        outputsPane.setBorder(BorderFactory.createTitledBorder("Report Output"));
        main.add(outputsPane);
        main.add(Box.createRigidArea(new Dimension(0,5)));
        JPanel SBMLpanel = new JPanel(new GridLayout(2, 1));
        SBMLpanel.add(this.saveSBML);
        JPanel SBMLbrowsePane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        SBMLbrowsePane.add(this.SBMLfolder);
        SBMLbrowsePane.add(this.browseSBMLfolder);
        SBMLpanel.add(SBMLbrowsePane);
        SBMLpanel.setBorder(BorderFactory.createTitledBorder("Save ACR Networks"));
        main.add(SBMLpanel);
        main.add(Box.createRigidArea(new Dimension(0,5)));
        this.getContentPane().add(main);

        this.EnumertaionswithACR.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent arg0) {
                EnumerationFrame.this.updateEnumerationBoxes();
            }
        });
        this.updateEnumerationBoxes();
        this.savetofile.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                EnumerationFrame.this.updateBrowse();
            }
        });
        this.restrictACR.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent arg0) {
                EnumerationFrame.this.speciesDropDown.setEnabled(EnumerationFrame.this.restrictACR.isSelected());
            }
        });
        this.saveSBML.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent arg0) {
                EnumerationFrame.this.updateBrowse();
            }
        });
        this.updateBrowse();
        this.browseFile.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EnumerationFrame.this.browsefile();
            }
        });
        this.browseSBMLfolder.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EnumerationFrame.this.browsefolder();
            }
        });
    }

    private void createSpeciesDropDown() {
        HashSet<String> speciesnames = new HashSet<String>();
        for (ModuleNode n : this.s.nodes) {
            for (Reaction r : n.modelNode.getModel().getReactions()) {
                for (Species spe2 : r.products) {
                    speciesnames.add(String.valueOf(n.modelNode.getIDWOM()) + "." + spe2.getSymbol());
                }
                for (Species spe2 : r.reactants) {
                    speciesnames.add(String.valueOf(n.modelNode.getIDWOM()) + "." + spe2.getSymbol());
                }
            }
        }
        Object[] namesarray = speciesnames.toArray(new String[0]);
        Arrays.sort(namesarray);
        this.speciesDropDown = new JComboBox(namesarray);
        this.speciesDropDown.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXX");
        this.speciesDropDown.setEnabled(false);
    }

    private void updateEnumerationBoxes() {
        this.ACRNetworks.setEnabled(this.EnumertaionswithACR.isSelected());
        this.ACRspecies.setEnabled(this.EnumertaionswithACR.isSelected());
    }

    private void updateBrowse() {
        this.fileURI.setEnabled(this.savetofile.isSelected());
        this.browseFile.setEnabled(this.savetofile.isSelected());
        this.SBMLfolder.setEnabled(this.saveSBML.isSelected());
        this.browseSBMLfolder.setEnabled(this.saveSBML.isSelected());
    }

    private void browsefile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(0);
        int ret = chooser.showSaveDialog(this);
        if (ret == 0) {
            int result;
            if (chooser.getSelectedFile().exists() && (result = JOptionPane.showConfirmDialog(this, "Overwrite existing file?", "Overwrite", 2)) == 2) {
                return;
            }
            this.fileURI.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void browsefolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(1);
        int ret = chooser.showSaveDialog(this);
        if (ret == 0) {
            this.SBMLfolder.setText(String.valueOf(chooser.getSelectedFile().getAbsolutePath()) + File.separator);
        }
    }

}
