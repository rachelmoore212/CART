package CART;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by jamie on 5/25/17.
 */
public class ClassifierModel {
    DataSource dataSource;
    Node classiferRoot;

    //Constructs the model and trains it on the dataset, where K is the min number of points in a
    // bin
    public ClassifierModel(DataSource d, int K) {
        this.dataSource = d;
        classiferRoot = recursiveClassify(d.getData(),K);
    }

    //Method that runs the node tree thing to do the complmacated mathematitics
    private Node recursiveClassify(List<DataSource.Datapoint> nodeAssignments ,int K) {

    }


    //Method that classifies the data after its done
    public String ClassifyDatapoint(DataSource.Datapoint point){
        return "FOOOOOOOOOOOOO";
    }





    static public void mapData(List<DataSource.Datapoint> datapoints){
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
                        if (data.getClassification().equals("0")) {
                            classify_array[0]++;
                        } else {
                            classify_array[1]++;
                        }

                    } else {
                        int [] classify_array = new int [] {0,0};
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
                    int [] classify_array = new int [] {0,0};
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
        for(int i:categories_to_classifications.keySet()){
            System.out.println(i + "stuffffffffffffffffffffff");
            for(String s: categories_to_classifications.get(i).keySet()){
                System.out.println(s+"     "+categories_to_classifications.get(i).get(s)[1]);
            }
        }
}
