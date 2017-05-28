package CART;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * Created by Rachel on 5/21/2017.
 */

// Store/edit the resulting tree, traverse it to get classification
public class BinaryTree {


    public ArrayList<Node> node_list = new ArrayList<Node>();

    public Node start_node;

    public ArrayList<String> categories = new ArrayList<String>();

    public Integer min_leaf_size = 0;

    public BinaryTree(DataSource dataSource, int min_leaf) {

        int num_categories = dataSource.getDataCategorialNames().length;
        int num_numeric = dataSource.getDataNumericalnames().length;
        min_leaf_size = min_leaf;

        start_node = new Node(true);
        classifyNode(start_node, dataSource.getData(), num_categories, num_numeric);
        writeGraph(node_list);
        graphRecursionPrint(0, start_node);

    }


    /**
     * Primary class used in the createion of the binary tree, this method calculates the best
     * GINI for either a numerical or categorical
     *
     * @param n
     * @param datapoints
     * @param num_cat
     * @param num_numeric
     */
    public void classifyNode(Node n, List<DataSource.Datapoint> datapoints, int num_cat, int
            num_numeric) {

        Map[] map_list = mapData(datapoints);
        //Map<Integer, Map<String, List<DataSource.Datapoint>>> categories_to_points = map_list[0];

        Map<Integer, Map<String, int[]>> categories_to_classifications = map_list[1];
        System.out.println("made new maps");
        //Double[] categorical_ginis = new Double[num_cat];
        //Calculating the total size of the keys in the dataset
        int[] canonvalues = new int[2];
        Map<String, int[]> internalmap= categories_to_classifications.get
                (categories_to_classifications.keySet().iterator().next());
        for (int[] entry : internalmap.values()) {
            for (int i = 0; i<entry.length; i++){
                canonvalues[i] += entry[i];
            }
        }

        // Setting the GINI to its current value
        double gini_original = ClassifierModel.GINI(canonvalues[0],canonvalues[1]);
        double most_max_gini = -10.0;
        int best_gini_index = -1;
        Set<String> very_best_comb = new HashSet<>();
        double splitValue = 0;

        Double[] numeric_ginis = new Double[num_numeric];

        for (int i = 0; i < num_cat; i++) {
            //Double[] ginis = new Double[(int)Math.pow(2, categories_to_classifications.get(i).keySet().size())-1];
            double max_gini = -1000;
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
                        gini = -1000;
                    } else {
                        //gini = ClassifierModel.GINI(sum_left_0, sum_left_1) + ClassifierModel.GINI(sum_right_0, sum_right_1);
                        double gini_left = ClassifierModel.GINI(sum_left_0,sum_left_1);
                        double gini_right = ClassifierModel.GINI(sum_right_0,sum_right_1);
                        double left_weight = (1.0*(sum_left_0 + sum_left_1)/
                                (sum_left_0+sum_left_1+sum_right_0+sum_right_1));
                        gini = gini_original - left_weight*gini_left - (1.0 - left_weight)
                                *gini_right;
                    }
                    if (gini > max_gini) {
                        max_gini = gini;
                        best_comb = comb;
                    }
                }

            }
            if (max_gini > most_max_gini) {
                most_max_gini = max_gini;
                very_best_comb = best_comb;
                best_gini_index = i;
            }
        }

        //Now testing all the numerical attributes
        for (int i = 0; i < num_numeric; i++) {
            int sum_left_0 = 0;
            int sum_left_1 = 0;
            int sum_right_0 = canonvalues[0];
            int sum_right_1 = canonvalues[1];

            final int finalI = i;
            Collections.sort(datapoints, ((o1, o2) -> {
                return o1.getNumericalData().get(finalI).compareTo(o2
                        .getNumericalData().get(finalI));
            }));

            double lastVlaue = -1212341.1234123;//TODO bullshit
            for(int pointer = 0; pointer < datapoints.size(); pointer++) {
                if (datapoints.get(pointer).getClassification().equals("0")) {
                    sum_left_0++;
                    sum_right_0--;
                } else {
                    sum_left_1++;
                    sum_right_1--;
                }
                if ((pointer+1>=min_leaf_size)&&(pointer<(datapoints.size()-min_leaf_size))) {
                    double value = datapoints.get(pointer).getNumericalData().get(i);
                    double nextValue = datapoints.get(pointer+1).getNumericalData().get(i);

                    if (nextValue!= value) {
                        double gini_left = ClassifierModel.GINI(sum_left_0, sum_left_1);
                        double gini_right = ClassifierModel.GINI(sum_right_0, sum_right_1);
                        double left_weight = (1.0 * (sum_left_0 + sum_left_1) /
                                (sum_left_0 + sum_left_1 + sum_right_0 + sum_right_1));
                        double gini = gini_original - left_weight * gini_left - (1.0 - left_weight)
                                * gini_right;
                        //System.out.println("value is: " + value);

                        if (gini > most_max_gini) {
                            most_max_gini = gini;
                            very_best_comb.clear();
                            best_gini_index = i;
                            splitValue = value;
                            //System.out.println("pointer was: " + pointer);
                        }
                    }
                }
            }
        }



        // Here will be code for finding best numeric split
        //if (!(most_min_gini<ClassifierModel.GINI(canonvalues[0],canonvalues[1]))) {
        if (most_max_gini < 0) {
            String assignedValue = "1";
            if (canonvalues[0]>canonvalues[1]) {//TODO make this robust for many assignments
                assignedValue = "0";
            }
            n.setLeaf(assignedValue);
            System.out.println("made a leaf node");
            return;
        }


        // Determining what the new split for the data will be based on the rule
        List<DataSource.Datapoint> new_data_left;
        List<DataSource.Datapoint> new_data_right;
        // Calculations for numeric data
        if (very_best_comb.isEmpty()) {
            addNumericSplit(n, best_gini_index, splitValue);
            //get list of data
            new_data_left = new ArrayList<>();
            new_data_right = new ArrayList<>();
            for (DataSource.Datapoint data : datapoints) {
                if (data.getNumericalData().get(best_gini_index) <= splitValue) {
                    new_data_left.add(data);
                } else {
                    new_data_right.add(data);
                }
            }

        // Calculations for categorical data
        } else {
            addCategoricalSplit(n, best_gini_index, very_best_comb);
            new_data_left = new ArrayList<>();
            new_data_right = new ArrayList<>();
            for (DataSource.Datapoint data : datapoints) {
                if (very_best_comb.contains(data.getCategoricalData().get(best_gini_index))) {
                    new_data_left.add(data);
                } else {
                    new_data_right.add(data);
                }
            }
        }
        System.out.println("Classify right/left nodes");
        System.out.println("Right side has "+new_data_right.size()+" things");
        System.out.println("Left side has "+new_data_left.size()+" things");
        if (very_best_comb.isEmpty()) {
            System.out.println("Numerical rule problem value is: "+splitValue);
            System.out.println("Numerical rule problem name is: "+DataSource.getDataNumericalnames()
                    [best_gini_index]);
        } else {
            System.out.println("The category split was: " + DataSource.getDataCategorialNames()[best_gini_index]);
            System.out.println("The categorical rule is: " + very_best_comb);
        }


        System.out.println("Most Min GINIS was: "+most_max_gini);
        Node left = n.getLeft_node();
        Node right = n.getRight_node();
        classifyNode(left, new_data_left, num_cat, num_numeric);
        classifyNode(right, new_data_right, num_cat, num_numeric);

    }















    public void writeGraph(ArrayList<Node> nodes){
        FileWriter fileWriter = null;
        String fileName = "nodes.txt";
        String COMMA_DELIMITER = " ";
        int counter = 2;
        String NEW_LINE_SEPARATOR = "\n";
        try {
            fileWriter = new FileWriter(fileName);
            //Add a new line separator after the header
            //Write a new student object list to the CSV file
            for (Node node : nodes) {
                if (node.isLeaf){
                    continue;
                }
                String node_name = node.toString();
                Node node_left = node.getLeft_node();
                Node node_right = node.getRight_node();
                String node_left_name = node_left.toString();
                String node_right_name = node_right.toString();
                if (node_left_name.equals("0") || node_left_name.equals("1")){
                    node_left_name = node_left_name + "_" + Integer.toString(counter);
                    counter++;
                }
                if (node_right_name.equals("0") || node_right_name.equals("1")){
                    node_right_name = node_right_name + "_" + Integer.toString(counter);
                    counter++;
                }
                fileWriter.append(node_name);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(node_left_name);
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(node_name);
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(node_right_name);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }

    private void graphRecursionPrint(int depth, Node curnode) {
        String output = "";
        for (int i = depth; i > 0; i--) {
            output = output + " | ";
        }
        System.out.println(output + curnode.toString());

        if (!curnode.isLeaf){
            graphRecursionPrint(depth + 1, curnode.getLeft_node());
            graphRecursionPrint(depth + 1, curnode.getRight_node());
        }
    }


//    /**
//     * Method that pefroms the voting of datapoints in the classifcation
//     * @param datapoints
//     * @return
//     */
//    public String decideClassification(List<DataSource.Datapoint> datapoints){
//
//    }

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
//        for (int i : categories_to_classifications.keySet()) {
//            System.out.println(DataSource.getDataCategorialNames()[i] +
//                    " Category");
//            for (String s : categories_to_classifications.get(i).keySet()) {
//                System.out.println(s + "     [" +categories_to_classifications.get(i).get(s)[0]+
//                        " , " + categories_to_classifications.get(i).get(s)[1] + "]");
//            }
//        }

        whyJavaWhy[0] = categories_to_points;
        whyJavaWhy[1] = categories_to_classifications;

        return whyJavaWhy;
    }


    public void addCategoricalSplit(Node node, int category_index, Set<String> category) {
        node.setCategoricalRule(category_index, category);
        Node left_node = new Node(false);
        Node right_node = new Node(false);
        node.setLeft_node(left_node);
        node.setRight_node(right_node);
        node_list.add(left_node);
        node_list.add(right_node);
    }

    public void addNumericSplit(Node node, int index, double value) {
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


    /**
     * Node class
     *
     * Used to store information in the binary tree for later traversal.
     */
    public class Node {

        public boolean isStart = false;
        public boolean isLeaf;
        public boolean isCategorical = false;
        public String assignedValue = "FOOO";
        public int category_value = 0;
        public Set<String> move_left_categorical = new HashSet<String>();
        public double move_left_less_than = 0;
        public Node left_node;
        public Node right_node;
        public String node_name = "";


        public String traverse(DataSource.Datapoint data) {
            //Return true if it is a leaf node
            if (isLeaf) {
                return assignedValue;
            }

            // Otherwise traverse based on the rule
            if (isCategorical == true) {
                int index = category_value;
                Set<String> leftMove = move_left_categorical;
                if (leftMove.contains(data.getCategoricalData().get(index))) {
                    return left_node.traverse(data);
                } else {
                    return right_node.traverse(data);
                }

            } else {//TODO be very sure about less than or equal to
                if (data.getNumericalData().get(category_value) <= move_left_less_than) {
                    return left_node.traverse(data);
                }
                return right_node.traverse(data);
            }
        }

        public Node(boolean start){

            isStart = start;
            isLeaf = false;

        }

        @Override
        public String toString(){
            if (isLeaf) {
                return assignedValue;
            }
            if (isCategorical) {
                Set<String> cats = move_left_categorical;
                node_name = node_name + DataSource.getDataCategorialNames()[category_value] + "_{";

                for (String cat : cats) {
                    node_name = node_name + cat + "_";
                }
                node_name = node_name + "}";
            } else {
                node_name = node_name + DataSource.getDataNumericalnames()[category_value] + "_<"
                        + Double.toString(move_left_less_than);
            }
            return node_name;
        }

        public void setCategoricalRule(int index, Set<String> categories){
            isLeaf = false;
            isCategorical = true;
            category_value = index;
            move_left_categorical = categories;

        }

        private void setNumericRule(int index, double value){
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


