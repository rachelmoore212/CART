package CART;

import java.util.*;

/**
 * Created by Rachel on 5/21/2017.
 */
public class Main {
    static int [] categorical_indexes = new int [] {2,3,4,6,7,8,9,10,11};

    static int [] numeric_indexes = new int [] {1,5,12,13,14,15,16,17,18,19,20,21,22,23};
    public static void main(String[] args){


        System.out.println("hello world");
        DataSource source = new DataSource("Data/CreditCards/credit-data.csv",numeric_indexes,
                categorical_indexes, 24);

        Set<String> attributes = new HashSet<String>(Arrays.asList("meh","penis","vaginenis"));
        System.out.println(BinaryTree.getSubsets(attributes));

        //System.out.println(ClassifierModel.GINI(100,0));
        //System.out.println(ClassifierModel.GINI(90,10));

        DataSource accuracyEvaluation = source.splitDataset(0.2);
        DataSource crossValidation = source.splitDataset(0.2);
        //DataSource first30pts = source.splitDataset2(30);
        //System.out.println(first30pts.getData().size());


        //ClassifierModel model = new ClassifierModel(crossValidation, 10);
        BinaryTree tree = new BinaryTree(source, 30);

        System.out.println(ClassifierModel.checkAccuracy(tree,accuracyEvaluation));
        tree.crossValidate(crossValidation);
        double z = 1.95;
        tree.pruneTree(z);
        System.out.println(ClassifierModel.checkAccuracy(tree,accuracyEvaluation));
        //System.out.println(tree.start_node.isLeaf);
        //System.out.println(tree.start_node.getLeft_node());


        //mapData(source.getData());
    }

    /*public double[] getBestCategoricalSplit(List<DataSource.Datapoint> data){
        for (int i = 0; i < categorical_indexes.length; i++){

        }
    }*/

    public void mapData(List<DataSource.Datapoint> datapoints){
        // Our data


        // Our dictionary that maps index of a category to a dictionary of values in that category
        // to data that matches it
        Map<Integer,Map<String,List<DataSource.Datapoint>>> categories_to_points = new Hashtable();

        Map<Integer,Map<String, int[]>> categories_to_classifications = new Hashtable<>();

        // Add each datapoint to the dictionary
        for (DataSource.Datapoint data: datapoints){
            List<String> cat_data = data.getCategoricalData();
            for (int i = 0; i < cat_data.size(); i++){

                // Check if category is a key
                if(categories_to_points.containsKey(i)){

                    Map<String,List<DataSource.Datapoint>> value_map = categories_to_points.get(i);
                    String value = cat_data.get(i);

                    // Check if value at category is a key
                    if(value_map.containsKey(value)) {
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
                if(categories_to_classifications.containsKey(i)){

                    Map<String, int[]> classify_map = categories_to_classifications.get(i);
                    String value = cat_data.get(i);

                    // Check if value at category is a key
                    if(classify_map.containsKey(value)) {
                        int[] classify_array = classify_map.get(value);
                        if (cat_data.getClass().equals("0")) {
                            classify_array[0]++;
                        } else {
                            classify_array[1]++;
                        }

                    } else {
                        int [] classify_array = new int [] {0,0};
                        if (cat_data.getClass().equals("0")) {
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
                    int [] classify_array = new int [] {0,0};
                    if (cat_data.getClass().equals("0")) {
                        classify_array[0]++;
                    } else {
                        classify_array[1]++;
                    }
                    new_map.put(value, classify_array);
                    categories_to_classifications.put(i, new_map);
                }

            }
        }

    }
}
