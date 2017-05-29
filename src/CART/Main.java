package CART;

import java.util.*;

/**
 * Created by Rachel on 5/21/2017.
 */
public class Main {
    /*static int [] categorical_indexes = new int [] {2,3,4,6,7,8,9,10,11};

    static int [] numeric_indexes = new int [] {1,5,12,13,14,15,16,17,18,19,20,21,22,23};

    static String[] targetValue = new String[]{"0","1"};
    static int classifyIndex = 24;
    static String datasource = "Data/CreditCards/credit-data.csv";
    /*/
    static int [] categorical_indexes = new int [] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,
            19,20,21,22};

    static int [] numeric_indexes = new int [] {};

    static String[] targetValue = new String[]{"e","p"};
    static int classifyIndex = 0;
    static String datasource = "Data/Mushroom/agaricus-lepiota.data";



    public static void main(String[] args){


        //System.out.println("hello world");
        DataSource source = new DataSource(datasource,numeric_indexes,
                categorical_indexes, classifyIndex);



        //Set<String> attributes = new HashSet<String>(Arrays.asList("meh","penis","vaginenis"));
        //System.out.println(BinaryTree.getSubsets(attributes));

        //System.out.println(ClassifierModel.GINI(100,0));
        //System.out.println(ClassifierModel.GINI(90,10));

        DataSource accuracyEvaluation = source.splitDataset(0.2, false);
        DataSource crossValidation = source.splitDataset(0.2, false);
        //DataSource first30pts = source.splitDataset2(30);
        //System.out.println(first30pts.getData().size());

//        RandomForestModel model = new RandomForestModel(source,0.8,100,50,1.95);
//        System.out.println(model.checkAccuracy(accuracyEvaluation));

        BinaryTree tree = new BinaryTree(source, 10);
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

    /*public double[] getBestCategoricalSplit(List<DataSource.Datapoint> data){
        for (int i = 0; i < categorical_indexes.length; i++){

        }
    }*/

}
