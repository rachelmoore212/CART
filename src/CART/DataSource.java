package CART;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by jamie on 5/21/17.
 */
public class DataSource {
    public List<Datapoint> getData() {
        return data;
    }

    private List<Datapoint> data;


    public DataSource(String source, int[] numericalCategoires) {
        try {
            CSVReader reader = new CSVReader(new FileReader
                    ("/Users/jamie/Documents/College/Senior/DataMining/Final Project/CART/Data/CreditCards/credit-data.csv"));

            List<String[]> myentries = reader.readAll();
            for (Object s: myentries){
                System.out.print(s.toString());
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method that partitions the array into smaller chunks probablilistially for the purposes of
    // training
    public DataSource splitDataset(double factor) {
        if ((factor > 0)&& (factor < 1)) {
            DataSource output = new DataSource();
            //TODO do the acutal partitioning of the dataset

            return output;
        }
    }

    public class Datapoint {
        double[] numericalData;
        String[] categoricalData;

        public double[] getNumericalData() {
            return numericalData;
        }

        public String[] getCategoricalData() {
            return categoricalData;
        }
    }
}
