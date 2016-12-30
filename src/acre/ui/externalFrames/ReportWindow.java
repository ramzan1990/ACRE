
package acre.ui.externalFrames;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class ReportWindow
        extends JFrame {
    public ReportWindow(String report) {
        JEditorPane text = new JEditorPane();
        text.setText(report);
        text.setEditable(false);
        text.setPreferredSize(new Dimension(250, 145));
        JScrollPane scroll = new JScrollPane(text);
        this.getContentPane().add(scroll);
    }
}

