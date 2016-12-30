 package acre.ui;

 import java.awt.Graphics2D;
 import java.awt.geom.Rectangle2D;
 import java.io.Serializable;
 import java.util.HashSet;
 import acre.sbml.Model;
 import acre.sbml.ModelNode;





 public class ModuleNode
   extends RectangularNode
   implements Serializable
 {
   protected Model model;
   public ModelNode modelNode;
   private double midHeight;
   private double botHeight;
   private MultiLineString name = new MultiLineString();
   private MultiLineString inputs;
   private MultiLineString outputs;
   private MultiLineString inputsText;
   private MultiLineString outputsText;
   private static int DEFAULT_COMPARTMENT_HEIGHT = 20;
   private static int DEFAULT_WIDTH = 94;
   private static int DEFAULT_HEIGHT = 60;

   public ModuleNode() {
     this.name.setSize(3);
     this.inputs = new MultiLineString();
     this.outputs = new MultiLineString();
     this.inputsText = new MultiLineString(4);
     this.inputsText.setText("In");
     this.outputsText = new MultiLineString(4);
     this.outputsText.setText("Out");
     setBounds(new Rectangle2D.Double(0.0D, 0.0D, DEFAULT_WIDTH, DEFAULT_HEIGHT));
     this.midHeight = DEFAULT_COMPARTMENT_HEIGHT;
     this.botHeight = DEFAULT_COMPARTMENT_HEIGHT;
   }

   public void draw(Graphics2D paramGraphics2D)
   {
     layout(paramGraphics2D);
     Rectangle2D.Double localDouble1 = new Rectangle2D.Double(getBounds().getX(), getBounds().getY(), getBounds().getWidth(), getBounds().getHeight() - this.midHeight - this.botHeight);
     paramGraphics2D.draw(localDouble1);
     this.name.draw(paramGraphics2D, localDouble1);
     double d = localDouble1.getWidth() / 2.0D;
     Rectangle2D.Double localDouble2 = new Rectangle2D.Double(localDouble1.getX(), localDouble1.getMaxY(), d, this.midHeight);
     paramGraphics2D.draw(localDouble2);
     this.inputsText.draw(paramGraphics2D, localDouble2);
     Rectangle2D.Double localDouble3 = new Rectangle2D.Double(localDouble1.getX() + d, localDouble1.getMaxY(), d, this.midHeight);
     paramGraphics2D.draw(localDouble3);
     this.outputsText.draw(paramGraphics2D, localDouble3);
     Rectangle2D.Double localDouble4 = new Rectangle2D.Double(localDouble1.getX(), localDouble2.getMaxY(), d, this.botHeight);
     paramGraphics2D.draw(localDouble4);
     this.inputs.draw(paramGraphics2D, localDouble4);
     Rectangle2D.Double localDouble5 = new Rectangle2D.Double(localDouble1.getX() + d, localDouble2.getMaxY(), d, this.botHeight);
     paramGraphics2D.draw(localDouble5);
     this.outputs.draw(paramGraphics2D, localDouble5);
   }

   public void layout(Graphics2D paramGraphics2D) {
     Rectangle2D.Double localDouble1 = new Rectangle2D.Double(0.0D, 0.0D, DEFAULT_WIDTH, DEFAULT_COMPARTMENT_HEIGHT);
     Rectangle2D localRectangle2D1 = this.name.getBounds(paramGraphics2D);
     localRectangle2D1.add(localDouble1);
     Rectangle2D localRectangle2D2 = this.outputs.getBounds(paramGraphics2D);
     Rectangle2D localRectangle2D3 = this.inputs.getBounds(paramGraphics2D);
     this.botHeight = Math.max(localRectangle2D2.getHeight(), localRectangle2D3.getHeight());
     if ((this.midHeight == 0.0D) && (this.botHeight == 0.0D)) {
       localRectangle2D1.add(new Rectangle2D.Double(0.0D, 0.0D, DEFAULT_WIDTH, 3 * DEFAULT_COMPARTMENT_HEIGHT));
     } else {
       localRectangle2D3.add(localDouble1);
       localRectangle2D2.add(localDouble1);
       this.botHeight = Math.max(localRectangle2D2.getHeight(), localRectangle2D3.getHeight());
     }
     Rectangle2D.Double localDouble2 = new Rectangle2D.Double(getBounds().getX(), getBounds().getY(), Math.max(localRectangle2D1.getWidth(), Math.max(localRectangle2D2.getWidth(), localRectangle2D3.getWidth())), localRectangle2D1.getHeight() + this.midHeight + this.botHeight);
     setBounds(localDouble2);
   }

   public void setName(MultiLineString paramMultiLineString) {
     this.name = paramMultiLineString;
   }

   public void setName(String paramString) {
     MultiLineString localMultiLineString = new MultiLineString();
     localMultiLineString.setText(paramString);
     this.name = localMultiLineString;
   }

   public MultiLineString getName() {
     return this.name;
   }

   public void setInputs(MultiLineString paramMultiLineString) {
     this.inputs = paramMultiLineString;
   }

   public void setInputs(HashSet<String> paramHashSet) {
     MultiLineString localMultiLineString = new MultiLineString();
     localMultiLineString.setTextFromList(paramHashSet);
     this.inputs = localMultiLineString;
   }

   public MultiLineString getinputs() {
     return this.inputs;
   }

   public void setOutputs(MultiLineString paramMultiLineString) {
     this.outputs = paramMultiLineString;
   }

   public void setOutputs(HashSet<String> paramHashSet) {
     MultiLineString localMultiLineString = new MultiLineString();
     localMultiLineString.setTextFromList(paramHashSet);
     this.outputs = localMultiLineString;
   }

   public MultiLineString getoutputs() {
     return this.outputs;
   }

   public Object clone()
   {
     ModuleNode localModuleNode = (ModuleNode)super.clone();
     localModuleNode.name = ((MultiLineString)this.name.clone());
     localModuleNode.outputs = ((MultiLineString)this.outputs.clone());
     localModuleNode.inputs = ((MultiLineString)this.inputs.clone());
     localModuleNode.inputsText = ((MultiLineString)this.inputsText.clone());
     localModuleNode.outputsText = ((MultiLineString)this.outputsText.clone());
     localModuleNode.botHeight = this.botHeight;
     localModuleNode.midHeight = this.midHeight;
     localModuleNode.model = ((Model)this.model.clone());
     if (this.modelNode != null) {
       localModuleNode.modelNode = ((ModelNode)this.modelNode.clone());
       localModuleNode.modelNode.setModel(localModuleNode.model);
     }
     return localModuleNode;
   }

   public String toString() {
     String str = "Inputs: ";
     str = String.valueOf(str) + this.inputs;
     str = String.valueOf(str) + "Outputs: ";
     str = String.valueOf(str) + this.outputs;
     return str;
   }
 }


