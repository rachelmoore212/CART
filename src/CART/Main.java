package CART;

/**
 * Created by Rachel on 5/21/2017.
 */
public class Main {

    public static void main(String[] args){

        System.out.println("hello world");
        DataSource d = new DataSource("/Users/jamie/Documents/College/Senior/DataMining/Final " +
                "Project/CART/Data/CreditCards/credit-data.csv",new int[]{10,10,2,5,30,65},new
                int[]{1,2,3,4});
    }
}
