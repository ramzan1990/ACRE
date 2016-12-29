package ui;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public abstract interface Node
  extends Serializable, Cloneable
{
  public abstract void draw(Graphics2D paramGraphics2D);
  
  public abstract void translate(double paramDouble1, double paramDouble2);
  
  public abstract boolean contains(Point2D paramPoint2D);
  
  public abstract Point2D getConnectionPoint(Point2D paramPoint2D);
  
  public abstract Rectangle2D getBounds();
  
  public abstract Object clone();
}


