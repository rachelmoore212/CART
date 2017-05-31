package CART;

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

    /*
    static int [] adult_categorical_indexes = new int [] {1,3,5,6,7,8,9,13};
    static int [] adult_numeric_indexes = new int [] {0,2,4,10,11,12};
    static String[] adult_targetValue = new String[]{"<=50K",">=50K"};
    static int adult_classifyIndex = 14;
    static String adult_datasource = "Data/Money/adult.data";
    */


    static int [] cancer_categorical_indexes = new int [] {};
    static int [] cancer_numeric_indexes = new int [] {2,3,4,5,6,7,8,9,10,11};
    static String[] cancer_targetValue = new String[]{"M","B"};
    static int cancer_classifyIndex = 1;
    static String cancer_datasource = "Data/Cancer/breast_cancer.csv";


    /**
     * Command line arguments
     * @param args
     */
    public static void main(String[] args){
        int min_leaf_size = 50;
        pruningmethod pruning = pruningmethod.noprune;
        double Zvalue = 0;
        double crossValidationPercent = 0;
        boolean randomForest = false;
        int numTrees = 1;


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

//            } else if (args[0].equals("adult")) {
//                categorical_indexes = adult_categorical_indexes;
//                numeric_indexes = adult_numeric_indexes;
//                targetValue = adult_targetValue;
//                classifyIndex = adult_classifyIndex;
//                datasource = adult_datasource;

            } else if (args[0].equals("cancer")) {
                categorical_indexes = cancer_categorical_indexes;
                numeric_indexes = cancer_numeric_indexes;
                targetValue = cancer_targetValue;
                classifyIndex = cancer_classifyIndex;
                datasource = cancer_datasource;

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

            if (args.length>2) {
                // Setting the t values
                if ((args.length == 6) || ((args.length == 4) && (args[2].equals("randomForest")))) {
                    randomForest = true;
                    numTrees = Integer.parseInt(args[args.length - 1]);
                }


                //pruning values
                if (!((args.length == 4) && (args[2].equals("randomForest")))) {
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
                }
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
        DataSource crossValidation=null;

        if (pruning==pruningmethod.crossValidation) {
            crossValidation = source.splitDataset(crossValidationPercent, false);
        }


        System.out.println("Building Model...");
        TreeModel model;
        // Actual execution of the code
        if (randomForest) {
            model = new RandomForestModel(source,0.30,numTrees,min_leaf_size);

        } else model = new ClassifierModel(source, min_leaf_size);

        System.out.println("Finished Building Model");
        // Running any pruning techniques on the dataset
        if (pruning==pruningmethod.crossValidation) {
            System.out.println("Pruning Tree...");
            model.crossValidate(crossValidation);
            System.out.println("Finished Pruning Tree");

        } else if (pruning==pruningmethod.pessimistic) {
            System.out.println("Pruning tree...");
            model.pessimisticPrune(Zvalue);
            System.out.println("Finished Pruning Tree");
        }

        // Printing out the resulting tree
        if (!randomForest) {
            System.out.println("\nA graphical representation of the tree:");
        }
        model.printTree();

        System.out.println("\nFinal Accuracy over 20% test data: "+ model.checkAccuracy
                (accuracyEvaluation));

    }

    private static void printHelpMessage() {
        System.out.println("Please include input in the following format: $ CART [credit| " +
                "mushroom| cancer] min_leaf_size (optional)[crossValidation %ofTestToUse| " +
                "pessimistic Z] (optional)[randomForest num_trees]" +
                " ]");
    }

    private enum pruningmethod {noprune,crossValidation, pessimistic};

}
