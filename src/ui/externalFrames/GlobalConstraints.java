
package ui.externalFrames;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.*;
import javax.swing.*;

public class GlobalConstraints
        extends JFrame {
    JLabel l1;
    JLabel l2;
    JTextField MinEdges;
    JTextField MaxEdges;

    public GlobalConstraints(int min, int max) {
        super("Global Constraints");
        Container pane = this.getContentPane();
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        this.l1 = new JLabel("Min Edges   ");
        this.l2 = new JLabel("Max Edges   ");
        this.MinEdges = new JTextField(10);
        this.MaxEdges = new JTextField(10);
        this.MinEdges.setText(String.valueOf(min));
        this.MaxEdges.setText(String.valueOf(max));
        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(this.l1,BorderLayout.WEST);
        p1.add(this.MinEdges,BorderLayout.CENTER);
        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(this.l2,BorderLayout.WEST);
        p2.add(this.MaxEdges,BorderLayout.CENTER);
        p.add(p1);
        p.add(Box.createRigidArea(new Dimension(0,5)));
        p.add(p2);
        pane.add(p);

        Dimension d = this.l1.getPreferredSize();
        d.width = this.l2.getPreferredSize().width;
        this.l1.setPreferredSize(d);
    }

    private static void createAndShowGUI() {
        GlobalConstraints demo = new GlobalConstraints(0, 0);
        demo.setDefaultCloseOperation(3);
        Object[] options = new Object[]{"Done", "Cancel"};
        JOptionPane.showOptionDialog(null, demo.getContentPane(), "Node Constrains Selection", 0, -1, null, options, options[0]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                GlobalConstraints.createAndShowGUI();
            }
        });
    }

    public int getMin() {
        return Integer.parseInt(this.MinEdges.getText());
    }

    public int getMax() {
        return Integer.parseInt(this.MaxEdges.getText());
    }

}

