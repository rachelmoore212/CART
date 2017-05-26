package CART;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamie on 5/21/17.
 */
public class DataSource {
    public List<Datapoint> getData() {
        return data;
    }

    private List<Datapoint> data = new ArrayList<>();

    public String[] getDataNumericalnames() {
        return dataNumericalnames;
    }

    public String[] getDataCategorialNames() {
        return dataCategorialNames;
    }

    private String[] dataCategorialNames;
    private String[] dataNumericalnames;


    public DataSource(String source, int[] numericalCategoires, int[] categoricalCategories, int
            classfiedVariable) {
        try {
            CSVReader reader = new CSVReader(new FileReader("Data/CreditCards/credit-data.csv"));

            // Checking the rows of credit data
            String[] creditRows = {};
            boolean passedFirst = true;
            if (source.endsWith("credit-data.csv")) {
                passedFirst = false;
                creditRows = null;
            }
            dataCategorialNames = new String[categoricalCategories.length];
            dataNumericalnames = new String[numericalCategoires.length];
//            List<Integer> toNumber = Arrays.stream(numericalCategoires).boxed().collect(Collectors
//                    .<Integer>toList());


            // Actually iterating over the data to generate the dataset
            List<String[]> myentries = reader.readAll();
            for (String[] s: myentries){
                if (passedFirst) {
                    if (creditRows!=null) {

                        // creating a new datapoint for storing the data
                        List<Double> numerical = new ArrayList<>();
                        List<String> categorical = new ArrayList<>();
                        for (int i: numericalCategoires){
                            numerical.add(Double.parseDouble(s[i]));
                        }
                        for (int i: categoricalCategories){
                            categorical.add(s[i]);
                        }

                        data.add(new Datapoint(numerical, categorical, s[classfiedVariable]));
                    } else {
                        creditRows = s;
                        for (int i: numericalCategoires){
                            dataNumericalnames[i] = s[numericalCategoires[i]];
                        }
                        for (int i: categoricalCategories){
                            dataCategorialNames[i] = s[categoricalCategories[i]];
                        }

                    }

                } else{
                    passedFirst = true;
                }
            }
            //System.out.print(data.toString());
            dataNames = creditRows;

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataSource(List<Datapoint> splitData) {
        this.data = splitData;
    }

    //Method that partitions the array into smaller chunks probablilistially for the purposes of
    // training
    public DataSource splitDataset(double factor) {
        if ((factor > 0)&& (factor < 1)) {
            DataSource output = new DataSource(new ArrayList<Datapoint>());
            //TODO do the acutal partitioning of the dataset

            return output;
        }
        else return null;
    }

    //public List<Datapoint> getPoints(int [] indexes){

        //return data.get(indexes);
    //}


    public final class Datapoint {
        List<Double> numericalData;
        List<String> categoricalData;
        String classification;

        public Datapoint(List<Double> numerical, List<String> categorical, String classification) {
            this.numericalData = numerical;
            this.categoricalData = categorical;
            this.classification = classification;
        }

        public List<Double> getNumericalData() {
            return numericalData;
        }

        public List<String> getCategoricalData() {
            return categoricalData;
        }

        public String getClassification(){
            return classification;
        }

        public String toString() {
            String output = "Datapoint:"+classification+" Numbers:{" +numericalData.toString()+"}, Categories " +
                    "={"+categoricalData.toString()+"}";
            return output;
        }
    }
}
