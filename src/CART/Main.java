package CART;

/**
 * Created by Rachel on 5/21/2017.
 */
public class Main {

    public static void main(String[] args){

        int [] categorical_indexes = new int [] {2,3,4,6,7,8,9,10,11};

        int [] numeric_indexes = new int [] {1,5,12,13,14,15,16,17,18,19,20,21,22,23};
        System.out.println("hello world");
        DataSource d = new DataSource("/Users/jamie/Documents/College/Senior/DataMining/Final " +
                "Project/CART/Data/CreditCards/credit-data.csv",numeric_indexes,categorical_indexes);
    }
}
