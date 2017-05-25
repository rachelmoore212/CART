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
    public void firstPass(DataSource d){

        List<DataSource.Datapoint> datapoints = d.getData();

        Map<Integer,Map<String,List<DataSource.Datapoint>>> categories_to_points = new Hashtable();

        for (DataSource.Datapoint data: datapoints){
            List<String> cat_data = data.getCategoricalData();
            for (int i = 0; i < cat_data.size(); i++){
                if(categories_to_points.containsKey(i)){

                    Map<String,List<DataSource.Datapoint>> value_map = categories_to_points.get(i);
                    String value = cat_data.get(i);
                    if(value_map.containsKey(value)) {
                        List<DataSource.Datapoint> object_list = value_map.get(value);
                        object_list.add(data);
                    } else {
                        List<DataSource.Datapoint> new_list = new ArrayList<>();
                        new_list.add(data);
                        value_map.put(value, new_list);

                    }
                } else {
                    Map<String, List<DataSource.Datapoint>> new_map = new Hashtable<>();
                    String value = cat_data.get(i);
                    List<DataSource.Datapoint> new_list = new ArrayList<>();
                    new_map.put(value, new_list);
                    categories_to_points.put(i, new_map);
                }

            }
        }

    }
}
