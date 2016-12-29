 package ui;

 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.geom.AffineTransform;
 import java.awt.geom.Rectangle2D;
 import java.io.Serializable;
 import java.util.ArrayList;
 import javax.swing.ButtonGroup;
 import javax.swing.Icon;
 import javax.swing.JToggleButton;
 import javax.swing.JToolBar;

 public class ToolBar
   extends JToolBar implements Serializable
 {
   JToggleButton grabberButton;
   private ButtonGroup group;
   private ArrayList<Object> tools;
   private ArrayList<ModuleNode> nodes;
   private static final int BUTTON_SIZE = 35;
   private static final int OFFSET = 4;

   public ToolBar(Graph graph)
   {
     this.group = new ButtonGroup();
     this.tools = new ArrayList();

     this.grabberButton = new JToggleButton(
       new Icon()
       {
         public int getIconHeight() { return 35; }
         public int getIconWidth() { return 35; }

         public void paintIcon(Component c, Graphics g, int x, int y)
         {
           Graphics2D g2 = (Graphics2D)g;
           GraphPanel.drawGrabber(g2, x + 4, y + 4);
           GraphPanel.drawGrabber(g2, x + 4, y + 35 - 4);
           GraphPanel.drawGrabber(g2, x + 35 - 4, y + 4);
           GraphPanel.drawGrabber(g2, x + 35 - 4, y + 35 - 4);


         }



       });
     this.group.add(this.grabberButton);
     add(this.grabberButton);
     this.grabberButton.setSelected(true);
     this.tools.add(null);

     Node[] nodeTypes = graph.getNodePrototypes();
     Node[] arrayOfNode1; int j = (arrayOfNode1 = nodeTypes).length; for (int i = 0; i < j; i++) { Node n = arrayOfNode1[i];
       add(n);
     }
   }




   public void LoadState(WorkSpace space)
   {
     for (Object n : space.tools)
     {
       add((ModuleNode)n);
     }
   }

   public void SaveState(WorkSpace space) {
     space.tools = new ArrayList();
     for (Object t : this.tools)
     {
       if (t != null)
       {
         space.tools.add((ModuleNode)((ModuleNode)t).clone());
       }
     }
   }




   public void removeAll()
   {
     Component[] arrayOfComponent;



     int j = (arrayOfComponent = getComponents()).length; for (int i = 0; i < j; i++) { Component c = arrayOfComponent[i];

       int index = getComponentIndex(c);
       if (index != 0)
       {
         this.group.remove((JToggleButton)c);
         this.tools.remove(index);
         remove(c);
       } }
     repaint();
   }

   public ModuleNode removeSelected() {
     ModuleNode ret = null;
     Component[] arrayOfComponent; int j = (arrayOfComponent = getComponents()).length; for (int i = 0; i < j; i++) { Component c = arrayOfComponent[i];

       if (((JToggleButton)c).isSelected())
       {
         int index = getComponentIndex(c);
         if (index == 0)
           break;
         ret = (ModuleNode)this.tools.get(index);
         this.group.remove((JToggleButton)c);
         this.tools.remove(index);
         remove(c);
       }
     }
     repaint();
     return ret;
   }

   public Object getSelectedTool() {
     int i = 0;
     for (Object o : this.tools)
     {
       JToggleButton button = (JToggleButton)getComponent(i++);
       if (button.isSelected())
       {
         button.setSelected(false);
         this.grabberButton.setSelected(true);
         return o;
       }
     }
     return null;
   }






   public void add(final Node n)
   {
     JToggleButton button = new JToggleButton(
       new Icon()
       {
         public int getIconHeight() { return 35; }
         public int getIconWidth() { return 35; }

         public void paintIcon(Component c, Graphics g, int x, int y)
         {
           double width = n.getBounds().getWidth();
           double height = n.getBounds().getHeight();

           Graphics2D g2 = (Graphics2D)g;
           double scaleX = 31.0D / width;
           double scaleY = 31.0D / height;

           double scale = Math.min(scaleX, scaleY);

           AffineTransform oldTransform = g2.getTransform();
           g2.translate(x, y);
           g2.scale(scale, scale);
           g2.translate(Math.max((height - width) / 2.0D, 0.0D), Math.max((width - height) / 2.0D, 0.0D));
           g2.setColor(Color.black);
           n.draw(g2);
           g2.setTransform(oldTransform);
         }
       });
     if ((n instanceof ModuleNode))
     {
       ModuleNode n2 = (ModuleNode)n;
       button.setToolTipText(n2.toString());
     }
     this.group.add(button);
     add(button);
     this.tools.add(n);
   }
 }


