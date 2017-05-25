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
        DataSource d = new DataSource("/Users/jamie/Documents/College/Senior/DataMining/Final " +
                "Project/CART/Data/CreditCards/credit-data.csv",numeric_indexes,categorical_indexes);
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
                        //if (cat_data.getClass() == "0"){

                        //}
                        //if cat_data.get
                        // add it if its not
                    } else {
                        int [] new_list = new int [] {0,0};
                        //new_list.add(data);
                        //classify_map.put(value, new_list);
                    }
                    // if category is not a key already, add it
                } else {
                    Map<String, int[]> new_map = new Hashtable<>();
                    String value = cat_data.get(i);
                    List<DataSource.Datapoint> new_list = new ArrayList<>();
                    //new_map.put(value, new_list);
                   // categories_to_points.put(i, new_map);
                }

            }
        }

    }
}
