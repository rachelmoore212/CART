package CART;

/**
 * Created by Rachel on 5/21/2017.
 */
public class Node {

    public boolean isStart = false;
    public boolean isLeaf = true;
    public boolean isCategorical = false;
    public String move_left_categorical = null;
    public int move_left_less_than = 0;
    public int move_left_greater_than = 0;
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

}
