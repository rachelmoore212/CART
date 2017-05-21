import java.util.ArrayList;


/**
 * Created by Rachel on 5/21/2017.
 */

// Store/edit the resulting tree, traverse it to get classification
public class BinaryTree {


    Node [] node_list = new Node[100];

    Node start_node = new Node();

    ArrayList<String> categories = new ArrayList<String>();

    Integer min_leaf_size = 0;

    public BinaryTree(ArrayList<String> categories, int min_leaf_size){
        categories = categories;
        min_leaf_size = min_leaf_size;

    }

    public String classifyNode(){

        return "TBD";
    }

    public void addSplit(Node n, String category, int val){
        //add split
    }
    public void pruneNode(Node n){
        //prune shit
    }




}


