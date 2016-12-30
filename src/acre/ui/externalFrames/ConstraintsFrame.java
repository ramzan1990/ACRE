 package acre.ui.externalFrames;

 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Container;
 import java.awt.Dimension;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.util.ArrayList;
 import java.util.HashSet;
 import javax.swing.BorderFactory;
 import javax.swing.JButton;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JOptionPane;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.JTable;
 import javax.swing.JTextArea;
 import javax.swing.JTextField;
 import javax.swing.SwingUtilities;
 import javax.swing.UIManager;
 import javax.swing.UnsupportedLookAndFeelException;
 import javax.swing.table.DefaultTableModel;
 import javax.swing.table.TableModel;








 public class ConstraintsFrame
   extends JFrame
 {
   private JButton clearButton;
   private JPanel nodeIDPanel;
   private JPanel tablesHandlingPanel;
   private JPanel tablesPanel;
   private JPanel buttonsPanel2;
   private JScrollPane scrollPaneInput;
   private JScrollPane scrollPaneOutput;
   private JScrollPane areaPanel;
   private JLabel nodeIDLabel;
   private JTable inputTable;
   private JTable outputTable;
   private JTextArea textArea;
   private String[] inputData;
   private String[] outputData;
   private Object[][] inputTableData;
   private Object[][] outputTableData;
   private JTextField nodeIDField;

   public ConstraintsFrame(String title, HashSet<String> inputsSet, ArrayList<String> outputsSet, String id, String reactions)
   {
     super(title);

     Container pane = getContentPane();

     this.nodeIDPanel = new JPanel();
     this.nodeIDLabel = new JLabel("Node ID2");

     this.nodeIDField = new JTextField(id);
     this.nodeIDField.setEditable(false);
     this.nodeIDField.setPreferredSize(new Dimension(80, 20));
     this.nodeIDField.setHorizontalAlignment(0);

     this.nodeIDPanel.add(this.nodeIDLabel);
     this.nodeIDPanel.add(this.nodeIDField);


     this.buttonsPanel2 = new JPanel();

     this.clearButton = new JButton("Clear All");
     this.clearButton.addActionListener(new ActionListener()
     {
       public void actionPerformed(ActionEvent arg0) {
         ConstraintsFrame.this.clear();
       }

     });
     this.buttonsPanel2.add(this.clearButton);


     this.tablesPanel = new JPanel();
     this.inputTable = new JTable();
     this.inputTable.setSelectionMode(0);
     this.outputTable = new JTable();
     this.outputTable.setSelectionMode(0);
     this.outputTable.setCellSelectionEnabled(true);

     buildTable(inputsSet, outputsSet);

     this.tablesPanel.add(this.scrollPaneInput);
     this.tablesPanel.add(this.scrollPaneOutput);


     this.tablesHandlingPanel = new JPanel(new BorderLayout());
     this.tablesHandlingPanel.add(this.tablesPanel, "Center");
     this.tablesHandlingPanel.add(this.buttonsPanel2, "Last");
     this.tablesHandlingPanel.setBorder(BorderFactory.createLineBorder(Color.black));


     this.textArea = new JTextArea();
     this.areaPanel = new JScrollPane(this.textArea);


     this.textArea.setText(reactions);

     pane.add(this.nodeIDPanel, "North");
     pane.add(this.tablesHandlingPanel, "Center");
     pane.add(this.areaPanel, "South");
   }

   public void buildTable(HashSet<String> inputs, ArrayList<String> outputs) {
     DefaultTableModel intputModel = new DefaultTableModel();
     this.inputTableData = new Object[inputs.size()][1];

     int i = 0;
     for (String in : inputs) {
       this.inputTableData[i][0] = in;
       i++;
     }

     intputModel.setDataVector(this.inputTableData, new String[] { "Inputs" });
     this.inputTable.setModel(intputModel);

     String[] outputsColumns = new String[0];
     outputsColumns = (String[])outputs.toArray(outputsColumns);

     Object outputModel = new DefaultTableModel() {
       public Class getColumnClass(int c) {
         return getValueAt(0, c).getClass();
       }


     };
     this.outputTableData = new Object[inputs.size()][outputs.size()];

     for (int a = 0; a < inputs.size(); a++) {
       for (int b = 0; b < outputs.size(); b++) {
         this.outputTableData[a][b] = new Boolean(false);
       }
     }

     ((DefaultTableModel)outputModel).setDataVector(this.outputTableData, outputsColumns);
     this.outputTable.setModel((TableModel)outputModel);

     this.outputTable.setAutoResizeMode(0);

     int sizeCount = 0;
     int length = 0;
     int colSize = 0;

     for (int k = 0; k < outputs.size(); k++) {
       length = ((String)outputs.get(k)).length();
       colSize = length * 10;
       //System.out.println(length);
       this.outputTable.getColumnModel().getColumn(k).setPreferredWidth(colSize);
       sizeCount += colSize;
     }

     //System.err.println(this.outputTable.getRowHeight(1));

     int height = inputs.size() * 16 + 19;

     this.scrollPaneInput = new JScrollPane(this.inputTable);

     this.scrollPaneOutput = new JScrollPane(this.outputTable);
   }


   public String[] buildSpeciesArray(HashSet<String> speciesSet)
   {
     String[] comboBoxItems = (String[])speciesSet.toArray(new String[0]);
     return comboBoxItems;
   }

   public void clear() {
     setTables();
   }

   public void done() {
     this.inputData = getTableData(this.inputTable);
     this.outputData = getTableData(this.outputTable);
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
     String[] tableData = new String[nRow];
     for (int i = 0; i < nRow; i++)
       tableData[i] = ((String)dtm.getValueAt(i, 0));
     return tableData;
   }

   public void setTables() {
     DefaultTableModel intputModel = new DefaultTableModel();
     this.inputTableData = new Object[0][0];
     intputModel.setDataVector(this.inputTableData, new String[] { "Inputs" });
     this.inputTable.setModel(intputModel);

     this.outputTableData = new Object[0][0];
     DefaultTableModel outputModel = new DefaultTableModel();
     outputModel.setDataVector(this.outputTableData, new String[] { "Outputs" });
     this.outputTable.setModel(outputModel);
   }







   private static void createAndShowGUI()
   {
     HashSet<String> ss = new HashSet();

     ss.add("A");
     ss.add("R");
     ss.add("C");
     ss.add("D");

     ArrayList<String> ss2 = new ArrayList();

     ss2.add("n2.SY");
     ss2.add("n2.R");
     ss2.add("n3.SY");


















     ConstraintsFrame demo = new ConstraintsFrame("Node Constrains Selection", ss, ss2, "n1", "A + B --> C\nD + A --> E\nK + R --> E");
     demo.setDefaultCloseOperation(3);

     Object[] options = { "Done", "Cancel" };
     JOptionPane.showOptionDialog(null,
       demo.getContentPane(),
       "Node Constrains Selection",
       0,
       -1,
       null, options,
       options[0]);
   }


   public static void main(String[] args)
   {
     try
     {
       UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
     } catch (UnsupportedLookAndFeelException ex) {
       ex.printStackTrace();
     } catch (IllegalAccessException ex) {
       ex.printStackTrace();
     } catch (InstantiationException ex) {
       ex.printStackTrace();
     } catch (ClassNotFoundException ex) {
       ex.printStackTrace();
     }

     UIManager.put("swing.boldMetal", Boolean.FALSE);



     SwingUtilities.invokeLater(new Runnable()
     {
       public void run() {}
     });
   }
 }


