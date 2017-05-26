package CART;

import java.util.ArrayList;


/**
 * Created by Rachel on 5/21/2017.
 */

// Store/edit the resulting tree, traverse it to get classification
public class BinaryTree {


    public ArrayList<Node>  node_list = new ArrayList<Node>();

    public Node start_node = new Node(true);

    public ArrayList<String> categories = new ArrayList<String>();

    public Integer min_leaf_size = 0;

    public BinaryTree(ArrayList<String> categories, int min_leaf_size){
        categories = categories;
        min_leaf_size = min_leaf_size;


    }

    public String classifyNode(){

        return "TBD";
    }

//    public void addCategoricalSplit(Node node, int category){
//        node.setCategoricalRule(category);
//        Node left_node = new Node(false);
//        Node right_node = new Node(false);
//        node.setLeft_node(left_node);
//        node.setRight_node(right_node);
//        node_list.add(left_node);
//        node_list.add(right_node);
//    }
//    public void addNumericSplit(Node node, int value){
//        node.setNumericRule(value);
//        Node left_node = new Node(false);
//        Node right_node = new Node(false);
//        node.setLeft_node(left_node);
//        node.setRight_node(right_node);
//        node_list.add(left_node);
//        node_list.add(right_node);
//    }
    public String traverse(DataSource.Datapoint data){

        Node current = start_node;

        while (current.isLeaf == false){

            if (current.isCategorical == true){
                int index = current.category_value;
                //if (data[index] == current.getMove_left_categorical()){

                //}
            }



        }

        return false;
    }

    public void pruneNode(Node n){
        //prune shit
    }

    private class Node {

        public boolean isStart = false;
        public boolean isLeaf = true;
        public boolean isCategorical = false;
        public int category_value = 0;
        public int numeric_value = 0;
        public String[] move_left_categorical = {};
        public double move_left_less_than = 0;
        public Node left_node;
        public Node right_node;




        public Node(boolean start){

            isStart = start;
            isLeaf = true;

        }

        public void setCategoricalRule(int index, String[] categories){
            isLeaf = false;
            isCategorical = true;
            category_value = index;
            move_left_categorical = categories;

        }

        private void setNumericRule(int index, int value){
            isLeaf = false;
            isCategorical = false;
            numeric_value = index;
            move_left_less_than = value;
        }
        public boolean isStart() {
            return isStart;
        }

        public void setStart(boolean start) {
            isStart = start;
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setLeaf(boolean leaf) {
            isLeaf = leaf;
        }

        public boolean isCategorical() {
            return isCategorical;
        }

        public void setCategorical(boolean categorical) {
            isCategorical = categorical;
        }

        public Integer getMove_left_categorical() {
            return move_left_categorical;
        }

        public void setMove_left_categorical(int move_left_categorical) {
            this.move_left_categorical = move_left_categorical;
        }

        public int getMove_left_less_than() {
            return move_left_less_than;
        }

        public void setMove_left_less_than(int move_left_less_than) {
            this.move_left_less_than = move_left_less_than;
        }

        public Node getLeft_node() {
            return left_node;
        }

        public void setLeft_node(Node left_node) {
            this.left_node = left_node;
        }

        public Node getRight_node() {
            return right_node;
        }

        public void setRight_node(Node right_node) {
            this.right_node = right_node;
        }
    }


}


