import java.util.ArrayList;


/**
 * Created by Rachel on 5/21/2017.
 */

// Store/edit the resulting tree, traverse it to get classification
public class BinaryTree {


    Node [] node_list = new Node[100];

    Node start_node = new Node(true);

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

    private class Node {

        public boolean isStart = false;
        public boolean isLeaf = true;
        public boolean isCategorical = false;
        public String move_left_categorical = null;
        public int move_left_less_than = 0;
        public Node left_node;
        public Node right_node;


        public Node(boolean isStart){

            isStart = isStart;
            isLeaf = true;

        }

        public void setCategoricalRule(String category){
            isLeaf = false;
            isCategorical = true;
            move_left_categorical = category;

        }

        private void setNumericRule(int value){
            isLeaf = false;
            isCategorical = false;
            move_left_less_than = value;
        }

    }


}


