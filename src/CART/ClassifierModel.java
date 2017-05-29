package CART;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by jamie on 5/25/17.
 */
public class ClassifierModel {
    DataSource dataSource;
    Node classiferRoot;

    //Constructs the model and trains it on the dataset, where K is the min number of points in a
    // bin
    public ClassifierModel(DataSource d, int K) {
        this.dataSource = d;
        //classiferRoot = recursiveClassify(d.getData(),K);
    }

    //Method that runs the node tree thing to do the complmacated mathematitics
    /*private Node recursiveClassify(List<DataSource.Datapoint> nodeAssignments ,int K) {


        return newNode();
    }*/

    /**
     * Method that evaluates the overall accuracy of a model on some test dataset
     */
    public static double checkAccuracy(BinaryTree tree, DataSource testData) {
        int numCorrect = 0;
        int numIncorrect = 0;

        for (DataSource.Datapoint d : testData.getData()) {

            String assignment = tree.findAssignment(d);
            if (assignment.equals(d.classification)) {
                numCorrect++;
            } else {
                numIncorrect++;
            }
        }
        return (1.0*numCorrect)/(numCorrect + numIncorrect);
    }

    public static BinaryTree crossValidate(BinaryTree tree, DataSource testdata){



        for (BinaryTree.Node node: tree.node_list){
            if (!node.isLeaf){
                double initial_accuracy = ClassifierModel.checkAccuracy(tree, testdata);
                //System.out.println("initial acc " + initial_accuracy);
                node.setLeafValueOnly(true);
                double after_accuracy = ClassifierModel.checkAccuracy(tree, testdata);
                //System.out.println("New acc: " + after_accuracy);
                if (initial_accuracy < after_accuracy){
                    System.out.println("trimmed the tree");

                } else {
                    node.setLeafValueOnly(false);
                }
            }
        }
        return tree;

    }


    // Method that computes the GINI coefictent
    public static double GINI(int success, int fail) {
        double percent_success = (1.0 * success / (success + fail));
        double percent_fail = (1.0 * fail / (success + fail));
        return 1.0 - (percent_fail*percent_fail) - (percent_success*percent_success);
        //System.out.println((1.0 * success / (success + fail)) * (1.0 - (1.0 *success / (success + fail))));
        //System.out.println((1.0 * fail / (success + fail)) * (1.0 - (1.0 * fail / (success + fail))));
        //return (1.0 * success / (success + fail)) * (1.0 - (1.0 *success / (success + fail))) +
        //        (1.0 * fail / (success + fail)) * (1.0 - (1.0 *fail / (success + fail)));
    }


    //Method that classifies the data after its done
    public String ClassifyDatapoint(DataSource.Datapoint point) {
        return "FOOOOOOOOOOOOO";
    }

    public static double pessemisticError(int wrong, int total, double z){
        //System.out.println("wrong: "+wrong+"  total:"+total+"   z:"+z);
        double p = 1.0*wrong/total;
        if (p == 0.0){
           return 0.0;

        }
        double top_error_rate = p + z * Math.sqrt(1.0*(p*(1-p))/total);
        double error = 1.0*total*top_error_rate;
        //System.out.println("Error: " + error);
        return error;
    }

}
