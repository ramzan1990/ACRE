package acre.ui;

import java.io.Serializable;
import java.util.ArrayList;

public class WorkSpace
  implements Serializable
{
  public ArrayList<ModuleNode> tools;
  public ArrayList<ModuleNode> nodes;
  public int modelcounter;
  public int modlenodeconter;
  public int minEdges;
  public int maxEdges;
}


