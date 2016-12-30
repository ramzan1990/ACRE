 package acre.ui;

 import java.awt.geom.Point2D;
 import java.awt.geom.Rectangle2D;
 import java.awt.geom.RectangularShape;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;
 import java.io.Serializable;







 public abstract class RectangularNode
   implements Node, Serializable
 {
   private transient Rectangle2D bounds;

   public Object clone()
   {
     try
     {
       RectangularNode cloned = (RectangularNode)super.clone();
       cloned.bounds = ((Rectangle2D)this.bounds.clone());
       return cloned;
     }
     catch (CloneNotSupportedException exception) {}

     return null;
   }


   public void translate(double dx, double dy)
   {
     this.bounds.setFrame(this.bounds.getX() + dx,
       this.bounds.getY() + dy,
       this.bounds.getWidth(),
       this.bounds.getHeight());
   }

   public boolean contains(Point2D p)
   {
     return this.bounds.contains(p);
   }

   public Rectangle2D getBounds()
   {
     return (Rectangle2D)this.bounds.clone();
   }

   public void setBounds(Rectangle2D newBounds)
   {
     this.bounds = newBounds;
   }

   public Point2D getConnectionPoint(Point2D aPoint)
   {
     double slope = this.bounds.getHeight() / this.bounds.getWidth();
     double x = this.bounds.getCenterX();
     double y = this.bounds.getCenterY();
     double ex = aPoint.getX() - x;
     double ey = aPoint.getY() - y;

     if ((ex != 0.0D) && (-slope <= ey / ex) && (ey / ex <= slope))
     {

       if (ex > 0.0D)
       {
         x = this.bounds.getMaxX();
         y += this.bounds.getWidth() / 2.0D * ey / ex;
       }
       else
       {
         x = this.bounds.getX();
         y -= this.bounds.getWidth() / 2.0D * ey / ex;
       }
     }
     else if (ey != 0.0D)
     {

       if (ey > 0.0D)
       {
         x += this.bounds.getHeight() / 2.0D * ex / ey;
         y = this.bounds.getMaxY();
       }
       else
       {
         x -= this.bounds.getHeight() / 2.0D * ex / ey;
         y = this.bounds.getY();
       }
     }
     return new Point2D.Double(x, y);
   }

   private void writeObject(ObjectOutputStream out)
     throws IOException
   {
     out.defaultWriteObject();
     writeRectangularShape(out, this.bounds);
   }










   private static void writeRectangularShape(ObjectOutputStream out, RectangularShape s)
     throws IOException
   {
     out.writeDouble(s.getX());
     out.writeDouble(s.getY());
     out.writeDouble(s.getWidth());
     out.writeDouble(s.getHeight());
   }

   private void readObject(ObjectInputStream in)
     throws IOException, ClassNotFoundException
   {
     in.defaultReadObject();
     this.bounds = new Rectangle2D.Double();
     readRectangularShape(in, this.bounds);
   }









   private static void readRectangularShape(ObjectInputStream in, RectangularShape s)
     throws IOException
   {
     double x = in.readDouble();
     double y = in.readDouble();
     double width = in.readDouble();
     double height = in.readDouble();
     s.setFrame(x, y, width, height);
   }
 }


