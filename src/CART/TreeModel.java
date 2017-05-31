package CART;

/**
 * Interface that says we have a model
 */
public interface TreeModel {

    double checkAccuracy(DataSource d);
    void crossValidate(DataSource testdata);
    void pessimisticPrune(double z);
    void printTree();

}
