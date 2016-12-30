package acre.ui;

import acre.ACR.ACRAnalyzer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import acre.sbml.Model;
import acre.sbml.ModelNode;
import acre.sbml.NetEnum;
import acre.sbml.Reaction;
import acre.sbml.SBMLLoader;
import acre.ui.externalFrames.EnumerationFrame;
import acre.ui.externalFrames.IOSelectionFrame;
import acre.ui.externalFrames.ReportWindow;

public class GraphFrame
        extends JFrame
        implements Serializable
{
    private Graph graph;
    private GraphPanel panel;
    private JScrollPane scrollPane;
    private ToolBar toolBar;
    private JLabel statusBar;
    private WorkSpace workSpace;
    private JMenuItem aboutButton;
    private JMenuItem termsButton;
    private JFileChooser fileChooser;
    public static final int FRAME_WIDTH = 600;
    public static final int FRAME_HEIGHT = 400;

    public GraphFrame(Graph graph)
    {
        setSize(600, 400);
        setDefaultCloseOperation(3);

        this.graph = graph;
        setTitle("ACRE");
        BufferedImage image = null;

        this.fileChooser = new JFileChooser();

        setIconImage(new ImageIcon("images/chemistry.png").getImage());

        constructFrameComponents();

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem openItem = new JMenuItem("Load");
        openItem.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        GraphFrame.this.openFile();
                    }
                });
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save Workspace");
        saveItem.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        GraphFrame.this.SaveState();
                    }
                });
        fileMenu.add(saveItem);

        JMenuItem loadItem = new JMenuItem("Load Workspace");
        loadItem.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        GraphFrame.this.LoadState();
                    }
                });
        fileMenu.add(loadItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        System.exit(0);
                    }
                });
        fileMenu.add(exitItem);

        JMenuItem deleteItem = new JMenuItem("Delete Node");
        deleteItem.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        GraphFrame.this.panel.removeSelected();
                    }
                });
        JMenuItem deleteModule = new JMenuItem("Delete Module");
        deleteModule.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        GraphFrame.this.removeSelectedModule();
                    }
                });
        JMenuItem constraintsItem = new JMenuItem("Constraints");
        constraintsItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                GraphFrame.this.panel.editConstraints();
            }
        });
        JMenuItem gconstraintsItem = new JMenuItem("Global Constraints");
        gconstraintsItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                GraphFrame.this.panel.editgConstraints();
            }
        });
        JMenu editMenu = new JMenu("Edit");
        editMenu.add(deleteItem);
        editMenu.add(deleteModule);
        editMenu.add(constraintsItem);
        editMenu.add(gconstraintsItem);
        menuBar.add(editMenu);

        JMenuItem Enum = new JMenuItem("Enumerate");
        Enum.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                EnumerationFrame f = new EnumerationFrame(GraphFrame.this.PackSpace());
                JOptionPane.showMessageDialog(null, f.getContentPane(), "Enumeration Options", -1);

                NetEnum runresult = new NetEnum(GraphFrame.this.PackSpace(), f);
                if (f.showtoScreen.isSelected())
                {
                    ReportWindow rwindow = new ReportWindow(runresult.report.toString());
                    JOptionPane newpane = new JOptionPane(rwindow.getContentPane());
                    JDialog dialog = newpane.createDialog("Results");
                    dialog.setResizable(true);
                    dialog.setVisible(true);
                }
                if (f.savetofile.isSelected()) {
                    try
                    {
                        File savefile = new File(f.fileURI.getText());
                        if (savefile.exists()) {
                            savefile.delete();
                        }
                        savefile.createNewFile();
                        PrintWriter writer = new PrintWriter(savefile);
                        writer.print(runresult.report.toString());
                        writer.close();
                    }
                    catch (Exception e)
                    {
                        //System.out.println(e.getMessage());
                    }
                }
            }
        });
        JMenu RunMenu = new JMenu("Run");
        RunMenu.add(Enum);
        menuBar.add(RunMenu);

        JMenu AboutMenu = new JMenu("Info");
        this.aboutButton = new JMenuItem("About");
        this.aboutButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, "ACRE (Absolute Concentration Robustness Exploration) is a tool to \nenumerate all the biochemical reaction networks that consist of \nuser-created nodes from user-selected modules under user-specified \nconstraints; to apply Shinar and Feinberg's theorem; and to determine \nwhich of the networks have the absolute concentration robustness \n(ACR) property. \n\nACRE is developed by Xin Gao, Islam Almasri, Ramzan Umarov, Basil Arkasosy, \nand Hiroyuki Kuwahara.\n\nContact: Xin Gao, xin.gao@kaust.edu.sa.", "About", 1);
            }
        });
        AboutMenu.add(this.aboutButton);
        this.termsButton = new JMenuItem("Terms of use");
        this.termsButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, "Copyright (c) 2016 SFB\r\n\r\nLicensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License.", "Terms of use", 1);
            }
        });
        AboutMenu.add(this.termsButton);
        menuBar.add(AboutMenu);
    }

    private void ResetWorkspace()
    {
        this.panel.removeAllNodes();
        this.toolBar.removeAll();
    }

    private WorkSpace PackSpace()
    {
        WorkSpace ret = new WorkSpace();
        this.toolBar.SaveState(ret);
        this.panel.SaveState(ret);
        ret.modelcounter = Model.GetIDCounter();
        ret.modlenodeconter = ModelNode.GetIDCounter();
        return ret;
    }

    private void UnpackSpace(WorkSpace space)
    {
        this.toolBar.LoadState(space);
        this.panel.LoadState(space);
        Model.SetIDCounter(space.modelcounter);
        ModelNode.SetIDCounter(space.modlenodeconter);
    }

    private void SaveState()
    {
        int r = this.fileChooser.showSaveDialog(this);
        if (r == 0) {
            try
            {
                File file = this.fileChooser.getSelectedFile();
                if (file.exists())
                {
                    int result = JOptionPane.showConfirmDialog(this, "Overwrite existing file?", "Overwrite", 2);
                    if (result == 2) {
                        return;
                    }
                    file.delete();
                }
                file.createNewFile();
                ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream(file));
                WorkSpace tempspace = PackSpace();
                s.writeObject(tempspace);
                s.flush();
                s.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void LoadState()
    {
        int r = this.fileChooser.showOpenDialog(this);
        if (r == 0) {
            try
            {
                File file = this.fileChooser.getSelectedFile();
                ObjectInputStream s = new ObjectInputStream(new FileInputStream(file));
                WorkSpace temps = (WorkSpace)s.readObject();
                ResetWorkspace();
                UnpackSpace(temps);
                invalidate();
                repaint();
            }
            catch (Exception localException) {}
        }
    }

    private void removeSelectedModule()
    {
        ModuleNode n = this.toolBar.removeSelected();
        this.panel.removeAllNodes(n);
    }

    private void constructFrameComponents()
    {
        this.toolBar = new ToolBar(this.graph);
        this.statusBar = new JLabel(" ");
        this.panel = new GraphPanel(this.toolBar, this.graph, this.statusBar);
        this.scrollPane = new JScrollPane(this.panel);
        this.toolBar.setOrientation(1);
        add(this.toolBar, "Before");

        add(this.scrollPane, "Center");

        this.scrollPane.setBackground(Color.WHITE);
        this.scrollPane.setOpaque(true);
        this.toolBar.setBackground(Color.WHITE);
        add(this.statusBar, "South");
    }

    private String checkSupportedModel(Model m)
    {
        for (Reaction r : m.getReactions())
        {
            if (r.reactants.size() == 0) {
                return r.printReaction();
            }
            if (r.products.size() == 0) {
                return r.printReaction();
            }
        }
        return "";
    }

    private void openFile()
    {
        int r = this.fileChooser.showOpenDialog(this);
        if (r == 0) {
            try
            {
                File file = this.fileChooser.getSelectedFile();
                Model model = SBMLLoader.loadModelFromSBML(file);
                CSVNetwork n2 = new CSVNetwork(model);
                String CSV = n2.getCSV();
                acre.ACR.Graph p = ACRAnalyzer.analyze(CSV);
                if (p.isACR()) {
                    JOptionPane.showMessageDialog(this, "Warning: The module already has ACR");
                }
                String supported = checkSupportedModel(model);
                if (supported.length() > 0) {
                    throw new Exception("Reactions with no products or no reactants are not supported:\n" + supported);
                }
                ModuleNode node = new ModuleNode();
                node.model = model;

                IOSelectionFrame ex = new IOSelectionFrame("Modules Inputs / Outputs", model);

                MultiLineString ss = new MultiLineString();
                Object[] options = { "Done", "Cancel" };
                int option = JOptionPane.showOptionDialog(null,
                        ex.getContentPane(),
                        "Model Inputs/ Outputs Selection",
                        0,
                        -1,
                        null, options,
                        options[0]);
                if (option == 0)
                {
                    ex.done();
                    model.setInputs(ex.getInputData());
                    model.setOutputs(ex.getOutputData());
                    if (ex.getModuleName().trim().length() <= 0)
                    {
                        ss.setText(model.getAutoID());
                    }
                    else
                    {
                        ss.setText(ex.getModuleName());
                        model.setAutoID(ex.getModuleName());
                    }
                    ss.setSize(3);
                    node.setName(ss);

                    this.graph.modelMgr.addModule(model);
                    node.setInputs(model.getInputs());
                    node.setOutputs(model.getOutputs());

                    this.toolBar.add(node);
                    setVisible(true);
                }
            }
            catch (IOException exception)
            {
                JOptionPane.showMessageDialog(null,
                        exception);
            }
            catch (ClassNotFoundException exception)
            {
                JOptionPane.showMessageDialog(null,
                        exception);
            }
            catch (Exception th)
            {
                JOptionPane.showMessageDialog(this, th.getMessage());
            }
        }
    }
}
