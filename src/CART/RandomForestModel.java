package CART;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jamie on 5/28/17.
 */
public class RandomForestModel {
    List<BinaryTree> treeList;
    double treeFactor;
    DataSource source;
    double minNodeSize;


    public RandomForestModel(DataSource source, double treeFactor, int K, int minNodeSize, double z) {
        this.source = source;
        this.treeFactor = treeFactor;
        this.treeList = new ArrayList<>();
        this.minNodeSize = minNodeSize;

        // Generating a bunch of datasources and trees based off of them
        for (int i = K; i>0; i--) {
            DataSource subdata = source.splitDataset(treeFactor, true);
            System.out.println(subdata.getData().size());

            BinaryTree tree = new BinaryTree(subdata, minNodeSize);
            //tree.pruneTree(z);
            treeList.add(tree);
        }
    }


    /**
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
        System.out.println(results.entrySet());

        return results.entrySet().stream().max((entry1, entry2) -> entry1.getValue() >
                entry2.getValue() ? 1 : -1).get().getKey();
    }

}