 package acre.ui;

 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.Point;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseMotionAdapter;
 import java.awt.geom.Line2D;
 import java.awt.geom.Point2D;
 import java.awt.geom.Rectangle2D;
 import java.util.ArrayList;
 import java.util.HashMap;
 import javax.swing.JComponent;
 import javax.swing.JLabel;
 import javax.swing.JOptionPane;

 import acre.sbml.ModelNode;
 import acre.ui.externalFrames.ConstraintsFrameTree;
 import acre.ui.externalFrames.GlobalConstraints;

 public class GraphPanel extends JComponent implements java.io.Serializable
 {
   private Graph graph;
   private ToolBar toolBar;
   private Point2D lastMousePoint;
   private Point2D rubberBandStart;
   private Point2D dragStartPoint;
   private Rectangle2D dragStartBounds;
   private Object selected;

   public GraphPanel(ToolBar aToolBar, Graph aGraph, JLabel statusBar)
   {
     this.toolBar = aToolBar;
     this.graph = aGraph;
     this.statusBar = statusBar;
     setBackground(Color.WHITE);


     addMouseListener(
       new java.awt.event.MouseAdapter()
       {
         public void mousePressed(MouseEvent event)
         {
           Point2D mousePoint = event.getPoint();
           Node n = GraphPanel.this.graph.findNode(mousePoint);
           Object tool = GraphPanel.this.toolBar.getSelectedTool();
           if (tool == null)
           {
             if (n != null)
             {
               GraphPanel.this.selected = n;
               GraphPanel.this.dragStartPoint = mousePoint;
               GraphPanel.this.dragStartBounds = n.getBounds();
               if (event.getClickCount() == 2)
               {
                 GraphPanel.this.editConstraints();
               }
             }
             else
             {
               GraphPanel.this.selected = null;
             }
           }
           else if ((tool instanceof Node))
           {
             Node prototype = (Node)tool;
             ModuleNode newNode = (ModuleNode)prototype.clone();

             ModelNode modelNode = GraphPanel.this.graph.modelMgr.createNode(newNode.model);
             newNode.setName(modelNode.getID());
             newNode.modelNode = modelNode;
             GraphPanel.this.ModeltoModule.put(modelNode, newNode);


             boolean added = GraphPanel.this.graph.add(newNode, mousePoint);
             if (added)
             {
               GraphPanel.this.selected = newNode;
               GraphPanel.this.dragStartPoint = mousePoint;
               GraphPanel.this.dragStartBounds = newNode.getBounds();
             }
             else if (n != null)
             {
               GraphPanel.this.selected = n;
               GraphPanel.this.dragStartPoint = mousePoint;
               GraphPanel.this.dragStartBounds = n.getBounds();
             }
           }



           GraphPanel.this.lastMousePoint = mousePoint;
           GraphPanel.this.updateStatusBar();
           GraphPanel.this.revalidate();
           GraphPanel.this.repaint();
         }

         public void mouseReleased(MouseEvent event)
         {
           Object tool = GraphPanel.this.toolBar.getSelectedTool();
           if (GraphPanel.this.rubberBandStart != null)
           {
             Point localPoint = event.getPoint();
           }

           GraphPanel.this.revalidate();
           GraphPanel.this.repaint();

           GraphPanel.this.lastMousePoint = null;
           GraphPanel.this.dragStartBounds = null;
           GraphPanel.this.rubberBandStart = null;
         }

       });
     addMouseMotionListener(
       new MouseMotionAdapter()
       {
         public void mouseDragged(MouseEvent event)
         {
           Point2D mousePoint = event.getPoint();
           if (GraphPanel.this.dragStartBounds != null)
           {
             if ((GraphPanel.this.selected instanceof Node))
             {
               Node n = (Node)GraphPanel.this.selected;
               Rectangle2D bounds = n.getBounds();
               n.translate(
                 GraphPanel.this.dragStartBounds.getX() - bounds.getX() +
                 mousePoint.getX() - GraphPanel.this.dragStartPoint.getX(),
                 GraphPanel.this.dragStartBounds.getY() - bounds.getY() +
                 mousePoint.getY() - GraphPanel.this.dragStartPoint.getY());
             }
           }
           GraphPanel.this.lastMousePoint = mousePoint;
           GraphPanel.this.revalidate();
           GraphPanel.this.repaint();
         }
       });
   }

   public void updateStatusBar()
   {
     if ((this.selected instanceof Node))
       this.statusBar.setText(((ModuleNode)this.selected).model.getPrintedReactions());
   }

   public void paintComponent(Graphics g) {
     Graphics2D g2 = (Graphics2D)g;
     Rectangle2D bounds = getBounds();
     Rectangle2D graphBounds = this.graph.getBounds(g2);
     this.graph.draw(g2);

     if ((this.selected instanceof Node))
     {
       Rectangle2D grabberBounds = ((Node)this.selected).getBounds();
       drawGrabber(g2, grabberBounds.getMinX(), grabberBounds.getMinY());
       drawGrabber(g2, grabberBounds.getMinX(), grabberBounds.getMaxY());
       drawGrabber(g2, grabberBounds.getMaxX(), grabberBounds.getMinY());
       drawGrabber(g2, grabberBounds.getMaxX(), grabberBounds.getMaxY());
     }

     if (this.rubberBandStart != null)
     {
       Color oldColor = g2.getColor();
       g2.setColor(PURPLE);
       g2.draw(new Line2D.Double(this.rubberBandStart, this.lastMousePoint));
       g2.setColor(oldColor);
     }
   }




   public void removeSelected()
   {
     if ((this.selected instanceof Node))
     {
       this.graph.modelMgr.deleteNode(((ModuleNode)this.selected).modelNode);
       this.graph.removeNode((Node)this.selected);
     }

     this.selected = null;
     revalidate();
     repaint();
   }

   public void removeAllNodes(ModuleNode n) {
     ArrayList<ModelNode> deletes = this.graph.modelMgr.deleteAllNodes(n.model.getAutoID());
     for (int i = 0; i < deletes.size(); i++)
     {
       this.graph.removeNode((Node)this.ModeltoModule.get(deletes.get(i)));
     }
     this.selected = null;
     revalidate();
     repaint();
   }

   public void removeAllNodes() {
     this.graph.modelMgr.deleteAllNodes();
     this.graph.removeAllNodes();
     revalidate();
     repaint();
   }

   public void SaveState(WorkSpace s)
   {
     s.nodes = new ArrayList();
     for (Node n : this.graph.nodes)
     {
       s.nodes.add((ModuleNode)((ModuleNode)n).clone());
     }
     s.minEdges = this.minEdges.intValue();
     s.maxEdges = this.maxEdges.intValue();
   }



   public void LoadState(WorkSpace s)
   {
     this.minEdges = Integer.valueOf(s.minEdges);
     this.maxEdges = Integer.valueOf(s.maxEdges);
     for (Object n : s.nodes)
     {
       ModuleNode newNode = (ModuleNode)n;
       this.graph.modelMgr.addNode(newNode.modelNode);
       ModelNode modelNode = newNode.modelNode;
       newNode.setName(modelNode.getID());
       newNode.modelNode = modelNode;
       this.ModeltoModule.put(modelNode, newNode);
       Point2D p = new Point2D.Double(newNode.getBounds().getMinX(), newNode.getBounds().getMinY());
       boolean added = this.graph.add(newNode, p);
       if (added)
       {
         this.selected = newNode;
         this.dragStartPoint = p;
         this.dragStartBounds = newNode.getBounds();
       }
     }


     revalidate();
     repaint();
   }

   public void editConstraints()
   {
     if ((this.selected instanceof Node)) {
       ModuleNode selectedNode = (ModuleNode)this.selected;
       ModelNode modelNode = selectedNode.modelNode;



       ConstraintsFrameTree demo = new ConstraintsFrameTree("Node Constrains Selection", modelNode, modelNode.getModel().getInputs(),
         this.graph.modelMgr.getOutputsExceptNode(modelNode), modelNode.getID(), modelNode.getModel().getPrintedReactions());

       demo.setDefaultCloseOperation(3);
       Object[] options = { "Done", "Cancel" };

       JOptionPane.showOptionDialog(null, demo.getContentPane(), "Node Constrains Selection",
         0, -1, null, options, options[0]);
       demo.updateConstraints(modelNode);
     }
   }




   public void editgConstraints()
   {
     GlobalConstraints demo = new GlobalConstraints(this.minEdges.intValue(), this.maxEdges.intValue());


     demo.setDefaultCloseOperation(3);
     Object[] options = { "Done", "Cancel" };

     JOptionPane.showOptionDialog(null, demo.getContentPane(), "Node Constrains Selection",
       0, -1, null, options, options[0]);
     this.minEdges = Integer.valueOf(demo.getMin());
     this.maxEdges = Integer.valueOf(demo.getMax());
   }








   public static void drawGrabber(Graphics2D g2, double x, double y)
   {
     int SIZE = 5;
     Color oldColor = g2.getColor();
     g2.setColor(PURPLE);
     g2.fill(new Rectangle2D.Double(x - 2.0D,
       y - 2.0D, 5.0D, 5.0D));
     g2.setColor(oldColor);
   }

   public Dimension getPreferredSize()
   {
     Rectangle2D bounds =
       this.graph.getBounds((Graphics2D)getGraphics());
     return new Dimension(
       (int)bounds.getMaxX(),
       (int)bounds.getMaxY());
   }








   private static final Color PURPLE = new Color(0.7F, 0.4F, 0.7F);
   private HashMap<ModelNode, ModuleNode> ModeltoModule = new HashMap();
   private JLabel statusBar;
   private Integer minEdges = Integer.valueOf(0); private Integer maxEdges = Integer.valueOf(Integer.MAX_VALUE);
 }


