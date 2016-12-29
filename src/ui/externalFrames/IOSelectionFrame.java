
package ui.externalFrames;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import javax.swing.*;
import sbml.Model;

public class IOSelectionFrame
        extends JFrame {
    private JButton inputButton;
    private JButton outputButton;
    private JButton removeButton;
    private JButton removeOutputButton;
    private JButton clearButton;
    private JPanel combBoxPanel;
    private JPanel tablesHandlingPanel;
    private JPanel tablesPanel;
    private JPanel buttonsPanel1;
    private JPanel buttonsPanel2;
    private JPanel namePanel;
    private JScrollPane scrollPaneInput;
    private JScrollPane scrollPaneOutput;
    private JScrollPane areaPanel;
    private JLabel speciesLabel;
    private JTextField moduleName;
    private JComboBox speciesBox;
    private JTable inputTable;
    private JTable outputTable;
    private JTextArea textArea;
    private HashSet<String> speciesSet;
    private String[] inputData;
    private String[] outputData;
    private Object[][] inputTableData;
    private Object[][] outputTableData;

    public IOSelectionFrame(String title, Model model) {
        this(title, model.getSpeciesSet(), model.getPrintedReactions());
    }

    public IOSelectionFrame(String title, HashSet<String> speciesSet, String reactions) {
        super(title);
        Container pane = this.getContentPane();
        this.namePanel = new JPanel(new BorderLayout());
        this.moduleName = new JTextField();
        JLabel mnl = new JLabel("Module Name   ");
        this.namePanel.add(mnl,BorderLayout.WEST);
        this.namePanel.add(this.moduleName,BorderLayout.CENTER);
        this.combBoxPanel = new JPanel();
        this.combBoxPanel.setLayout(new BoxLayout(this.combBoxPanel, BoxLayout.Y_AXIS));
        this.speciesLabel = new JLabel("All Species   ");

        Dimension d = this.speciesLabel.getPreferredSize();
        d.width = mnl.getPreferredSize().width;
        this.speciesLabel.setPreferredSize(d);

        this.speciesSet = speciesSet;
        String[] comboBoxItems = this.buildSpeciesArray(speciesSet);
        this.speciesBox = new JComboBox<String>(comboBoxItems);

        this.speciesBox.setEditable(false);
        JPanel p = new JPanel(new BorderLayout());
        p.add(this.speciesLabel,BorderLayout.WEST);
        p.add(this.speciesBox,BorderLayout.CENTER);
        this.combBoxPanel.add(this.namePanel);
        this.combBoxPanel.add(Box.createRigidArea(new Dimension(0,5)));
        this.combBoxPanel.add(p);
        this.buttonsPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.inputButton = new JButton("Add Input");
        this.inputButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                IOSelectionFrame.this.addInput();
            }
        });
        this.outputButton = new JButton("Add Output");
        this.outputButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                IOSelectionFrame.this.addOutput();
            }
        });
        this.buttonsPanel1.add(this.inputButton);
        this.buttonsPanel1.add(this.outputButton);
        this.buttonsPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.removeButton = new JButton("Remove");
        this.removeButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                IOSelectionFrame.this.removeInput();
            }
        });
        this.removeOutputButton = new JButton("Remove Output");
        this.removeOutputButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                IOSelectionFrame.this.removeOutput();
            }
        });
        this.clearButton = new JButton("Clear All");
        this.clearButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                IOSelectionFrame.this.clear();
            }
        });
        this.buttonsPanel2.add(this.removeButton);
        this.buttonsPanel2.add(this.clearButton);
        this.tablesPanel = new JPanel();
        this.inputTable = new JTable();
        this.inputTable.setSelectionMode(0);
        this.inputTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                IOSelectionFrame.this.selectDeselect(1);
            }
        });
        this.outputTable = new JTable();
        this.outputTable.setSelectionMode(0);
        this.outputTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                IOSelectionFrame.this.selectDeselect(2);
            }
        });
        this.setTables();
        this.scrollPaneInput = new JScrollPane(this.inputTable);
        this.scrollPaneInput.setPreferredSize(new Dimension(200, 200));
        this.scrollPaneOutput = new JScrollPane(this.outputTable);
        this.scrollPaneOutput.setPreferredSize(new Dimension(200, 200));
        this.tablesPanel.add(this.scrollPaneInput);
        this.tablesPanel.add(this.scrollPaneOutput);
        this.tablesHandlingPanel = new JPanel(new BorderLayout());
        this.tablesHandlingPanel.add((Component)this.buttonsPanel1, "North");
        this.tablesHandlingPanel.add((Component)this.tablesPanel, "Center");
        this.tablesHandlingPanel.add((Component)this.buttonsPanel2, "South");
        this.textArea = new JTextArea();
        this.areaPanel = new JScrollPane(this.textArea);
        this.areaPanel.setPreferredSize(new Dimension(200, 200));
        this.textArea.setText(reactions);
        JPanel mpan = new JPanel();
        mpan.setLayout(new BoxLayout(mpan, BoxLayout.Y_AXIS));
        this.combBoxPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 5));
        mpan.add(this.combBoxPanel);
        mpan.add(this.tablesHandlingPanel);
        this.areaPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        mpan.add(this.areaPanel);
        pane.add(mpan, "Center");
    }

    public String getModuleName() {
        return this.moduleName.getText();
    }

    public void selectDeselect(int source) {
        if (source == 1 && this.inputTable.getSelectedRow() > -1) {
            this.outputTable.getSelectionModel().clearSelection();
        }
        if (source == 2 && this.outputTable.getSelectedRow() > -1) {
            this.inputTable.getSelectionModel().clearSelection();
        }
    }

    public void addInput() {
        String selectedSpecies = (String)this.speciesBox.getSelectedItem();
        if (selectedSpecies != null && selectedSpecies.trim().length() > 0) {
            DefaultTableModel model = (DefaultTableModel)this.inputTable.getModel();
            model.insertRow(model.getRowCount(), new Object[]{selectedSpecies});
            this.speciesBox.removeItem(selectedSpecies);
        }
    }

    public void addOutput() {
        String selectedSpecies = (String)this.speciesBox.getSelectedItem();
        if (selectedSpecies != null && selectedSpecies.trim().length() > 0) {
            DefaultTableModel model = (DefaultTableModel)this.outputTable.getModel();
            model.insertRow(model.getRowCount(), new Object[]{selectedSpecies});
            this.speciesBox.removeItem(selectedSpecies);
        }
    }

    public String[] buildSpeciesArray(HashSet<String> speciesSet) {
        String[] comboBoxItems = speciesSet.toArray(new String[0]);
        return comboBoxItems;
    }

    public void removeInput() {
        int index = this.inputTable.getSelectedRow();
        if (index > -1) {
            DefaultTableModel model = (DefaultTableModel)this.inputTable.getModel();
            String species = (String)model.getValueAt(index, 0);
            model.removeRow(index);
            this.speciesBox.addItem(species);
        } else {
            index = this.outputTable.getSelectedRow();
            if (index > -1) {
                DefaultTableModel model = (DefaultTableModel)this.outputTable.getModel();
                String species = (String)model.getValueAt(index, 0);
                model.removeRow(index);
                this.speciesBox.addItem(species);
            }
        }
    }

    public void removeOutput() {
        int index = this.outputTable.getSelectedRow();
        if (index > -1) {
            DefaultTableModel model = (DefaultTableModel)this.outputTable.getModel();
            String species = (String)model.getValueAt(index, 0);
            model.removeRow(index);
            this.speciesBox.addItem(species);
        }
    }

    public void clear() {
        this.setTables();
        this.speciesBox.removeAllItems();
        for (String s : this.speciesSet) {
            this.speciesBox.addItem(s);
        }
    }

    public void done() {
        this.inputData = this.getTableData(this.inputTable);
        this.outputData = this.getTableData(this.outputTable);
    }

    public String[] getInputData() {
        return this.inputData;
    }

    public String[] getOutputData() {
        return this.outputData;
    }

    public String[] getTableData(JTable table) {
        DefaultTableModel dtm = (DefaultTableModel)table.getModel();
        int nRow = dtm.getRowCount();
        int nCol = dtm.getColumnCount();
        String[] tableData = new String[nRow];
        int i = 0;
        while (i < nRow) {
            tableData[i] = (String)dtm.getValueAt(i, 0);
            ++i;
        }
        return tableData;
    }

    public void setTables() {
        DefaultTableModel intputModel = new DefaultTableModel();
        this.inputTableData = new Object[0][0];
        intputModel.setDataVector(this.inputTableData, new String[]{"Inputs"});
        this.inputTable.setModel(intputModel);
        this.outputTableData = new Object[0][0];
        DefaultTableModel outputModel = new DefaultTableModel();
        outputModel.setDataVector(this.outputTableData, new String[]{"Outputs"});
        this.outputTable.setModel(outputModel);
    }

    private static void createAndShowGUI() {
        HashSet<String> ss = new HashSet<String>();
        ss.add("A");
        ss.add("B");
        ss.add("C");
        ss.add("D");
        ss.add("E");
        IOSelectionFrame demo = new IOSelectionFrame("Modules Inputs/Outputs", ss, "A + B --> C\nD + A --> E");
        demo.setDefaultCloseOperation(3);
        Object[] options = new Object[]{"Done", "Cancel"};
        JOptionPane.showOptionDialog(null, demo.getContentPane(), "Model Inputs/Outputs Selection", 0, -1, null, options, options[0]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                IOSelectionFrame.createAndShowGUI();
            }
        });
    }

}

