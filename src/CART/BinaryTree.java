package CART;

import java.util.*;


/**
 * Created by Rachel on 5/21/2017.
 */

// Store/edit the resulting tree, traverse it to get classification
public class BinaryTree {


    public ArrayList<Node> node_list = new ArrayList<Node>();

    public Node start_node = new Node(true);

    public ArrayList<String> categories = new ArrayList<String>();

    public Integer min_leaf_size = 0;

    public BinaryTree(DataSource dataSource, int min_leaf) {

        int num_categories = dataSource.getDataCategorialNames().length;
        int num_numeric = dataSource.getDataNumericalnames().length;
        min_leaf_size = min_leaf;

        Node start_node = new Node(true);


    }

    public String classifyNode(List<DataSource.Datapoint> datapoints, int num_cat, int num_numeric) {

        Map[] map_list = mapData(datapoints);
        Map<Integer, Map<String, List<DataSource.Datapoint>>> categories_to_points = map_list[0];

        Map<Integer, Map<String, int[]>> categories_to_classifications = map_list[1];

        //Double[] categorical_ginis = new Double[num_cat];
        double most_min_gini = 1000;
        int best_gini_index = -1;
        Set<String> very_best_comb = new HashSet<>();

        Double[] numeric_ginis = new Double[num_numeric];

        for (int i = 0; i < num_cat; i++) {
            //Double[] ginis = new Double[(int)Math.pow(2, categories_to_classifications.get(i).keySet().size())-1];
            double min_gini = 1000;
            Set<String> best_comb = new HashSet<>();
            Map<String, int[]> attribute_map = categories_to_classifications.get(i);
            Set<String> keys = attribute_map.keySet();
            List<Set<String>> combinations = getSubsets(keys);
            for (Set<String> comb : combinations) {
                if (comb.size() < Math.ceil(keys.size() / 2)) {
                    int sum_left_0 = 0;
                    int sum_left_1 = 0;
                    int sum_right_0 = 0;
                    int sum_right_1 = 0;
                    for (String key : keys) {
                        if (comb.contains(key)) {
                            sum_left_0 = sum_left_0 + attribute_map.get(key)[0];
                            sum_left_1 = sum_left_1 + attribute_map.get(key)[1];
                        } else {
                            sum_right_0 = sum_right_0 + attribute_map.get(key)[0];
                            sum_right_1 = sum_right_1 + attribute_map.get(key)[1];
                        }
                    }
                    double gini = 1000;
                    if (((sum_left_0 + sum_left_1) < min_leaf_size )||((sum_right_0 + sum_right_1) < min_leaf_size)){
                        gini = 1000;
                    } else {
                        gini = ClassifierModel.GINI(sum_left_0, sum_left_1) + ClassifierModel.GINI(sum_right_0, sum_right_1);
                    }
                    if (gini < min_gini) {
                        min_gini = gini;
                        best_comb = comb;
                    }
                }

            }
            if (min_gini < most_min_gini) {
                most_min_gini = min_gini;
                very_best_comb = best_comb;
                best_gini_index = i;
            }
        }

        // Here will be code for finding best numeric split

        return "TBD";
    }

    /**
     * Method that calucaltes all the subsets of a particular subset array for
     *
     * @param attributes
     * @return
     */
    public static List<Set<String>> getSubsets(Set<String> attributes) {
        String[] sets = attributes.toArray(new String[attributes.size()]);

        int n = attributes.size();
        List<Set<String>> output = new ArrayList<Set<String>>();

        // Run a loop for printing all 2^n
        // subsets one by obe
        for (int i = 0; i < (1 << n); i++) {
            Set<String> toadd = new HashSet<>();

            //
            for (int j = 0; j < n; j++)

                if ((i & (1 << j)) > 0)
                    toadd.add(sets[j]);

            output.add(toadd);
        }
        return output;
    }

    static public Map[] mapData(List<DataSource.Datapoint> datapoints) {
        // Our data

        Map[] whyJavaWhy = new Map[2];
        // Our dictionary that maps index of a category to a dictionary of values in that category
        // to data that matches it
        Map<Integer, Map<String, List<DataSource.Datapoint>>> categories_to_points = new Hashtable();

        Map<Integer, Map<String, int[]>> categories_to_classifications = new Hashtable<>();

        // Add each datapoint to the dictionary
        for (DataSource.Datapoint data : datapoints) {
            List<String> cat_data = data.getCategoricalData();
            for (int i = 0; i < cat_data.size(); i++) {

                // Check if category is a key
                if (categories_to_points.containsKey(i)) {

                    Map<String, List<DataSource.Datapoint>> value_map = categories_to_points.get(i);
                    String value = cat_data.get(i);

                    // Check if value at category is a key
                    if (value_map.containsKey(value)) {
                        List<DataSource.Datapoint> object_list = value_map.get(value);
                        object_list.add(data);
                        // add it if its not
                    } else {
                        List<DataSource.Datapoint> new_list = new ArrayList<>();
                        new_list.add(data);
                        value_map.put(value, new_list);
                    }
                    // if category is not a key already, add it
                } else {
                    Map<String, List<DataSource.Datapoint>> new_map = new Hashtable<>();
                    String value = cat_data.get(i);
                    List<DataSource.Datapoint> new_list = new ArrayList<>();
                    new_map.put(value, new_list);
                    categories_to_points.put(i, new_map);
                }

                // Check if category is a key
                if (categories_to_classifications.containsKey(i)) {

                    Map<String, int[]> classify_map = categories_to_classifications.get(i);
                    String value = cat_data.get(i);

                    // Check if value at category is a key
                    if (classify_map.containsKey(value)) {
                        int[] classify_array = classify_map.get(value);
                        if (data.getClassification().equals("0")) {
                            classify_array[0]++;
                        } else {
                            classify_array[1]++;
                        }

                    } else {
                        int[] classify_array = new int[]{0, 0};
                        if (data.getClassification().equals("0")) {
                            classify_array[0]++;
                        } else {
                            classify_array[1]++;
                        }
                        classify_map.put(value, classify_array);
                    }
                    // if category is not a key already, add it
                } else {
                    Map<String, int[]> new_map = new Hashtable<>();
                    String value = cat_data.get(i);
                    int[] classify_array = new int[]{0, 0};
                    if (data.getClassification().equals("0")) {
                        classify_array[0]++;
                    } else {
                        classify_array[1]++;
                    }
                    new_map.put(value, classify_array);
                    categories_to_classifications.put(i, new_map);
                }

            }
        }
        //System.out.println(categories_to_classifications.toString());
        //System.out.println(Arrays.toString(categories_to_classifications.entrySet().toArray()));
        for (int i : categories_to_classifications.keySet()) {
            System.out.println(i + "stuffffffffffffffffffffff");
            for (String s : categories_to_classifications.get(i).keySet()) {
                System.out.println(s + "     " + categories_to_classifications.get(i).get(s)[1]);
            }
        }

        whyJavaWhy[0] = categories_to_points;
        whyJavaWhy[1] = categories_to_classifications;

        return whyJavaWhy;
    }


    public void addCategoricalSplit(Node node, int category_index, String[] category) {
        node.setCategoricalRule(category_index, category);
        Node left_node = new Node(false);
        Node right_node = new Node(false);
        node.setLeft_node(left_node);
        node.setRight_node(right_node);
        node_list.add(left_node);
        node_list.add(right_node);
    }

    public void addNumericSplit(Node node, int index, int value) {
        node.setNumericRule(index, value);
        Node left_node = new Node(false);
        Node right_node = new Node(false);
        node.setLeft_node(left_node);
        node.setRight_node(right_node);
        node_list.add(left_node);
        node_list.add(right_node);
    }

    public String findAssignment(DataSource.Datapoint datapoint) {
        return start_node.traverse(datapoint);
    }

    public void pruneNode(Node n){
        //prune shit
    }

    private class Node {

        public boolean isStart = false;
        public boolean isLeaf = true;
        public boolean isCategorical = false;
        public String assignedValue = "";
        public int category_value = 0;
        public String[] move_left_categorical = {};
        public double move_left_less_than = 0;
        public Node left_node;
        public Node right_node;


        public String traverse(DataSource.Datapoint data) {
            //Return true if it is a leaf node
            if (isLeaf) {
                return assignedValue;
            }

            // Otherwise traverse based on the rule
            if (isCategorical == true) {
                int index = category_value;
                String[] leftMove = move_left_categorical;
                for (String s : leftMove) {
                    if (data.getCategoricalData().get(index).equals(s)) {
                        return left_node.traverse(data);
                    }
                }
                return right_node.traverse(data);

            } else {//TODO be very sure about less than or equal to
                if (data.getNumericalData().get(category_value) <= move_left_less_than) {
                    return left_node.traverse(data);
                }
                return right_node.traverse(data);
            }
        }

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
            category_value = index;
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

        public void setLeaf(String assignedValue) {
            isLeaf = true;
            this.assignedValue = assignedValue;
        }

        public boolean isCategorical() {
            return isCategorical;
        }

        public void setCategorical(boolean categorical) {
            isCategorical = categorical;
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


