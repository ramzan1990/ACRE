 package ui;

 import java.awt.Dimension;
 import java.awt.Graphics2D;
 import java.awt.geom.Rectangle2D;
 import java.awt.geom.Rectangle2D.Double;
 import java.io.Serializable;
 import java.util.HashSet;
 import java.util.StringTokenizer;
 import javax.swing.JLabel;


 public class MultiLineString
   implements Cloneable, Serializable
 {
   public static final int LEFT = 0;
   public static final int CENTER = 1;
   public static final int RIGHT = 2;
   public static final int LARGE = 3;
   public static final int NORMAL = 4;
   public static final int SMALL = 5;
   private static final int GAP = 3;
   private String text;
   private int justification;
   private int size;
   private boolean underlined;

   public MultiLineString()
   {
     this.text = "";
     this.justification = 1;
     this.size = 4;
     this.underlined = false;
   }

   public MultiLineString(int size)
   {
     this.text = "";
     this.justification = 1;
     this.size = size;
     this.underlined = false;
   }



   public void setText(String newValue)
   {
     this.text = newValue;
   }

   public String getText()
   {
     return this.text;
   }


   public void setJustification(int newValue)
   {
     this.justification = newValue;
   }


   public int getJustification()
   {
     return this.justification;
   }

   public boolean isUnderlined()
   {
     return this.underlined;
   }

   public void setUnderlined(boolean newValue)
   {
     this.underlined = newValue;
   }

   public void setSize(int newValue)
   {
     this.size = newValue;
   }

   public int getSize()
   {
     return this.size;
   }

   public String toString() {
     return this.text.replace('\n', '|');
   }

   public void setTextFromList(HashSet<String> inputs) {
     String newText = "";

     for (String input : inputs) {
       newText = newText + input + "\n";
     }
     this.text = newText;
   }

   private void setLabelText()
   {
     StringBuffer prefix = new StringBuffer();
     StringBuffer suffix = new StringBuffer();
     StringBuffer htmlText = new StringBuffer();
     prefix.append("&nbsp;");
     suffix.insert(0, "&nbsp;");
     if (this.underlined)
     {
       prefix.append("<u>");
       suffix.insert(0, "</u>");
     }
     if (this.size == 3)
     {
       prefix.append("<font size=\"+10\">");
       suffix.insert(0, "</font>");
     }
     if (this.size == 5)
     {
       prefix.append("<font size=\"-1\">");
       suffix.insert(0, "</font>");
     }
     htmlText.append("<html>");
     StringTokenizer tokenizer = new StringTokenizer(this.text, "\n");
     boolean first = true;
     while (tokenizer.hasMoreTokens())
     {
       if (first) first = false; else htmlText.append("<br>");
       htmlText.append(prefix);
       htmlText.append(tokenizer.nextToken());
       htmlText.append(suffix);
     }
     htmlText.append("</html>");
     label.setText(htmlText.toString());
     if (this.justification == 0) { label.setHorizontalAlignment(2);
     } else if (this.justification == 1) { label.setHorizontalAlignment(0);
     } else if (this.justification == 2) { label.setHorizontalAlignment(4);
     }
   }





   public Rectangle2D getBounds(Graphics2D g2)
   {
     if (this.text.length() == 0) return new Rectangle2D.Double();
     setLabelText();
     Dimension dim = label.getPreferredSize();
     return new Rectangle2D.Double(0.0D, 0.0D, dim.getWidth(), dim.getHeight());
   }






   public void draw(Graphics2D g2, Rectangle2D r)
   {
     setLabelText();
     label.setFont(g2.getFont());
     label.setBounds(0, 0, (int)r.getWidth(), (int)r.getHeight());
     g2.translate(r.getX(), r.getY());
     label.paint(g2);
     g2.translate(-r.getX(), -r.getY());
   }

   public Object clone()
   {
     try
     {
       MultiLineString ret = new MultiLineString();
       ret.text = this.text;
       ret.justification = this.justification;
       ret.size = this.size;
       ret.underlined = this.underlined;
       label = new JLabel(label.getText());
       return ret;
     }
     catch (Exception exception) {}

     return null;
   }
















   private static JLabel label = new JLabel();
 }


