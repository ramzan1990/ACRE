 package ui;

 import java.util.ArrayList;
 import javax.swing.ImageIcon;
 import javax.swing.UIManager;

 public class NetworkEditor
 {
   public static void main(String[] paramArrayOfString)
   {
     try
     {
       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
     }
     catch (Exception localException1) {
       System.out.println(localException1.getMessage());
     }
     GraphFrame localGraphFrame = new GraphFrame(new NetworkGraph());
     try {
       ArrayList localArrayList = new ArrayList();
       localArrayList.add(new ImageIcon(NetworkEditor.class.getResource("img/ic16.png")).getImage());
       localArrayList.add(new ImageIcon(NetworkEditor.class.getResource("img/ic32.png")).getImage());
       localArrayList.add(new ImageIcon(NetworkEditor.class.getResource("img/ic64.png")).getImage());
       localArrayList.add(new ImageIcon(NetworkEditor.class.getResource("img/ic128.png")).getImage());
       localGraphFrame.setIconImages(localArrayList);
     } catch (Exception localException2) {
       System.out.println(localException2.getMessage());
     }
     localGraphFrame.setVisible(true);
   }
 }


