package CART;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by jamie on 5/21/17.
 */
public class DataSource {

    private List<Datapoint> data = new ArrayList<>();
    private static String[] dataCategorialNames;
    private static String[] dataNumericalnames;


    public DataSource(String source, int[] numericalCategoires, int[] categoricalCategories, int
            classfiedVariable) {
        try {
            CSVReader reader = new CSVReader(new FileReader("Data/CreditCards/credit-data.csv"));

            // Checking the rows of credit data
            boolean creditRows = true;
            boolean passedFirst = true;
            if (source.endsWith("credit-data.csv")) {
                passedFirst = false;
                creditRows = false;
            }
            dataCategorialNames = new String[categoricalCategories.length];
            dataNumericalnames = new String[numericalCategoires.length];
//            List<Integer> toNumber = Arrays.stream(numericalCategoires).boxed().collect(Collectors
//                    .<Integer>toList());


            // Actually iterating over the data to generate the dataset
            List<String[]> myentries = reader.readAll();
            for (String[] s: myentries){
                if (passedFirst) {
                    if (creditRows) {

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
                        creditRows = true;
                        for (int i = 0; i < dataNumericalnames.length; i++){
                            dataNumericalnames[i] = s[numericalCategoires[i]];
                        }
                        for (int i = 0; i < dataCategorialNames.length; i++){
                            dataCategorialNames[i] = s[categoricalCategories[i]];
                        }

                    }

                } else{
                    passedFirst = true;
                }
            }
            //System.out.print(data.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataSource(List<Datapoint> splitData, String[] dataCategorialNames, String[] dataNumericalnames) {
        this.data = splitData;
        this.dataCategorialNames = dataCategorialNames;
        this.dataNumericalnames = dataNumericalnames;
    }

    //Method that partitions the array into smaller chunks probablilistially for the purposes of
    // training
    public DataSource splitDataset(double factor) {
        if ((factor > 0)&& (factor < 1)) {
            if (factor<(1.0/data.size())) {
                factor = 1.0/data.size();
            }
            if (factor>(1.0-(1.0/data.size()))) {
                factor = 1.0-1.0/data.size();
            }
            Collections.shuffle(data);

            List<Datapoint> newData = data.subList(0, (int)(data.size()*1.0* factor));
            data = data.subList((int)(data.size()*1.0* factor), data.size());

            DataSource output = new DataSource(newData, dataCategorialNames, dataNumericalnames);
            return output;
        }
        else return null;
    }

    //
    public DataSource splitDataset2(int number) {

        List<Datapoint> newData = data.subList(0, number);

        DataSource output = new DataSource(newData, dataCategorialNames, dataNumericalnames);
        return output;

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
    public static String[] getDataNumericalnames() {
        return dataNumericalnames;
    }

    public static String[] getDataCategorialNames() {
        return dataCategorialNames;
    }

    public List<Datapoint> getData() {
        return data;
    }
}
