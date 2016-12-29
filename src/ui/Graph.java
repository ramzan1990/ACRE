 package ui;

 import java.awt.Graphics2D;
 import java.awt.geom.Point2D;
 import java.awt.geom.Rectangle2D;
 import java.awt.geom.Rectangle2D.Double;
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;
 import sbml.ModelManager;
 import sbml.ModelNode;











 public abstract class Graph
   implements Serializable
 {
   public ArrayList<Node> nodes;
   protected ModelManager modelMgr;

   public Graph()
   {
     this.nodes = new ArrayList();
     this.modelMgr = new ModelManager();
   }








   public boolean add(Node n, Point2D p)
   {
     Rectangle2D bounds = n.getBounds();
     n.translate(p.getX() - bounds.getX(),
       p.getY() - bounds.getY());
     this.nodes.add(n);
     return true;
   }






   public Node findNode(Point2D p)
   {
     for (int i = this.nodes.size() - 1; i >= 0; i--)
     {
       Node n = (Node)this.nodes.get(i);
       if (n.contains(p)) return n;
     }
     return null;
   }






   public void draw(Graphics2D g2)
   {
     for (Node n : this.nodes) {
       n.draw(g2);
     }
   }





   public void removeNode(Node n)
   {
     for (Node nod : this.nodes)
     {
       ((ModuleNode)nod).modelNode.removeConnections(((ModuleNode)n).modelNode);
     }
     this.nodes.remove(n);
   }

   public void removeAllNodes() {
     this.nodes.clear();
   }

   public void removeAllNodes(String ID)
   {
     for (int i = 0; i < this.nodes.size(); i++) {}
   }









   public Rectangle2D getBounds(Graphics2D g2)
   {
     Rectangle2D r = null;
     for (Node n : this.nodes)
     {
       Rectangle2D b = n.getBounds();
       if (r == null) r = b; else {
         r.add(b);
       }
     }
     return r == null ? new Rectangle2D.Double() : r;
   }






   public abstract Node[] getNodePrototypes();





   public List<Node> getNodes()
   {
     return Collections.unmodifiableList(this.nodes);
   }
 }


