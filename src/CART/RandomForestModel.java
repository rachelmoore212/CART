package CART;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A random forest model which consists of many tree models that vote on the correct answer
 */
public class RandomForestModel implements TreeModel {
    List<BinaryTree> treeList;
    double treeFactor;
    DataSource source;
    double minNodeSize;


    public RandomForestModel(DataSource source, double treeFactor, int K, int minNodeSize) {
        this.source = source;
        this.treeFactor = treeFactor;
        this.treeList = new ArrayList<>();
        this.minNodeSize = minNodeSize;

        // Generating a bunch of datasources and trees based off of them
        for (int i = K; i>0; i--) {
            DataSource subdata = source.splitDataset(treeFactor, true);
            System.out.println(subdata.getData().size());

            BinaryTree tree = new BinaryTree(subdata, minNodeSize);
            treeList.add(tree);
            System.out.println("Finished building a tree, "+i+" trees left to build");
        }
    }


    /**
     * Implementation of the model that checks the accuracy all the trees in the dataset by voting
     *
     * @param testData
     * @return
     */
    public double checkAccuracy(DataSource testData) {
        int numCorrect = 0;
        int numIncorrect = 0;

        for (DataSource.Datapoint d : testData.getData()) {

            String result = evaluatePoint(d);
            if (result.equals(d.classification)) {
                numCorrect++;
            } else {
                numIncorrect++;
            }
        }
        return (1.0 * numCorrect) / (numCorrect + numIncorrect);
    }

    /**
     * Implements interface method to cross validation prune every tree
     * @param testdata
     */
    public void crossValidate(DataSource testdata) {
        for (int i = 0; i<treeList.size(); i++){
            treeList.set(i, ClassifierModel.crossValidateTree(testdata,treeList.get(i)));
        }
    }


    public void pessimisticPrune(double z) {
        for (BinaryTree t : treeList){
            t.pruneTree(z, true);
        }
    }

    @Override
    public void printTree() {
        System.out.println("Forrest with a total of "+treeList.size()+" trees in the model");
    }

    /**
     * Method that evaluates a datapoint by taking its mode when it is evaluated independently by
     * each tree in the datasource
     * @param d
     * @return
     */
    private String evaluatePoint(DataSource.Datapoint d) {
        Map<String,Integer> results = new HashMap<>();

        //puting the results into our result total map
        for (BinaryTree tree: treeList) {
            String aresult = tree.findAssignment(d);

            results.putIfAbsent(aresult, 0);
            results.put(aresult,results.get(aresult));
        }

        return results.entrySet().stream().max((entry1, entry2) -> entry1.getValue() >
                entry2.getValue() ? 1 : -1).get().getKey();
    }

}