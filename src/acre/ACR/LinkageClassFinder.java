
package acre.ACR;



import java.util.ArrayList;

public class LinkageClassFinder {

    private static int index = 0;
    private static ArrayList<ArrayList<Node>> linkageClasses = null;

    public static ArrayList<ArrayList<Node>> find(Graph graph) {
        index = 0;
        linkageClasses = new ArrayList<ArrayList<Node>>();
        ArrayList<Node> nodes = graph.nodes;
        int len = nodes.size();

        for( int i = 0; i < len; i++ ) {
            Node node = nodes.get(i);
            node.visited  = false;
            node.index = i;
        }

        for( int i = len - 1; i >= 0; i-- ) {
            boolean changed = false;
            for( int j = len - 1; j >= 0; j-- ) {
                Node node = nodes.get(j);
                changed = (changed || findFromRoot( graph, node ));


            }
            if(!changed) {
                break;
            }


        }

        for( int i = len - 1; i >= 0; i-- ) {
            Node node = nodes.get(i);
            if( !node.visited ) {
                ArrayList<Node> sccList = createLinkageClassFromRoot( nodes, i );
                linkageClasses.add(sccList);
            }
        }
        return linkageClasses;
    }

    private static boolean findFromRoot( Graph graph, Node node ) {
        boolean changed = false;
        int max = node.index;
        ArrayList<Node> adjacents = graph.getAdjacentNodes(node);
        for (Node reachable : adjacents) {
            if( reachable.index > max ) {
                max = reachable.index;
                changed = true;
            }
        }
        for (Node reachable : adjacents) {
            reachable.index = max;
        }
        node.index = max;
        return changed;


    }


    private static ArrayList<Node> createLinkageClassFromRoot(ArrayList<Node> nodes, int index ) {
        Node node = nodes.get(index);
        ArrayList<Node> sccList = new ArrayList<Node>();
        int key = node.index;

        for( int i = key; i >= 0; i-- ) {
            node = nodes.get(i);
            if( node.index == key ) {
                sccList.add(node);
                node.visited = true;
            }
        }
        return sccList;
    }

}



