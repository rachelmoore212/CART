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

        mapData(source.getData());
    }

    /*public double[] getBestCategoricalSplit(List<DataSource.Datapoint> data){
        for (int i = 0; i < categorical_indexes.length; i++){

        }
    }*/
    
}
