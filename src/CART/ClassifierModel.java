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

}
