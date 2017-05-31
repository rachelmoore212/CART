package CART;

import java.util.*;

/**
 * Main class from which the code is expected to be executed
 */
public class CART {
    // Variables needed to turn on different datasets in the code
    static int [] categorical_indexes = new int [] {2,3,4,6,7,8,9,10,11};
    static int [] numeric_indexes = new int [] {1,5,12,13,14,15,16,17,18,19,20,21,22,23};
    static String[] targetValue = new String[]{"0","1"};
    static int classifyIndex = 24;
    static String datasource = "Data/CreditCards/credit-data.csv";


    static int [] mushroom_categorical_indexes = new int [] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,
            17,18, 19,20,21,22};
    static int [] mushroom_numeric_indexes = new int [] {};
    static String[] mushroom_targetValue = new String[]{"e","p"};
    static int mushroom_classifyIndex = 0;
    static String mushroom_datasource = "Data/Mushroom/agaricus-lepiota.data";


    static int [] adult_categorical_indexes = new int [] {1,3,5,6,7,8,9,13};
    static int [] adult_numeric_indexes = new int [] {0,2,4,10,11,12};
    static String[] adult_targetValue = new String[]{"<=50K",">=50K"};
    static int adult_classifyIndex = 14;
    static String adult_datasource = "Data/Money/adult.data";


    /**
     * Command line arguments
     * @param args
     */
    public static void main(String[] args){
        int min_leaf_size = 50;
        pruningmethod pruning = pruningmethod.noprune;
        double Zvalue;
        double crossValidationPercent;
        boolean randomForest = false;


        // Parsing of command line arguments
        if ((args.length == 2) || (args.length == 4) || (args.length == 6)) {
            if (args[0].equals("credit")) {
                // default behavior

            } else if (args[0].equals("mushroom")) {
                categorical_indexes = mushroom_categorical_indexes;
                numeric_indexes = mushroom_numeric_indexes;
                targetValue = mushroom_targetValue;
                classifyIndex = mushroom_classifyIndex;
                datasource = mushroom_datasource;

            } else if (args[0].equals("adult")) {
                categorical_indexes = adult_categorical_indexes;
                numeric_indexes = adult_numeric_indexes;
                targetValue = adult_targetValue;
                classifyIndex = adult_classifyIndex;
                datasource = adult_datasource;

            } else {
                printHelpMessage();
                System.exit(1);
            }

            try {
                min_leaf_size = Integer.parseInt(args[1]);

            } catch (NumberFormatException e) {
                System.out.print("please provide a min_index_size in integer format");
                printHelpMessage();
                System.exit(1);
            }

            // Setting the pruning values
            if ((args.length == 6)||((args.length==4)&& (args[2].equals("randomForest")))) {


            }


            try {
                if (args[2].equals("crossValidation")) {
                    pruning = pruningmethod.crossValidation;
                    crossValidationPercent = Double.parseDouble(args[3]);

                } else if (args[2].equals("pessimistic")) {
                    pruning = pruningmethod.pessimistic;
                    Zvalue = Double.parseDouble(args[3]);

                } else {
                    printHelpMessage();
                    System.exit(1);
                }


            } catch (NumberFormatException e) {
                System.out.print("please provide a valid pruning parameter in decimal format " +
                        "format");
                printHelpMessage();
                System.exit(1);
            }




        } else {
            printHelpMessage();
            System.exit(1);
        }

        // Reading in the datasource and parsing it into a readable form
        DataSource source = new DataSource(datasource,numeric_indexes,
                categorical_indexes, classifyIndex);

        // Splitting off a portion of the dataset for accuracy evalution
        DataSource accuracyEvaluation = source.splitDataset(0.2, false);
        DataSource crossValidation;

        if (pruning==pruningmethod.crossValidation) {
            crossValidation = source.splitDataset(0.2, false);
        }


//        RandomForestModel model = new RandomForestModel(source,0.8,100,50,1.95);
//        System.out.println(model.checkAccuracy(accuracyEvaluation));

        BinaryTree tree = new BinaryTree(source, 100);
        BinaryTree tree1 = ClassifierModel.crossValidate(tree, crossValidation);
        //tree.pruneTree(1.95);
        tree.graphRecursionPrint(0,tree.start_node);
        System.out.println(ClassifierModel.checkAccuracy(tree,accuracyEvaluation));
        System.out.println(ClassifierModel.checkAccuracy(tree1,accuracyEvaluation));


//        int [] min_leafs = new int [] {1, 5, 10, 15, 20, 30, 40, 50, 60, 70, 80, 90,100};
//        String results = "[";
//        for (int i : min_leafs){
//            System.out.println(i);
//            BinaryTree tree = new BinaryTree(source, i);
//            tree.pruneTree(1);
//            results = results + Double.toString(ClassifierModel.checkAccuracy(tree,accuracyEvaluation)) + ", ";
//        }
//        System.out.println(results);
        /*
        //ClassifierModel model = new ClassifierModel(crossValidation, 10);
        BinaryTree tree = new BinaryTree(source, 20);

        System.out.println(ClassifierModel.checkAccuracy(tree,accuracyEvaluation));
        //tree.crossValidate(crossValidation);
        double z = 1.95;
        tree.pruneTree(z);
        System.out.println(ClassifierModel.checkAccuracy(tree,accuracyEvaluation));
        BinaryTree new_tree = ClassifierModel.crossValidate(tree, crossValidation);
        System.out.println(ClassifierModel.checkAccuracy(new_tree, accuracyEvaluation));
        //System.out.println(tree.start_node.isLeaf);
        //System.out.println(tree.start_node.getLeft_node());

        */
        //mapData(source.getData());
    }

    private static void printHelpMessage() {
        System.out.println("Please include input in the following format: $ CART [credit| " +
                "mushroom| adult] min_leaf_size (optional)[crossValidation %ofTestToUse| " +
                "pessimistic Z] (optional)[randomForest num_trees]" +
                " ]");
    }

    private enum pruningmethod {noprune,crossValidation, pessimistic};

}
